package com.timer.jike.timemanager.bean;

/**
 * Created by jike on 2017/5/14.
 */

public class BaseProperty {
    protected Long id;
    protected String text;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

}
