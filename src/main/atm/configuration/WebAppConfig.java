package main.atm.configuration;

import java.util.List;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@ComponentScan(basePackages = "main.atm.presentation.controller")
public class WebAppConfig extends DelegatingWebMvcConfiguration {

	/* Property Fields */
	private int asyncExecutorCorePoolSize;
	private int asyncExecutorMaxPoolSize;
	private long asyncExecutorDefaultTimeoutMs;

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

	/* Overridden Methods */

	/*
	* (non-Javadoc)
	* @see
	* org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addResourceHandlers(org.springframework
	* .web.servlet.config.annotation.ResourceHandlerRegistry)
	*/
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// map static content URLs to the appropriate location on disk
		registry.addResourceHandler("/content/**").addResourceLocations("WEB-INF/content/");
	}

	/*
	* (non-Javadoc)
	* @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureAsyncSupport(org.
	* springframework .web.servlet.config.annotation.AsyncSupportConfigurer)
	*/
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		super.configureAsyncSupport(configurer);
		configurer.setTaskExecutor(this.asyncTaskExecutor());
		configurer.setDefaultTimeout(this.asyncExecutorDefaultTimeoutMs);
	}
	
	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.addDefaultHttpMessageConverters(converters);
		for (HttpMessageConverter<?> converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
				mapper.configure(MapperFeature.USE_ANNOTATIONS, true);
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
				mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
				mapper.setSerializationInclusion(Include.NON_NULL);
			}
		}
	}

	/* Property Setters */

	@Value("${async.web.default.timeout.ms}")
	void setAsyncExecutorDefaultTimeoutMs(int asyncExecutorDefaultTimeoutMs) {
		this.asyncExecutorDefaultTimeoutMs = asyncExecutorDefaultTimeoutMs;
	}

	@Value("${async.web.core.pool.size}")
	void setAsyncExecutorCorePoolSize(int asyncExecutorCorePoolSize) {
		this.asyncExecutorCorePoolSize = asyncExecutorCorePoolSize;
	}

	@Value("${async.web.max.pool.size}")
	void setAsyncExecutorMaxPoolSize(int asyncExecutorMaxPoolSize) {
		this.asyncExecutorMaxPoolSize = asyncExecutorMaxPoolSize;
	}

	/* Bean Definitions */

	@Bean
	InternalResourceViewResolver setupViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean(destroyMethod = "shutdown")
	AsyncTaskExecutor asyncTaskExecutor() {
		ThreadPoolTaskExecutor pooledTaskExecutor = new ThreadPoolTaskExecutor();
		pooledTaskExecutor.setCorePoolSize(this.asyncExecutorCorePoolSize);

		// only set if it is at least zero and at least the same size as the core pool size, default otherwise
		if (this.asyncExecutorMaxPoolSize > -1 && this.asyncExecutorMaxPoolSize >= this.asyncExecutorCorePoolSize) {
			pooledTaskExecutor.setMaxPoolSize(this.asyncExecutorMaxPoolSize);
		}

		return pooledTaskExecutor;
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:dispense");
	}
}
