package main.atm.configuration;

import java.io.IOException;

import main.atm.service.AutomaticTellerMachineService;
import main.atm.service.DozerMapperService;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@Import(DbDataAccessConfig.class)
public class AppConfig {
	static final String CRYPT_PASSWORD = "HBqY8OBnsiFQViZGDtTpUg==";
	static final String CRYPT_ALGORITHM_NAME = "PBEWithMD5AndDES";

	@Autowired
	private DbDataAccessConfig dbDataAccessConfig;
	/*
	* Normally never static. This is the exception to the rule
	*/
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholder() {
		StandardPBEStringEncryptor cryptProvider = new StandardPBEStringEncryptor();
		SimpleStringPBEConfig cryptConfig = new SimpleStringPBEConfig();
		cryptConfig.setPassword(AppConfig.CRYPT_PASSWORD);
		cryptConfig.setAlgorithm(AppConfig.CRYPT_ALGORITHM_NAME);
		cryptProvider.setConfig(cryptConfig);
		cryptProvider.initialize(); // call initialize otherwise it won't work
		PropertyPlaceholderConfigurer propertyPlaceholder = new EncryptablePropertyPlaceholderConfigurer(cryptProvider);
		return propertyPlaceholder;
	}
	
	@Bean
	public DozerMapperService dozerMapperService(){
		return new DozerMapperService();
	}
	
	@Bean()
	public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean(){
		DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
		Resource[] resources = null;
		try {
			resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/*mapping.xml");
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		dozerBeanMapperFactoryBean.setMappingFiles(resources);
		return dozerBeanMapperFactoryBean;
	}

	@Bean
	AutomaticTellerMachineService automaticTellerMachineService() {
		return new AutomaticTellerMachineService(dbDataAccessConfig.automaticTellerMachineDao());
	}
}
