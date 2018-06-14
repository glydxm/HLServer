package com.glyfly.main.config;

import com.glyfly.main.controller.*;
import com.glyfly.main.model.*;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

/**
 * Created by Administrator on 2017/4/9.
 */
public class AppConfig extends JFinalConfig {

    public static void main(String[] args) {
        /**
         * 特别注意：IDEA 之下建议的启动方式，仅比 eclipse 之下少了最后一个参数
         */
        JFinal.start("src/main/webapp", 2017, "/", 5);
    }

    public void configConstant(Constants constants) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        PropKit.use("a_little_config.txt");
        constants.setDevMode(PropKit.getBoolean("devMode", false));
    }

    public void configRoute(Routes routes) {
        routes.add("/highlife/launcher", LauncherController.class);
        routes.add("/highlife", LoginController.class);
        routes.add("/highlife/user", UserController.class);

        routes.add("/highlife/home", HomeControler.class);

        routes.add("/highlife/know/question", QuestionController.class);
        routes.add("/highlife/know/answer", AnswerController.class);
    }

    public void configEngine(Engine engine) {
        engine.addSharedFunction("/index.html");
    }

    public void configPlugin(Plugins plugins) {
        // 配置Mysql数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        plugins.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.addMapping("user", UserModel.class);
        arp.addMapping("user_login", UserLoginModel.class);
        arp.addMapping("know_questions", QuestionModel.class);
        arp.addMapping("know_answers", AnswerModel.class);
        arp.addMapping("news", NewsModel.class);
        plugins.add(arp);
    }

    public void configInterceptor(Interceptors interceptors) {

    }

    public void configHandler(Handlers handlers) {

    }
}
