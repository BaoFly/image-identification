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
import org.springframework.web.bind.annotation.*;

@Api(tags = "UserInfo相关接口")
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserInformationServiceImpl userService;

    @ApiOperation("User用户登录接口")
    @PostMapping("/login")
    public ResultJson userLogin(@RequestParam("userName") String userName,
                                @RequestParam("password") String password) {
        if (StringUtils.isBlank(userName.trim()) || StringUtils.isBlank(password.trim())) {
            return ResultJson.fail(CommonConstant.USER_INFO_ERROR_MSG);
        }
        userService.checkUserInfo(userName.trim(), password.trim());
        return ResultJson.success();
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
        userService.userDelete(userVo);
        return ResultJson.success();
    }

    @ApiOperation("新增一个User")
    @PostMapping("/add")
    public ResultJson userAdd(@RequestBody UserInformation userInformation) {
        userService.add(userInformation);
        return ResultJson.success();
    }

}
