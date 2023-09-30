package jp.co.axa.apidemo.controller;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.common.ResultType;
import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.models.*;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test class for {@link EmployeeController}
 */
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void init() throws ApiBusinessException {
        initMocks(this);

        // Set test Employee entities
        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Some Name");
        employee1.setDepartment("Some Department");
        employee1.setSalary(3000);

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Other Name");
        employee2.setDepartment("Other Department");
        employee2.setSalary(4500);

        final List<Employee> employees = new java.util.ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        when(employeeService.getEmployees()).thenReturn(employees);
        when(employeeService.saveEmployee(any(ApiV1EmployeesSaveEmployeeRequest.class)))
                .thenReturn(employee1);
        when(employeeService.deleteEmployee((any(ApiV1EmployeesDeleteEmployeeRequest.class))))
                .thenReturn(employee1);
        when(employeeService.updateEmployee((any(ApiV1EmployeesUpdateEmployeeRequest.class))))
                .thenReturn(Employee.builder()
                        .id(1L)
                        .name("Different Name")
                        .department("Different Department")
                        .salary(6000)
                        .build());
    }

    /**
     * getEmployees - Success case
     */
    @Test
    void test_getEmployees_success() {
        final ApiV1EmployeesGetEmployeesResponse response = employeeController.getEmployees();

        assertThat(response.getResultType(), is(ResultType.SUCCESS.getCode()));
        assertThat(response.getErrorCode(), is(nullValue()));
        assertThat(response.getErrorMessage(), is(nullValue()));
        assertThat(response.getEmployees().size(), is(2));
        assertThat(response.getEmployees().get(0).getEmployeeId(), is(1L));
        assertThat(response.getEmployees().get(0).getName(), is("Some Name"));
        assertThat(response.getEmployees().get(0).getDepartment(), is("Some Department"));
        assertThat(response.getEmployees().get(0).getSalary(), is(3000));
        assertThat(response.getEmployees().get(1).getEmployeeId(), is(2L));
        assertThat(response.getEmployees().get(1).getName(), is("Other Name"));
        assertThat(response.getEmployees().get(1).getDepartment(), is("Other Department"));
        assertThat(response.getEmployees().get(1).getSalary(), is(4500));
    }

    /**
     * getEmployees - Success case - service returns empty list
     */
    @Test
    void test_getEmployees_success_empty_db() {
        when(employeeService.getEmployees()).thenReturn(new ArrayList<>());

        final ApiV1EmployeesGetEmployeesResponse response = employeeController.getEmployees();

        assertThat(response.getResultType(), is(ResultType.SUCCESS.getCode()));
        assertThat(response.getErrorCode(), is(nullValue()));
        assertThat(response.getErrorMessage(), is(nullValue()));
        assertThat(response.getEmployees().size(), is(0));
    }

    /**
     * getEmployees - Success case - service throws general exception
     */
    @Test
    void test_getEmployees_success_RuntimeException() {
        doThrow(RuntimeException.class).when(employeeService).getEmployees();

        final ApiV1EmployeesGetEmployeesResponse response = employeeController.getEmployees();

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.UNKNOWN.getCode()));
        assertThat(response.getErrorMessage(), is("Unknown error"));
        assertThat(response.getEmployees(), is(nullValue()));
    }

    /**
     * getEmployee - Success case
     */
    @Test
    void test_getEmployee_success() throws ApiBusinessException {
        when(employeeService.getEmployee(any(ApiV1EmployeesGetEmployeeRequest.class)))
                .thenReturn(employee1);
        final ApiV1EmployeesGetEmployeeResponse response = employeeController.getEmployee(1L);
        assertThat(response.getResultType(), is(ResultType.SUCCESS.getCode()));
        assertThat(response.getErrorCode(), is(nullValue()));
        assertThat(response.getErrorMessage(), is(nullValue()));
        assertThat(response.getEmployee(), is(notNullValue()));
        assertThat(response.getEmployee().getEmployeeId(), is(1L));
        assertThat(response.getEmployee().getName(), is("Some Name"));
        assertThat(response.getEmployee().getDepartment(), is("Some Department"));
        assertThat(response.getEmployee().getSalary(), is(3000));
    }

    /**
     * getEmployee - Failure case - employeeId is invalid
     */
    @Test
    void test_getEmployee_invalid_employeeId() {
        final ApiV1EmployeesGetEmployeeResponse response = employeeController.getEmployee(-1L);
        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
    }

    /**
     * getEmployee - Failure case - employeeId is valid but is not found by the service
     */
    @Test
    void test_getEmployee_nonexistent_employeeId() throws ApiBusinessException {
        when(employeeService.getEmployee(any(ApiV1EmployeesGetEmployeeRequest.class)))
                .thenThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.NOT_FOUND, "anyMessage"));
        final ApiV1EmployeesGetEmployeeResponse response = employeeController.getEmployee(5L);
        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.NOT_FOUND.getCode()));
        assertThat(response.getErrorMessage(), is("Employee not found"));
        assertThat(response.getEmployee(), is(nullValue()));
    }

    /**
     * getEmployee - Failure case - service throws ApiBusinessException
     */
    @Test
    void test_getEmployee_ApiBusinessException() throws ApiBusinessException {
        doThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.SYSTEM_ERROR, "anyErrorMessage"))
                .when(employeeService).getEmployee(any(ApiV1EmployeesGetEmployeeRequest.class));

        final ApiV1EmployeesGetEmployeeResponse response = employeeController.getEmployee(1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.SYSTEM_ERROR.getCode()));
        assertThat(response.getErrorMessage(), is("System error"));
        assertThat(response.getEmployee(), is(nullValue()));
    }

    /**
     * getEmployee - Failure case - service throws general exception
     */
    @Test
    void test_getEmployee_RuntimeException() throws ApiBusinessException {
        doThrow(RuntimeException.class).when(employeeService).getEmployee(any(ApiV1EmployeesGetEmployeeRequest.class));

        final ApiV1EmployeesGetEmployeeResponse response = employeeController.getEmployee(1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.UNKNOWN.getCode()));
        assertThat(response.getErrorMessage(), is("Unknown error"));
        assertThat(response.getEmployee(), is(nullValue()));
    }

    /**
     * saveEmployees - Success case
     */
    @Test
    void test_saveEmployees_success() throws ApiBusinessException {
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Other Name")
                .department("Other Department")
                .salary(4500)
                .build();

        final ApiV1EmployeesSaveEmployeeResponse response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.SUCCESS.getCode()));
        assertThat(response.getErrorCode(), is(nullValue()));
        assertThat(response.getErrorMessage(), is(nullValue()));
        assertThat(response.getEmployee(), is(notNullValue()));
        assertThat(response.getEmployee().getEmployeeId(), is(1L));
        assertThat(response.getEmployee().getName(), is("Some Name"));
        assertThat(response.getEmployee().getDepartment(), is("Some Department"));
        assertThat(response.getEmployee().getSalary(), is(3000));
        verify(employeeService, times(1)).saveEmployee(request);
    }

    /**
     * saveEmployees - Failure case - invalid name (min length)
     */
    @Test
    void test_saveEmployees_failure_invalid_name_min() throws ApiBusinessException {
        // Test name length less than minimum
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("A")
                .department("Other Department")
                .salary(4500)
                .build();

        final ApiV1EmployeesSaveEmployeeResponse response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).saveEmployee(request);
    }

    /**
     * saveEmployees - Failure case - invalid name (max length)
     */
    @Test
    void test_saveEmployees_failure_invalid_name_max() throws ApiBusinessException {
        // Test name length less than minimum
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("123456789012345678901234567890123456789012345678901234567890123456789012345678901") // 81 characters
                .department("Other Department")
                .salary(4500)
                .build();

        final ApiV1EmployeesSaveEmployeeResponse response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).saveEmployee(request);
    }

    /**
     * saveEmployees - Failure case - invalid department (min length)
     */
    @Test
    void test_saveEmployees_failure_invalid_department_min() throws ApiBusinessException {
        // Test department length less than minimum
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Other Name")
                .department("A")
                .salary(4500)
                .build();

        final ApiV1EmployeesSaveEmployeeResponse response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).saveEmployee(request);
    }

    /**
     * saveEmployees - Failure case - invalid department (max length)
     */
    @Test
    void test_saveEmployees_failure_invalid_department_max() throws ApiBusinessException {
        // Test department length less than minimum
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Other Name")
                .department("123456789012345678901234567890123456789012345678901234567890123456789012345678901")
                .salary(4500)
                .build();

        final ApiV1EmployeesSaveEmployeeResponse response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).saveEmployee(request);
    }

    /**
     * saveEmployees - Failure case - service throws ApiBusinessException
     */
    @Test
    void test_saveEmployees_failure_ApiBusinessException() throws ApiBusinessException {
        when(employeeService.saveEmployee(any(ApiV1EmployeesSaveEmployeeRequest.class)))
                .thenThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.SYSTEM_ERROR, "anyMessage"));
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Other Name")
                .department("Other Department")
                .salary(4500)
                .build();

        final ApiV1EmployeesSaveEmployeeResponse response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.SYSTEM_ERROR.getCode()));
        assertThat(response.getErrorMessage(), is("System error"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(1)).saveEmployee(request);
    }

    /**
     * saveEmployees - Failure case - service throws general exception
     */
    @Test
    void test_saveEmployees_failure_RuntimeException() throws ApiBusinessException {
        when(employeeService.saveEmployee(any(ApiV1EmployeesSaveEmployeeRequest.class)))
                .thenThrow(RuntimeException.class);
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Other Name")
                .department("Other Department")
                .salary(4500)
                .build();

        final ApiV1EmployeesSaveEmployeeResponse response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.UNKNOWN.getCode()));
        assertThat(response.getErrorMessage(), is("Unknown error"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(1)).saveEmployee(request);
    }

    /**
     * saveEmployees - Failure case - invalid salary
     */
    @Test
    void test_saveEmployees_failure_salary_invalid() throws ApiBusinessException {
        // Test salary is negative
        ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Other Name")
                .department("Other Department")
                .salary(-100)
                .build();

        ApiV1EmployeesSaveEmployeeResponse response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).saveEmployee(request);

        // Test salary is zero
        request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Other Name")
                .department("Other Department")
                .salary(0)
                .build();

        response = employeeController.saveEmployee(request);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).saveEmployee(request);
    }

    /**
     * deleteEmployee - Success case
     */
    @Test
    void test_deleteEmployee_success() throws ApiBusinessException {
        ApiV1EmployeesDeleteEmployeeResponse response = employeeController.deleteEmployee(1L);

        assertThat(response.getResultType(), is(ResultType.SUCCESS.getCode()));
        assertThat(response.getErrorCode(), is(nullValue()));
        assertThat(response.getErrorMessage(), is(nullValue()));
        assertThat(response.getEmployee(), is(notNullValue()));
        assertThat(response.getEmployee().getEmployeeId(), is(1L));
        assertThat(response.getEmployee().getName(), is("Some Name"));
        assertThat(response.getEmployee().getDepartment(), is("Some Department"));
        assertThat(response.getEmployee().getSalary(), is(3000));
        verify(employeeService, times(1)).deleteEmployee(any(ApiV1EmployeesDeleteEmployeeRequest.class));
    }

    /**
     * deleteEmployee - Failure case - employeeId is invalid
     */
    @Test
    void test_deleteEmployee_invalid_employeeId() {
        final ApiV1EmployeesDeleteEmployeeResponse responseNegative = employeeController.deleteEmployee(-1L);
        assertThat(responseNegative.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(responseNegative.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(responseNegative.getErrorMessage(), is("Invalid request parameter"));
        assertThat(responseNegative.getEmployee(), is(nullValue()));

        final ApiV1EmployeesDeleteEmployeeResponse responseZero = employeeController.deleteEmployee(0L);
        assertThat(responseZero.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(responseZero.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(responseZero.getErrorMessage(), is("Invalid request parameter"));
        assertThat(responseNegative.getEmployee(), is(nullValue()));
    }

    /**
     * deleteEmployee - Failure case - employeeId not found by service
     */
    @Test
    void test_deleteEmployee_nonexistent_employeeId() throws ApiBusinessException {
        when(employeeService.deleteEmployee((any(ApiV1EmployeesDeleteEmployeeRequest.class))))
                .thenThrow(new ApiBusinessException("somePlaceCode", ErrorCode.NOT_FOUND, "someMessage"));
        final ApiV1EmployeesDeleteEmployeeResponse response = employeeController.deleteEmployee(10L);
        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.NOT_FOUND.getCode()));
        assertThat(response.getErrorMessage(), is("Employee not found"));
        assertThat(response.getEmployee(), is(nullValue()));
    }

    /**
     * deleteEmployee - Failure case - service throws ApiBusinessException
     */
    @Test
    void test_deleteEmployee_invalid_ApiBusinessException() throws ApiBusinessException {
        when(employeeService.deleteEmployee((any(ApiV1EmployeesDeleteEmployeeRequest.class))))
                .thenThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.SYSTEM_ERROR, "System error"));
        final ApiV1EmployeesDeleteEmployeeResponse response = employeeController.deleteEmployee(1L);
        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.SYSTEM_ERROR.getCode()));
        assertThat(response.getErrorMessage(), is("System error"));
        assertThat(response.getEmployee(), is(nullValue()));
    }

    /**
     * deleteEmployee - Failure case - service throws general exception
     */
    @Test
    void test_deleteEmployee_invalid_RuntimeException() throws ApiBusinessException {
        when(employeeService.deleteEmployee((any(ApiV1EmployeesDeleteEmployeeRequest.class))))
                .thenThrow(RuntimeException.class);
        final ApiV1EmployeesDeleteEmployeeResponse response = employeeController.deleteEmployee(1L);
        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.UNKNOWN.getCode()));
        assertThat(response.getErrorMessage(), is("Unknown error"));
        assertThat(response.getEmployee(), is(nullValue()));
    }

    /**
     * updateEmployee - Success case
     */
    @Test
    void test_updateEmployee_success() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different Name")
                .department("Different Department")
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 1L);

        assertThat(response.getResultType(), is(ResultType.SUCCESS.getCode()));
        assertThat(response.getEmployee(), is(notNullValue()));
        assertThat(response.getEmployee().getEmployeeId(), is(1L));
        assertThat(response.getEmployee().getName(), is("Different Name"));
        assertThat(response.getEmployee().getDepartment(), is("Different Department"));
        assertThat(response.getEmployee().getSalary(), is(6000));
        verify(employeeService, times(1)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - invalid employeeId
     */
    @Test
    void test_updateEmployee_failure_invalid_employeeId() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different Name")
                .department("Different Department")
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, -1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - invalid name (min length)
     */
    @Test
    void test_updateEmployee_failure_invalid_name_min() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("A")
                .department("Different Department")
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - invalid name (max length)
     */
    @Test
    void test_updateEmployee_failure_invalid_name_max() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("123456789012345678901234567890123456789012345678901234567890123456789012345678901") // 81 characters
                .department("Different Department")
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - invalid department (min length)
     */
    @Test
    void test_updateEmployee_failure_invalid_department_min() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different name")
                .department("A")
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - invalid department (max length)
     */
    @Test
    void test_updateEmployee_failure_invalid_department_max() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different name") // 81 characters
                .department("123456789012345678901234567890123456789012345678901234567890123456789012345678901") // 81 characters
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - invalid salary (zero)
     */
    @Test
    void test_updateEmployee_failure_invalid_salary_zero() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different name")
                .department("Different Department")
                .salary(0)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - invalid salary (negative)
     */
    @Test
    void test_updateEmployee_failure_invalid_salary_negative() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different name")
                .department("Different Department")
                .salary(-1)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 1L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER.getCode()));
        assertThat(response.getErrorMessage(), is("Invalid request parameter"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(0)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - Not Found
     */
    @Test
    void test_updateEmployee_failure_not_found() throws ApiBusinessException {
        when(employeeService.updateEmployee((any(ApiV1EmployeesUpdateEmployeeRequest.class))))
                .thenThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.NOT_FOUND, "anyMessage"));
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different Name") // 81 characters
                .department("Different Department")
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 5L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.NOT_FOUND.getCode()));
        assertThat(response.getErrorMessage(), is("Employee not found"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(1)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - ApiBusinessException
     */
    @Test
    void test_updateEmployee_failure_ApiBusinessException() throws ApiBusinessException {
        when(employeeService.updateEmployee((any(ApiV1EmployeesUpdateEmployeeRequest.class))))
                .thenThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.SYSTEM_ERROR, "anyMessage"));
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different Name") // 81 characters
                .department("Different Department")
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 5L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.SYSTEM_ERROR.getCode()));
        assertThat(response.getErrorMessage(), is("System error"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(1)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }

    /**
     * updateEmployee - Failure case - ApiBusinessException
     */
    @Test
    void test_updateEmployee_failure_RuntimeException() throws ApiBusinessException {
        when(employeeService.updateEmployee((any(ApiV1EmployeesUpdateEmployeeRequest.class))))
                .thenThrow(RuntimeException.class);
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .name("Different Name") // 81 characters
                .department("Different Department")
                .salary(6000)
                .build();

        ApiV1EmployeesUpdateEmployeeResponse response = employeeController.updateEmployee(request, 5L);

        assertThat(response.getResultType(), is(ResultType.FAILURE.getCode()));
        assertThat(response.getErrorCode(), is(ErrorCode.UNKNOWN.getCode()));
        assertThat(response.getErrorMessage(), is("Unknown error"));
        assertThat(response.getEmployee(), is(nullValue()));
        verify(employeeService, times(1)).updateEmployee(any(ApiV1EmployeesUpdateEmployeeRequest.class));
    }
}
