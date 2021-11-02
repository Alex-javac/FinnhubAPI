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



# Additional features

Once a day, the application updates data on shares of companies in the database. 
It also checks users once a day for the expiration of a paid subscription. 
During the last three days, the application sends messages to the user's mail with a reminder of the expiration of the paid version, and then switches to the free version.
