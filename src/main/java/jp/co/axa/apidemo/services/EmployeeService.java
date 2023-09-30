package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1EmployeesDeleteEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesGetEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesSaveEmployeeRequest;

import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployees();

    Employee getEmployee(ApiV1EmployeesGetEmployeeRequest request) throws ApiBusinessException;

    Employee saveEmployee(ApiV1EmployeesSaveEmployeeRequest employee) throws ApiBusinessException;

    Employee deleteEmployee(ApiV1EmployeesDeleteEmployeeRequest employeeId) throws ApiBusinessException;

    void updateEmployee(Employee employee);
}