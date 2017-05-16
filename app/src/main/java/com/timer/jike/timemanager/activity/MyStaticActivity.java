package com.timer.jike.timemanager.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.Event;
import com.timer.jike.timemanager.bean.PropertyType;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilDate;
import com.timer.jike.timemanager.utils.UtilLog;
import com.timer.jike.timemanager.utils.UtilString;
import com.timer.jike.timemanager.view.MyMarkerView;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyStaticActivity extends AppCompatActivity {
    private static final String TYPE_LAST_WEEK = "TYPE_LAST_WEEK";
    private static final String TYPE_LAST_MONTH = "TYPE_LAST_MONTH";
    private static final String TYPE_ALL_HISTORY = "TYPE_ALL_HISTORY";
    private static final String TAG = "MyStaticActivity";



    private int[] mColors = new int[] {
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2]
    };


    private LineChart mLcType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_static);

        mLcType = (LineChart) findViewById(R.id.lc_msa_line_type);


//        mLcType.setOnChartGestureListener(this);//TODO
//        mLcType.setOnChartValueSelectedListener(this);//TODO
        mLcType.setDrawGridBackground(false);

        // no description text
        mLcType.getDescription().setEnabled(false);

        // enable touch gestures
        mLcType.setTouchEnabled(true);

        // enable scaling and dragging
        mLcType.setDragEnabled(true);
        mLcType.setScaleEnabled(true);
        // lcType.setScaleXEnabled(true);
        // lcType.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLcType.setPinchZoom(true);

        // set an alternative background color
        // lcType.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mLcType); // For bounds control
        mLcType.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mLcType.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        /*LimitLine ll1 = new LimitLine(150f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTypeface(tf);*/

        /*LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTypeface(tf);*/

        YAxis leftAxis = mLcType.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(24f);
        leftAxis.setAxisMinimum(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mLcType.getAxisRight().setEnabled(false);

        //lcType.getViewPortHandler().setMaximumScaleY(2f);
        //lcType.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setDatas(TYPE_LAST_WEEK);

//        lcType.setVisibleXRange(20);
//        lcType.setVisibleYRange(20f, AxisDependency.LEFT);
//        lcType.centerViewTo(20, 50, AxisDependency.LEFT);

        mLcType.animateX(2500);
        //lcType.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mLcType.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
    }

    public void showLastWeek(View view) {
        setDatas(TYPE_LAST_WEEK);
        // redraw
        mLcType.invalidate();
    }

    public void showLastMonth(View view) {
        setDatas(TYPE_LAST_WEEK);
        // redraw
        mLcType.invalidate();
    }

    public void showAllHistory(View view) {
        setDatas(TYPE_LAST_WEEK);
        // redraw
        mLcType.invalidate();
    }

    private void setDatas(String type){
        List<PropertyType> list = UtilDB.getTypeList();
        int types = list.size();//某种属性有多少类
        int count = 7;
        switch (type){
            case TYPE_LAST_WEEK:
                count = 7;
                break;
            case TYPE_LAST_MONTH:
                break;
            case TYPE_ALL_HISTORY:
                break;
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        for (int z = 0; z < types; z++) {


            ArrayList<Entry> values = getEntry(count, list.get(z).getId());

            LineDataSet d = new LineDataSet(values, list.get(z).getText());
            d.setLineWidth(2.5f);
            d.setCircleRadius(4f);

            int color = mColors[z % mColors.length];
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }

        // make the first DataSet dashed
//        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
//        ((LineDataSet) dataSets.get(0)).setColors(ColorTemplate.VORDIPLOM_COLORS);
//        ((LineDataSet) dataSets.get(0)).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data = new LineData(dataSets);
        mLcType.setData(data);
        mLcType.invalidate();
    }

    private void setData(String typeLastWeek) {
        // 获取数据
        ArrayList<Entry> values = getData(typeLastWeek);

        LineDataSet set1;

        if (mLcType.getData() != null &&
                mLcType.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mLcType.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mLcType.getData().notifyDataChanged();
            mLcType.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mLcType.setData(data);
        }

    }

    private ArrayList<Entry> getData(String typeLastWeek) {
        int count;
        ArrayList<Entry> values = null;
        switch (typeLastWeek){
            case TYPE_LAST_WEEK:
                count = 7;
                getEntry(count,UtilDB.getTypeList().get(0).getId());
                break;
            case TYPE_LAST_MONTH:
                break;
            case TYPE_ALL_HISTORY:
                break;
        }

        return values;
    }

    private ArrayList<Entry> getEntry(int count, long typeId) {
        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            //get calendar
            Calendar cal= Calendar.getInstance();
            cal.setTime(new Date());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            UtilLog.d(TAG,"thisWeek",cal.toString());
            cal.add(Calendar.DATE,i-6-dayOfWeek);
            UtilLog.d(TAG,"LastWeek",cal.toString());


            List<Event> events = UtilDB.queryEventsBy(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE), typeId);

            long val = 0;
            for (int j = 0; j < events.size(); j++) {
                val += events.get(j).getDuration();
            }
            float hourFloat = UtilString.getHourFloat(val);
            values.add(new Entry(i, hourFloat));
        }
        
        return values;
    }
}
