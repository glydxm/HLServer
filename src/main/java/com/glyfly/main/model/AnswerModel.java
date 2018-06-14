package com.glyfly.main.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Administrator on 2017/11/7.
 */
public class AnswerModel extends Model<AnswerModel> {
    public static AnswerModel answerModel = new AnswerModel().dao();
}
