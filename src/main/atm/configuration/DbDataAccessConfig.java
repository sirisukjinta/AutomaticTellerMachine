package main.atm.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import main.atm.dataaccess.AutomaticTellerMachineDao;
import main.atm.util.KeyValuePair;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class DbDataAccessConfig extends BaseDataAccessConfig {
	
	/* Property Fields */

	private String atmDbDriverClassName;
	private String atmDbUrl;
	private String atmDbUsername;
	private String atmDbPassword;
	private String atmDbInitSql;
	private String atmDbDriverProps;
	private long atmDbConnMaxAgeMilliSec;
	private int atmDbInitialConnections;
	private int atmDbMaxTotalConnections;
	private int atmDbIdleConnectionsMax;
	private int atmDbIdleConnectionsMin;
	
	/* Bean Definitions */
	@Bean
	public DataSource pooledDataSourceAtmDb() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDefaultAutoCommit(true);
		dataSource.setDefaultReadOnly(true);
		dataSource.setDriverClassName(atmDbDriverClassName);
		dataSource.setUrl(atmDbUrl);
		dataSource.setUsername(atmDbUsername);
		dataSource.setPassword(atmDbPassword);
		dataSource.setInitialSize(atmDbInitialConnections);
		dataSource.setMaxTotal(atmDbMaxTotalConnections);
		dataSource.setMaxIdle(atmDbIdleConnectionsMax);
		dataSource.setMinIdle(atmDbIdleConnectionsMin);
		
		dataSource.setMaxConnLifetimeMillis(atmDbConnMaxAgeMilliSec);

		// set initialization SQL, if any
		if (StringUtils.isNotBlank(atmDbInitSql)) {
			List<String> initSqls = new ArrayList<String>(1);
			initSqls.add(atmDbInitSql);
			dataSource.setConnectionInitSqls(initSqls);
		}

		// Add connection properties, if any
		List<KeyValuePair<String, String>> props = buildPropertiesFromString(this.atmDbDriverProps);
		if (props != null) {
			for (KeyValuePair<String, String> kvp : props)
				dataSource.addConnectionProperty(kvp.getKey(), kvp.getValue());
		}

		return dataSource;
	}
	
	@Bean
	public NamedParameterJdbcOperations namedTemplateForPooledDataSourceAtm() {
		NamedParameterJdbcOperations jdbcOperations = new NamedParameterJdbcTemplate(this.pooledDataSourceAtmDb());
		return jdbcOperations;
	}

	@Bean
	AutomaticTellerMachineDao automaticTellerMachineDao() {
		return new AutomaticTellerMachineDao(namedTemplateForPooledDataSourceAtm());
	}
	
	@Bean
	public CommonsMultipartResolver multipartResolver(){
		CommonsMultipartResolver resolver =  new CommonsMultipartResolver();
		resolver.setMaxUploadSize(10000000);
		return resolver;
		
	}

	/* Property Assignments */
	
	@Value("${atm.db.driverName}")
	void setAtmDbDriverClassName(String atmDbDriverClassName) {
		this.atmDbDriverClassName = atmDbDriverClassName;
	}

	@Value("${atm.db.url}")
	void setAtmDbUrl(String atmDbUrl) {
		this.atmDbUrl = atmDbUrl;
	}

	@Value("${atm.db.username}")
	void setAtmDbUsername(String atmDbUsername) {
		this.atmDbUsername = atmDbUsername;
	}

	@Value("${atm.db.password}")
	void setAtmDbPassword(String atmDbPassword) {
		this.atmDbPassword = atmDbPassword;
	}
	
	@Value("${atm.db.initial.connections}")
	void setAtmDbInitialConnections(int atmDbInitialConnections) {
		this.atmDbInitialConnections = atmDbInitialConnections;
	}

	@Value("${atm.db.total.max.connections}")
	void setAtmDbMaxTotalConnections(int atmDbMaxTotalConnections) {
		this.atmDbMaxTotalConnections = atmDbMaxTotalConnections;
	}

	@Value("${atm.db.idle.max.connections}")
	void setAtmDbIdleConnectionsMax(int atmDbIdleConnectionsMax) {
		this.atmDbIdleConnectionsMax = atmDbIdleConnectionsMax;
	}
	
	@Value("${atm.db.idle.min.connections}")
	void setAtmDbIdleConnectionsMin(int atmDbIdleConnectionsMin) {
		this.atmDbIdleConnectionsMin = atmDbIdleConnectionsMin;
	}

	@Value("${atm.db.connection.max.age.minutes}")
	void setAtmDbMaxAgeMinutes(int atmDbMaxAgeMinutes) {
		this.atmDbConnMaxAgeMilliSec = atmDbMaxAgeMinutes * 60 * 1000;
	}

	@Value("${atm.db.driver.props}")
	void setAtmDbDriverProps(String atmDbDriverProps) {
		this.atmDbDriverProps = atmDbDriverProps;
	}

	@Value("${atm.db.init.sql}")
	void setAtmDbInitSql(String atmDbInitSql) {
		this.atmDbInitSql = atmDbInitSql;
	}
	
}
