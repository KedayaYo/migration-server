package com.skysys.service.component.oss;

import com.skysys.service.component.oss.model.OssTypeEnum;
import com.skysys.service.model.enums.StorageTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wangrui
 * @date: 2023/12/20 0:07
 * @description:
 */
@ConfigurationProperties(prefix = "oss")
@Component
public class OssConfiguration {

    /**
     * @see OssTypeEnum
     */
    public static OssTypeEnum provider;

    /**
     * Whether to use the object storage service.
     */
    public static boolean enable;

    /**
     * The protocol needs to be included at the beginning of the address.
     */
    public static String endpoint;

    public static String accessKey;

    public static String secretKey;

    public static String region;

    public static Long expire;

    public static String roleSessionName;

    public static String roleArn;

    public static String bucket;

    public static String objectDirPrefix;

    public static Integer getProviderCode() {

        if (StorageTypeEnum.ALI.getProvider().equals(provider.getType())) {
            return StorageTypeEnum.ALI.getType();
        }

        if (StorageTypeEnum.MINIO.getProvider().equals(provider.getType())) {
            return StorageTypeEnum.MINIO.getType();
        }

        return null;
    }

    public void setProvider(OssTypeEnum provider) {
        OssConfiguration.provider = provider;
    }

    public void setEnable(boolean enable) {
        OssConfiguration.enable = enable;
    }

    public void setEndpoint(String endpoint) {
        OssConfiguration.endpoint = endpoint;
    }

    public void setAccessKey(String accessKey) {
        OssConfiguration.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        OssConfiguration.secretKey = secretKey;
    }

    public void setRegion(String region) {
        OssConfiguration.region = region;
    }

    public void setExpire(Long expire) {
        OssConfiguration.expire = expire;
    }

    public void setRoleSessionName(String roleSessionName) {
        OssConfiguration.roleSessionName = roleSessionName;
    }

    public void setRoleArn(String roleArn) {
        OssConfiguration.roleArn = roleArn;
    }

    public void setBucket(String bucket) {
        OssConfiguration.bucket = bucket;
    }

    public void setObjectDirPrefix(String objectDirPrefix) {
        OssConfiguration.objectDirPrefix = objectDirPrefix;
    }
}
