package com.lin.missyou.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//用类来读取配置文件
@Component //注入容器
@PropertySource("classpath:config/exception-code.properties") //配置文件位置（默认在resources目录下）
@ConfigurationProperties(prefix = "lin") //符合条件配置的前缀
public class ExceptionCodeConfiguration {
    private Map<Integer, String> codes = new HashMap<>();

    public String getMessage(int code) {
        String message = codes.get(code); //Map数据结构的方法
        return message;
    }

    //为了private变量codes的外部读取
    public Map<Integer, String> getCodes() {
        return codes;
    }

    public void setCodes(Map<Integer, String> codes) {
        this.codes = codes;
    }
}
