package com.timer.jike.timemanager.activity;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.BaseProperty;
import com.timer.jike.timemanager.bean.Event;
import com.timer.jike.timemanager.bean.PropertyImportance;
import com.timer.jike.timemanager.bean.PropertyPredictability;
import com.timer.jike.timemanager.bean.PropertyType;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilDate;
import com.timer.jike.timemanager.utils.UtilDialog;
import com.timer.jike.timemanager.utils.UtilList;
import com.timer.jike.timemanager.utils.UtilLog;
import com.timer.jike.timemanager.utils.UtilString;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public abstract class BaseActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader
        .MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    String TAG;
    private List<PropertyImportance> mImportanceList;
    private List<PropertyPredictability> mPredictList;
    private List<PropertyType> mTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        TAG = getLocalClassName();

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        mImportanceList = UtilDB.getImportanceList();
        mPredictList = UtilDB.getPredictList();
        mTypeList = UtilDB.getTypeList();

        UtilLog.d(TAG,"onCreate",mImportanceList,mPredictList,mTypeList);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id) {
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                            .getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                            .getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                            .getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                            .getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                            .getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                            .getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
                            .getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources()
                            .getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources()
                            .getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in
                // -java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventDescription(Event event) {
        String predic = event.getPredictability() == null ? "" : event.getPredictability().getSignal();
        String importance = event.getImportance() == null ? "" : event.getImportance().getSignal();
        return String.format("%1$s%2$s%3$s/%4$s", predic,importance, event.getTitle(), UtilString.getHour(event.getDuration()));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Event event1 = UtilDB.queryEventsBy(event.getId()).list().get(0);

        View inflate = View.inflate(this, R.layout.dialog_event, null);
        EditText title = (EditText) inflate.findViewById(R.id.tv_de_title);
        EditText detail = (EditText) inflate.findViewById(R.id.tv_de_detail);
        EditText duration = (EditText) inflate.findViewById(R.id.tv_de_duration);
        EditText start = (EditText) inflate.findViewById(R.id.tv_de_start);
        EditText end = (EditText) inflate.findViewById(R.id.tv_de_end);

        Spinner importanceSpinner = (Spinner) inflate.findViewById(R.id.sp_de_importance);
        Spinner predictSpinner = (Spinner) inflate.findViewById(R.id.sp_de_predict);
        Spinner typeSpinner = (Spinner) inflate.findViewById(R.id.sp_de_type);

        MyAdapter<PropertyImportance> importanceMyAdapter = new MyAdapter<>(this, mImportanceList);
        MyAdapter<PropertyPredictability> predictMyAdapter = new MyAdapter<>(this, mPredictList);
        MyAdapter<PropertyType> typeMyAdapter = new MyAdapter<>(this, mTypeList);

        importanceSpinner.setAdapter(importanceMyAdapter);
        predictSpinner.setAdapter(predictMyAdapter);
        typeSpinner.setAdapter(typeMyAdapter);

        int positionImportance = UtilList.getPosition(mImportanceList, event1.getImportance());
        if (positionImportance != -1)
            importanceSpinner.setSelection(positionImportance);
        int positionPredict = UtilList.getPosition(mPredictList, event1.getPredictability());
        if (positionPredict != -1)
            predictSpinner.setSelection(positionPredict);
        int positionType = UtilList.getPosition(mTypeList, event1.getType());
        if (positionType != -1)
            typeSpinner.setSelection(positionType);

        importanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event1.setImportance(mImportanceList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        predictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event1.setPredictability(mPredictList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event1.setType(mTypeList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        MaterialDialog.SingleButtonCallback callbackN = (dialog, which) -> {
            if ("修改".equals(dialog.getActionButton(which).getText())){
                dialog.setTitle("修改记录");
                title.setEnabled(true);
                detail.setEnabled(true);
                start.setEnabled(true);
                end.setEnabled(true);
                dialog.getActionButton(DialogAction.POSITIVE).setText("保存");
                dialog.getActionButton(DialogAction.NEGATIVE).setText("取消");
            } else {
                dialog.dismiss();
            }

        };
        title.setText(event1.getTitle());
        detail.setText(event1.getDetail());
        duration.setText(UtilString.getHour(event1.getDuration()));
        start.setText(UtilDate.date2String(event1.getBegin_time()));
        end.setText(UtilDate.date2String(event1.getEnd_time()));
        MaterialDialog.SingleButtonCallback callbackP = (dialog, which) -> {
            if ("知道了".equals(dialog.getActionButton(which).getText())){
                dialog.dismiss();
            } else {
                Date startTime = UtilDate.string2Date(start.getText().toString());
                Date endTime = UtilDate.string2Date(end.getText().toString());
                long duration1 = endTime.getTime() - startTime.getTime();

                if(validateInfos(startTime,endTime,duration1)){

                    event1.setBegin_time(startTime);
                    event1.setEnd_time(endTime);
                    event1.setTitle(title.getText().toString());
                    event1.setDetail(detail.getText().toString());
                    event1.setDuration(duration1);
                    UtilDB.updateEvent(event1);

                    Toast.makeText(BaseActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    mWeekView.notifyDatasetChanged();
                    dialog.dismiss();
                }
            }
        };
        UtilDialog.buildDialog(this, "查看记录", "知道了", "修改", callbackP, callbackN, inflate, false);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        MaterialDialog.SingleButtonCallback callbackP = (dialog, which) -> {
            Event event1 = UtilDB.queryEventsBy(event.getId()).list().get(0);
            UtilDB.setEventDeleted(event1);
            Toast.makeText(BaseActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            mWeekView.notifyDatasetChanged();
        };
        MaterialDialog dialog = UtilDialog.buildDialog(this, "确认删除","确认","取消",callbackP,null );
        dialog.setContent("删除了可没后悔药，是否真的要删除？");
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        MaterialDialog.SingleButtonCallback callbackP = (dialog, which) -> {
            View inflate = View.inflate(this, R.layout.dialog_event, null);
            EditText title = (EditText) inflate.findViewById(R.id.tv_de_title);
            EditText detail = (EditText) inflate.findViewById(R.id.tv_de_detail);
            EditText duration = (EditText) inflate.findViewById(R.id.tv_de_duration);
            EditText start = (EditText) inflate.findViewById(R.id.tv_de_start);
            EditText end = (EditText) inflate.findViewById(R.id.tv_de_end);
            Button calDuration = (Button) inflate.findViewById(R.id.btn_de_cal_duration);
            calDuration.setVisibility(View.VISIBLE);
            calDuration.setOnClickListener(v -> {
                Date startTime = UtilDate.string2Date(start.getText().toString());
                Date endTime = UtilDate.string2Date(end.getText().toString());
                long duration1 = endTime.getTime() - startTime.getTime();
                if(validateInfos(startTime, endTime, duration1)){
                    duration.setText(UtilString.getHour(duration1));
                }
            });


            Spinner importanceSpinner = (Spinner) inflate.findViewById(R.id.sp_de_importance);
            Spinner predictSpinner = (Spinner) inflate.findViewById(R.id.sp_de_predict);
            Spinner typeSpinner = (Spinner) inflate.findViewById(R.id.sp_de_type);

            MyAdapter<PropertyImportance> importanceMyAdapter = new MyAdapter<>(this, mImportanceList);
            MyAdapter<PropertyPredictability> predictMyAdapter = new MyAdapter<>(this, mPredictList);
            MyAdapter<PropertyType> typeMyAdapter = new MyAdapter<>(this, mTypeList);

            importanceSpinner.setAdapter(importanceMyAdapter);
            predictSpinner.setAdapter(predictMyAdapter);
            typeSpinner.setAdapter(typeMyAdapter);

            Event event2 = new Event();

            importanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    event2.setImportance(mImportanceList.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            predictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    event2.setPredictability(mPredictList.get(position));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    event2.setType(mTypeList.get(position));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            title.setEnabled(true);
            detail.setEnabled(true);
            start.setEnabled(true);
            end.setEnabled(true);

            start.setText(UtilDate.calendar2String(time));
            end.setText(UtilDate.calendar2String(time));

            MaterialDialog.SingleButtonCallback callbackP1 = (dialog2, which2) -> {

                Date startTime = UtilDate.string2Date(start.getText().toString());
                Date endTime = UtilDate.string2Date(end.getText().toString());
                long duration1 = endTime.getTime() - startTime.getTime();

                if(validateInfos(startTime,endTime,duration1)){
                    event2.setTitle(title.getText().toString());
                    event2.setDetail(detail.getText().toString());
                    event2.setBegin_time(startTime);
                    event2.setEnd_time(endTime);
                    event2.setDuration(duration1);
                    UtilDB.insertEvent(event2);

                    Toast.makeText(BaseActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    //不知道为啥没有刷新,强制刷新一下
                    Calendar instance = Calendar.getInstance();
                    onMonthChange(instance.get(Calendar.YEAR),instance.get(Calendar.MONTH));
                    mWeekView.notifyDatasetChanged();
                    dialog2.dismiss();
                }
            };
            UtilDialog.buildDialog(this, "查看记录", "保存", "取消", callbackP1, null, inflate);
        };
        MaterialDialog dialogCreate = UtilDialog.buildDialog(this, "创建记录","确认","取消",callbackP,null );
        dialogCreate.setContent("要创建一个新记录？注意开始时间和结束时间要按规则填写！");
    }

    private boolean validateInfos(Date startTime, Date endTime, long duration1) {
        boolean isValid = false;
        if(startTime == null){
            Toast.makeText(BaseActivity.this, "开始时间格式错误", Toast.LENGTH_SHORT).show();
        } else if (endTime == null){
            Toast.makeText(BaseActivity.this, "结束时间格式错误", Toast.LENGTH_SHORT).show();
        } else if(duration1 < 0){
            Toast.makeText(this, "开始时间不得晚于结束时间", Toast.LENGTH_SHORT).show();
        } else
            isValid = true;
        return isValid;
    }

    public WeekView getWeekView() {
        return mWeekView;
    }


    class MyAdapter<T extends BaseProperty> extends BaseAdapter {
        private List<T> mList;
        private Context mContext;

        public MyAdapter(Context pContext, List<T> pList) {
            this.mContext = pContext;
            this.mList = pList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public BaseProperty getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
            TextView textView;
            if(convertView!=null && convertView instanceof TextView) {
                textView = (TextView) convertView.findViewById(R.id.tv_si_text);
            } else {
                convertView=_LayoutInflater.inflate(R.layout.spinner_item, null);
                textView = (TextView) convertView.findViewById(R.id.tv_si_text);
            }
            textView.setText(mList.get(position).getText());
            return convertView;
        }
    }
}
