package com.gpower.yygh.hosp.service;

import com.gpower.yygh.model.hosp.Hospital;
import com.gpower.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectHospPage(int page, int limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Hospital getHospByid(String id);
}
