package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Test class for {@link EmployeeServiceImplTest}
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
        employee2.setId(1L);
        employee2.setName("Other Name");
        employee2.setDepartment("Other Department");
        employee2.setSalary(3000);

        when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee2));
    }

    /**
     * Success case
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
     * Failure case - employeeId is invalid
     */
    @Test
    void test_getEmployee_invalid_employeeId() {
        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class, () -> employeeService.getEmployee(-1L));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.NOT_FOUND));
        assertThat(apiBusinessException.getMessage(), is("Employee not found"));
    }

    /**
     * Failure case - employeeId is valid but does not exist in the database
     */
    @Test
    void test_getEmployee_nonexistent_employeeId() {
        final ApiBusinessException apiBusinessException = assertThrows(ApiBusinessException.class, () -> employeeService.getEmployee(5L));
        assertThat(apiBusinessException.getErrorCode(), is(ErrorCode.NOT_FOUND));
        assertThat(apiBusinessException.getMessage(), is("Employee not found"));
    }
}
