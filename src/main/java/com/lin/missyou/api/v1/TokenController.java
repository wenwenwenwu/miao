package com.lin.missyou.api.v1;

import com.lin.missyou.exception.exception.NotFoundException;
import com.lin.missyou.model.dataTransferObject.TokenDTO;
import com.lin.missyou.model.dataTransferObject.TokenGetDTO;
import com.lin.missyou.service.WxAuthenticationService;
import com.lin.missyou.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "token")
public class TokenController {

    @Autowired
    WxAuthenticationService authenticationService;

    //即前端的注册/登录接口
    @PostMapping()
    public Map<String, String> getToken(@RequestBody @Validated TokenGetDTO userData) {
        //返回到前端的所有数据都要是对象
        Map<String, String> map = new HashMap<>();
        String token = null;
        switch (userData.getType()) {
            case USER_WX:
                token = authenticationService.code2Token(userData.getAccount());
                break;
            case USER_Email:
                break;
            default:
                throw new NotFoundException(10003);
        }
        map.put("token",token);
        return map;
    }

    @PostMapping("/verify")
    public Map<String,Boolean>verify(@RequestBody TokenDTO token){
        Map<String,Boolean> map = new HashMap<>();
        Boolean valid = JwtToken.verifyToken(token.getToken());
        map.put("is_valid",valid);
        return map;
    }
}
