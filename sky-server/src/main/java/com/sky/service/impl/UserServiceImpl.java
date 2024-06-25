package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO);

        //判断openid是否为空 , 为空则抛出异常
        if (openid == null){
            throw new LoginFailedException("登录失败");
        }
        //查询用户表 , 看该用户是否注册
        User user = userMapper.getByOpenid(openid);
        //如果没注册 , 把该用户添加到数据库中
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
        }

        return user;
    }

    private String getOpenid(UserLoginDTO userLoginDTO) {
        //向微信发送请求获取openid
        Map entity = new HashMap();
        entity.put("appid", weChatProperties.getAppid());
        entity.put("secret", weChatProperties.getSecret());
        entity.put("js_code", userLoginDTO.getCode());
        entity.put("grant_type", "authorization_code");
        //获得json数据
        String json = HttpClientUtil.doGet(WX_LOGIN, entity);
        //解析json数据 , 拿到openid
        JSONObject jsonObject = JSON.parseObject(json);
        String openid =(String)jsonObject.get("openid");
        return openid;
    }
}
