package foo.bar.configclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
//El name del registro de base de datos tiene como valor limites.VERSION
//Al poner limites en ConfigurationProperties estamos filtrando los nombres que empiezan por ese valor
@ConfigurationProperties("keycloak")
public class Configuration {
	private String url;
	private String secret; 
	private String realm;
}
