package com.glyfly.main.controller;

import com.glyfly.main.model.UserModel;
import com.jfinal.core.Controller;

import java.util.List;

/**
 * Created by Administrator on 2017/4/9.
 */
public class UserController extends Controller {

    public void get(){

        List<UserModel> list=UserModel.userModel.find("select * from USER");
        renderJson(list);
    }

    public void set(){
        String id = getPara("id");
        String name = getPara("name");
        String age = getPara("age");
        String phone = getPara("phone");
        UserModel userModel = new UserModel();
        userModel.set("id",id);
        userModel.set("name",name);
        userModel.set("age",age);
        userModel.set("phone",phone);
        userModel.save();
        renderJson("success");
    }



}
