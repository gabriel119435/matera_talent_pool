# Matera Talent Pool
Repository for a sample code project including a simple CRUD through REST APIs

## Minimum requirements
 - [Java 8](https://www.java.com/en/)
 - [Docker](https://www.docker.com)
 - [Maven 3.5.4](https://maven.apache.org/download.cgi)

## Docker
At `docker/`, run 
```
docker build -t simple_psql .
```
This will create an image called simple_psql based on `Dockerfile`. Running `docker images` should return something like
```
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
simple_psql         latest              d0f10a1527cf        8 seconds ago       230MB
```
Running 
```
docker run --name container_name -d -p 5432:5432 -e POSTGRES_PASSWORD=postgres simple_psql
```
creates a container named `container_name` based on `simple_psql` image. This container is ready to be used as our database.

## Configuration
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
`localhost:5432` should be match your container's ip and port
```
hibernate:
  ddl-auto: create-drop
```
This will drop all related tables and create new ones on every startup. To tests purposes this will suffice. If you want to keep data persisted, use this once and change to `validate` afterwards.
 ```
# LOGGING
logging:
  path: matera_talent_pool-log
 ```
You should change this path to where log files will be generated
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

## Initial load
When `ddl-auto` is set to `create-drop` or `create`, the application will use `src\main\resources\import.sql` script to generate a initial load. The user can add as many lines as he wants following this pattern:
```
INSERT INTO employee (date_of_birth, date_of_employment, first_name, last_name, middle_initial, status) VALUES ('1991-04-11', '2023-09-21', 'John','Doe', 'A', 0);
```

## Build
Running `mvn clean package -DskipTests` at root directory will build just the necessary files to start the application, creating a `outputDirectory` with two files:
- `matera_talent_pool.jar`
- `application.yml`

`application.yml` will be a copy of the previous edited one.

To execute tests, run `mvn test`. There will be 11 tests in total.

To run the application, go to `outputDirectory` and run `java -jar matera_talent_pool.jar`. Last printed line should be similar to this:
```
2019-04-02 07:39:11 [INFO ] [org.springframework.boot.StartupInfoLogger                            .logStarted                    ] Started Application in 6.203 seconds (JVM running for 6.704)
```

## API Documentation
Api documentation can be retrieved in two ways:
1. [Swagger Editor](https://editor.swagger.io/), online editor to vizualize REST calls

To use swagger, import file `doc\swagger\employeeAPIs.yaml` or paste its content into the black left text box on swagger editor as shown below:

![](https://i.imgur.com/0ftUrjw.png)

2. [Postman](https://www.getpostman.com/), desktop application to make REST calls
After installing Postman, go to File -> Import and select `\doc\postman\matera_talent_pool.postman_collection.json`:

![](https://i.imgur.com/Cant7x3.png)