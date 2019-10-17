package foo.bar.configclient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BeanConfiguration {
	private int  maxConnections;
	private int minConnections;	
	private String remoteServiceUrl;
	
	public  BeanConfiguration(String remoteServiceUrl,int maxConnections,int minConnections)
	{
		this.remoteServiceUrl=remoteServiceUrl;
		this.maxConnections=maxConnections;
		this.minConnections=minConnections;
	}
}
