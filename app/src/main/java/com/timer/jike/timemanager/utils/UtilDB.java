package com.timer.jike.timemanager.utils;

import android.app.Application;

import com.alamkanak.weekview.WeekViewEvent;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.timer.jike.timemanager.bean.BaseProperty;
import com.timer.jike.timemanager.bean.ColorRule;
import com.timer.jike.timemanager.bean.ColorRuleDao;
import com.timer.jike.timemanager.bean.DaoMaster;
import com.timer.jike.timemanager.bean.DaoSession;
import com.timer.jike.timemanager.bean.Event;
import com.timer.jike.timemanager.bean.EventDao;
import com.timer.jike.timemanager.bean.PropertyImportance;
import com.timer.jike.timemanager.bean.PropertyPredictability;
import com.timer.jike.timemanager.bean.PropertyType;
import com.timer.jike.timemanager.bean.PropertyTypeDao;

import org.greenrobot.greendao.query.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jike on 2017/5/6.
 */

public class UtilDB {

    private static final String TAG = "UtilDB";
    // A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
//    public static final boolean ENCRYPTED = false;
    private static DaoSession daoSession;

    public static void init(Application app) {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(app, ENCRYPTED ? "events-db-encrypted" : "events-db");
//        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();


        MigrationHelper.DEBUG = true; //如果你想查看日志信息，请将DEBUG设置为true
        UtilSQLiteOpenHelper helper = new UtilSQLiteOpenHelper(app, "events-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
//        Database db = helper.getWritableDb();
        daoSession = daoMaster.newSession();
    }

    public static Query<Event> queryEventsBy(int year, int month) {
        Calendar start = Calendar.getInstance();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        start.set(Calendar.DATE, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);

        Calendar end = (Calendar) start.clone();
        end.set(Calendar.MONTH, month);

        Date startTime = start.getTime();
        Date endTime = end.getTime();

        return daoSession.getEventDao().queryBuilder()
                .where(EventDao.Properties.Begin_time.between(startTime, endTime))
                .where(EventDao.Properties.IsDeleted.eq(false))//新增isDeleted
                .orderDesc(EventDao.Properties.Begin_time).build();
    }

    public static List<Event> queryEventsBy(int year, int month, int date, long typeId) {
        UtilLog.d(TAG,"queryEventsBy",year, month, date);
        Calendar start = Calendar.getInstance();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month);//注意,Calendar对象默认一月为0
        start.set(Calendar.DATE, date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);


        Date startTime = start.getTime();
        Date endTime = new Date(startTime.getTime()+1000*24*60*60);

        return daoSession.getEventDao().queryBuilder()
                .where(EventDao.Properties.IsDeleted.eq(false))//新增isDeleted
                .where(EventDao.Properties.TypeId.eq(typeId))
                .where(EventDao.Properties.Begin_time.between(startTime, endTime))
                .orderDesc(EventDao.Properties.Begin_time).build().list();
    }

    public static Query<Event> queryEventsBy(long id) {
        return daoSession.getEventDao().queryBuilder()
                .where(EventDao.Properties.Id.eq(id))
                .orderDesc(EventDao.Properties.Begin_time).build();
    }

    public static ColorRuleDao getColorRuleDao() {
        return daoSession.getColorRuleDao();
    }

    public static Query<ColorRule> queryColorRules() {
        return daoSession.getColorRuleDao().queryBuilder().build();
    }

    public static void updateEvent(Event event) {
        Date date = new Date();
        event.setUpdatedAtClient(date);
        daoSession.getEventDao().update(event);
    }

    public static void setEventDeleted(Event event) {
        event.setIsDeleted(true);
        event.setUpdatedAtClient(new Date());
        daoSession.getEventDao().update(event);
    }

    public static void insertEvent(Event event) {
        Date date = new Date();
        event.setCreatedAtClient(date);
        event.setUpdatedAtClient(date);
        event.setUserServerUid(UtilBmob.getUserServerUid());
        event.setIsDeleted(false);
        daoSession.getEventDao().insert(event);
    }

    public static List<PropertyImportance> getImportanceList(){
        return daoSession.getPropertyImportanceDao().queryBuilder().build().list();
    }

    public static List<PropertyPredictability> getPredictList(){
        return daoSession.getPropertyPredictabilityDao().queryBuilder().build().list();
    }

    public static List<PropertyType> getTypeList(){
        return daoSession.getPropertyTypeDao().queryBuilder().build().list();
    }

    public static void updateProperty(BaseProperty property) {
        if (property instanceof PropertyType){
            UtilDB.updatePropertyType((PropertyType) property);
        } else if(property instanceof PropertyImportance){
            UtilDB.updatePropertyType((PropertyType) property);
        } else if(property instanceof PropertyPredictability){
            UtilDB.updatePropertyPredict((PropertyPredictability) property);
        } else {
            throw new RuntimeException("property 类型不合法！");
        }
    }

    public static void insertProperty(BaseProperty property) {
        if (property instanceof PropertyType){
            daoSession.getPropertyTypeDao().insert((PropertyType) property);
        } else if(property instanceof PropertyImportance){
            daoSession.getPropertyImportanceDao().insert((PropertyImportance) property);
        } else if(property instanceof PropertyPredictability){
            daoSession.getPropertyPredictabilityDao().insert((PropertyPredictability) property);
        } else {
            throw new RuntimeException("property 类型不合法！");
        }
    }

    private static void updatePropertyType(PropertyType propertyType){
        daoSession.getPropertyTypeDao().update(propertyType);
    }

    private static void updatePropertyPredict(PropertyPredictability propertyPredictability){
        daoSession.getPropertyPredictabilityDao().update(propertyPredictability);
    }

    public static void updatePropertyImportance(PropertyImportance propertyImportance){
        daoSession.getPropertyImportanceDao().update(propertyImportance);
    }
}
