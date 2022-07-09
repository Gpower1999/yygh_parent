package com.gpower.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gpower.yygh.cmn.client.DictFeignClient;
import com.gpower.yygh.hosp.repository.HospitalRepository;
import com.gpower.yygh.hosp.service.HospitalService;
import com.gpower.yygh.model.hosp.Hospital;
import com.gpower.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        //参数map转为hospital对象
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);
        //判断是否存在相同数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
        //如果存在，进行修改,不存在，进行添加
        if (hospitalExist != null) {
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(int page, int limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        Example<Hospital> example = Example.of(hospital, matcher);
        //调用方法 实现查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        //获取查询list集合 遍历
        pages.getContent().stream().forEach(item ->{
            this.setHospitalHosType(item);
        });
        return pages;
    }
    //更新医院信息
    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Hospital getHospByid(String id) {
        Hospital hospital = hospitalRepository.findById(id).get();
        return hospital;
    }

    private Hospital setHospitalHosType(Hospital hospital) {
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        String cityString = dictFeignClient.getName("Hostype", hospital.getCityCode());
        String districtString = dictFeignClient.getName("Hostype", hospital.getDistrictCode());
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("cityString",cityString);
        hospital.getParam().put("districtString",districtString);
        return hospital;
    }
}
