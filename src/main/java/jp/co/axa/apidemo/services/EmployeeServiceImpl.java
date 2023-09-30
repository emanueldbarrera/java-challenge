package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1EmployeesDeleteEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesGetEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesSaveEmployeeRequest;
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

    public Employee saveEmployee(ApiV1EmployeesSaveEmployeeRequest employeeRequest) throws ApiBusinessException {
        final Employee employee = Employee.builder()
                .name(employeeRequest.getName())
                .department(employeeRequest.getDepartment())
                .salary(employeeRequest.getSalary())
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
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            throw new ApiBusinessException("0-0-3", ErrorCode.NOT_FOUND, "Employee not found");
        }
        // Delete employee and return a copy
        try {
            employeeRepository.deleteById(employeeId);
        } catch (Exception e) {
            throw new ApiBusinessException("0-0-4", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        return optionalEmployee.get();
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.saveAndFlush(employee);
    }
}