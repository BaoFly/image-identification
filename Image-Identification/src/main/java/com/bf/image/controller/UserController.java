package com.bf.image.controller;

import com.bf.image.constant.CommonConstant;
import com.bf.image.pojo.UserInformation;
import com.bf.image.service.UserInformationService;
import com.bf.image.vo.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Api(tags = "UserInfo相关接口")
@RestController
@RequestMapping("/login")
@CrossOrigin
public class UserController {

    @Autowired
    private UserInformationService userService;

    @ApiOperation("User用户登录接口")
    @PostMapping()
    public ResultJson userLogin(@RequestParam("userName") String userName,
                                @RequestParam("password") String password) {
        if (StringUtils.isBlank(userName.trim()) || StringUtils.isBlank(password.trim())) {
            return ResultJson.fail(CommonConstant.USER_INFO_ERROR_MSG);
        }
        userService.checkUserInfo(userName.trim(), password.trim());
        return ResultJson.success();
    }

    @ApiOperation("新增一个User")
    @PostMapping("/add")
    public ResultJson userAdd(@RequestBody UserInformation userInformation) {
        if (Objects.isNull(userInformation)) {
            return ResultJson.fail(null);
        }
        userService.add(userInformation);
        return ResultJson.success();
    }

}
