package foo.bar.configclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import foo.bar.configclient.bean.BeanConfiguration;
import foo.bar.configclient.bean.BeanKeycloakConfig;

@RestController
public class ConfigServiceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceController.class);
	
	@Value("${keycloak.url}")
	String url;
	@Value("${keycloak.secret}") 
	String secret;
	@Value("${keycloak.realm}") 
	String realm;
	
	@Autowired
	private Configuration configuration;

	@Autowired
	private ConfigurationData configurationData;


	@GetMapping("/configuration")
	public BeanConfiguration getConfiguration()
	{
		LOGGER.info("Remote Service Url:{}", configurationData.getRemoteServiceUrl());
		LOGGER.info("Max Connections:{}", configurationData.getMaxConnections());
		LOGGER.info("Min Connections:{}", configurationData.getMinConnections());
		return new BeanConfiguration(configurationData.getRemoteServiceUrl(),
			configurationData.getMaxConnections(), configurationData.getMinConnections());
	}

	@GetMapping("/configuration-value")
	public BeanConfiguration getConfiguracionRefresh(@Value("${remoteServiceUrl}") String remoteServiceUrl,
		@Value("${maxConnections}") int maxConnections, @Value("${minConnections}") int minConnections)
	{
		LOGGER.info("Remote Service Url:{}", remoteServiceUrl);
		LOGGER.info("Max Connections:{}", maxConnections);
		LOGGER.info("Min Connections:{}", minConnections);
		return new BeanConfiguration(remoteServiceUrl, maxConnections, minConnections);
	}

	@GetMapping("/keycloak")
	public BeanKeycloakConfig getConfigurationKeycloak()
	{
		LOGGER.info("keycloak.url:{}", configuration.getUrl());
		LOGGER.info("keycloak.secret:{}", configuration.getSecret());
		LOGGER.info("keycloak.realm:{}", configuration.getRealm());
		return new BeanKeycloakConfig(configuration.getUrl(), configuration.getSecret(), configuration.getRealm());
	}
	
	@GetMapping("/keycloak-value")
	public BeanKeycloakConfig getConfigurationKeycloakInner()
	{
		LOGGER.info("keycloak.url:{}", url);
		LOGGER.info("keycloak.secret:{}", secret);
		LOGGER.info("keycloak.realm:{}", realm);
		return new BeanKeycloakConfig(url, secret, realm);
	}	
}
