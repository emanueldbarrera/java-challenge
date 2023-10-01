package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.common.ApiBusinessException;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.models.ApiV1DepartmentsDeleteDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsGetDepartmentsRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsSaveDepartmentRequest;
import jp.co.axa.apidemo.models.ApiV1DepartmentsUpdateDepartmentRequest;

import java.util.List;

public interface DepartmentService {

    List<Department> getDepartments(ApiV1DepartmentsGetDepartmentsRequest request);

    Department getDepartment(ApiV1DepartmentsGetDepartmentRequest request) throws ApiBusinessException;

    Department saveDepartment(ApiV1DepartmentsSaveDepartmentRequest request) throws ApiBusinessException;

    Department deleteDepartment(ApiV1DepartmentsDeleteDepartmentRequest request) throws ApiBusinessException;

    Department updateDepartment(ApiV1DepartmentsUpdateDepartmentRequest request) throws ApiBusinessException;
}