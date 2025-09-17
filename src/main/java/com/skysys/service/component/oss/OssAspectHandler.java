package com.skysys.service.component.oss;

import jakarta.annotation.Resource;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author: wangrui
 * @date: 2023/12/20 0:10
 * @description:
 */
@Aspect
@Component
public class OssAspectHandler {

    @Resource
    private OssServiceContext ossServiceContext;

    @Before("execution(public * com.skysys.service.component.oss.OssServiceContext.*(..))")
    public void before() {
        if (!OssConfiguration.enable) {
            throw new IllegalArgumentException("请启用OssConfiguration。");
        }
        if (this.ossServiceContext.getOssService() == null) {
            throw new IllegalArgumentException("请检查OssConfiguration配置。");
        }
        this.ossServiceContext.createClient();
    }
}
