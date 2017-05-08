package com.timer.jike.timemanager.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by jike on 2017/5/7.
 */

public class BmobEvent extends BmobObject{
    private String userId;//关联user表
    private Long idLocal;
    private Long duration;
    private String detail;
    private String title;
    private Date endTime;
    private Date beginTime;

    public BmobEvent() {
        this.setTableName("Event");//后台的表名
    }
}
