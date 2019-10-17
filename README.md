# spring-cloud-jdbc
Spring Cloud config server con JDBC y PostgreSQL


## Base de datos

### Base de datos con una tabla de configuración creada que queremos reaprovechar

Suponemos que tenemos una base de datos creada, en un esquema `develop` con una tabla `parameters` que queremos aprovechar para 
nuestro servidor de configuración. Cuyo DDL es el siguiente:

```sql
CREATE TABLE develop."parameter" (
	id int8 NOT NULL,
	default_value varchar(255) NULL,
	name varchar(255) NULL,
	CONSTRAINT parameter_pkey PRIMARY KEY (id),
	CONSTRAINT uk_ll10bbifmynb347ajltx7bi60 UNIQUE (name)
);
```

Primeramente es necesario añadir las siguientes columnas:

* **application**: que coincidira con el nombre de la aplicacion [spring.application.name]
* **profile**: mapea el perfil de la aplicacion [spring.cloud.config.profile], valor por defecto `default`
* **label**: una forma de filtrar por versiones [spring.cloud.config.label], valor por defecto `master`

```sql
ALTER TABLE develop.parameter
ADD application character varying(50);
ALTER TABLE develop.parameter
ADD profile character varying(50);
ALTER TABLE develop.parameter
ADD label character varying(50);
```

Una vez modificada nuestra tabla vamos a llenarla de datos, que luego usaremos para recoger datos en el cliente

```sql
INSERT INTO develop.parameter
("id", "name", "default_value", "application", "profile", "label")
VALUES
(1, 'maxConnections', '10', 'config-client', 'default', 'master'),
(2, 'minConnections', '5', 'config-client', 'default', 'master'),
(3, 'remoteServiceUrl', 'http://server.io', 'config-client', 'default', 'master'),
(4, 'keycloak.url', 'http://keycloak.io/auth', 'config-client', 'default', 'master'),
(5, 'keycloak.secret', '27c939cd-fe78-4560-9a9b-aae64a50527f', 'config-client', 'default', 'master'),
(6, 'keycloak.realm', 'my-realm', 'config-client', 'default', 'master');
```

### Base de datos vacía
En este caso usaremos podemos usar la siguiente tabla:

```sql
CREATE TABLE public.properties
(
    application character varying(50) COLLATE pg_catalog."default",
    profile character varying(50) COLLATE pg_catalog."default",
    label character varying(50) COLLATE pg_catalog."default",
    key character varying(50) COLLATE pg_catalog."default",
    value character varying(500) COLLATE pg_catalog."default"
);

```

por lo que no sera necesario definir la propiedad `cloud.config.server.jdbc.sql` dentro del fichero `application.yaml` o podemos cambiarlo a:

```yml
spring:
  cloud:
    config:
      server:
        jdbc:
          sql: SELECT key, value from properties where application=? and profile=? and label=?

```

## Config Server

Arrancamos el server con: `mvn clean package spring-boot:run`

El servidor expone los siguientes servicios rest:

```
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```
<http://localhost:8888/config-client/default/label>
<http://localhost:8888/config-client-default.yml>
<http://localhost:8888/master/config-client-default.yml>
<http://localhost:8888/config-client-default.properties>
<http://localhost:8888/master/config-client-default.properties>


Ejemplo:

**`curl http://localhost:8888/config-client/default`** 

```json
{
  "name": "config-client",
  "profiles": [
    "default"
  ],
  "label": null,
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "config-client-default",
      "source": {
        "maxConnections": "10",
        "minConnections": "5",
        "remoteServiceUrl": "http://server.io",
        "keycloak.url": "http://keycloak.io/auth",
        "keycloak.secret": "27c939cd-fe78-4560-9a9b-aae64a50527f",
        "keycloak.realm": "my-realm"
      }
    }
  ]
}
```

## Config Client

Arrancamos el cliente con: `mvn clean package spring-boot:run`

Una vez arrancado podemos acceder a los distintos servicios rest expuestos:

<http://localhost:8080/configuration>
```json
{
  "maxConnections": 10,
  "minConnections": 5,
  "remoteServiceUrl": "http://server.io"
}
```
<http://localhost:8080/configuration-value>
```json
{
  "maxConnections": 10,
  "minConnections": 5,
  "remoteServiceUrl": "http://server.io"
}
```
<http://localhost:8080/keycloak-value>
```json
{
  "url": "http://keycloak.io/auth",
  "secret": "27c939cd-fe78-4560-9a9b-aae64a50527f",
  "realm": "my-realm"
}
```
<http://localhost:8080/keycloak-value>
```json
{
  "url": "http://keycloak.io/auth",
  "secret": "27c939cd-fe78-4560-9a9b-aae64a50527f",
  "realm": "my-realm"
}
```

### Recarga de variables
Si cambiamos algún valor en bbdd, y queremos refrescarlos en el cliente es necesario recargarlo invocando a la url `/actuactor/refresh` mediante el método **POST**:  
 
`curl http://localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json"`


# Referencias
<https://www.devglan.com/spring-cloud/jdbc-backend-spring-cloud-config>
<https://medium.com/@kishansingh.x/spring-cloud-config-server-with-jdbc-backend-a8a629846115>
<>

# TODO

[ ] Encriptacion de valores. <https://www.devglan.com/spring-cloud/encrypt-decrypt-cloud-config-properties>