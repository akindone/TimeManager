package com.timer.jike.timemanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;

/**
 * Created by jike on 2017/5/6.
 */

@Entity(indexes = {
        @Index(value = "id, begin_time DESC", unique = true)
})
public class Event {

    @Id
    private Long id;

    private Date begin_time;
    private Date end_time;
    private String title;
    private String detail;
    private long duration;

    @Generated(hash = 1338613400)
    public Event(Long id, Date begin_time, Date end_time, String title, String detail, long duration) {
        this.id = id;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.title = title;
        this.detail = detail;
        this.duration = duration;
    }

    @Generated(hash = 344677835)
    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Date begin_time) {
        this.begin_time = begin_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}


