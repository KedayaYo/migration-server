package com.skysys.service.component.oss.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author: wangrui
 * @date: 2023/12/20 0:08
 * @description:
 */
public enum OssTypeEnum {

    ALIYUN("ali"),

    AWS("aws"),

    MINIO("minio");

    private final String type;

    OssTypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
