package com.glyfly.main.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Administrator on 2017/5/19.
 */
public class NewsModel extends Model<NewsModel> {
    public static NewsModel newsModel = new NewsModel().dao();
}
