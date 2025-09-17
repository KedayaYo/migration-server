package com.skysys.service.component.oss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.skysys.service.component.oss.OssConfiguration;
import com.skysys.service.component.oss.model.CredentialsToken;
import com.skysys.service.component.oss.model.OssTypeEnum;
import com.skysys.service.component.oss.service.IOssService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wangrui
 * @date: 2023/12/20 0:10
 * @description:
 */
@Service
@Slf4j
public class AliyunOssServiceImpl implements IOssService {

    private OSS ossClient;

    @Override
    public OssTypeEnum getOssType() {
        return OssTypeEnum.ALIYUN;
    }

    @Override
    public CredentialsToken getCredentials() {
        return this.getCredentials(null);
    }

    @SneakyThrows
    @Override
    public CredentialsToken getCredentials(String dirPrefix) {

        try {
            DefaultProfile profile = DefaultProfile.getProfile(
                    OssConfiguration.region, OssConfiguration.accessKey, OssConfiguration.secretKey);
            IAcsClient client = new DefaultAcsClient(profile);

            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setDurationSeconds(OssConfiguration.expire);
            request.setRoleArn(OssConfiguration.roleArn);
            request.setRoleSessionName(OssConfiguration.roleSessionName);

            String policy = "{\n" +
                    "    \"Version\": \"1\",\n" +
                    "    \"Statement\": [\n" +
                    "        {\n" +
                    "            \"Effect\": \"Allow\",\n" +
                    "            \"Action\": \"oss:*\",\n" +
                    "            \"Resource\": [\n" +
                    "                \"acs:oss:*:*:dispatch-base-test\",\n" +
                    "                \"acs:oss:*:*:dispatch-base-test/*\"\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

            request.setPolicy(policy);

            AssumeRoleResponse response = client.getAcsResponse(request);
            return new CredentialsToken(response.getCredentials(), OssConfiguration.expire);

        } catch (ClientException e) {
            log.debug("获取sts失败...");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey) {

        return this.getObjectUrl(bucket, objectKey, OssConfiguration.expire);
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey, Long expiration) {
        boolean isExist = ossClient.doesObjectExist(bucket, objectKey);
        if (!isExist) {
            throw new OSSException("对象不存在");
        }

        return ossClient.generatePresignedUrl(bucket, objectKey, new Date(System.currentTimeMillis() + expiration * 1000));
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey, Long expiration, Map<String, String> extraQueryParams) {
        // 检查文件是否存在
        if (!ossClient.doesObjectExist(bucket, objectKey)) {
            throw new OSSException("对象不存在");
        }

        // 构建请求
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, objectKey);
        request.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000));

        // 添加额外的查询参数
        if (extraQueryParams != null && !extraQueryParams.isEmpty()) {
            extraQueryParams.forEach(request::addQueryParameter);
        }

        return ossClient.generatePresignedUrl(request);
    }

    @Override
    public Boolean deleteObject(String bucket, String objectKey) {
        if (!ossClient.doesObjectExist(bucket, objectKey)) {
            return true;
        }
        ossClient.deleteObject(bucket, objectKey);
        return true;
    }

    @Override
    public void deleteDir(String bucket, String dir) {
        ossClient.deleteDirectory(bucket, dir);
    }

    @Override
    public InputStream getObject(String bucket, String objectKey) {
        return ossClient.getObject(bucket, objectKey).getObjectContent();
    }

    @Override
    public void putObject(String bucket, String objectKey, InputStream input) {
        if (ossClient.doesObjectExist(bucket, objectKey)) {
            throw new RuntimeException(String.format("The filename already exists.为%s", objectKey));
        }
        PutObjectResult objectResult = ossClient.putObject(new PutObjectRequest(bucket, objectKey, input, new ObjectMetadata()));
        log.info("Upload FlighttaskCreateFile: {}", objectResult.getETag());
    }

    public void createClient() {
        if (Objects.nonNull(this.ossClient)) {
            return;
        }
        this.ossClient = new OSSClientBuilder()
                .build(OssConfiguration.endpoint, OssConfiguration.accessKey, OssConfiguration.secretKey);
    }

    @Override
    public Boolean copyObject(String sourceBucket, String destinationBucket, String sourceKey, String destinationKey) {
        CopyObjectResult copyObjectResult = ossClient.copyObject(sourceBucket, sourceKey, destinationBucket, destinationKey);
        return StringUtils.hasText(copyObjectResult.getETag());
    }

    @Override
    public List<String> listObjectKeys(String bucket, String prefix) {

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucket);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        if (Objects.nonNull(objectListing)) {
            List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            return objectSummaries.stream().map(OSSObjectSummary::getKey).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Boolean exists(String bucket, String objectKey) {

        return ossClient.doesObjectExist(bucket, objectKey);
    }
}

