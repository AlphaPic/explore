package com.fan.distributedlock.config;

import com.fan.distributedlock.config.mapper.MethodNameMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author:fanwenlong
 * @date:2018-05-07 14:08:54
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component
public class MybatisConfig {
    private static Logger logger = Logger.getLogger(MybatisConfig.class);

    String MYSQL_DRIVER        = "com.mysql.jdbc.Driver";                  /** mysql驱动名称 */
    String MYSQL_URL           = "jdbc:mysql://10.112.2.17:3306/distributor"; /** mysql驱动url-monitor */
    String MYSQL_URL_MOVIE     = "jdbc:mysql://10.112.2.17:3306/movie";   /** mysql驱动url-movie */
    String MYSQL_USERNAME      = "root";                                   /** mysql用户名 */
    String MYSQL_PASSWORD      = "xiwang";                                 /** mysql密码 */

    String MYSQL_ENVIRONMENT   = "mysqlEnvironment";                       /** mysql的环境名称 */
    /** 数据库的属性配置 */
    @Bean(name = "MysqlProperties")
    public Properties getMysqlProperties(){
        Properties properties = new Properties();
        properties.setProperty("driver", MYSQL_DRIVER);
        properties.setProperty("url",MYSQL_URL);
        properties.setProperty("username",MYSQL_USERNAME);
        properties.setProperty("password",MYSQL_PASSWORD);
        return properties;
    }

    /** 获取数据源 */
    @Bean
    public DataSource getBasicDataSource(@Qualifier("MysqlProperties") Properties properties){
        DataSource dataSource = null;
        try{
            dataSource = new PooledDataSource(properties.getProperty("driver"),
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password"));
        }catch (Exception e){
            logger.error("初始化数据源失败" + e.getMessage());
        }
        return dataSource;
    }

    /** 获取事务 */
    @Bean
    public TransactionFactory getTransactionFactory(){
        return new JdbcTransactionFactory();
    }

    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager(DataSource dataSource){
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }


    /** 会话工厂的配置 */
    @Bean
    public org.apache.ibatis.session.Configuration getSqlConfiguration(TransactionFactory transactionFactory,DataSource dataSource){
        Environment environment = new Environment(MYSQL_ENVIRONMENT,transactionFactory,dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setEnvironment(environment);
        /** 扫描包中的所有映射文件 */
        configuration.addMappers("com.fan.distributedlock.config.mapper");
        return configuration;
    }

    /** 创建sql会话工厂 */
    @Bean(name = "MysqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(org.apache.ibatis.session.Configuration configuration){
        try {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            return sqlSessionFactory;
        } catch (Exception e) {
            logger.error("初始化工厂失败" + e.getMessage());
            return null;
        }
    }

    /** 创建会话 */
    @Bean
    public SqlSession getMySqlSqlSession(@Qualifier("MysqlSessionFactory") SqlSessionFactory sf){
        SqlSession sqlSession = sf.openSession();
        return sqlSession;
    }

    /** 一个简单的测试 */
    public static void main(String[] args) throws SQLException {
        MybatisConfig data = new MybatisConfig();
        org.apache.ibatis.session.Configuration configuration = data.getSqlConfiguration(data.getTransactionFactory(),data.getBasicDataSource(data.getMysqlProperties()));
//        configuration.addMapper(MethodNameMapper.class);
        SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession session = data.getMySqlSqlSession(sf);
        System.out.println(session.getConnection().isClosed());

        MethodNameMapper mapper = session.getMapper(MethodNameMapper.class);
        System.out.println(mapper.getMethodName("hello").getDescription());

    }
}
