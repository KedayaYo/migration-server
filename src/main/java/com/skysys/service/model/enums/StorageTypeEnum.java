package com.skysys.service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: wangrui
 * @date: 2024/1/24 11:22
 * @description:
 */
@AllArgsConstructor
@Getter
public enum StorageTypeEnum {

    /**
     * 阿里云
     */
    ALI("ali", 1),

    /**
     * FTP
     */
    FTP("ftp", 2),

    /**
     * minio
     */
    MINIO("minio", 4);

    private final String provider;

    private final Integer type;

    public static StorageTypeEnum find(Integer type) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.getType().equals(type)).findAny().get();
    }
}
