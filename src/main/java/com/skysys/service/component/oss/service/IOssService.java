package com.skysys.service.component.oss.service;


import com.skysys.service.component.oss.model.CredentialsToken;
import com.skysys.service.component.oss.model.OssTypeEnum;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author: wangrui
 * @date: 2023/12/20 0:09
 * @description:
 */
public interface IOssService {

    OssTypeEnum getOssType();

    /**
     * Get temporary credentials.
     *
     * @return
     */
    CredentialsToken getCredentials();

    CredentialsToken getCredentials(String dirPrefix);

    /**
     * Get the address of the object based on the bucket name and the object name.
     *
     * @param bucket    bucket name
     * @param objectKey object name
     * @return download link
     */
    URL getObjectUrl(String bucket, String objectKey);

    URL getObjectUrl(String bucket, String objectKey, Long expiration);

    URL getObjectUrl(String bucket, String objectKey, Long expiration, Map<String, String> extraQueryParams);

    /**
     * Deletes the object in the storage bucket.
     *
     * @param bucket
     * @param objectKey
     * @return
     */
    Boolean deleteObject(String bucket, String objectKey);

    void deleteDir(String bucket, String dir);

    /**
     * Get the contents of an object.
     *
     * @param bucket
     * @param objectKey
     * @return
     */
    InputStream getObject(String bucket, String objectKey);

    void putObject(String bucket, String objectKey, InputStream input);

    void createClient();

    /**
     * copyObject 【后加】
     *
     * @param sourceBucket
     * @param destinationBucket
     * @param sourceKey
     * @param destinationKey
     * @return
     */
    Boolean copyObject(String sourceBucket, String destinationBucket, String sourceKey, String destinationKey);

    /**
     * 【后加】列举唯一标识
     *
     * @param bucket 桶名
     * @param prefix 前缀
     * @return ObjectKey列表
     */
    List<String> listObjectKeys(String bucket, String prefix);

    Boolean exists(String bucket, String objectKey);
}
