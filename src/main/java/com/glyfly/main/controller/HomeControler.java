package com.glyfly.main.controller;

import com.glyfly.main.model.Response;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/4.
 */
public class HomeControler extends Controller {

    public void data(){
        String pin = getHeader("pin");
        List<Record> data = new ArrayList<>();
        List<Record> allData = Db.find("select * from home_floor where isSelect = '" + "1" + "'");
        List<Record> floorData1 = new ArrayList<>();
        List<Record> floorData2 = new ArrayList<>();
        List<Record> floorData3 = new ArrayList<>();
        List<Record> floorData4 = new ArrayList<>();
        for (Record record : allData) {
            if ("1".equals(record.getStr("floorType"))) {
                floorData1.add(record);
            } else if ("2".equals(record.getStr("floorType"))) {
                floorData2.add(record);
            } else if ("32".equals(record.getStr("floorType"))) {
                floorData3.add(record);
            } else if ("33".equals(record.getStr("floorType"))) {
                floorData4.add(record);
            }
        }
        Record record1 = new Record();
        record1.set("floorType", "1");
        record1.set("floorData", floorData1);
        data.add(record1);

        Record record2 = new Record();
        record2.set("floorType", "2");
        record2.set("floorData", floorData2);
        data.add(record2);

        Record record3 = new Record();
        record3.set("floorType", "3");
        record3.set("floorData", floorData3);
        data.add(record3);

        Record record4 = new Record();
        record4.set("floorType", "3");
        record4.set("floorData", floorData4);
        data.add(record4);
        List<Record> listData = Db.find("select * from home_list where state = '" + "0" + "'");
        for (Record record : listData) {
            Record object = new Record();
            object.set("floorType", "0");
            object.set("listItem", record);
            data.add(object);
        }

        Response response = new Response();
        try {
            response.setList(data);
            response.setCode(0);
            response.setMsg("success");
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(1);
            response.setMsg("fail");
        }
        renderJson(response);
    }
}
