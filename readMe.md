# matera talent pool

repository for a sample code project

### Security

At the bottom of `application.yml`, we have:

```
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

The authorities must match each API's authority, described in `doc/swagger/employeeAPIs.yml` and at `doc/postman/matera_talent_pool.postman_collection.json`


### Initial Load

At `src/main/resources/import.sql`, we have a sql script to insert initial data

