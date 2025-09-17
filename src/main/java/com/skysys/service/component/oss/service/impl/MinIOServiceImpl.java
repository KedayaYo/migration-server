package com.skysys.service.component.oss.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.skysys.service.component.oss.OssConfiguration;
import com.skysys.service.component.oss.model.CredentialsToken;
import com.skysys.service.component.oss.model.OssTypeEnum;
import com.skysys.service.component.oss.service.IOssService;
import io.minio.*;
import io.minio.credentials.AssumeRoleProvider;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: wangrui
 * @date: 2023/12/20 0:09
 * @description:
 */
@Service
@Slf4j
public class MinIOServiceImpl implements IOssService {

    private MinioClient client;

    @Override
    public OssTypeEnum getOssType() {
        return OssTypeEnum.MINIO;
    }

    @Override
    public CredentialsToken getCredentials() {
        try {
            AssumeRoleProvider provider = new AssumeRoleProvider(OssConfiguration.endpoint, OssConfiguration.accessKey, OssConfiguration.secretKey, Math.toIntExact(OssConfiguration.expire), null, OssConfiguration.region, null, null, null, null);
            return new CredentialsToken(provider.fetch(), OssConfiguration.expire);
        } catch (NoSuchAlgorithmException e) {
            log.debug("获取sts失败。");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CredentialsToken getCredentials(String path) {
        return null;
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey) {

        return this.getObjectUrl(bucket, objectKey, OssConfiguration.expire);
    }

    @SneakyThrows
    @Override
    public URL getObjectUrl(String bucket, String objectKey, Long expiration) {
        try {
            return new URL(client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucket).object(objectKey).expiry(Math.toIntExact(expiration)).build()));
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException |
                 NoSuchAlgorithmException | XmlParserException | ServerException e) {
            throw new RuntimeException("OssConfiguration上不存在该文件。");
        }
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey, Long expiration, Map<String, String> extraQueryParams) {
        try {
            return new URL(client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry(Math.toIntExact(expiration))
                            .extraQueryParams(extraQueryParams)
                            .build()));
        } catch (Exception e) {
            throw new RuntimeException("生成文件URL失败", e);
        }
    }

    @Override
    public Boolean deleteObject(String bucket, String objectKey) {
        try {
            client.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectKey).build());
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            log.error("Failed to delete file.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @SneakyThrows
    @Override
    public void deleteDir(String bucket, String dir) {

        // 获取文件夹中的所有对象
        Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(dir).recursive(true).build());

        // 构建要删除的对象列表
        List<DeleteObject> objectsToDelete = new ArrayList<>();
        for (Result<Item> result : objects) {
            Item item = result.get();
            objectsToDelete.add(new DeleteObject(item.objectName()));
        }

        // 删除文件夹中的所有对象
        client.removeObjects(RemoveObjectsArgs.builder().bucket(bucket).objects(objectsToDelete).build());
    }

    @SneakyThrows
    @Override
    public InputStream getObject(String bucket, String objectKey) {
        try {
            GetObjectResponse object = client.getObject(GetObjectArgs.builder().bucket(bucket).object(objectKey).build());
            return new ByteArrayInputStream(object.readAllBytes());
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException |
                 NoSuchAlgorithmException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }
        return InputStream.nullInputStream();
    }

    @Override
    public void putObject(String bucket, String objectKey, InputStream input) {
        try {

            // 检查Bucket是否存在，不存在则创建
            boolean isExist = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!isExist) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("创建 bucket: {}", bucket);
            }

            client.statObject(StatObjectArgs.builder().bucket(bucket).object(objectKey).build());
            throw new RuntimeException("文件名已存在。");
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.info("文件不存在，请开始上传。");
            try {
                ObjectWriteResponse response = client.putObject(PutObjectArgs.builder().bucket(bucket).object(objectKey).stream(input, input.available(), 0).build());
                log.info("上传: {}", response.etag());
            } catch (MinioException | IOException | InvalidKeyException |
                     NoSuchAlgorithmException ex) {
                log.error("上传 {} 失败，原因：{}", objectKey, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void createClient() {
        if (Objects.nonNull(this.client)) {
            return;
        }
        this.client = MinioClient.builder().endpoint(OssConfiguration.endpoint).credentials(OssConfiguration.accessKey, OssConfiguration.secretKey).region(OssConfiguration.region).build();
    }

    @SneakyThrows
    @Override
    public Boolean copyObject(String sourceBucket, String destinationBucket, String sourceKey, String destinationKey) {
        ObjectWriteResponse objectWriteResponse = null;
        try {
            objectWriteResponse = client.copyObject(CopyObjectArgs.builder().object(destinationKey).bucket(destinationBucket).source(CopySource.builder().bucket(sourceBucket).object(sourceKey).build()).build());
        } catch (ErrorResponseException | XmlParserException | ServerException |
                 NoSuchAlgorithmException | IOException | InvalidResponseException |
                 InvalidKeyException | InternalException | InsufficientDataException e) {
            e.printStackTrace();
        }
        return Objects.nonNull(objectWriteResponse);
    }

    @Override
    public List<String> listObjectKeys(String bucket, String prefix) {
        Iterable<Result<Item>> results = client.listObjects(ListObjectsArgs.builder().bucket(bucket).prefix(prefix).maxKeys(1000).build());

        ArrayList<String> ObjectKeys = new ArrayList<>();
        results.forEach(itemResult -> {
            try {
                String objectName = itemResult.get().objectName();
                ObjectKeys.add(objectName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        log.info("minio的ObjectKeys为{}", JSONObject.toJSONString(ObjectKeys));
        return ObjectKeys;
    }

    @Override
    public Boolean exists(String bucket, String objectKey) {

        try {
            client.statObject(StatObjectArgs.builder().bucket(bucket).object(objectKey).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

