package com.bf.image.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.bf.image.exception.CustomException;
import com.bf.image.security.entity.UserDetailBean;
import com.bf.image.security.utils.JwtUtils;
import com.bf.image.utils.JsonParser;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头获取token
        String token = httpServletRequest.getHeader("Authorization");

        // 截取 token
        if (StringUtils.isNotBlank(token)) {
            // 验证token 取其中的username
            Claims claims = JwtUtils.verifyJwt(token);

            if (Objects.isNull(claims)) {
                throw new CustomException("token异常，请重新登录");
            }

            String userObj = redisTemplate.opsForValue().get(token);
            UserDetailBean userDetailBean = JsonParser.parseJsonToEntity(userObj, UserDetailBean.class);

            redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userDetailBean), 7, TimeUnit.DAYS);

            if (Objects.nonNull(userDetailBean) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                // 手动放到上下文当中
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetailBean, null, userDetailBean.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            // 继续下一个过滤器
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
