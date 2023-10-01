package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.common.ErrorCode;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.models.ApiV1DepartmentsDeleteDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentsRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsSaveDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsUpdateDepartmentRequest;
import jp.co.axa.apidemo.repositories.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getDepartments(ApiV1DepartmentsGetDepartmentsRequest request) {
        Page<Department> departmentPage = departmentRepository.findAll(
                PageRequest.of(request.getOffset(), request.getLimit(), Sort.by("id")));
        return departmentPage.getContent();
    }

    public Department getDepartment(ApiV1DepartmentsGetDepartmentRequest request) throws ApiBusinessException {
        final Long departmentId = request.getId();
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
        if (!optionalDepartment.isPresent()) {
            throw new ApiBusinessException("0-1-1", ErrorCode.NOT_FOUND, "Department not found");
        }
        return optionalDepartment.get();
    }

    public Department saveDepartment(ApiV1DepartmentsSaveDepartmentRequest request) throws ApiBusinessException {
        // Save department
        final Department department = Department.builder()
                .name(request.getName())
                .build();
        try {
            return departmentRepository.saveAndFlush(department);
        } catch (Exception e) {
            throw new ApiBusinessException("0-1-2", ErrorCode.SYSTEM_ERROR, "Database error");
        }
    }

    public Department deleteDepartment(ApiV1DepartmentsDeleteDepartmentRequest request) throws ApiBusinessException {
        // Get department from database
        final Long departmentId = request.getDepartmentId();
        Optional<Department> optionalDepartment;
        try {
            optionalDepartment = departmentRepository.findById(departmentId);
        } catch (Exception e) {
            throw new ApiBusinessException("0-1-3", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        if (!optionalDepartment.isPresent()) {
            throw new ApiBusinessException("0-1-4", ErrorCode.NOT_FOUND, "Department not found");
        }

        // Delete department and return a copy
        try {
            departmentRepository.deleteById(departmentId);
        } catch (Exception e) {
            throw new ApiBusinessException("0-1-5", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        return optionalDepartment.get();
    }

    public Department updateDepartment(ApiV1DepartmentsUpdateDepartmentRequest request) throws ApiBusinessException {
        // Get department from database
        Optional<Department> optionalDepartment;
        try {
            optionalDepartment = departmentRepository.findById(request.getId());
        } catch (Exception e) {
            throw new ApiBusinessException("0-1-6", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        if (!optionalDepartment.isPresent()) {
            throw new ApiBusinessException("0-1-7", ErrorCode.NOT_FOUND, "Department not found");
        }

        // Update Department entity
        final Department department = optionalDepartment.get();
        try {
            department.setName(request.getName() != null ? request.getName() : department.getName());
            departmentRepository.saveAndFlush(department);
        } catch (Exception e) {
            throw new ApiBusinessException("0-1-8", ErrorCode.SYSTEM_ERROR, "Database error");
        }
        return department;
    }
}