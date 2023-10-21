package com.bf.image.controller;

import com.bf.image.pojo.UserInformation;
import com.bf.image.service.UserInformationService;
import com.bf.image.vo.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserInformationService userService;

    @PostMapping()
    public ResultJson userLogin(@RequestBody UserInformation userInformation) {
        userService.checkUserInfo(userInformation);
        return ResultJson.success();
    }

    @PostMapping("/add")
    public ResultJson userAdd(@RequestBody UserInformation userInformation) {
        if (Objects.isNull(userInformation)) {
            return ResultJson.success(null);
        }
        userService.add(userInformation);
        return ResultJson.success();
    }

}
