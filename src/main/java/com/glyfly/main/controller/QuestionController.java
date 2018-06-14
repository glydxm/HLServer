package com.glyfly.main.controller;

import com.glyfly.main.model.QuestionModel;
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
 * Created by Administrator on 2017/9/4.
 */
public class QuestionController extends Controller {

    //提问
    public void askQuestion(){
        String pin = getHeader("pin");
        String username = getHeader("username");
        String q_title = getPara("qTitle");
        String q_tag = getPara("qTag");
        String q_picture = getPara("qPicture");
        long q_time = System.currentTimeMillis();
        if (!StringUtils.isNullOrEmpty(pin) && !StringUtils.isNullOrEmpty(q_title)){
            QuestionModel questionModel = new QuestionModel();
            questionModel.set("pin", pin);
            questionModel.set("q_id", ToolNum.randomNum(8));
            questionModel.set("q_username", username);
            questionModel.set("q_title", q_title);
            questionModel.set("q_time", q_time);
            questionModel.set("q_tag", q_tag);
            questionModel.set("q_picture", q_picture);
            Response response = new Response();
            try {
                questionModel.save();
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

    //关注问题
    public void attentionQuestion(){
        String pin = getHeader("pin");
        String q_id = getPara("qId");
        long a_time = System.currentTimeMillis();
        if (!StringUtils.isNullOrEmpty(pin) && !StringUtils.isNullOrEmpty(q_id)){
            Response response = new Response();
            try {
                Record record = new Record().set("pin", pin).set("q_id", q_id).set("a_time", a_time);
                Db.save("q_attention", record);
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

    //获取问题列表
    public void getQuestions(){
        String pin = getHeader("pin");
        Integer qType = getParaToInt("qType");
        Integer page_size = getParaToInt("pageSize");
        Integer page_num = getParaToInt("pageNum");
        if (page_size == null || page_size < 0){
            page_size = 20;
        }
        if (page_num == null || page_num < 0){
            page_num = 1;
        }
        List<QuestionModel> list = new ArrayList<QuestionModel>();
        Page<Record> questions  = null;
        if (qType == 1) {//热门
            questions = Db.paginate(page_num, page_size, "select *","from know_questions where q_look_num > ?", -1);
        }else if (qType == 2){//最新
            questions = Db.paginate(page_num, page_size, true,"select *","from know_questions group by q_time desc");
        }else if (qType == 3){//高悬赏
            questions = Db.paginate(page_num, page_size, true,"select *","from know_questions where q_high_reward > 0 group by q_high_reward");
        }else if (qType == 4){//关注
            Page<Record> attentions = Db.paginate(page_num, page_size, "select *","from q_attention where pin = " + pin);
            if (attentions != null) {
                for (Record attention : attentions.getList()){
                    QuestionModel questionModel = new QuestionModel();
                    Record q = Db.findFirst("select * from know_questions where " + attention.get("q_id") + " = " + attention.get("q_id"));
                    long curTime = System.currentTimeMillis();
                    long qTime = q.get("q_time");
                    questionModel.put("qTime", ToolDateTime.getBetween(qTime, curTime));
                    questionModel.put("qId", q.get("q_id"));
                    questionModel.put("qTitle", q.get("q_title"));
                    questionModel.put("qTag", q.get("q_tag"));
                    questionModel.put("qPicture", q.get("q_picture"));
                    questionModel.put("qAnswerNum", q.get("q_answer_num"));
                    questionModel.put("qAttentionNum", q.get("q_attention_num"));
                    questionModel.put("qLookNum", q.get("q_look_num"));
                    Record user = Db.findFirst("select * from user where pin = " + pin);
                    if (user != null) {
                        questionModel.put("qUsername", user.get("username"));
                    }
                    list.add(questionModel);
                }
            }
        }
        if (questions != null) {
            for (Record record : questions.getList()){
                QuestionModel questionModel = new QuestionModel();
                long curTime = System.currentTimeMillis();
                long qTime = record.get("q_time");
                Record user = Db.findFirst("select * from user where pin = '" + pin + "'");
                questionModel.put("qTime", ToolDateTime.getBetween(qTime, curTime));
                questionModel.put("qId", record.get("q_id"));
                questionModel.put("qTitle", record.get("q_title"));
                questionModel.put("qTag", record.get("q_tag"));
                questionModel.put("qPicture", record.get("q_picture"));
                questionModel.put("qAnswerNum", record.get("q_answer_num"));
                questionModel.put("qAttentionNum", record.get("q_attention_num"));
                questionModel.put("qLookNum", record.get("q_look_num"));
                if (user != null) {
                    questionModel.put("qUsername", user.get("username"));
                }
                list.add(questionModel);
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

    //根据问题id获取问题详情
    public void getQuestionByQId(){
        String q_id = getPara("q_id");
        Record record = Db.findFirst("select * from know_answers where q_id = " + q_id);
        QuestionModel questionModel = new QuestionModel();
        if (record != null){
            long curTime = System.currentTimeMillis();
            long qTime = record.get("q_time");
            questionModel.put("qTime", ToolDateTime.getBetween(qTime, curTime));
            questionModel.put("qId", record.get("q_id"));
            questionModel.put("qTitle", record.get("q_title"));
            questionModel.put("qTag", record.get("q_tag"));
            questionModel.put("qPicture", record.get("q_picture"));
            questionModel.put("qUsername", record.get("q_username"));
        }
        Response response = new Response();
        try {
            response.setObject(questionModel);
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
