package com.skysys.service.component.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.skysys.service.handler.DefaultDBFieldHandler;
import com.skysys.service.utils.SpringUtils;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
// 配置mybatis的接口类放的地方
@MapperScan(basePackages = "com.skysys.service.mapper.slave1", sqlSessionTemplateRef = "slave1SqlSessionTemplate")
public class Slave1DataSourceConfig {
    @Resource
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    @Value("${mybatis-plus.typeAliasesPackage}")
    private String typeAliasesPackage;

    /**
     * 创建数据源
     */
    @Bean(name = "slave1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource slave1DataSource() throws SQLException {

        // 选择 druid 数据源
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();

        // 启用 stat 过滤器
        druidDataSource.setFilters("stat");
        return druidDataSource;
    }

    /**
     * 创建SqlSessionFactory
     *
     * @Qualifier表示查找Spring容器中名字为slave1DataSource的对象
     */
    @Bean(name = "slave1SqlSessionFactory")
    public SqlSessionFactory slave1SqlSessionFactory(
            @Qualifier("slave1DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(SpringUtils.getBean(MybatisConfiguration.class));
        bean.setTypeAliasesPackage(typeAliasesPackage);

        // 设置全局配置 自定义sql注入
        // 获取mybatis-plus全局配置
        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        bean.setGlobalConfig(globalConfig);

        // 添加mybatisPlus插件
        bean.setPlugins(mybatisPlusInterceptor);
        return bean.getObject();
    }

    /**
     * 创建事务管理
     */
    @Bean(name = "slave1TransactionManager")
    public DataSourceTransactionManager slave1TransactionManager(@Qualifier("slave1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建SqlSessionTemplate
     */
    @Bean(name = "slave1SqlSessionTemplate")
    public SqlSessionTemplate slave1SqlSessionTemplate(
            @Qualifier("slave1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}