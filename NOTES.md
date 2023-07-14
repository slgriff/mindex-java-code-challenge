# Developer Notes

## Task 1

- `employee` field in response object is ambiguous
    - could be either a nested Employee object or id reference to employee
        - implemented as if were id reference
- Caching of results is not performed
    - traversal to get reporting structure computes count for direct reports but information is not saved for subsequent calls
- 404 Not Found is returned via controller if employee id does not exist in database
     - Unit tests make check against null object reporting structure (employeeId is null and number of reports is zero)

## Task 2

- `employee` field in response object is ambiguous
    - could be either a nested Employee object or id reference to employee
        - implemented as if were id reference
- `salary` field structure is ambiguous and would need to handle localization/different currencies
    - identifier to separate dollars and cents (e.g. "1.00" or "1,00")
    - identifier to group digits (e.g. "1,000,000" vs "1.000.000" vs "1_000_000" vs "1000000")
    - identifier for currency type (e.g. "$1.00", "1.00USD", "£100", "100GBP", etc. )
- `effectiveDate` field structure is ambiguous
    - if string-ly typed
        - dealing with consistency of "YYYY-MM-DD", "YYYY-DD-MM", "MM/DD/YYYY", etc in data
        - validation of date field and subcomponents
    - is timestamp portion useful?
        - assumed not necessary for this usecase
    - Chose to use Java Date object
- Validation of employee ID for creation of compensation records is not performed
    - may insert compensation records for an employee id with no corresponding employee object in database
- Multiple compensation entities per employee are allowed
    - Method to get compensation returns list of all compensation objects created for given employee
        - unsorted
        - no option to filter by effectiveDate (between dates, after a given date, before a given date)
            - inclusive and exclusive
    - Method to create compensation inserts additional record
        - multiple objects for an employee with same effective date can be inserted
        - compensation records are not updated nor deleted

## General

- Endpoints for both tasks were added as nested resources to `/employee` REST API due to logically being connected via an Employee entity
    - applied same logic in adding compensation methods to `EmployeeService`
- Some error handling and validation was added but would need more thorough coverage to be production ready