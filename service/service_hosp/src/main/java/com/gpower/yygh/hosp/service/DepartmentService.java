package com.gpower.yygh.hosp.service;

import org.springframework.data.domain.Page;
import com.gpower.yygh.model.hosp.Department;
import com.gpower.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface DepartmentService  {
    void save(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);
}
