package com.skysys.service.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author tth
 * @description 默认数据库字段填充
 */
@Component
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String now = DateUtil.now();
        this.strictInsertFill(metaObject, "createTime", String.class, now);
        this.strictInsertFill(metaObject, "updateTime", String.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String now = DateUtil.now();
        this.strictUpdateFill(metaObject, "updateTime", String.class, now);
    }
}