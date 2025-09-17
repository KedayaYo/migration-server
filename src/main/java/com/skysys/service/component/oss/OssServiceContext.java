package com.skysys.service.component.oss;

import com.skysys.service.component.oss.model.CredentialsToken;
import com.skysys.service.component.oss.model.OssTypeEnum;
import com.skysys.service.component.oss.service.IOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: wangrui
 * @date: 2023/12/20 0:21
 * @description:
 */
@Service
public class OssServiceContext {

    public static String POLICY = "{\"Version\":\"1.1\",\"Statement\":[{\"Action\":[\"obs:object:PutObject\",\"obs:object:GetObject\",\"obs:object:DeleteObject\",\"obs:object:ListMultipartUploadParts\",\"obs:object:AbortMultipartUpload\",\"obs:object:CompleteMultipartUpload\",\"obs:object:UploadPart\"],\"Resource\":[\"obs:*:*:object:{{bucket}}/{{path}}\"],\"Effect\":\"Allow\"}]}";
    private IOssService ossService;

    @Autowired
    public OssServiceContext(List<IOssService> ossServices, OssConfiguration configuration) {
        if (!OssConfiguration.enable) {
            return;
        }
        this.ossService = ossServices.stream()
                .filter(ossService -> ossService.getOssType() == OssConfiguration.provider)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("OSS提供商是非法的：" +
                        Arrays.toString(Arrays.stream(OssTypeEnum.values()).map(OssTypeEnum::getType).toArray())));
    }

    IOssService getOssService() {
        return this.ossService;
    }

    public CredentialsToken getCredentials() {
        return this.ossService.getCredentials();
    }

    public CredentialsToken getCredentials(String dirPrefix) {
        return this.ossService.getCredentials(dirPrefix);
    }

    public URL getObjectUrl(String bucket, String objectKey) {
        if (!StringUtils.hasText(bucket) || !StringUtils.hasText(objectKey)) {
            throw new IllegalArgumentException();
        }
        return this.ossService.getObjectUrl(bucket, objectKey);
    }

    public URL getObjectUrl(String bucket, String objectKey, Long expiration) {
        if (!StringUtils.hasText(bucket) || !StringUtils.hasText(objectKey)) {
            throw new IllegalArgumentException();
        }
        return this.ossService.getObjectUrl(bucket, objectKey, expiration);
    }

    public URL getObjectUrl(String bucket, String objectKey, Long expiration, Map<String, String> extraQueryParams) {
        if (!StringUtils.hasText(bucket) || !StringUtils.hasText(objectKey)) {
            throw new IllegalArgumentException();
        }
        return this.ossService.getObjectUrl(bucket, objectKey, expiration, extraQueryParams);
    }

    public Boolean deleteObject(String bucket, String objectKey) {
        return this.ossService.deleteObject(bucket, objectKey);
    }

    public void deleteDir(String bucket, String dir) {
        this.ossService.deleteDir(bucket, dir);
    }

    public InputStream getObject(String bucket, String objectKey) {
        return this.ossService.getObject(bucket, objectKey);
    }

    public void putObject(String bucket, String objectKey, InputStream stream) {
        this.ossService.putObject(bucket, objectKey, stream);
    }

    void createClient() {
        this.ossService.createClient();
    }

    /**
     * copyObject
     *
     * @param sourceBucket
     * @param destinationBucket
     * @param sourceKey
     * @param destinationKey
     * @return
     */
    public Boolean copyObject(String sourceBucket, String destinationBucket, String sourceKey, String destinationKey) {
        return this.ossService.copyObject(sourceBucket, destinationBucket, sourceKey, destinationKey);
    }

    /**
     * listObjectKeys
     *
     * @param bucket
     * @param prefix
     * @return
     */
    public List<String> listObjectKeys(String bucket, String prefix) {
        return this.ossService.listObjectKeys(bucket, prefix);
    }

    public Boolean exists(String bucket, String objectKey) {
        return this.ossService.exists(bucket, objectKey);
    }
}
