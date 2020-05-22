package com.github.wxpay.sdk;

import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

public class LinWXPayConfig extends WXPayConfig {

    @Value("wx.appId")
    private String appId;

    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return "1589111117"; //微信商户平台ID
    }

    @Override
    public String getKey() {
        return "yDDDsDv6kFG1qXXX6jP"; //微信商户平台Key
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return null;
            }
        };
        return iwxPayDomain;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
