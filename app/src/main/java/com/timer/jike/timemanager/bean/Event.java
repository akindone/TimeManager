package com.timer.jike.timemanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.DaoException;

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
    //是否是计划中；类别；重要程度；类别是不能删的
    //每周1 每月1 每年1 去生成统计
    //按日期查询

    private Long predictabilityId;
    private Long typeId;
    private Long importanceId;
    
    @ToOne(joinProperty = "predictabilityId")
    private PropertyPredictability predictability;
    @ToOne(joinProperty = "typeId")
    private PropertyType type;
    @ToOne(joinProperty = "importanceId")
    private PropertyImportance importance;

    private Date begin_time;
    private Date end_time;
    private String title;
    private String detail;
    private long duration;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1542254534)
    private transient EventDao myDao;
    @Generated(hash = 2017738211)
    private transient Long predictability__resolvedKey;
    @Generated(hash = 506996655)
    private transient Long type__resolvedKey;
    @Generated(hash = 499670077)
    private transient Long importance__resolvedKey;

    @Generated(hash = 1298950197)
    public Event(Long id, String userServerUid, Date createdAtClient, Date updatedAtClient, Boolean isDeleted,
            Long predictabilityId, Long typeId, Long importanceId, Date begin_time, Date end_time, String title,
            String detail, long duration) {
        this.id = id;
        this.userServerUid = userServerUid;
        this.createdAtClient = createdAtClient;
        this.updatedAtClient = updatedAtClient;
        this.isDeleted = isDeleted;
        this.predictabilityId = predictabilityId;
        this.typeId = typeId;
        this.importanceId = importanceId;
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

    public Long getPredictabilityId() {
        return this.predictabilityId;
    }

    public void setPredictabilityId(Long predictabilityId) {
        this.predictabilityId = predictabilityId;
    }

    public Long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getImportanceId() {
        return this.importanceId;
    }

    public void setImportanceId(Long importanceId) {
        this.importanceId = importanceId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1093874354)
    public PropertyPredictability getPredictability() {
        Long __key = this.predictabilityId;
        if (predictability__resolvedKey == null || !predictability__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PropertyPredictabilityDao targetDao = daoSession.getPropertyPredictabilityDao();
            PropertyPredictability predictabilityNew = targetDao.load(__key);
            synchronized (this) {
                predictability = predictabilityNew;
                predictability__resolvedKey = __key;
            }
        }
        return predictability;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 439439022)
    public void setPredictability(PropertyPredictability predictability) {
        synchronized (this) {
            this.predictability = predictability;
            predictabilityId = predictability == null ? null : predictability.getId();
            predictability__resolvedKey = predictabilityId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1978375366)
    public PropertyType getType() {
        Long __key = this.typeId;
        if (type__resolvedKey == null || !type__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PropertyTypeDao targetDao = daoSession.getPropertyTypeDao();
            PropertyType typeNew = targetDao.load(__key);
            synchronized (this) {
                type = typeNew;
                type__resolvedKey = __key;
            }
        }
        return type;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1708601851)
    public void setType(PropertyType type) {
        synchronized (this) {
            this.type = type;
            typeId = type == null ? null : type.getId();
            type__resolvedKey = typeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1971085823)
    public PropertyImportance getImportance() {
        Long __key = this.importanceId;
        if (importance__resolvedKey == null || !importance__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PropertyImportanceDao targetDao = daoSession.getPropertyImportanceDao();
            PropertyImportance importanceNew = targetDao.load(__key);
            synchronized (this) {
                importance = importanceNew;
                importance__resolvedKey = __key;
            }
        }
        return importance;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 849748644)
    public void setImportance(PropertyImportance importance) {
        synchronized (this) {
            this.importance = importance;
            importanceId = importance == null ? null : importance.getId();
            importance__resolvedKey = importanceId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1459865304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEventDao() : null;
    }
}


