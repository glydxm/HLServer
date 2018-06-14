package com.glyfly.main.controller;

import com.glyfly.main.bean.UserBean;
import com.glyfly.main.model.Response;
import com.glyfly.main.model.UserLoginModel;
import com.glyfly.main.model.UserModel;
import com.glyfly.tools.ToolNum;
import com.glyfly.tools.encrypt.MD5Util;
import com.jfinal.core.Controller;
import com.mysql.jdbc.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
public class LoginController extends Controller {

    public void login(){
        String username = getPara("username");
        String password = getPara("password");
        String login_ip = getHeader("login_ip");
        if (!StringUtils.isNullOrEmpty(username) && !StringUtils.isNullOrEmpty(password)){
            List<UserModel> list = null;
            try {
                list = UserModel.userModel.find("select * from user where username = " + username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserModel userModel;
            Response response = new Response();
            if (list == null || list.size() == 0) {
                response.setCode(1);
                response.setMsg("用户不存在！");
            } else {
                userModel = list.get(0);
                if (username.equals(userModel.get("username")) && password.equals(userModel.get("password"))){
                    try {
                        String pin = userModel.get("pin");
                        UserLoginModel userLoginModel = UserLoginModel.userLoginModel.findFirst("select * from user_login where pin = '" + pin+ "'");
                        int login_times = userLoginModel.getInt("login_times");
                        if (userLoginModel == null){
                            userLoginModel = new UserLoginModel();
                            userLoginModel.set("login_times",login_times + 1);
                            userLoginModel.set("login_time",System.currentTimeMillis());
                            userLoginModel.set("login_ip",login_ip);
                            userLoginModel.save();
                        }else {
                            login_times = userLoginModel.get("login_times");
                            userLoginModel.set("login_times",login_times + 1);
                            userLoginModel.set("login_time",System.currentTimeMillis());
                            userLoginModel.set("login_ip",login_ip);
                            userLoginModel.update();
                        }
                        UserBean userBean = new UserBean();
                        userBean.setPin((String)userModel.get("pin"));
                        userBean.setUsername((String)userModel.get("username"));
                        userBean.setPicture((String)userModel.get("picture"));
                        userBean.setRealname((String)userModel.get("realname"));
                        userBean.setAge((Integer) userModel.get("age"));
                        userBean.setPhone((String) userModel.get("phone"));
                        userBean.setSex((String) userModel.get("sex"));
                        userBean.setAddress((String) userModel.get("address"));
                        userBean.setCookies((String) userModel.get("cookies"));

                        response.setCode(0);
                        response.setMsg("登陆成功！");
                        response.setObject(userBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.setCode(3);
                        response.setMsg("保存数据异常！");
                    }
                }else {
                    response.setCode(2);
                    response.setMsg("密码错误！");
                }
            }
            renderJson(response);
        }
    }

    public void register(){
        String username = getPara("username");
        String password = getPara("password");
        String login_ip = getHeader("login_ip");
        if (!StringUtils.isNullOrEmpty(username) && !StringUtils.isNullOrEmpty(password)){
            List<UserModel> list = null;
            try {
                list = UserModel.userModel.find("select * from user where username = " + username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Response response = new Response();
            long cookies_save_time = System.currentTimeMillis();
            String cookies = generateCookies(username, password, cookies_save_time);
            if (list == null || list.size() == 0) {
                try {
                    UserModel userModel = new UserModel();
                    String pin = ToolNum.generatePin();
                    userModel.set("pin", pin);
                    userModel.set("username",username);
                    userModel.set("password",password);
                    userModel.save();

                    UserLoginModel userLoginModel = new UserLoginModel();
                    userLoginModel.set("pin", pin);
                    userLoginModel.set("register_time", System.currentTimeMillis());
                    userLoginModel.set("login_time", System.currentTimeMillis());
                    userLoginModel.set("login_times", 1);
                    userLoginModel.set("register_ip", login_ip);
                    userLoginModel.set("login_ip", login_ip);
                    userLoginModel.set("cookies", cookies);
                    userLoginModel.set("cookies_save_time", cookies_save_time);
                    userLoginModel.save();

                    response.setCode(0);
                    response.setMsg("注册成功");
                    UserBean userBean = new UserBean();
                    userBean.setPin(pin);
                    userBean.setUsername(username);
                    userBean.setCookies(cookies);
                    response.setObject(userBean);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setCode(1);
                    response.setMsg("保存数据失败！");
                }
            } else {
                response.setCode(2);
                response.setMsg("用户已存在！");
            }
            renderJson(response);
        }
    }

    private String generateCookies(String username, String password, long time){
        String cookies = MD5Util.md5(username + password + time);
        return "[" + cookies + "]";
    }
}
