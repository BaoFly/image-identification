package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.constant.CommonConstant;
import com.bf.image.exception.CustomException;
import com.bf.image.pojo.UserInformation;
import com.bf.image.service.UserInformationService;
import com.bf.image.mapper.UserInformationMapper;
import com.bf.image.utils.TimeUtil;
import com.bf.image.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
* @author Administrator
* @description 针对表【user_information】的数据库操作Service实现
* @createDate 2023-10-14 18:40:50
*/
@Service
public class UserInformationServiceImpl extends ServiceImpl<UserInformationMapper, UserInformation>
    implements UserInformationService{

    @Autowired
    private UserInformationMapper userMapper;


    @Override
    public void checkUserInfo(UserInformation userInformation) {
        // 拿到去除前后空格的用户名和密码
        String username = userInformation.getUsername().trim();
        String password = userInformation.getPassword().trim();

        // 判断是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new CustomException(CommonConstant.FAIL_CODE, CommonConstant.USER_INFO_ERROR_MSG);
        }

        // 查数据库
        LambdaQueryWrapper<UserInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInformation::getUsername, username)
                .eq(UserInformation::getPassword, password);
        UserInformation record = userMapper.selectOne(queryWrapper);

        // 如果没有该用户
        if (Objects.isNull(record)) {
            throw new CustomException(CommonConstant.FAIL_CODE, CommonConstant.USER_NOT_EXIST_MSG);
        }


    }

    /**
     * 添加一个user
     * @param userInformation
     */
    @Transactional
    @Override
    public void add(UserInformation userInformation) {
        // 拿到去除前后空格的用户名和密码
        String username = userInformation.getUsername().trim();
        String password = userInformation.getPassword().trim();

        // 查数据库
        LambdaQueryWrapper<UserInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInformation::getUsername, username);
        UserInformation record = userMapper.selectOne(queryWrapper);

        // 如果有该用户
        if (!Objects.isNull(record)) {
            throw new CustomException(CommonConstant.FAIL_CODE, CommonConstant.USER_EXIST_MSG);
        }
        

        Long uuid = UUIDUtil.generateUUID();
        Date currentTime = TimeUtil.getCurrentTime();
        Date updateTime = TimeUtil.getCurrentTime();

        userInformation.setUserId(uuid);
        userInformation.setUsername(username);
        userInformation.setPassword(password);
        userInformation.setCreateTime(currentTime);
        userInformation.setUpdateTime(updateTime);

        userMapper.insert(userInformation);

    }
}




