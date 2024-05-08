package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.constant.CommonConstant;
import com.bf.image.exception.CustomException;
import com.bf.image.pojo.TevInformation;
import com.bf.image.pojo.UserInformation;
import com.bf.image.service.UserInformationService;
import com.bf.image.mapper.UserInformationMapper;
import com.bf.image.utils.TimeUtil;
import com.bf.image.utils.UUIDUtil;
import com.bf.image.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
    public void checkUserInfo(String username, String password) {

        // 1 查数据库
        LambdaQueryWrapper<UserInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInformation::getUsername, username);
        List<UserInformation> users = userMapper.selectList(queryWrapper);

        // 2 看是否有该用户名
        if (CollectionUtils.isEmpty(users)) {
            throw new CustomException(CommonConstant.FAIL_CODE, CommonConstant.USER_NOT_EXIST_MSG);
        }

        // 3 看是否有该密码的用户
        UserInformation userInformation = users.stream().filter(user -> user.getPassword().equals(password)).findFirst().orElse(null);

        // 如果没有该用户
        if (Objects.isNull(userInformation)) {
            throw new CustomException(CommonConstant.FAIL_CODE, CommonConstant.PASSWORD_ERROR);
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

    @Override
    public Page<UserInformation> userPageVo(UserVo userVo) {
        Integer pageSize = userVo.getPageSize();
        Integer offset = (userVo.getCurrent() - 1) * pageSize;
        Page<UserInformation> newPage = new Page<>();
        newPage.setCurrent(offset);
        newPage.setSize(pageSize);

        Date startTime = null;
        Date endTime = null;

        if (userVo.getDateRange() != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(userVo.getDateRange())) {
            startTime = userVo.getDateRange().get(0);
            endTime = userVo.getDateRange().get(1);
        }

        Page<UserInformation> page = this.page(newPage,
                Wrappers.lambdaQuery(UserInformation.class)
                        .like(Objects.nonNull(userVo.getUsername()), UserInformation::getUsername, userVo.getUsername())
                        .between(Objects.nonNull(startTime), UserInformation::getCreateTime, startTime, endTime)
                        .like(Objects.nonNull(userVo.getEmail()), UserInformation::getEmail, userVo.getEmail())
                        .eq(Objects.nonNull(userVo.getSex()), UserInformation::getSex, userVo.getSex())
                        .orderByDesc(UserInformation::getCreateTime)
        );
        return page;
    }

    @Override
    public void userDelete(UserVo userVo) {

    }


}




