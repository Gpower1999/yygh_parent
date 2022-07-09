package com.gpower.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gpower.yygh.cmn.listener.DictListener;
import com.gpower.yygh.cmn.mapper.DictMapper;
import com.gpower.yygh.cmn.service.DictService;
import com.gpower.yygh.common.result.Result;
import com.gpower.yygh.model.cmn.Dict;
import com.gpower.yygh.model.hosp.HospitalSet;
import com.gpower.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //设置haschildren
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            dict.setHasChildren(isChildren(dictId));
        }
        return dictList;
    }

    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        // String fileName = URLEncoder.encode("数据字典", "UTF-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<Dict> dictList = baseMapper.selectList(null);
        //Dict -> DictEeVo
        List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
        for (Dict dict : dictList) {
            DictEeVo dictVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictVo, DictEeVo.class);
            dictVoList.add(dictVo);
        }
        //调用方法，进行写入
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空，直接根据value查询
        if (StringUtils.isEmpty(dictCode)) {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {
            Dict codeDict = this.getDictByDictCode(dictCode);
            long parent_id = codeDict.getId();
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parent_id)
                    .eq("value", value));
            return finalDict.getName();

        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictCode获取对应id
        Dict dict = this.getDictByDictCode(dictCode);
        //根据id获取获取子节点
        List<Dict> list = this.findChildData(dict.getId());
        return list;
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dictCode", dictCode);
        return baseMapper.selectOne(wrapper);
    }

    //判断id下面是否还有子节点
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
