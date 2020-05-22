package com.lin.missyou.core.hake;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

//RequestMappingHandlerMapping类是@RequestMapping类的路由处理器
public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {
    @Value("${missyou.api-package}")
    private String apiPackagePath;

    @Override
    //根据方法获取路由信息的方法
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);
        if (mappingInfo != null) {
            String prefix = this.getPrefix(handlerType);
            //根据prefex字符串生成prefixMappingInfo
            RequestMappingInfo prefixMappingInfo = RequestMappingInfo.paths(prefix).build();
            //和原来的mappingInfo组合成新的mappingInfo
            return prefixMappingInfo.combine(mappingInfo);
        }
        return mappingInfo;
    }

    private String getPrefix(Class<?> handlerType) {
        String packageName = handlerType.getPackage().getName();//com.wu.miao.api.v1
        String dotPath = packageName.replaceAll(this.apiPackagePath, "");//this.apiPackagePath = com.wu.miao.api
        return dotPath.replace(".", "/");
    }
}
