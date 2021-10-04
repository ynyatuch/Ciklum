Ciklum Hybris Academy test task.

To use application MySQL and Java should be installed on working machine.

MySQL download page:
https://dev.mysql.com/downloads/mysql/

Java download page:
https://www.oracle.com/java/technologies/downloads/

Prerequisites:
1. Run MySQL Shell.
2. Change language to SQL: 
               
         \sql
3. Connect to the MySQL server:  
               
         \connect <username>>@localhost:<port>

Use username and port set during MySQL installation (e.g: \connect root@localhost:3306)

To run application download it and run terminal in "Ciklum" directory.
I. Update local.properties file with used username and password.

II. Run command:
            
               java -jar Ciklum.jar

III. Follow application's instructions.

To start automation test run command:

               java -jar Test.jar

P.S. test coverage is very poor - my bad :(. 