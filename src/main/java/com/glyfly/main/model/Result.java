package com.glyfly.main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
public class Result<T> {

    private T object;
    private List<T> list;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void addList(List<T> list) {
        if (this.list == null){
            this.list = new ArrayList<T>();
        }
        this.list.addAll(list);
    }
}
