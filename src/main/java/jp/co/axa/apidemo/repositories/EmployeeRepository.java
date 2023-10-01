package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static jp.co.axa.apidemo.config.EhCacheConfig.CACHE_GET_EMPLOYEES;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Cacheable(cacheNames = CACHE_GET_EMPLOYEES)
    Page<Employee> findAll(Pageable pageable);
}
