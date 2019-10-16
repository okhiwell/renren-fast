//package io.renren.datasources;
//
//
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
//import javax.sql.DataSource;
//
///**
// * @author CT
// * @date 2019/6/2122:14
// * @describe secondDataSourceConfig用于
// */
//
//@Configuration
//@MapperScan(basePackages = "io.renren.modules.test.dao", sqlSessionTemplateRef = "secondSqlSessionTemplate")
//public class SecondDataSourceConfig {
//    @Bean(name = "secondDataSource")
//    @ConfigurationProperties("spring.datasource.druid.second")
//    public DataSource setDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "secondTransactionManager")
//    public DataSourceTransactionManager setTransactionManager(@Qualifier("secondDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "secondSqlSessionFactory")
//    public SqlSessionFactory setSqlSessionFactory(@Qualifier("secondDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
//        return bean.getObject();
//    }
//
//    @Bean(name = "secondSqlSessionTemplate")
//    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("secondSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}