package com.skysys.service.handler;

import com.alibaba.fastjson2.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 基于fastjson2的JSON数组类型处理器
 */
public class Fastjson2ArrayTypeHandler extends BaseTypeHandler<Double[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Double[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public Double[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseArray(json);
    }

    @Override
    public Double[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseArray(json);
    }

    @Override
    public Double[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseArray(json);
    }

    private Double[] parseArray(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return JSON.parseObject(json, Double[].class);
        } catch (Exception e) {
            return null;
        }
    }
}
