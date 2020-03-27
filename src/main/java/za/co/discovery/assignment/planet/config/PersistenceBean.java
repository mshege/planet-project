package za.co.discovery.assignment.planet.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class PersistenceBean {

    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource, @Qualifier("hibernateProperties") Properties properties) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("za.co.discovery.assignment.planet.entity");
        sessionFactory.setHibernateProperties(properties);
        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    @Qualifier("hibernateProperties")
    public Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("connection.release_mode", "AFTER_TRANSACTION");
        properties.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
        properties.setProperty("rewriteBatchedStatements", "true");
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
//        properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
        return properties;
    }
}

