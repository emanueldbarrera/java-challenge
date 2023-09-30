package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
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
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void init() {
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
        when(employeeRepository.findAll()).thenReturn(employees);

        when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee2));
    }

    /**
     * retrieveEmployees - Success case
     */
    @Test
    void test_retrieveEmployees_success() {
        final List<Employee> employees = employeeService.retrieveEmployees();

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
     * retrieveEmployees - Success case - empty database
     */
    @Test
    void test_retrieveEmployees_success_empty_db() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        final List<Employee> employees = employeeService.retrieveEmployees();

        assertThat(employees.size(), is(0));
    }

    /**
     * getEmployee - Success case
     *
     * @throws ApiBusinessException
     */
    @Test
    void test_getEmployee_success() throws ApiBusinessException {
        final Employee employee = employeeService.getEmployee(1L);

        assertThat(employee.getId(), is(1L));
        assertThat(employee.getDepartment(), is("Some Department"));
        assertThat(employee.getName(), is("Some Name"));
        assertThat(employee.getSalary(), is(3000));
    }

    /**
     * getEmployee - Failure case - employeeId is invalid
     */
    @Test
    void test_getEmployee_invalid_employeeId() {
        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class, () -> employeeService.getEmployee(-1L));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.NOT_FOUND));
        assertThat(apiBusinessException.getMessage(), is("Employee not found"));
    }

    /**
     * getEmployee - Failure case - employeeId is valid but does not exist in the database
     */
    @Test
    void test_getEmployee_nonexistent_employeeId() {
        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class, () -> employeeService.getEmployee(5L));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.NOT_FOUND));
        assertThat(apiBusinessException.getMessage(), is("Employee not found"));
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

        employeeService.saveEmployee(employee);

        verify(employeeRepository, times(1)).saveAndFlush(employee);
    }

    /**
     * deleteEmployee - Success case
     */
    @Test
    void test_deleteEmployee_success() {
        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    /**
     * updateEmployee - Success case
     */
    @Test
    void test_updateEmployee_success() {
        final Employee employee = new Employee();
        employee.setId(2L);
        employee.setName("Other Name");
        employee.setDepartment("Other Department");
        employee.setSalary(4500);

        employeeService.updateEmployee(employee);

        verify(employeeRepository, times(1)).saveAndFlush(employee);
    }
}
