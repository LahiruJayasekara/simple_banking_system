### Future Improvements
1. Add logs 
2. Integrate with a DB 
3. Fix concurrency issues while inserting several transactions at once

* since this doesn't run a scheduler to calculate the interest at end of each month it's not possible to add an Interest transaction, since it is possible to add an interest rule to a back date.