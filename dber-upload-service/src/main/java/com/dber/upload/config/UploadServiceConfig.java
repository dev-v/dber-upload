package com.dber.upload.config;

import com.dber.base.mybatis.plugin.pagination.PaginationInterceptor;
import com.dber.base.util.DBUtil;
import com.dber.cache.config.CacheConfig;
import com.dber.config.SystemConfig;
import com.dber.upload.server.config.UploadServerConfig;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * <li>文件名称: UploadService.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月21日
 */
@Configuration
@EnableConfigurationProperties({SystemConfig.class})
@EnableAutoConfiguration
@EnableTransactionManagement
@Import({CacheConfig.class, UploadServerConfig.class})
@ComponentScan("com.dber.upload.service")
@MapperScan(basePackages = {"com.dber.upload.mapper"})
public class UploadServiceConfig {
    @Autowired
    private SystemConfig systemConfig;

    @Bean
    public DataSource uploadDataSource() {
        DataSource uploadDataSource = DBUtil.dataSource(systemConfig.getService().getDatabase());
        return uploadDataSource;
    }

    @Bean
    public DataSourceTransactionManager uploadTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(uploadDataSource());
        return transactionManager;
    }

    @Bean
    public org.apache.ibatis.session.Configuration uploadMybatisConfiguration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.getTypeAliasRegistry().registerAliases("com.dber.upload.api.entity");
        return configuration;
    }

    @Bean
    public SqlSessionFactoryBean uploadSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean uploadSqlSessionFactoryBean = new SqlSessionFactoryBean();

        uploadSqlSessionFactoryBean.setDataSource(uploadDataSource());

        uploadSqlSessionFactoryBean.setConfiguration(uploadMybatisConfiguration());

        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        uploadSqlSessionFactoryBean
                .setMapperLocations(resourceResolver.getResources("classpath*:/mapper/*_mapper.xml"));

        Interceptor[] interceptors = {PaginationInterceptor.getInstance()};
        uploadSqlSessionFactoryBean.setPlugins(interceptors);

        return uploadSqlSessionFactoryBean;
    }
}
