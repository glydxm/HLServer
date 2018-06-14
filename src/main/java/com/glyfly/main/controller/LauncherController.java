package com.glyfly.main.controller;

import com.jfinal.core.Controller;

/**
 * Created by Administrator on 2017/9/7.
 */
public class LauncherController extends Controller{

    public void getPreInfo(){
        renderHtml("preInfo");
    }
}
