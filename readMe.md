# matera talent pool
Repository for a sample code project

## minimum requirements
 - [Java 8]([https://www.java.com/en/](https://www.java.com/en/))
 - [PostgreSQL 10]([https://www.postgresql.org/download/](https://www.postgresql.org/download/))
 - [Maven 3.5.4](https://maven.apache.org/download.cgi)

## configuration
All configuration is at `src/main/resources/application.yml`:
 ```
# PORT
server:
  port: 8081
 ```
Here you can set which port will be used.
 ```
# DATASOURCE
spring:
  # defines database connection properties
  datasource:
    url: jdbc:postgresql://localhost:5432/myDataBase
    username: postgres
    password: postgres
 ```
This should be altered according to your PostgreSQL installation. If you don't want to change this settings, make sure you installed PostgreSQL with default settings, `postgres` password and a database called `myDataBase`.
```
hibernate:
  ddl-auto: create-drop
```
This will drop all related tables and create new ones on every startup. To tests purposes this will suffice. If you want to keep data persisted, use this once and change to `validate` afterwards.
 ```
# LOGGING
logging:
  path: C:\temp\matera_talent_pool-log
 ```
You should change this path to where log files will be generated. If you're in Linux, use `/` instead of Windows default directory separator `\` as shown above.
```
# CUSTOM SECURITY used on config/SecurityConfig.java
custom:
  users:
    - username: admin
      password: admin
      authorities:
        - WRITE
        - READ
    - username: user
      password: user
      authorities:
        - READ
```
Here you can determine which users will have access to different authorities. `username` and `password` fields could be customized to your liking, spaces are not allowed. There are two possible authorities:
- READ (which allows the user to access data)
- WRITE (which allows user to alter data, including insert, update and delete)

All other values should be left unaltered.

## initial load
When `ddl-auto` is set to `create-drop` or `create`, the application will use `src\main\resources\import.sql` script to generate a initial load. The user can add as many lines as he wants following this pattern:
```
INSERT INTO employee (date_of_birth, date_of_employment, first_name, last_name, middle_initial, status) VALUES ('1991-04-11', '2023-09-21', 'John','Doe', 'A', 0);
```

## build
Running `mvn clean package -DskipTests` at root directory will build just the necessary files to start the application, creating a `outputDirectory` with two files:
- `matera_talent_pool.jar`
- `application.yml`

`application.yml` will be a copy of the previous edited one.

To execute tests, run `mvn test`. There will be 11 tests in total.

To run the application, go to `outputDirectory` and run `java -jar matera_talent_pool.jar`. Last printed line should be similar to this:
```
2019-04-02 07:39:11 [INFO ] [org.springframework.boot.StartupInfoLogger                            .logStarted                    ] Started Application in 6.203 seconds (JVM running for 6.704)
```

## API documentation
Api documentation can be retrieved in two ways:
1. [Swagger Editor](https://editor.swagger.io/), online editor to vizualize REST calls

To use swagger, import file `doc\swagger\employeeAPIs.yaml` or paste its content into the black left text box on swagger editor as shown below:
<img src="https://i.imgur.com/0ftUrjw.png">

2. [Postman](https://www.getpostman.com/), desktop application to make REST calls
After installing Postman, go to File -> Import and select `\doc\postman\matera_talent_pool.postman_collection.json`:

<img src="https://i.imgur.com/Cant7x3.png">