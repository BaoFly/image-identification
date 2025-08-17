package com.bf.image.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.constant.CommonConstant;
import com.bf.image.domin.ResultJson;
import com.bf.image.exception.CustomException;
import com.bf.image.entity.UserInformation;
import com.bf.image.mapper.UserInformationMapper;
import com.bf.image.security.utils.JwtUtils;
import com.bf.image.utils.TimeUtil;
import com.bf.image.utils.UUIDUtil;
import com.bf.image.domin.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
* @author Administrator
* @description 针对表【user_information】的数据库操作Service实现
* @createDate 2023-10-14 18:40:50
*/
@Service
public class UserInformationServiceImpl extends ServiceImpl<UserInformationMapper, UserInformation> {

    @Autowired
    private UserInformationMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public ResultJson checkUserInfo(String username, String password) {

        // 1 查数据库
        LambdaQueryWrapper<UserInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInformation::getUsername, username);
        UserInformation user = userMapper.selectOne(queryWrapper);

        // 2 看是否有该用户名
        if (Objects.isNull(user)) {
            throw new CustomException(CommonConstant.FAIL_CODE, CommonConstant.USER_NOT_EXIST_MSG);
        }

        // 3 校验密码
        String passwordData = user.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean matches = passwordEncoder.matches(password, passwordData);

        // 如果有该用户
        if (matches) {
            String jwtToken = JwtUtils.getJwtToken(user.getUserId().toString(), username);

            redisTemplate.opsForValue().set(jwtToken, JSONObject.toJSONString(user), 7, TimeUnit.DAYS);

            return ResultJson.success("登录成功", jwtToken);
        } else {
            return ResultJson.fail("账号或密码错误");
        }
    }

    /**
     * 添加一个user
     * @param userInformation
     */
    @Transactional
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


}




