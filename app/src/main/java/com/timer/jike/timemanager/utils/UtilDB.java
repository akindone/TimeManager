package com.timer.jike.timemanager.utils;

import android.app.Application;

import com.timer.jike.timemanager.bean.ColorRule;
import com.timer.jike.timemanager.bean.ColorRuleDao;
import com.timer.jike.timemanager.bean.DaoMaster;
import com.timer.jike.timemanager.bean.DaoSession;
import com.timer.jike.timemanager.bean.Event;
import com.timer.jike.timemanager.bean.EventDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jike on 2017/5/6.
 */

public class UtilDB {

// A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
//    public static final boolean ENCRYPTED = false;
    private static DaoSession daoSession;

    public static void init(Application app) {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(app, ENCRYPTED ? "events-db-encrypted" : "events-db");
//        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(app, "events-db" );
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static EventDao getEventDao() {
        return daoSession.getEventDao();
    }

    public static Query<Event> queryEvents() {
        return daoSession.getEventDao().queryBuilder().orderAsc(EventDao.Properties.Begin_time).build();
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
                .orderDesc(EventDao.Properties.Begin_time).build();
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

}
