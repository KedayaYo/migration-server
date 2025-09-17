package com.skysys.service.model.vo;

/**
 * 数据迁移结果
 * @author: 陶添浩
 * @date: 2025/9/17 16:35:00
 */
public class MigrationResultVO {
    private boolean success;
    private int totalCount;
    private int successCount;
    private int failedCount;
    private String message;

    // 构造器
    public MigrationResultVO() {}

    public MigrationResultVO(boolean success, int totalCount, int successCount, int failedCount, String message) {
        this.success = success;
        this.totalCount = totalCount;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
