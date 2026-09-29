# PlayWithStreams Service

Spring Boot project illustrating stream operations over employee data. Salaries are `BigDecimal`
so totals and averages are exact. The data is processed in memory to demonstrate the Stream API;
in a real application, simple filters belong in repository queries.

## Endpoints

| Method | Path | Stream feature | Description |
|-------|------|----------------|-------------|
| GET | `/employees/high-salary?minSalary=5000` | `filter` | Employees earning more than `minSalary` (default 5000). |
| GET | `/employees/names-uppercase` | `map` | All employee names in uppercase. |
| GET | `/employees/group-by-department` | `groupingBy` | Employees grouped by department (sorted). |
| GET | `/employees/total-salary` | `map` + `reduce` | Total salary of all employees. |
| GET | `/employees/partition-by-salary?minSalary=5000` | `partitioningBy` | `true`/`false` split by salary. |
| GET | `/employees/average-salary-by-department` | `groupingBy` + `collectingAndThen` | Average salary per department. |
| GET | `/employees/top-earner-by-department` | `toMap` + `BinaryOperator.maxBy` | Highest-paid employee per department. |
| GET | `/employees/salary-range` | `teeing(minBy, maxBy)` | Lowest- and highest-paid employee in one pass. |
