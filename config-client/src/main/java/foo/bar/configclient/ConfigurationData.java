package foo.bar.configclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties()
public class ConfigurationData {

	private String remoteServiceUrl;
	private int minConnections;
	private int maxConnections;	
}