package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1EmployeesDeleteEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesGetEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesSaveEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesUpdateEmployeeRequest;
import jp.co.axa.apidemo.repositories.DepartmentRepository;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(ApiV1EmployeesGetEmployeeRequest request) throws ApiBusinessException {
        final Long employeeId = request.getEmployeeId();
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            throw new ApiBusinessException("0-0-1", ErrorCode.NOT_FOUND, "Employee not found");
        }
        return optionalEmployee.get();
    }

    public Employee saveEmployee(ApiV1EmployeesSaveEmployeeRequest request) throws ApiBusinessException {
        // Get department from database
        final Optional<Department> optionalDepartment = departmentRepository.findById(request.getDepartmentId());
        if (!optionalDepartment.isPresent()) {
            throw new ApiBusinessException("0-0-10", ErrorCode.INVALID_REQUEST_PARAMETER, "Department not found");
        }

        // Save employee
        final Employee employee = Employee.builder()
                .name(request.getName())
                .department(optionalDepartment.get())
                .salary(request.getSalary())
                .build();
        try {
            return employeeRepository.saveAndFlush(employee);
        } catch (Exception e) {
            throw new ApiBusinessException("0-0-2", ErrorCode.SYSTEM_ERROR, "Database error");
        }
    }

    public Employee deleteEmployee(ApiV1EmployeesDeleteEmployeeRequest request) throws ApiBusinessException {
        // Get employee from database
        final Long employeeId = request.getEmployeeId();
        Optional<Employee> optionalEmployee;
        try {
            optionalEmployee = employeeRepository.findById(employeeId);
        } catch (Exception e) {
            throw new ApiBusinessException("0-0-3", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        if (!optionalEmployee.isPresent()) {
            throw new ApiBusinessException("0-0-4", ErrorCode.NOT_FOUND, "Employee not found");
        }

        // Delete employee and return a copy
        try {
            employeeRepository.deleteById(employeeId);
        } catch (Exception e) {
            throw new ApiBusinessException("0-0-5", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        return optionalEmployee.get();
    }

    public Employee updateEmployee(ApiV1EmployeesUpdateEmployeeRequest request) throws ApiBusinessException {
        // Get employee from database
        Optional<Employee> optionalEmployee;
        try {
            optionalEmployee = employeeRepository.findById(request.getEmployeeId());
        } catch (Exception e) {
            throw new ApiBusinessException("0-0-7", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        if (!optionalEmployee.isPresent()) {
            throw new ApiBusinessException("0-0-8", ErrorCode.NOT_FOUND, "Employee not found");
        }

        // Get department from database
        Department department = optionalEmployee.get().getDepartment();
        if (request.getDepartmentId() != null) {
            final Optional<Department> optionalDepartment = departmentRepository.findById(request.getDepartmentId());
            if (!optionalDepartment.isPresent()) {
                throw new ApiBusinessException("0-0-11", ErrorCode.INVALID_REQUEST_PARAMETER, "Department not found");
            }
            department = optionalDepartment.get();
        }

        // Update Employee entity
        final Employee employee = optionalEmployee.get();
        try {
            employee.setName(request.getName() != null ? request.getName() : employee.getName());
            employee.setDepartment(department);
            employee.setSalary(request.getSalary() != null ? request.getSalary() : employee.getSalary());
            employeeRepository.saveAndFlush(employee);
        } catch (Exception e) {
            throw new ApiBusinessException("0-0-9", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        return employee;
    }
}