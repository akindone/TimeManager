package com.timer.jike.timemanager.utils;

import android.app.Application;
import android.util.Log;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.timer.jike.timemanager.activity.MyStaticActivity;
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

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

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
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        MigrationHelper.DEBUG = true; //如果你想查看日志信息，请将DEBUG设置为true
        UtilSQLiteOpenHelper helper = new UtilSQLiteOpenHelper(app, "events-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
//        Database db = helper.getWritableDb();
        daoSession = daoMaster.newSession();
    }

    public static Query<Event> queryEventsByMonth(int year, int month) {
        Calendar start = Calendar.getInstance();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month-1);
        start.set(Calendar.DATE, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);

        Calendar end = (Calendar) start.clone();
        end.set(Calendar.MONTH, month);

        Date startTime = start.getTime();
        Date endTime = end.getTime();

        return queryEventsBetween(startTime, endTime);
    }

    private static Query<Event> queryEventsBetween(Date startTime, Date endTime) {
        return daoSession.getEventDao().queryBuilder()
                .where(EventDao.Properties.IsDeleted.eq(false))//新增isDeleted
                .where(EventDao.Properties.Begin_time.between(startTime, endTime))
                .orderDesc(EventDao.Properties.Begin_time).build();
    }

    public static List<Event> queryEventsBy(String typeDuration, long propertyId, int propertyType){
        Calendar start = Calendar.getInstance();
        start.setTime(new Date());
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        Calendar end = (Calendar) start.clone();

        int dayOfMonth = start.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);

        Date startTime;
        Date endTime;
        switch (typeDuration){
            case MyStaticActivity.TYPE_LAST_MONTH:
                Calendar temp = (Calendar) start.clone();
                temp.add(Calendar.DATE,-dayOfMonth);//调到上个月
                temp.getTime();
                int lastMonthDays = temp.getActualMaximum(Calendar.DATE);
                end.add(Calendar.DATE, 1-dayOfMonth);
                endTime = end.getTime();
                start.add(Calendar.DATE,1-dayOfMonth-lastMonthDays);
                startTime = start.getTime();
                break;
            case MyStaticActivity.TYPE_ALL_HISTORY:
            case MyStaticActivity.TYPE_LAST_WEEK:
            default:
                end.add(Calendar.DATE, 1-dayOfWeek);
                endTime = end.getTime();
                start.add(Calendar.DATE,-6-dayOfWeek);
                startTime = start.getTime();
        }
        return getEventsBetween(propertyId, propertyType, startTime, endTime);
    }

    public static final int PROPERTY_TYPE_PREDICTABILITY = 0;
    public static final int PROPERTY_TYPE_IMPORTANCE = 1;
    public static final int PROPERTY_TYPE_TYPE = 2;

    public static List<Event> queryEventsByDay(int year, int month, int date, long propertyId, int propertyType) {
        UtilLog.d(TAG,"queryEventsByDay",year, month, date);
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

        return getEventsBetween(propertyId, propertyType, startTime, endTime);
    }

    private static List<Event> getEventsBetween(long propertyId, int propertyType, Date startTime, Date endTime) {
        Property property;
        switch (propertyType){
            case PROPERTY_TYPE_PREDICTABILITY:
                property = EventDao.Properties.PredictabilityId;
                break;
            case PROPERTY_TYPE_IMPORTANCE:
                property = EventDao.Properties.ImportanceId;
                break;
            case PROPERTY_TYPE_TYPE:
                property = EventDao.Properties.TypeId;
                break;
            default:
                throw new RuntimeException("propertyType 类型不合法");

        }

        UtilLog.d(TAG,"getEventsBetween",propertyId,propertyType,startTime, endTime);
        return daoSession.getEventDao().queryBuilder()
                .where(EventDao.Properties.IsDeleted.eq(false))//新增isDeleted
                .where(property.eq(propertyId))
                .where(EventDao.Properties.Begin_time.between(startTime, endTime))
                .orderDesc(EventDao.Properties.Begin_time).build().list();
    }

    public static Query<Event> queryEventsByMonth(long id) {
        return daoSession.getEventDao().queryBuilder()
                .where(EventDao.Properties.Id.eq(id))
                .orderDesc(EventDao.Properties.Begin_time).build();
    }

    public static Date getEventFirstDay(){
        List<Event> events = daoSession.getEventDao().queryBuilder()
                .where(EventDao.Properties.IsDeleted.eq(false))//新增isDeleted
                .orderAsc(EventDao.Properties.Begin_time)
                .limit(1)
                .build()
                .list();
        UtilLog.d(TAG,"getEventFirstDay",events.toString());
        if (events.size() == 1)
            return events.get(0).getBegin_time();
        else
            return new Date();
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

    public static List<? extends BaseProperty> getPropertyList(int type){
        switch (type){
            case UtilDB.PROPERTY_TYPE_PREDICTABILITY:
                return getPredictList();
            case UtilDB.PROPERTY_TYPE_IMPORTANCE:
                return getImportanceList();
            case UtilDB.PROPERTY_TYPE_TYPE:
                return getTypeList();
            default:
                throw new RuntimeException("propertyType字段不合法！");
        }
    }

    public static void updateProperty(BaseProperty property) {
        if (property instanceof PropertyType){
            UtilDB.updatePropertyType((PropertyType) property);
        } else if(property instanceof PropertyImportance){
            UtilDB.updatePropertyImportance((PropertyImportance) property);
        } else if(property instanceof PropertyPredictability){
            UtilDB.updatePropertyPredict((PropertyPredictability) property);
        } else {
            throw new RuntimeException("property 类型不合法！");
        }
    }

    public static long insertProperty(BaseProperty property) {
        if (property instanceof PropertyType){
            return daoSession.getPropertyTypeDao().insert((PropertyType) property);
        } else if(property instanceof PropertyImportance){
            return daoSession.getPropertyImportanceDao().insert((PropertyImportance) property);
        } else if(property instanceof PropertyPredictability){
            return daoSession.getPropertyPredictabilityDao().insert((PropertyPredictability) property);
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
