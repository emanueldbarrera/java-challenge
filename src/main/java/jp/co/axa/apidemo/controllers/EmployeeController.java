package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.ApiV1EmployeesGetEmployeeRequest;
import jp.co.axa.apidemo.models.ApiV1EmployeesGetEmployeeResponse;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.util.ApiV1EmployeesGetEmployeeResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }

    @GetMapping("/employees/{employeeId}")
    public ApiV1EmployeesGetEmployeeResponse getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        try {
            ApiV1EmployeesGetEmployeeRequest request = ApiV1EmployeesGetEmployeeRequest.builder().employeeId(employeeId).build();
            request.validate();
            final Employee employee = employeeService.getEmployee(employeeId);
            return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseSuccess(employee);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER,"Invalid request parameter");
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("Employee not found; id: " + employeeId);
                return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseFailure(e.getErrorCode(),"Employee not found");
            } else {
                log.info("System error");
                return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseFailure(e.getErrorCode(),"System error");
            }
        }
    }

    @PostMapping("/employees")
    public void saveEmployee(@RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        log.info("Employee Saved Successfully");
    }

    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        log.info("Employee Deleted Successfully");
    }

    @PutMapping("/employees/{employeeId}")
    public void updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name = "employeeId") Long employeeId) {
        try {
            Employee emp = employeeService.getEmployee(employeeId);
            if (emp != null) {
                employee.setId(employeeId);
                employeeService.updateEmployee(employee);
            }
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("Employee Not Found");
            } else {
                log.warn("System error");
            }
        }
    }

}
