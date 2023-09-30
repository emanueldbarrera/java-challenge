package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.*;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public ApiV1EmployeesGetEmployeesResponse getEmployees() {
        try {
            return ApiV1EmployeesGetEmployeesResponseUtil.buildResponseSuccess(employeeService.getEmployees());
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1EmployeesGetEmployeesResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }

    @GetMapping("/employees/{employeeId}")
    public ApiV1EmployeesGetEmployeeResponse getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        try {
            ApiV1EmployeesGetEmployeeRequest request = ApiV1EmployeesGetEmployeeRequest.builder().employeeId(employeeId).build();
            request.validate();
            final Employee employee = employeeService.getEmployee(request);
            return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseSuccess(employee);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("Employee not found; id: " + employeeId);
                return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseFailure(e.getErrorCode(), "Employee not found");
            } else {
                log.warn("System error");
                return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseFailure(e.getErrorCode(), "System error");
            }
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1EmployeesGetEmployeeResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }

    @PostMapping("/employees")
    public ApiV1EmployeesSaveEmployeeResponse saveEmployee(@RequestBody ApiV1EmployeesSaveEmployeeRequest employeeRequest) {
        try {
            employeeRequest.validate();
            final Employee employee = employeeService.saveEmployee(employeeRequest);
            log.info("Employee Saved Successfully; employeeId: " + employee.getId());
            return ApiV1EmployeesSaveEmployeeResponseUtil.buildResponseSuccess(employee);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1EmployeesSaveEmployeeResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (ApiBusinessException e) {
            log.warn("System error", e.getMessage());
            return ApiV1EmployeesSaveEmployeeResponseUtil.buildResponseFailure(ErrorCode.SYSTEM_ERROR, "System error");
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1EmployeesSaveEmployeeResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }

    @DeleteMapping("/employees/{employeeId}")
    public ApiV1EmployeesDeleteEmployeeResponse deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        try {
            ApiV1EmployeesDeleteEmployeeRequest request = ApiV1EmployeesDeleteEmployeeRequest.builder()
                    .employeeId(employeeId)
                    .build();
            request.validate();
            final Employee employee = employeeService.deleteEmployee(request);
            log.info("Employee Deleted Successfully; employeeId: " + employeeId);
            return ApiV1EmployeesDeleteEmployeeResponseUtil.buildResponseSuccess(employee);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1EmployeesDeleteEmployeeResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("Employee not found; id: " + employeeId);
                return ApiV1EmployeesDeleteEmployeeResponseUtil.buildResponseFailure(e.getErrorCode(), "Employee not found");
            } else {
                log.warn("System error", e.getMessage());
                return ApiV1EmployeesDeleteEmployeeResponseUtil.buildResponseFailure(ErrorCode.SYSTEM_ERROR, "System error");
            }
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1EmployeesDeleteEmployeeResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }

    @PutMapping("/employees/{employeeId}")
    public ApiV1EmployeesUpdateEmployeeResponse updateEmployee(@RequestBody ApiV1EmployeesUpdateEmployeeRequest request,
                                                               @PathVariable(name = "employeeId") Long employeeId) {
        try {
            // Validate fields to update
            final ApiV1EmployeesUpdateEmployeeRequest updatedRequest = request.toBuilder().employeeId(employeeId).build();
            updatedRequest.validate();
            // Update Employee entity
            final Employee employee = employeeService.updateEmployee(updatedRequest);
            return ApiV1EmployeesUpdateEmployeeResponseUtil.buildResponseSuccess(employee);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1EmployeesUpdateEmployeeResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("Employee not found; id: " + employeeId);
                return ApiV1EmployeesUpdateEmployeeResponseUtil.buildResponseFailure(e.getErrorCode(), "Employee not found");
            } else {
                log.warn("System error", e.getMessage());
                return ApiV1EmployeesUpdateEmployeeResponseUtil.buildResponseFailure(ErrorCode.SYSTEM_ERROR, "System error");
            }
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1EmployeesUpdateEmployeeResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }
}
