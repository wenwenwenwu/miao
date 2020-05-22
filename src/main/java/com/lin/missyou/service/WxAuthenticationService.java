package com.lin.missyou.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.exception.ParameterException;
import com.lin.missyou.model.dataAccessObject.User;
import com.lin.missyou.repository.UserRepository;
import com.lin.missyou.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxAuthenticationService {
    @Value("${wx.appId}")
    private String appid;
    @Value("${wx.appSecret}")
    private String appSecret; //8283f1d79e1ecbb509e5792a5c03c1ad
    @Value("${wx.code2session}")
    private String code2SessionUrl; //微信code获取接口 https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    public String code2Token(String code) {
        //根据微信code请求openId
        //MessageFormat是字符串拼接类。输入样式和参数就能返回拼接结果
        String url = MessageFormat.format(this.code2SessionUrl, this.appid, this.appSecret, code);
        //RestTemplate是网络请求类。传入url及返回值类型即可发起请求，返回JSON字符串
        RestTemplate restTemplate = new RestTemplate();
        String content = restTemplate.getForObject(url, String.class);
        Map<String,Object> map = new HashMap<>();
        try {
            //JSON反序列化
            map = mapper.readValue(content, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this.register(map);
    }

    private String register(Map<String, Object> map) {
        String openid = (String) map.get("openid");
        if (openid == null) {
            throw new ParameterException(20004);
        }
        //用户表中查找用户
        Optional<User> optionalUser = userRepository.findByOpenid(openid);
        //找到，返回新token（登录）
        if (optionalUser.isPresent()) {
            return JwtToken.makeToken(optionalUser.get().getId());
        }
        //找不到，新建用户、添加到用户表、返回新token（注册）
        User user = User.builder()
                .openid(openid)
                .build();
        userRepository.save(user);
        return JwtToken.makeToken(user.getId());
    }
}
