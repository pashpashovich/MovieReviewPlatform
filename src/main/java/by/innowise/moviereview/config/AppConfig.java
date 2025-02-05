package by.innowise.moviereview.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan("by.innowise")
@EnableJpaRepositories("by.innowise.moviereview.repository")
@EnableTransactionManagement
public class AppConfig {

    private final Map<String, Object> yamlProperties;

    public AppConfig() {
        try (InputStream input = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("application.yml"))) {
            org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
            this.yamlProperties = yaml.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить application.yml", e);
        }
    }

    private Map<String, Object> getNestedMap(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> currentMap = yamlProperties;
        for (String key : keys) {
            currentMap = (Map<String, Object>) currentMap.get(key);
            if (currentMap == null) {
                throw new IllegalStateException("Не удалось найти путь: " + path);
            }
        }
        return currentMap;
    }

    @Bean
    public DataSource dataSource() {
        Map<String, Object> datasource = getNestedMap("spring.datasource");
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName((String) datasource.get("driver-class-name"));
        hikariConfig.setJdbcUrl((String) datasource.get("url"));
        hikariConfig.setUsername((String) datasource.get("username"));
        hikariConfig.setPassword((String) datasource.get("password"));
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setConnectionTimeout(30000);
        return new HikariDataSource(hikariConfig);
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Map<String, Object> jpa = getNestedMap("spring.jpa");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        Map<String, Object> hibernateProperties = (Map<String, Object>) jpa.get("properties");
        if (hibernateProperties != null) {
            jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            jpaProperties.put("hibernate.hbm2ddl.auto", "validate");
        }

        factoryBean.setJpaProperties(jpaProperties);
        factoryBean.setPackagesToScan("by.innowise.moviereview.entity");
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

}
