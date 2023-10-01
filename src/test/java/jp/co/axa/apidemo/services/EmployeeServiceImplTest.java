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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Test class for {@link EmployeeServiceImpl}
 */
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void init() {
        initMocks(this);
        // Create test Department entities
        Department department1 = new Department();
        department1.setId(1L);
        department1.setName("Some Department");

        Department department2 = new Department();
        department2.setId(2L);
        department2.setName("Other Department");

        // Set test Employee entities
        final Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Some Name");
        employee1.setDepartment(department1);
        employee1.setSalary(3000);

        final Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Other Name");
        employee2.setDepartment(department2);
        employee2.setSalary(4500);

        List<Employee> employees = new java.util.ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee2));

        when(employeeRepository.saveAndFlush(any(Employee.class))).thenReturn(employee1);

        when(departmentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(department2));
    }

    /**
     * getEmployees - Success case
     */
    @Test
    void test_getEmployees_success() {
        final List<Employee> employees = employeeService.getEmployees();

        assertThat(employees.size(), is(2));
        assertThat(employees.get(0).getId(), is(1L));
        assertThat(employees.get(0).getName(), is("Some Name"));
        assertThat(employees.get(0).getDepartment().getId(), is(1L));
        assertThat(employees.get(0).getDepartment().getName(), is("Some Department"));
        assertThat(employees.get(0).getSalary(), is(3000));
        assertThat(employees.get(1).getId(), is(2L));
        assertThat(employees.get(1).getDepartment().getId(), is(2L));
        assertThat(employees.get(1).getDepartment().getName(), is("Other Department"));
        assertThat(employees.get(1).getSalary(), is(4500));
    }

    /**
     * getEmployees - Success case - empty database
     */
    @Test
    void test_getEmployees_success_empty_db() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        final List<Employee> employees = employeeService.getEmployees();

        assertThat(employees.size(), is(0));
    }

    /**
     * getEmployee - Success case
     *
     * @throws ApiBusinessException
     */
    @Test
    void test_getEmployee_success() throws ApiBusinessException {
        final ApiV1EmployeesGetEmployeeRequest request = ApiV1EmployeesGetEmployeeRequest.builder()
                .employeeId(1L)
                .build();
        final Employee employee = employeeService.getEmployee(request);

        assertThat(employee.getId(), is(1L));
        assertThat(employee.getDepartment().getId(), is(1L));
        assertThat(employee.getDepartment().getName(), is("Some Department"));
        assertThat(employee.getName(), is("Some Name"));
        assertThat(employee.getSalary(), is(3000));
    }

    /**
     * getEmployee - Failure case - employeeId is valid but does not exist in the database
     */
    @Test
    void test_getEmployee_nonexistent_employeeId() {
        final ApiV1EmployeesGetEmployeeRequest request = ApiV1EmployeesGetEmployeeRequest.builder()
                .employeeId(5L)
                .build();
        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.getEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.NOT_FOUND));
        assertThat(apiBusinessException.getMessage(), is("Employee not found"));
    }

    /**
     * saveEmployee - Success case
     */
    @Test
    void test_saveEmployee_success() throws ApiBusinessException {
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Some Name")
                .departmentId(1L)
                .salary(3000)
                .build();

        final Employee employee = employeeService.saveEmployee(request);

        verify(employeeRepository, times(1)).saveAndFlush(any(Employee.class));
        assertThat(employee, is(notNullValue()));
        assertThat(employee.getName(), is("Some Name"));
        assertThat(employee.getId(), is(1L));
        assertThat(employee.getDepartment().getId(), is(1L));
        assertThat(employee.getDepartment().getName(), is("Some Department"));
        assertThat(employee.getSalary(), is(3000));
    }

    /**
     * saveEmployee - Failure case - department not found
     */
    @Test
    void test_saveEmployee_failure_nonexistent_department() throws ApiBusinessException {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Some Name")
                .departmentId(1L)
                .salary(3000)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.saveEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER));
        assertThat(apiBusinessException.getMessage(), is("Department not found"));
    }

    /**
     * saveEmployee - Failure case - database error
     */
    @Test
    void test_saveEmployee_failure_database_error() throws ApiBusinessException {
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenThrow(RuntimeException.class);
        final ApiV1EmployeesSaveEmployeeRequest request = ApiV1EmployeesSaveEmployeeRequest.builder()
                .name("Some Name")
                .departmentId(1L)
                .salary(3000)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.saveEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.SYSTEM_ERROR));
        assertThat(apiBusinessException.getMessage(), is("Database error"));
    }

    /**
     * deleteEmployee - Success case
     */
    @Test
    void test_deleteEmployee_success() throws ApiBusinessException {
        final ApiV1EmployeesDeleteEmployeeRequest request = ApiV1EmployeesDeleteEmployeeRequest.builder()
                .employeeId(1L)
                .build();
        final Employee employee = employeeService.deleteEmployee(request);

        verify(employeeRepository, times(1)).deleteById(1L);
        assertThat(employee.getDepartment().getId(), is(1L));
        assertThat(employee.getDepartment().getName(), is("Some Department"));
        assertThat(employee.getName(), is("Some Name"));
        assertThat(employee.getSalary(), is(3000));
    }

    /**
     * deleteEmployee - Failure case - repository does not find entity
     */
    @Test
    void test_deleteEmployee_failure_not_found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        final ApiV1EmployeesDeleteEmployeeRequest request = ApiV1EmployeesDeleteEmployeeRequest.builder()
                .employeeId(1L)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.deleteEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.NOT_FOUND));
        assertThat(apiBusinessException.getMessage(), is("Employee not found"));
        verify(employeeRepository, times(0)).deleteById(1L);
    }

    /**
     * deleteEmployee - Failure case - database error on get
     */
    @Test
    void test_deleteEmployee_database_error_on_get() {
        doThrow(RuntimeException.class).when(employeeRepository).findById(1L);
        final ApiV1EmployeesDeleteEmployeeRequest request = ApiV1EmployeesDeleteEmployeeRequest.builder()
                .employeeId(1L)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.deleteEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.SYSTEM_ERROR));
        assertThat(apiBusinessException.getMessage(), is("Database error"));
        verify(employeeRepository, times(0)).deleteById(1L);
    }

    /**
     * deleteEmployee - Failure case - database error on delete
     */
    @Test
    void test_deleteEmployee_database_error_on_delete() {
        doThrow(RuntimeException.class).when(employeeRepository).deleteById(1L);
        final ApiV1EmployeesDeleteEmployeeRequest request = ApiV1EmployeesDeleteEmployeeRequest.builder()
                .employeeId(1L)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.deleteEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.SYSTEM_ERROR));
        assertThat(apiBusinessException.getMessage(), is("Database error"));
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    /**
     * updateEmployee - Success case
     */
    @Test
    void test_updateEmployee_success() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .employeeId(1L)
                .name("Different Name")
                .departmentId(2L)
                .salary(4500)
                .build();

        final Employee employee = employeeService.updateEmployee(request);

        assertThat(employee.getId(), is(1L));
        assertThat(employee.getName(), is("Different Name"));
        assertThat(employee.getDepartment().getId(), is(2L));
        assertThat(employee.getSalary(), is(4500));
        verify(employeeRepository, times(1)).saveAndFlush(any(Employee.class));
    }

    /**
     * updateEmployee - Success case - only update name
     */
    @Test
    void test_updateEmployee_success_update_name() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .employeeId(1L)
                .name("Different Name")
                .build();

        final Employee employee = employeeService.updateEmployee(request);

        assertThat(employee.getId(), is(1L));
        assertThat(employee.getName(), is("Different Name"));
        assertThat(employee.getDepartment().getId(), is(1L));
        assertThat(employee.getSalary(), is(3000));
        verify(employeeRepository, times(1)).saveAndFlush(any(Employee.class));
    }

    /**
     * updateEmployee - Success case - only update department
     */
    @Test
    void test_updateEmployee_success_update_department() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .employeeId(1L)
                .departmentId(2L)
                .build();

        final Employee employee = employeeService.updateEmployee(request);

        assertThat(employee.getId(), is(1L));
        assertThat(employee.getName(), is("Some Name"));
        assertThat(employee.getDepartment().getId(), is(2L));
        assertThat(employee.getSalary(), is(3000));
        verify(employeeRepository, times(1)).saveAndFlush(any(Employee.class));
    }

    /**
     * updateEmployee - Success case - only update salary
     */
    @Test
    void test_updateEmployee_success_update_salary() throws ApiBusinessException {
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .employeeId(1L)
                .salary(4500)
                .build();

        final Employee employee = employeeService.updateEmployee(request);

        assertThat(employee.getId(), is(1L));
        assertThat(employee.getName(), is("Some Name"));
        assertThat(employee.getDepartment().getId(), is(1L));
        assertThat(employee.getSalary(), is(4500));
        verify(employeeRepository, times(1)).saveAndFlush(any(Employee.class));
    }

    /**
     * updateEmployee - Failure case - repository does not find entity
     */
    @Test
    void test_updateEmployee_failure_not_found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .employeeId(1L)
                .name("Different Name")
                .departmentId(2L)
                .salary(4500)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.updateEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.NOT_FOUND));
        assertThat(apiBusinessException.getMessage(), is("Employee not found"));
        verify(employeeRepository, times(0)).deleteById(1L);
    }

    /**
     * updateEmployee - Failure case - department not found
     */
    @Test
    void test_updateEmployee_failure_nonexistent_department() {
        when(departmentRepository.findById(2L)).thenReturn(Optional.empty());
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .employeeId(1L)
                .name("Different Name")
                .departmentId(2L)
                .salary(4500)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.updateEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.INVALID_REQUEST_PARAMETER));
        assertThat(apiBusinessException.getMessage(), is("Department not found"));
        verify(employeeRepository, times(0)).deleteById(1L);
    }

    /**
     * updateEmployee - Failure case - database error on get
     */
    @Test
    void test_updateEmployee_database_error_on_get() {
        doThrow(RuntimeException.class).when(employeeRepository).findById(1L);
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .employeeId(1L)
                .name("Different Name")
                .departmentId(2L)
                .salary(4500)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.updateEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.SYSTEM_ERROR));
        assertThat(apiBusinessException.getMessage(), is("Database error"));
        verify(employeeRepository, times(0)).deleteById(1L);
    }

    /**
     * updateEmployee - Failure case - database error on delete
     */
    @Test
    void test_updateEmployee_database_error_on_delete() {
        doThrow(RuntimeException.class).when(employeeRepository).saveAndFlush(any(Employee.class));
        final ApiV1EmployeesUpdateEmployeeRequest request = ApiV1EmployeesUpdateEmployeeRequest.builder()
                .employeeId(1L)
                .name("Different Name")
                .departmentId(2L)
                .salary(4500)
                .build();

        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class,
                () -> employeeService.updateEmployee(request));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.SYSTEM_ERROR));
        assertThat(apiBusinessException.getMessage(), is("Database error"));
        verify(employeeRepository, times(1)).saveAndFlush(any(Employee.class));
    }
}
