package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId) throws ApiBusinessException;

    public void saveEmployee(Employee employee);

    public void deleteEmployee(Long employeeId);

    public void updateEmployee(Employee employee);
}