# ![Repo Linter](https://c1.iggcdn.com/indiegogo-media-prod-cld/image/upload/c_fill%2Cf_auto%2Ch_630%2Cw_1200/v1595721465/b1sg7igmx7c0v0czhyrg.jpg)

# FinnhubAPI

Descriptions:

The service provides an opportunity to monitor changes in the stock market, collect and analyze information on companies' shares of interest.

# Tests
Before starting to use the application, it is recommended to run tests to check its functionality.
 This can be done by selecting the FinnhubAPI-> src-> test-> java folder and pressing Ctrl + Shift + F10, or by right-clicking and selecting “Run ‘All Tests ’”.
 
 ## Configuration
Add the following properties to your `application.properties` file.
```properties
spring.liquibase.url: *** URL to your database ***
spring.liquibase.user: *** username to your database ***
spring.liquibase.password: *** password to your database ***
spring.datasource.url: *** URL to your database ***
spring.datasource.user: *** username to your database ***
spring.datasource.password: *** password to your database ***
```
when the application is launched on an empty database, it will be filled (with tables and data that are in the scripts)

# Database
the project uses a MySQL database. It contains 5 tables.
Users - stores information about users.
Companys - stores data about the companies being monitored.
Quote - Stores daily data on stocks of companies from the Company table.
Subscription - stores subscription data.
Roles - role description.

# Registration
To register go to "http: // localhost: 8080 / registration".
We must pass json format POST method:
```JSON
{
"login": "testLogin",
"email": "testMail@gmail.com",
"password": "testPassword",
"first_name": "optional field",
"last_name": "optional field"
}
```

to "http: // localhost: 8080 / registration" after which the user will be created and placed in the database.
A message with a username and password will be sent to the mail.


# Additional features

Once a day, the application updates data on shares of companies in the database. 
It also checks users once a day for the expiration of a paid subscription. 
During the last three days, the application sends messages to the user's mail with a reminder of the expiration of the paid version, and then switches to the free version.

# Controllers
```
http://localhost:8080/...
```
## user-rest-controller

- `POST`
```
/api/v1/user/deleteOneCompanyFromUser
```
>method removes one company from user by symbol. To do this, you need to pass json format:
 ```JSON
{
  "symbol"
}
```
- `POST`
```
/api/v1/user/addCompanyToUser
```
>method add one company from user by symbol. To do this, you need to pass json format:
 ```JSON
{
  "symbol"
}
```
- `GET`
```
/api/v1/user/getOneUser
```
>take the user out of context
- `GET`
```
/api/v1/user/getOneUser/{id}
```
>method get one user by id
- `GET`
```
/api/v1/user/getCompanyFromUser
```
>method returns a list of the user's companies
- `GET`
```
/api/v1/user/getAllUsers
```
>get data for all users saved in database

## main-controller

- `POST`
```
/api/v1/updateUser
```
 >method updated user. To do this, you need to pass json format:
 ```JSON
{
  "login": "string",
  "first_name": "string",
  "last_name": "string",
  "email": "string",
  "password": "string"
}
```
- `POST`
```
/api/v1/renewSubscription
```
>method renews subscription (Long this is months). To do this, you need to pass json format:
 ```JSON
{
"Long"
}
```
- `POST`
```
/api/v1/registration
```
>save user to database. To do this, you need to pass json format:
 ```JSON
{
  "login": "string",
  "first_name": "string",
  "last_name": "string",
  "email": "string",
  "password": "string"
}
```
`POST`
```
/api/v1/changeSubscription
```
>method changes subscription. To do this, you need to pass json format:
 ```JSON
{
 "LOW or MEDIUM or HIGH"
}
```
- `GET`
```
/api/v1/unlockingUser/{id}
```
>unblocks user by id
- `GET`
```
/api/v1/lockingUser/{id}
```
>blocks user by id
- `GET`
```
/api/v1/deleteUser/{id}
```
>removes user by id
## company-rest-controller

- `POST`
```
/api/v1/company/saveQuotes
```
>saves data on shares of companies throughout the day
 
- `POST`
```
/api/v1/company/saveAllCompanies
```
>saves companies to the database
 
- `POST`
```
/api/v1/company/deleteCompany/{symbol}
```
>removes company by symbol. To do this, you need to pass json format:
 ```JSON
{
  "symbol"
}
```
- `GET`
```
/api/v1/company/getOneCompany/{symbol}
```
>method returns companie by symbol
- `GET`
```
/api/v1/company/getAllCompanies
```
>get data for all Companies saved in database
## stock-rest-controller

- `GET`
```
/api/v1/stock/metric/{symbol}
```
>returns сomplete data on stocks of specific companies for 52 weeks
- `GET`
```
/api/v1/stock/financials/{symbol}
```
>returns financial statements of specific companies
