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
    private String userServerUid;
    private Date createdAtClient;
    private Date updatedAtClient;

    private Boolean isDeleted ;//如果新增字段，一定不要用基本数据类型，否则会报SQLiteConstraintException

    private Date begin_time;
    private Date end_time;
    private String title;
    private String detail;
    private long duration;

    @Generated(hash = 2021793152)
    public Event(Long id, String userServerUid, Date createdAtClient, Date updatedAtClient, Boolean isDeleted,
            Date begin_time, Date end_time, String title, String detail, long duration) {
        this.id = id;
        this.userServerUid = userServerUid;
        this.createdAtClient = createdAtClient;
        this.updatedAtClient = updatedAtClient;
        this.isDeleted = isDeleted;
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

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", begin_time=" + begin_time +
                ", end_time=" + end_time +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", duration=" + duration +
                '}';
    }

    public String getUserServerUid() {
        return this.userServerUid;
    }

    public void setUserServerUid(String userServerUid) {
        this.userServerUid = userServerUid;
    }

    public Date getCreatedAtClient() {
        return this.createdAtClient;
    }

    public void setCreatedAtClient(Date createdAtClient) {
        this.createdAtClient = createdAtClient;
    }

    public Date getUpdatedAtClient() {
        return this.updatedAtClient;
    }

    public void setUpdatedAtClient(Date updatedAtClient) {
        this.updatedAtClient = updatedAtClient;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}


