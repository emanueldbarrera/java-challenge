package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        Employee employee = null;
        try {
            return employeeService.getEmployee(employeeId);
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("Employee Not Found");
            } else {
                log.info("System error");
            }
        }
        return employee;
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
