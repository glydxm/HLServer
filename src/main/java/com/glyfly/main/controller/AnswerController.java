package com.glyfly.main.controller;

import com.glyfly.main.model.AnswerModel;
import com.glyfly.main.model.Response;
import com.glyfly.tools.ToolDateTime;
import com.glyfly.tools.ToolNum;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public class AnswerController extends Controller{

    //回答
    public void answerQuestion(){
        String pin = getHeader("pin");
        String username = getHeader("username");
        String q_id = getPara("qId");
        String a_content = getPara("aContent");
        if (!StringUtils.isNullOrEmpty(pin)){
            long q_time = System.currentTimeMillis();
            AnswerModel answerModel = new AnswerModel();
            answerModel.set("q_id", q_id);
            answerModel.set("pin", pin);
            answerModel.set("a_id", ToolNum.randomNum(8));
            answerModel.set("a_username", username);
            answerModel.set("a_content", a_content);
            answerModel.set("a_time", q_time);
            Response response = new Response();
            try {
                answerModel.save();
                Record record = Db.findFirst("select * from know_questions where q_id = '" + q_id+ "'");
                record.set("q_answer_num", record.getInt("q_answer_num") + 1);
                Db.update("know_questions", record);
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

    //根据问题id获取回答列表
    public void getAnswersByQId(){
        String q_id = getPara("qId");
        Integer page_size = getParaToInt("pageSize");
        Integer page_num = getParaToInt("pageNum");
        if (page_size == null || page_size < 0){
            page_size = 20;
        }
        if (page_num == null || page_num < 0){
            page_num = 1;
        }
        Page<Record> records = Db.paginate(page_num, page_size, "select *","from know_answers where q_id = '" + q_id+ "'");
        List<AnswerModel> list = new ArrayList<AnswerModel>();
        if (records != null) {
            for (Record record:records.getList()){
                AnswerModel answerModel = new AnswerModel();
                long curTime = System.currentTimeMillis();
                long aTime = record.getLong("a_time");
                answerModel.put("aTime", ToolDateTime.getBetween(aTime, curTime));
                answerModel.put("aId", record.get("a_id"));
                answerModel.put("aUsername", record.get("a_username"));
                answerModel.put("aContent", record.get("a_content"));
                answerModel.put("aPraiseNum", record.get("a_praise_num"));
                answerModel.put("aAccepted", record.getInt("a_accepted") == 0 ? false : true);
                list.add(answerModel);
            }
        }
        Response response = new Response();
        try {
            response.setList(list);
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
