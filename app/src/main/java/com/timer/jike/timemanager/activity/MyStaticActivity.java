package com.timer.jike.timemanager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.BaseProperty;
import com.timer.jike.timemanager.bean.Event;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilLog;
import com.timer.jike.timemanager.utils.UtilString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.timer.jike.timemanager.utils.UtilDB.PROPERTY_TYPE_IMPORTANCE;
import static com.timer.jike.timemanager.utils.UtilDB.PROPERTY_TYPE_PREDICTABILITY;
import static com.timer.jike.timemanager.utils.UtilDB.PROPERTY_TYPE_TYPE;

public class MyStaticActivity extends AppCompatActivity {
    public static final String TYPE_LAST_WEEK = "TYPE_LAST_WEEK";
    public static final String TYPE_LAST_MONTH = "TYPE_LAST_MONTH";
    public static final String TYPE_ALL_HISTORY = "TYPE_ALL_HISTORY";
    private static final String TAG = "MyStaticActivity";

    private static final String[] labels = {"预测性", "重要性", "类型"};


    private int[] mColors = new int[]{
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2],
            ColorTemplate.VORDIPLOM_COLORS[3],
            ColorTemplate.VORDIPLOM_COLORS[4]
    };


    private LineChart mLcPredict;
    private LineChart mLcImportant;
    private LineChart mLcType;
    private PieChart mPcPredict;
    private PieChart mPcImportant;
    private PieChart mPcType;

    private StringBuilder[] centerText = new StringBuilder[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_static);

        mLcPredict = (LineChart) findViewById(R.id.lc_msa_line_predict);
        mLcImportant = (LineChart) findViewById(R.id.lc_msa_line_important);
        mLcType = (LineChart) findViewById(R.id.lc_msa_line_type);

        initLineChart(mLcPredict, PROPERTY_TYPE_PREDICTABILITY);
        initLineChart(mLcImportant, PROPERTY_TYPE_IMPORTANCE);
        initLineChart(mLcType, PROPERTY_TYPE_TYPE);


        initCenterTextSB();
        mPcPredict = (PieChart) findViewById(R.id.pc_msa_predict);
        mPcImportant = (PieChart) findViewById(R.id.pc_msa_important);
        mPcType = (PieChart) findViewById(R.id.pc_msa_type);

        initPieChart(mPcPredict, PROPERTY_TYPE_PREDICTABILITY);
        initPieChart(mPcImportant, PROPERTY_TYPE_IMPORTANCE);
        initPieChart(mPcType, PROPERTY_TYPE_TYPE);


    }

    private void initPieChart(PieChart pieChart, int propertyType) {
//        pieChart.setBackgroundColor(Color.WHITE);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

//        mPcPredict.setCenterTextTypeface(mTfLight);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setMaxAngle(360f); // 180  HALF CHART
        pieChart.setRotationAngle(0f);
//        pieChart.setCenterTextOffset(0, -20);


        // add data
        setPieChartDatas(TYPE_LAST_WEEK, pieChart, propertyType);


        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(12f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
//        mPcPredict.setEntryLabelTypeface(mTfRegular);
        pieChart.setEntryLabelTextSize(12f);
    }

    private void setPieChartDatas(String typeDuration, PieChart pieChart, int propertyType) {
        PieData data = getPieChartData(propertyType, typeDuration);
        pieChart.setData(data);
        pieChart.setCenterText(centerText[propertyType].toString());
        pieChart.invalidate();
    }

    private PieData getPieChartData(int propertyType, String typeDuration) {
        List<? extends BaseProperty> list = UtilDB.getPropertyList(propertyType);
        int types = list.size();//某种属性有多少类

        if (centerText[propertyType] != null)
            centerText[propertyType].delete(0, centerText[propertyType].length());

        ArrayList<PieEntry> values = new ArrayList<>();

        for (int i = 0; i < types; i++) {
            BaseProperty property = list.get(i);
            List<Event> events = UtilDB.queryEventsBy(typeDuration, property.getId(), propertyType);
            float hours = sumDuration(events);
            values.add(new PieEntry(hours, property.getText()));
            if (centerText[propertyType] != null) {
                centerText[propertyType].append(property.getText()).append(":")
                        .append(UtilString.decimalFormat(hours)).append("h").append(i == types - 1 ? "" : "\n");
            }

        }

        PieDataSet dataSet = new PieDataSet(values, labels[propertyType]);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }

    private void initCenterTextSB() {
        centerText[0] = new StringBuilder();
        centerText[1] = new StringBuilder();
        centerText[2] = new StringBuilder();
    }

    private void initLineChart(LineChart lineChart, int propertyType) {
        lineChart.setDrawGridBackground(false);

        // no description text
        lineChart.getDescription().setEnabled(false);
        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);
        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(24f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        lineChart.getAxisRight().setEnabled(false);

        // add data
        setLineChartDatas(TYPE_LAST_WEEK, lineChart, propertyType);

        lineChart.animateX(2500);
        //lcType.invalidate();

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setXAxis(LineChart lineChart, String typeDruation) {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new MyAxisValueFormatter(typeDruation));
    }

    public void showLastWeek(View view) {
        setAllLineChartDatas(TYPE_LAST_WEEK);
//        toggleValues(mLcType,true);

    }

    public void showLastMonth(View view) {
        setAllLineChartDatas(TYPE_LAST_MONTH);
//        toggleValues(mLcType,false);
    }

    public void showAllHistory(View view) {
        setAllLineChartDatas(TYPE_LAST_WEEK);
        mLcType.invalidate();
    }

    public void setAllLineChartDatas(String type) {
        setLineChartDatas(type, mLcPredict, PROPERTY_TYPE_PREDICTABILITY);
        setLineChartDatas(type, mLcImportant, PROPERTY_TYPE_IMPORTANCE);
        setLineChartDatas(type, mLcType, PROPERTY_TYPE_TYPE);

        setPieChartDatas(type, mPcPredict, PROPERTY_TYPE_PREDICTABILITY);
        setPieChartDatas(type, mPcImportant, PROPERTY_TYPE_IMPORTANCE);
        setPieChartDatas(type, mPcType, PROPERTY_TYPE_TYPE);

    }

    private void setLineChartDatas(String type, LineChart lineChart, int propertyType) {
        setXAxis(lineChart, type);
        LineData data = getLineChartData(propertyType, type);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    private LineData getLineChartData(int propertyType, String durationType) {

        List<? extends BaseProperty> list = UtilDB.getPropertyList(propertyType);
        int types = list.size();//某种属性有多少类
        int count = 7;
        switch (durationType) {
            case TYPE_LAST_WEEK:
                count = 7;
                break;
            case TYPE_LAST_MONTH:
                Calendar instance = Calendar.getInstance();
                instance.setTime(new Date());
                int month = instance.get(Calendar.MONTH);
                instance.set(Calendar.MONTH, month - 1);
                instance.setTime(instance.getTime());
                int actualMaximum = instance.getActualMaximum(Calendar.DAY_OF_MONTH);// 此月份的天数
                Log.d(TAG, "actualMaximum " + actualMaximum);
                count = actualMaximum;
                break;
            case TYPE_ALL_HISTORY:
                //// TODO: 2017/5/17
                break;
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        for (int z = 0; z < types; z++) {
            ArrayList<Entry> values = getEntry(durationType, count, list.get(z).getId(), propertyType);

            LineDataSet d = new LineDataSet(values, list.get(z).getText());
            d.setLineWidth(2.5f);
            d.setCircleRadius(2f);

            int color = mColors[z % mColors.length];
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }

        return new LineData(dataSets);
    }


    private ArrayList<Entry> getEntry(String type, int count, long typeId, int propertyType) {
        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            //get calendar
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

            UtilLog.d(TAG, "thisWeek", cal.toString());
            int addAmount;
            switch (type) {
                case TYPE_LAST_WEEK:
                    addAmount = i - dayOfWeek - count + 1;
                    break;
                case TYPE_LAST_MONTH:
                    addAmount = i - dayOfMonth - count + 1;
                    break;
                case TYPE_ALL_HISTORY:
                default:
                    addAmount = i - dayOfWeek - count + 1;//// TODO: 2017/5/17
                    break;
            }
            cal.add(Calendar.DATE, addAmount);
            UtilLog.d(TAG, "LastWeek", cal.toString());


            List<Event> events = UtilDB.queryEventsByDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE), typeId, propertyType);

            float hourFloat = sumDuration(events);
            values.add(new Entry(i, hourFloat));
        }

        return values;
    }

    private float sumDuration(List<Event> events) {
        long val = 0;
        for (int j = 0; j < events.size(); j++) {
            val += events.get(j).getDuration();
        }
        return UtilString.getHourFloat(val);
    }


    private void toggleValues(LineChart chart, boolean isDrawValuesEnable) {
        List<ILineDataSet> sets = chart.getData().getDataSets();

        for (ILineDataSet iSet : sets) {

            LineDataSet set = (LineDataSet) iSet;
            set.setDrawValues(isDrawValuesEnable);
        }
        // redraw
        chart.invalidate();
    }

    class MyAxisValueFormatter implements IAxisValueFormatter {
        String typeDuration;

        String[] weekDays = {"星期天", "星期一", "星期二️", "星期三", "星期四", "星期五", "星期六"};

        public MyAxisValueFormatter(String typeDuration) {
            this.typeDuration = typeDuration;
        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int day = (int) value;
            switch (typeDuration) {
                case TYPE_LAST_WEEK:
                    return weekDays[day];
                case TYPE_LAST_MONTH:
                    return day + 1 + "日";
                case TYPE_ALL_HISTORY:
                    break;
            }
            return null;
        }
    }

}
