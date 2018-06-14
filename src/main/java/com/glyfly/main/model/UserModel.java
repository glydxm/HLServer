package com.glyfly.main.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Administrator on 2017/4/9.
 */
public class UserModel extends Model<UserModel> {
    public static UserModel userModel = new UserModel().dao();
}
