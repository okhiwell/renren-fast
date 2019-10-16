//package io.renren.datasources;
//
//
//import javax.sql.DataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
///**
// * @author CT
// * @date 2019/6/2122:14
// * @describe BaseDataSourceConfig用于
// */
//
//@Configuration
//@MapperScan(basePackages = "io.renren.modules.*.dao", sqlSessionTemplateRef = "baseSqlSessionTemplate")
//public class BaseDataSourceConfig {
//    @Bean(name = "baseDataSource")
//    @ConfigurationProperties("spring.datasource.druid.first")
//    @Primary
//    public DataSource setDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "baseTransactionManager")
//    @Primary
//    public DataSourceTransactionManager setTransactionManager(@Qualifier("baseDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager();
//    }
//
//    @Bean(name = "baseSqlSessionFactory")
//    @Primary
//    public SqlSessionFactory setSqlSessionFactory(@Qualifier("baseDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
//        return bean.getObject();
//    }
//
//    @Bean(name = "baseSqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("baseSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}