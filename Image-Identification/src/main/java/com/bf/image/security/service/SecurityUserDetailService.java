package com.bf.image.security.service;

import com.bf.image.entity.UserInformation;
import com.bf.image.security.entity.UserDetailBean;
import com.bf.image.service.impl.UserInformationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SecurityUserDetailService implements UserDetailsService {

    @Autowired
    private UserInformationServiceImpl userInformationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInformation userInformation = userInformationService.lambdaQuery()
                .eq(UserInformation::getUsername, username)
                .one();

        String name = userInformation.getUsername();
        String password = userInformation.getPassword();
        Date createTime = userInformation.getCreateTime();
        Long userId = userInformation.getUserId();

        UserDetailBean userDetailBean = UserDetailBean.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .createTime(createTime)
                .build();

        return userDetailBean;
    }
}
