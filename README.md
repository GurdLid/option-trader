# option-trader
Option Trading Platform created in Java, using Spring Boot, Spring Security, Maven, Lombok, JPA, Thymleaf

## Build instructions

The chosen server port: 8020

The current database set-up process requires some patience, but is soon to be fixed.

Initially, please start by running the SQL script 'optiontrader.sql' in MySQL workbench. This will create both the test and app databases.
To populate them with the correct tables, please run the program once (**ensuring to change the details in application.properties to your own MySQL login information**), along with running the tests once. One test will fail, this is fine.

1) Go to MySQL workbench and right-click on the 'optionLogin' schema in the panel on the left. 
2) Click 'Table Data Import Wizard' and locate the 'stockpricedata.csv' file.
3) Ensure to 'Use existing table': 'optionLogin.stockprices', then continue to click through the wizard.

7300 rows should be imported.

Repeat steps 1 through 3 on the 'optionLoginTest' schema.

You can now run the application and tests normally, and navigate to localhost:8020 to interact with the Option Trader platform.
