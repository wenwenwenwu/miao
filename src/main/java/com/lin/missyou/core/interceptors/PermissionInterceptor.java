package com.lin.missyou.core.interceptors;

import com.auth0.jwt.interfaces.Claim;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.exception.exception.ForbiddenException;
import com.lin.missyou.exception.exception.UnAuthenticatedException;
import com.lin.missyou.model.dataAccessObject.User;
import com.lin.missyou.service.UserService;
import com.lin.missyou.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserService userService;

    public PermissionInterceptor() {
        super();
    }

    @Override
    //Object handler 路由方法
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<ScopeLevel> scopeLevel = this.getScopeLevel(handler);
        //判断是否共有API
        if (!scopeLevel.isPresent()) {
            return true;
        }
        //获取token
        String bearerToken = request.getHeader("Authorization"); //传递token的键值对的键
        if (StringUtils.isEmpty(bearerToken)) {
            throw new UnAuthenticatedException(10004);
        }
        if (!bearerToken.startsWith("Bearer")) { //JWT令牌要以bearer开通
            throw new UnAuthenticatedException(10004);
        }
        String tokens[] = bearerToken.split(" ");
        if (!(tokens.length == 2)) {
            throw new UnAuthenticatedException(10004);
        }
        String token = tokens[1];
        //获取解析过的token
        Optional<Map<String, Claim>> optionalMap = JwtToken.getClaims(token);
        Map<String, Claim> map = optionalMap.orElseThrow(() ->
                new UnAuthenticatedException(10004)
        );
        //判断是否拦截
        Boolean valid = this.hasPermmision(scopeLevel.get(), map);
        //获取当前线程的用户信息
        if (valid){
            this.setToThreadLocal(map);
        }
        return valid;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalUser.clear();
        super.afterCompletion(request,response,handler,ex);
    }

    private void setToThreadLocal(Map<String, Claim> map) {
        Long uid = map.get("uid").asLong(); //Claim类的方法
        Integer scope = map.get("scope").asInt();
        User user = userService.getUserById(uid);
        LocalUser.set(user,scope);
    }

    private boolean hasPermmision(ScopeLevel scopeLevel, Map<String, Claim> map) {
        Integer level = scopeLevel.value(); //方法权限等级
        Integer scope = map.get("scope").asInt(); //用户权限等级
        if (level > scope) {
            throw new ForbiddenException(10005);
        }
        return true;
    }

    private Optional<ScopeLevel> getScopeLevel(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ScopeLevel scopeLevel = handlerMethod.getMethod().getAnnotation(ScopeLevel.class);
            if (scopeLevel == null) {
                return Optional.empty();
            }
            return Optional.of(scopeLevel);
        }
        return Optional.empty();
    }
}
