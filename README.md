## Starting the Database with Docker

To launch the database, execute the following command in the terminal:

```sh
docker-compose up
```

## Configuration Profiles

There are two main configuration profiles used in this project: `dev` and `test`. Each profile has its own properties file.

### `application-dev.properties`

This is the configuration file for the development environment. Create a file named `application-dev.properties` in the `src/main/resources` directory with the following content:

```properties
SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3306
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
USER_REGISTRATION_MIN_AGE=18
DB_NAME=microservices
```

### `application-test.properties`

This is the configuration file for the development environment. Create a file named `application-test.properties` in the `src/test/resources` directory with the following content:

```properties
SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3306
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
USER_REGISTRATION_MIN_AGE=18
DB_NAME=microservices