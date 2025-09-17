package com.skysys.service.component.oss.model;


import com.aliyuncs.auth.sts.AssumeRoleResponse;
import io.minio.credentials.Credentials;
import lombok.Data;

/**
 * @author: wangrui
 * @date: 2023/12/20 0:21
 * @description:
 */
@Data
public class CredentialsToken {

    private static final int DELAY = 300;

    private String accessKeyId;

    private String accessKeySecret;

    private Long expire;

    private String securityToken;

    public CredentialsToken(Credentials credentials, long expire) {
        this.accessKeyId = credentials.accessKey();
        this.accessKeySecret = credentials.secretKey();
        this.securityToken = credentials.sessionToken();
        this.expire = expire - DELAY;
    }

    public CredentialsToken(AssumeRoleResponse.Credentials credentials, long expire) {
        this.accessKeyId = credentials.getAccessKeyId();
        this.accessKeySecret = credentials.getAccessKeySecret();
        this.securityToken = credentials.getSecurityToken();
        this.expire = expire - DELAY;
    }
}
