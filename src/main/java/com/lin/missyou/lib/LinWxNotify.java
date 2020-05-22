package com.lin.missyou.lib;

import com.lin.missyou.exception.exception.ServerErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LinWxNotify {

    public static String readNotify(InputStream stream) {
        //缓存流的类
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            //ServerErrorException(9999)可以视作服务器内部错误，前端无需知道
            //也可以详细定义错误码，方便排查故障
            throw new ServerErrorException(9999);
        }
        //关闭stream
        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                //finally是收尾，不再抛出异常
            }
           return builder.toString();
        }

    }

    public static String fail() {
        return "false";
    }

    public static String success() {
        //微信规定的格式
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
