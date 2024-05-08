package com.bf.image.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.UserInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.image.vo.UserVo;

/**
* @author Administrator
* @description 针对表【user_information】的数据库操作Service
* @createDate 2023-10-14 18:40:50
*/
public interface UserInformationService extends IService<UserInformation> {


    void checkUserInfo(String userName, String password);

    void add(UserInformation userInformation);

    Page<UserInformation> userPageVo(UserVo userVo);

    void userDelete(UserVo userVo);
}
