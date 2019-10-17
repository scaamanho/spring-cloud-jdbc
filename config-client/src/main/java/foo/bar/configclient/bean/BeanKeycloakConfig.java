package foo.bar.configclient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BeanKeycloakConfig {
	private String  url;
	private String secret;	
	private String realm;
	
	public  BeanKeycloakConfig(String url,String secret,String realm)
	{
		this.url=url;
		this.secret=secret;
		this.realm=realm;
	}
}