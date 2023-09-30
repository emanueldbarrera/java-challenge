package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployees();

    Employee getEmployee(Long employeeId) throws ApiBusinessException;

    void saveEmployee(Employee employee);

    void deleteEmployee(Long employeeId);

    void updateEmployee(Employee employee);
}