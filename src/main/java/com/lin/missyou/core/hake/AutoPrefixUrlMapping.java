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
        //获得调用方法的RequestMapping对象，paths为/banner/test
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);
        if (mappingInfo != null) {
            //新建RequestMappingInfo对象，paths为/v1
            String prefix = this.getPrefix(handlerType);
            RequestMappingInfo prefixMappingInfo = RequestMappingInfo
                    .paths(prefix)
                    .build();
            //合并成新的mappingInfo对象返回，path为/v1/banner/test
            return prefixMappingInfo.combine(mappingInfo);
        }
        return mappingInfo;
    }

    private String getPrefix(Class<?> handlerType) {
        String packageName = handlerType.getPackage().getName();//com.wu.miao.api.v1
        String dotPath = packageName.replaceAll(this.apiPackagePath, "");//.v1
        return dotPath.replace(".", "/");///v1
    }
}
