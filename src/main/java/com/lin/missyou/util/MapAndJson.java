package com.lin.missyou.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.exception.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
//AttributeConverter是个接口，两个范型类分别是字段在实体中的类型和在数据库中的类型（JSON字段是个字符串）
public class MapAndJson implements AttributeConverter<Map<String, Object>, String> {
    //依赖注入SpringBoot中自带的Jackson序列化库的序列化实例
    @Autowired
    private ObjectMapper mapper;

    //序列化
    @Override
    public String convertToDatabaseColumn(Map<String, Object> stringObjectMap) {
        try {
            return mapper.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();//对前端无意义
            throw new ServerErrorException(9999);
        }
    }

    //反序列化
    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        try {
            if (s == null) {
                return null;
            }
            //返回HashMap类
            return mapper.readValue(s, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }
}
