# AutomaticTellerMachine
This project is simulating dispensing money from ATM by input the amount

# Technologies
- Java
- Spring Framework
- JSP
- Javascript
- JQeury
- AJAX
- Sitemesh Decorator
- CSS
- Jasypt (Encrypt / Decrypt password)
- Log4J
- JUnit
- Mockito
- DozerBeanMapper
- Ant Build
- Apache Tomcat
- Oracle

# Description
This project has both a basic Java main class to test the logic of dispense from ATM and also web application.

- For easy understanding the logic, Just run the main class named "AutomaticTellerMachineMain" and input amount to dispense in the console and press Enter.

- For the web application, it has been designed to input the amount to dispense then click on the Submit button and also has the data validation to display error message on the screen (if error). If it pass the validation, the amount will be checking against the balance notes from the database (Oracle). If the balance is enough to dispense, the amount will be in the calculation process to dispense in possible note types.

# Validation Process
- Not blank
- Must parseable (number in integer)
- Not negative
- Not coin
- Not more than max dispense per time
- Balance is enough to dispense
- Not be amount that's impossible to dispense (10, 30)

# Database
I've prepared the SQL script (ATM.sql) to create the ATM schema and also tables to support the web application. It needs to be run in the Oracle database.
