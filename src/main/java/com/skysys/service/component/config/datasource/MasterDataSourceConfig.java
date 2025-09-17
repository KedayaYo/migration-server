package com.skysys.service.component.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
// 配置mybatis的接口类放的地方
@MapperScan(basePackages = "com.skysys.service.mapper.master", sqlSessionTemplateRef = "masterSqlSessionTemplate")
public class MasterDataSourceConfig {
    @Resource
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    @Value("${mybatis-plus.typeAliasesPackage}")
    private String typeAliasesPackage;

    /**
     * 创建数据源
     */
    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() throws SQLException {

        // 选择 druid 数据源
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();

        // 启用 stat 过滤器
        druidDataSource.setFilters("stat");
        return druidDataSource;
    }

    /**
     * 创建SqlSessionFactory
     *
     * @Qualifier表示查找Spring容器中名字为masterDataSource的对象
     */
    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(
            @Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(getCfg());
        bean.setTypeAliasesPackage(typeAliasesPackage);

        // 设置全局配置 自定义sql注入
        GlobalConfig globalConfig = new GlobalConfig();
        bean.setGlobalConfig(globalConfig);

        // 添加mybatisPlus插件
        bean.setPlugins(mybatisPlusInterceptor);
        return bean.getObject();
    }

    /**
     * 创建mybatis-plus配置
     */
    @Bean
    @Scope(value = "prototype")
    @ConfigurationProperties(prefix = "mybatis-plus.configuration")
    public MybatisConfiguration getCfg() {
        return new MybatisConfiguration();
    }

    /**
     * 创建事务管理
     */
    @Primary
    @Bean(name = "masterTransactionManager")
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建SqlSessionTemplate
     */
    @Primary
    @Bean(name = "masterSqlSessionTemplate")
    public SqlSessionTemplate masterSqlSessionTemplate(
            @Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}