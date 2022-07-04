package com.gpower.easyexcel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    @Test
    public void testWrite(){
        List<UserData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUsername("LUCY"+i);
            list.add(userData);
        }
        //设置excel文件路径和文件名称
        String fileName = "E:\\data\\excel\\1.xlsx";
        EasyExcel.write(fileName,UserData.class).sheet("用户信息")
                .doWrite(list);
    }
}
