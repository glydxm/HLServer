package com.glyfly.main.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Administrator on 2017/9/4.
 */
public class QuestionModel extends Model<QuestionModel> {
    public static QuestionModel questionModel = new QuestionModel().dao();
}
