# PlayWithStreams Service

Spring Boot project illustrating stream operations over employee data. Salaries are `BigDecimal`
so totals and averages are exact. The data is processed in memory to demonstrate the Stream API;
in a real application, simple filters belong in repository queries.

## Managing employees

Paging, sorting and filtering here run in the database (Spring Data `Pageable`), unlike the
in-memory stream examples below.

| Method | Path | Description |
|-------|------|-------------|
| GET | `/employees?page=0&size=20&sort=salary,desc&department=IT` | Page of employees. `size` is capped at 100; `sort` accepts `id`, `name`, `department`, `salary` (others return `400`); `department` is case-insensitive. Response: `content`, `page`, `size`, `totalElements`, `totalPages`. |
| GET | `/employees/{id}` | One employee, `404` if missing. |
| POST | `/employees` | Create from `{"name", "department", "salary"}` (salary positive, at most two decimals). Returns `201` with `Location`. |
| PUT | `/employees/{id}` | Replace name, department and salary. |
| DELETE | `/employees/{id}` | Delete, `204` (or `404`). |

## Stream endpoints

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
| GET | `/employees/department-report` | custom `Collector.of(...)` + `groupingBy` + `teeing` | Per department and company-wide: headcount, total, average, min and max salary and sorted names, in one pass. |
