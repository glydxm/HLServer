package com.glyfly.main.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
public class Response<T> {
    private int code;
    private String msg;
    private Result<T> result;
    private Object object;
    private List<T> list;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Result getResult() {
        return result;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setResultList(List<T> list){
        if (list != null ){
            if (result == null) {
                result = new Result<T>();
            }
            result.setList(list);
        }
    }

    public void addResultList(List<T> list){
        if (list != null ){
            if (result == null) {
                result = new Result<T>();
            }
            result.addList(list);
        }
    }
}
