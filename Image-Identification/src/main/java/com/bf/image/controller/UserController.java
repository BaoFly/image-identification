package com.bf.image.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.constant.CommonConstant;
import com.bf.image.entity.UserInformation;
import com.bf.image.service.impl.UserInformationServiceImpl;
import com.bf.image.domin.ResultJson;
import com.bf.image.domin.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api(tags = "UserInfo相关接口")
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserInformationServiceImpl userService;

    @ApiOperation("User用户登录接口")
    @PostMapping("/login")
    public ResultJson userLogin(@RequestBody UserInformation userInformation) {
        if (StringUtils.isBlank(userInformation.getUsername().trim()) || StringUtils.isBlank(userInformation.getPassword().trim())) {
            return ResultJson.fail(CommonConstant.USER_INFO_ERROR_MSG);
        }

        return userService.checkUserInfo(userInformation.getUsername().trim(), userInformation.getPassword().trim());
    }

    @ApiOperation("User用户分页列表接口")
    @PostMapping("user/pageVo")
    public ResultJson userPageVo(@RequestBody UserVo userVo) {
        Page<UserInformation> page = userService.userPageVo(userVo);
        return ResultJson.success(page);
    }

    @ApiOperation("User用户删除接口")
    @PostMapping("user/delete")
    public ResultJson userDelete(@RequestBody UserVo userVo) {
        Long userId = userVo.getUserId();

        if (Objects.isNull(userId)) {
            return ResultJson.fail("用户ID不能为空");
        }

        userService.removeById(userId);
        return ResultJson.success();
    }

    @ApiOperation("新增一个User")
    @PostMapping("/add")
    public ResultJson userAdd(@RequestBody UserInformation userInformation) {
        String password = userInformation.getPassword();
        String username = userInformation.getUsername();

        if (StringUtils.isBlank(password) || StringUtils.isBlank(username)) {
            return ResultJson.fail("账号或密码不能为空");
        }

        List<UserInformation> sameUsernameList = userService.lambdaQuery()
                .eq(UserInformation::getUsername, username)
                .list();

        if (sameUsernameList.size() > 0) {
            return ResultJson.fail("已存在相同用户名的用户");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(password);

        userInformation.setPassword(encodePassword);

        userService.add(userInformation);
        return ResultJson.success();
    }

}
