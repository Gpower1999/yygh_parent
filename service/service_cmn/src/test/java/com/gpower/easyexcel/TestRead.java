package com.gpower.easyexcel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

public class TestRead {
    @Test
    public void testread(){
        String fileName = "E:\\data\\excel\\1.xlsx";
        EasyExcel.read(fileName,UserData.class,new ExcelListener()).sheet().doRead();
    }
}
