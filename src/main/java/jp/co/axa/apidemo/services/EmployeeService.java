package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1EmployeesDeleteEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesGetEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesSaveEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesUpdateEmployeeRequest;

import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployees();

    Employee getEmployee(ApiV1EmployeesGetEmployeeRequest request) throws ApiBusinessException;

    Employee saveEmployee(ApiV1EmployeesSaveEmployeeRequest request) throws ApiBusinessException;

    Employee deleteEmployee(ApiV1EmployeesDeleteEmployeeRequest request) throws ApiBusinessException;

    Employee updateEmployee(ApiV1EmployeesUpdateEmployeeRequest request) throws ApiBusinessException;
}