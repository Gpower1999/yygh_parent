package com.gpower.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gpower.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;
import com.gpower.yygh.hosp.repository.DepartmentRepository;
import com.gpower.yygh.hosp.service.DepartmentService;
import com.gpower.yygh.model.hosp.Department;
import com.gpower.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //paramMap -> Department
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString, Department.class);
        //根据医院编号 和 科室编号查询
        Department departmentExist = departmentRepository
                .getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (departmentExist != null) {
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(department);
        } else {
            departmentExist.setCreateTime(new Date());
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        //创建Pageable，设置当前页和每页记录数
        Pageable pageable = PageRequest.of(page - 1, limit);
        //创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department, matcher);

        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            departmentRepository.delete(department);
        }
    }

    //根据 医院编号查询医院所以科室列表
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //先创建list集合，用于最终的封装
        List<DepartmentVo> result = new ArrayList<>();
        //根据医院编号，查询医院所以科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        //所有科室列表信息
        List<Department> departmentList = departmentRepository.findAll(example);

        //根据大科室编号bigcode分组,获取每个大科室里面下级子科室
        Map<String, List<Department>> departmentMap
                = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        // 遍历map
        for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            //大科室编号
            String bigcode = entry.getKey();
            //大科室对应的全部数据
            List<Department> departmentList1 = entry.getValue();
            //封装大科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(departmentList1.get(0).getBigname());
            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : departmentList1) {
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                children.add(departmentVo2);
            }
            departmentVo1.setChildren(children);
            //最终放到result里面
            result.add(departmentVo1);
        }
        return result;
    }

    @Override
    public Object getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        return department != null ? department.getDepname() : null;
    }
}
