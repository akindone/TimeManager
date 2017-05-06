package com.timer.jike.timemanager.activity;

import com.alamkanak.weekview.WeekViewEvent;
import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.ColorRule;
import com.timer.jike.timemanager.bean.Event;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilDate;
import com.timer.jike.timemanager.utils.UtilLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class HistoryActivity extends BaseActivity {

    int[] colors = {
            R.color.event_color_01,
            R.color.event_color_02,
            R.color.event_color_03,
            R.color.event_color_04
    };
    private List<ColorRule> mColorRules;


    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        mColorRules = UtilDB.queryColorRules().list();

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        UtilLog.d(TAG,"onMonthChange",newYear,newMonth);

        List<Event> list = UtilDB.queryEventsBy(newYear,newMonth).list();
        for (int i = 0; i < list.size(); i++) {
            Event event = list.get(i);
            UtilLog.d(TAG,i,event);
            WeekViewEvent weekViewEvent = new WeekViewEvent(event.getId(), getEventDescription(event),
                    UtilDate.date2calendar(event.getBegin_time()), UtilDate.date2calendar(event.getEnd_time()));
            weekViewEvent.setColor(getColorByTitle(event.getTitle()));
            events.add(weekViewEvent);
        }


        return events;
    }

    private int getColorByTitle(String title) {
        for (int i = 0; i < mColorRules.size(); i++) {
            if(title.contains(mColorRules.get(i).getText())){
                return mColorRules.get(i).getColor();
            }
        }
        double v = Math.random() * colors.length;
        return colors[(int) Math.floor(v)];
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar instance = Calendar.getInstance();
        int hour = instance.get(Calendar.HOUR_OF_DAY)-2;
        getWeekView().goToHour(hour>0 ? hour:0);
    }
}
