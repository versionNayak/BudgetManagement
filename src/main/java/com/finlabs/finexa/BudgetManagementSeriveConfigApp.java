package com.finlabs.finexa;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.genericDao.GenericDao;
import com.finlabs.finexa.model.CacheInfoDTO;
import com.finlabs.finexa.model.MasterFinexaException;
import com.finlabs.finexa.model.MasterProductClassification;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class BudgetManagementSeriveConfigApp extends SpringBootServletInitializer {
	private EntityManager entityManager;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BudgetManagementSeriveConfigApp.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BudgetManagementSeriveConfigApp.class);
	}

	@Bean
	@Autowired
	public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(false);

		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.show_sql", "false");
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		localContainerEntityManagerFactoryBean.setPackagesToScan("com.finlabs.finexa");
		localContainerEntityManagerFactoryBean.setDataSource(dataSource);
		localContainerEntityManagerFactoryBean.setJpaProperties(jpaProperties);
		localContainerEntityManagerFactoryBean.afterPropertiesSet();
		this.entityManager = localContainerEntityManagerFactoryBean.getObject().createEntityManager();
		return localContainerEntityManagerFactoryBean.getObject();
	}

	@Bean

	@DependsOn("entityManagerFactory")
	public SessionFactory sessionfactory() {
		Session session = entityManager.unwrap(Session.class);
		SessionFactory sessionFactory = session.getSessionFactory();
		return sessionFactory;
	}

	/*
	 * @Bean
	 * 
	 * @Autowired public PlatformTransactionManager
	 * transactionManager(EntityManagerFactory entityManagerFactory) {
	 * JpaTransactionManager jpaTransactionManager = new
	 * JpaTransactionManager();
	 * jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
	 * return jpaTransactionManager; }
	 */

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionfactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionfactory);
		return txManager;
	}

	@Bean
	@Autowired
	public Mapper mapper() {
		return new DozerBeanMapper();
	}

	@Bean
	public MasterProductClassification masterProductClassification() {
		return new MasterProductClassification();
	}

	@Bean
	public GenericDao genericDao() {

		return new GenericDao();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost", "http://192.168.1.110",
						"https://192.168.1.110","http://testing.finexa.in","https://testing.finexa.in","https://staging.finexa.in","https://app.finexa.in","http://app.finexa.in","https://10.52.218.238","http://10.52.218.238");
			}
		};
	}

	@Bean
	public Map<String, String> exceptionmap() {
		@SuppressWarnings("unchecked")
		List<MasterFinexaException> listMasterFinexaException = entityManager
				.createQuery("SELECT mFinexaExp FROM MasterFinexaException mFinexaExp").getResultList();

		// masterFinexaExceptionRepository.findAll();
		Map<String, String> exceptionMap = listMasterFinexaException.stream()
				.collect(Collectors.toMap(mfe -> mfe.getErrorCode(), mfe -> mfe.getErrorDescription()));
		return exceptionMap;

	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.setUsePool(true);
		return factory;
	}

	@Bean
	RedisTemplate<String, CacheInfoDTO> redisTemplate() {
		RedisTemplate<String, CacheInfoDTO> redisTemplate = new RedisTemplate<String, CacheInfoDTO>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	CacheManager cacheManager() {
		return new RedisCacheManager(redisTemplate());
	}

	@Bean
	public CacheInfoService cacheInfoService() {

		return new CacheInfoService();
	}
}
