package com.lin.missyou.core.configuration;

import com.lin.missyou.core.hake.AutoPrefixUrlMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
//@RequestMapping类会调用WebMvcRegistrations接口的getRequestMappingHandlerMapping方法
public class AutoPrefixConfiguration implements WebMvcRegistrations {
    @Override
    //获取类RequestMappingHandlerMapping对象的方法
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        //返回子类AutoPrefixUrlMapping对象
        return new AutoPrefixUrlMapping();
    }
}
