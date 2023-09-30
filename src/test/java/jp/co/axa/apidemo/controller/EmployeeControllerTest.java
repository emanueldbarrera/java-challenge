package jp.co.axa.apidemo.controller;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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

    @BeforeEach
    void init() throws ApiBusinessException {
        initMocks(this);

        // Set test Employee entities
        final Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Some Name");
        employee1.setDepartment("Some Department");
        employee1.setSalary(3000);

        final Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Other Name");
        employee2.setDepartment("Other Department");
        employee2.setSalary(4500);

        List<Employee> employees = new java.util.ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        when(employeeService.retrieveEmployees()).thenReturn(employees);

        when(employeeService.getEmployee(any(Long.class)))
                .thenThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.NOT_FOUND, "anyMessage"));
        doReturn(employee1).when(employeeService).getEmployee(1L);
        doReturn(employee2).when(employeeService).getEmployee(2L);

        employeeController = new EmployeeController(employeeService);
    }

    /**
     * getEmployees - Success case
     */
    @Test
    void test_getEmployees_success() {
        final List<Employee> employees = employeeController.getEmployees();

        assertThat(employees.size(), is(2));
        assertThat(employees.get(0).getId(), is(1L));
        assertThat(employees.get(0).getName(), is("Some Name"));
        assertThat(employees.get(0).getDepartment(), is("Some Department"));
        assertThat(employees.get(0).getSalary(), is(3000));
        assertThat(employees.get(1).getId(), is(2L));
        assertThat(employees.get(1).getName(), is("Other Name"));
        assertThat(employees.get(1).getDepartment(), is("Other Department"));
        assertThat(employees.get(1).getSalary(), is(4500));
    }

    /**
     * getEmployees - Success case - service returns empty list
     */
    @Test
    void test_getEmployees_success_empty_db() {
        when(employeeService.retrieveEmployees()).thenReturn(new ArrayList<>());

        final List<Employee> employees = employeeController.getEmployees();

        assertThat(employees.size(), is(0));
    }

    /**
     * getEmployee - Success case
     */
    @Test
    void test_getEmployee_success() {
        final Employee employee = employeeController.getEmployee(1L);
        assertThat(employee.getId(), is(1L));
        assertThat(employee.getName(), is("Some Name"));
        assertThat(employee.getDepartment(), is("Some Department"));
        assertThat(employee.getSalary(), is(3000));
    }

    /**
     * getEmployee - Failure case - employeeId is invalid
     */
    @Test
    void test_getEmployee_invalid_employeeId() {
        final Employee employee = employeeController.getEmployee(-1L);
        assertThat(employee, is(nullValue()));
    }

    /**
     * getEmployee - Failure case - employeeId is valid but is not found by the service
     */
    @Test
    void test_getEmployee_nonexistent_employeeId() {
        final Employee employee = employeeController.getEmployee(5L);
        assertThat(employee, is(nullValue()));
    }

    /**
     * getEmployee - Failure case - ApiBusinessException
     */
    @Test
    void test_getEmployee_ApiBusinessException() throws ApiBusinessException {
        doThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.SYSTEM_ERROR, "anyErrorMessage"))
                .when(employeeService).getEmployee(any(Long.class));
        final Employee employee = employeeController.getEmployee(5L);
        assertThat(employee, is(nullValue()));
    }

    /**
     * saveEmployees - Success case
     */
    @Test
    void test_saveEmployees_success() {
        final Employee employee = new Employee();
        employee.setId(2L);
        employee.setName("Other Name");
        employee.setDepartment("Other Department");
        employee.setSalary(4500);

        employeeController.saveEmployee(employee);

        verify(employeeService, times(1)).saveEmployee(employee);
    }

    /**
     * deleteEmployee - Success case
     */
    @Test
    void test_deleteEmployee_success() {
        employeeController.deleteEmployee(1L);

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    /**
     * updateEmployee - Success case
     */
    @Test
    void test_updateEmployee_success() {
        final Employee employee = new Employee();
        employee.setName("Different Name");
        employee.setDepartment("Different Department");
        employee.setSalary(6000);

        employeeController.updateEmployee(employee, 2L);

        verify(employeeService, times(1)).updateEmployee(employee);
    }

    /**
     * updateEmployee - Failure case - employeeId is invalid
     */
    @Test
    void test_updateEmployee_invalid_employeeId() throws ApiBusinessException {
        final Employee employee = new Employee();
        employee.setName("Different Name");
        employee.setDepartment("Different Department");
        employee.setSalary(6000);

        doThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.SYSTEM_ERROR, "anyErrorMessage"))
                .when(employeeService).getEmployee(any(Long.class));
        employeeController.updateEmployee(employee, -1L);

        verify(employeeService, times(0)).updateEmployee(employee);
    }

    /**
     * updateEmployee - Failure case - employeeId is not found by the service
     */
    @Test
    void test_updateEmployee_nonexistent_employeeId() throws ApiBusinessException {
        final Employee employee = new Employee();
        employee.setName("Different Name");
        employee.setDepartment("Different Department");
        employee.setSalary(6000);

        doThrow(new ApiBusinessException("anyPlaceCode", ErrorCode.NOT_FOUND, "anyErrorMessage"))
                .when(employeeService).getEmployee(any(Long.class));
        employeeController.updateEmployee(employee, -1L);

        verify(employeeService, times(0)).updateEmployee(employee);
    }
}
