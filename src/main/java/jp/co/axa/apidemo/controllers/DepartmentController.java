package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.models.ApiV1DepartmentsDeleteDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsDeleteDepartmentResponse;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentResponse;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentsRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentsResponse;
import jp.co.axa.apidemo.models.ApiV1DepartmentsSaveDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsSaveDepartmentResponse;
import jp.co.axa.apidemo.models.ApiV1DepartmentsUpdateDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsUpdateDepartmentResponse;
import jp.co.axa.apidemo.services.DepartmentService;
import jp.co.axa.apidemo.util.ApiV1DepartmentsDeleteDepartmentResponseUtil;
import jp.co.axa.apidemo.util.ApiV1DepartmentsGetDepartmentResponseUtil;
import jp.co.axa.apidemo.util.ApiV1DepartmentsGetDepartmentsResponseUtil;
import jp.co.axa.apidemo.util.ApiV1DepartmentsSaveDepartmentResponseUtil;
import jp.co.axa.apidemo.util.ApiV1DepartmentsUpdateDepartmentResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * Returns a list of a page of departments, ordered by id
     *
     * @param offset the starting position of the page
     * @param limit  the size of the page
     * @return {@link ApiV1DepartmentsGetDepartmentsResponse}
     */
    @GetMapping("/departments")
    public ApiV1DepartmentsGetDepartmentsResponse getDepartments(@RequestParam Integer offset, @RequestParam Integer limit) {
        try {
            final ApiV1DepartmentsGetDepartmentsRequest request = ApiV1DepartmentsGetDepartmentsRequest.builder().limit(limit).offset(offset).build();
            request.validate();
            List<Department> departments = departmentService.getDepartments(request);
            return ApiV1DepartmentsGetDepartmentsResponseUtil.buildResponseSuccess(departments);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1DepartmentsGetDepartmentsResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1DepartmentsGetDepartmentsResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }

    /**
     * Returns a single instance of a department, if present
     *
     * @param departmentId the id of the target department
     * @return {@link ApiV1DepartmentsGetDepartmentResponse}
     */
    @GetMapping("/departments/{departmentId}")
    public ApiV1DepartmentsGetDepartmentResponse getDepartment(@PathVariable(name = "departmentId") Long departmentId) {
        try {
            ApiV1DepartmentsGetDepartmentRequest request = ApiV1DepartmentsGetDepartmentRequest.builder().id(departmentId).build();
            request.validate();
            final Department department = departmentService.getDepartment(request);
            return ApiV1DepartmentsGetDepartmentResponseUtil.buildResponseSuccess(department);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1DepartmentsGetDepartmentResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("placeCode: " + e.getPlaceCode() + " Department not found; id: " + departmentId);
                return ApiV1DepartmentsGetDepartmentResponseUtil.buildResponseFailure(e.getErrorCode(), "Department not found");
            } else {
                log.warn("placeCode: " + e.getPlaceCode() + " System error");
                return ApiV1DepartmentsGetDepartmentResponseUtil.buildResponseFailure(e.getErrorCode(), "System error");
            }
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1DepartmentsGetDepartmentResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }

    /**
     * Creates a new department to be stored in the database
     *
     * @param request {@link ApiV1DepartmentsSaveDepartmentRequest} a wrapper with all the department parameters
     * @return {@Link ApiV1DepartmentsSaveDepartmentResponse}
     */
    @PostMapping("/departments")
    public ApiV1DepartmentsSaveDepartmentResponse saveDepartment(@RequestBody ApiV1DepartmentsSaveDepartmentRequest request) {
        try {
            request.validate();
            final Department department = departmentService.saveDepartment(request);
            log.info("Department Saved Successfully; departmentId: " + department.getId());
            return ApiV1DepartmentsSaveDepartmentResponseUtil.buildResponseSuccess(department);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1DepartmentsSaveDepartmentResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (ApiBusinessException e) {
            log.warn("placeCode: " + e.getPlaceCode() + " System error", e.getMessage());
            return ApiV1DepartmentsSaveDepartmentResponseUtil.buildResponseFailure(ErrorCode.SYSTEM_ERROR, "System error");
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1DepartmentsSaveDepartmentResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }

    /**
     * Deletes a department, if exists
     *
     * @param departmentId The id of the department to be deleted
     * @return {@link ApiV1DepartmentsDeleteDepartmentResponse}
     */
    @DeleteMapping("/departments/{departmentId}")
    public ApiV1DepartmentsDeleteDepartmentResponse deleteDepartment(@PathVariable(name = "departmentId") Long departmentId) {
        try {
            ApiV1DepartmentsDeleteDepartmentRequest request = ApiV1DepartmentsDeleteDepartmentRequest.builder().departmentId(departmentId).build();
            request.validate();
            final Department department = departmentService.deleteDepartment(request);
            log.info("Department Deleted Successfully; departmentId: " + departmentId);
            return ApiV1DepartmentsDeleteDepartmentResponseUtil.buildResponseSuccess(department);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1DepartmentsDeleteDepartmentResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("placeCode: " + e.getPlaceCode() + " Department not found; id: " + departmentId);
                return ApiV1DepartmentsDeleteDepartmentResponseUtil.buildResponseFailure(e.getErrorCode(), "Department not found");
            } else {
                log.warn("placeCode: " + e.getPlaceCode() + " System error", e.getMessage());
                return ApiV1DepartmentsDeleteDepartmentResponseUtil.buildResponseFailure(ErrorCode.SYSTEM_ERROR, "System error");
            }
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1DepartmentsDeleteDepartmentResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }

    /**
     * Updates one or more fields of the given department
     *
     * @param request      {@link ApiV1DepartmentsUpdateDepartmentRequest} a wrapper with all the department parameters
     * @param departmentId the id of the target department to be updated
     * @return {@link ApiV1DepartmentsUpdateDepartmentResponse}
     */
    @PutMapping("/departments/{departmentId}")
    public ApiV1DepartmentsUpdateDepartmentResponse updateDepartment(@RequestBody ApiV1DepartmentsUpdateDepartmentRequest request, @PathVariable(name = "departmentId") Long departmentId) {
        try {
            // Validate fields to update
            final ApiV1DepartmentsUpdateDepartmentRequest updatedRequest = request.toBuilder().id(departmentId).build();
            updatedRequest.validate();
            // Update Department entity
            final Department department = departmentService.updateDepartment(updatedRequest);
            log.info("Department Updated Successfully; departmentId: " + departmentId);
            return ApiV1DepartmentsUpdateDepartmentResponseUtil.buildResponseSuccess(department);
        } catch (ConstraintViolationException e) {
            log.info("Validation error: " + e.getMessage());
            return ApiV1DepartmentsUpdateDepartmentResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
        } catch (ApiBusinessException e) {
            if (e.getErrorCode().equals(ErrorCode.INVALID_REQUEST_PARAMETER)) {
                log.info("placeCode: " + e.getPlaceCode() + " Validation error: " + e.getMessage());
                return ApiV1DepartmentsUpdateDepartmentResponseUtil.buildResponseFailure(ErrorCode.INVALID_REQUEST_PARAMETER, "Invalid request parameter");
            }
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND)) {
                log.info("placeCode: " + e.getPlaceCode() + " Department not found; id: " + departmentId);
                return ApiV1DepartmentsUpdateDepartmentResponseUtil.buildResponseFailure(e.getErrorCode(), "Department not found");
            } else {
                log.warn("placeCode: " + e.getPlaceCode() + " System error", e.getMessage());
                return ApiV1DepartmentsUpdateDepartmentResponseUtil.buildResponseFailure(ErrorCode.SYSTEM_ERROR, "System error");
            }
        } catch (Exception e) {
            log.warn("Unknown exception: " + e.getMessage());
            return ApiV1DepartmentsUpdateDepartmentResponseUtil.buildResponseFailure(ErrorCode.UNKNOWN, "Unknown error");
        }
    }
}
