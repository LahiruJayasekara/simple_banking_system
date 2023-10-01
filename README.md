### Future Improvements
1. Add logs 
2. Integrate with a DB 
3. Fix concurrency issues while inserting several transactions at once

* since this doesn't run a scheduler to calculate the interest at end of each month it's not possible to add an Interest transaction, since it is possible to add an interest rule to a back date.

### How to start the application
* Prerequisite - Java 17
1. Goto project root directory
2. Run `java -jar .\build\libs\simple_banking_system-1.0-SNAPSHOT-all.jar`

### Test commands
1. Define interests
```
20230520 RULE02 1.90
20230615 RULE03 2.20
```
2. Input Transactions
```
20230505 AC001 D 100.00
20230601 AC001 D 150.00
20230626 AC001 W 20.00
20230626 AC001 W 100.00
```
3. Print statement
```
AC001 202306
```
