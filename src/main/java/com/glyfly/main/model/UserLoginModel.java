package com.glyfly.main.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Administrator on 2017/9/5.
 */
public class UserLoginModel extends Model<UserLoginModel> {
    public static UserLoginModel userLoginModel = new UserLoginModel().dao();
}
