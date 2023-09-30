package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1EmployeesSaveEmployeeRequest;

import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployees();

    Employee getEmployee(Long employeeId) throws ApiBusinessException;

    Employee saveEmployee(ApiV1EmployeesSaveEmployeeRequest employee) throws ApiBusinessException;

    void deleteEmployee(Long employeeId);

    void updateEmployee(Employee employee);
}