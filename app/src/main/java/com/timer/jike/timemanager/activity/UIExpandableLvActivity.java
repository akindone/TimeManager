package com.timer.jike.timemanager.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.BaseProperty;
import com.timer.jike.timemanager.bean.ColorRule;
import com.timer.jike.timemanager.bean.PropertyImportance;
import com.timer.jike.timemanager.bean.PropertyPredictability;
import com.timer.jike.timemanager.bean.PropertyType;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilDialog;
import com.timer.jike.timemanager.utils.UtilLog;
import com.timer.jike.timemanager.utils.UtilSP;


import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UIExpandableLvActivity extends AppCompatActivity {

    private static final String TAG = "UIExpandableLvActivity";
    @BindView(R.id.elv_aul_expand_list) ExpandableListView mElvAulExpandList;

    public String[] groupStrings = {"预测性", "重要性", "类型"};
    public List<? extends BaseProperty>[] childStrings = new List[3];
    public long[] defaultEventPropertyId = new long[3];

    private MyExpandableListAdapter mExpandableListAdapter;
    private SharedPreferences mSpSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiexpandable_lv);
        ButterKnife.bind(this);

        mSpSetting = UtilSP.getSPSetting(this);

        loadData();
        UtilLog.d(TAG,"childStrings", Arrays.toString(childStrings));

        mExpandableListAdapter = new MyExpandableListAdapter(groupStrings, childStrings, this, defaultEventPropertyId);
        mElvAulExpandList.setAdapter(mExpandableListAdapter);

        setListener(mExpandableListAdapter);
    }

    private void loadData() {
        defaultEventPropertyId[0] = mSpSetting.getLong(UtilSP.EVENT_PROPERTY_PREDICT_DEFAULT, -1);
        defaultEventPropertyId[1] = mSpSetting.getLong(UtilSP.EVENT_PROPERTY_IMPORTANT_DEFAULT, -1);
        defaultEventPropertyId[2] = mSpSetting.getLong(UtilSP.EVENT_PROPERTY_TYPE_DEFAULT, -1);

        List<PropertyImportance> importanceList = UtilDB.getImportanceList();
        List<PropertyPredictability> predictList = UtilDB.getPredictList();
        List<PropertyType> typeList = UtilDB.getTypeList();
        childStrings[0]= predictList;
        childStrings[1]= importanceList;
        childStrings[2]= typeList;
    }

    private void setListener(final MyExpandableListAdapter adapter) {
        //        设置分组项的点击监听事件
        mElvAulExpandList.setOnGroupClickListener((parent, view, groupPosition, l) -> {
//            Toast.makeText(this, "setOnGroupClickListener", Toast.LENGTH_SHORT).show();
            return false;
        });

        //        设置子选项点击监听事件
        mElvAulExpandList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            BaseProperty property = childStrings[groupPosition].get(childPosition);
//            Toast.makeText(getApplicationContext(), property.getText(), Toast.LENGTH_SHORT).show();


            View inflate = View.inflate(this, R.layout.dialog_event_property, null);

            EditText text = (EditText) inflate.findViewById(R.id.et_dcs_text);
            EditText signal = (EditText) inflate.findViewById(R.id.et_dcs_signal);
            ColorPicker picker = (ColorPicker) inflate.findViewById(R.id.picker);
            SVBar svBar = (SVBar) inflate.findViewById(R.id.svbar);
            View signalLL = inflate.findViewById(R.id.ll_dep_signal);
            Switch setDefault = (Switch) inflate.findViewById(R.id.switch_dep_set_default);

            text.setText(property.getText());
            setDefault.setChecked(property.getId().equals(defaultEventPropertyId[groupPosition]));

            String spTag;
            switch (groupPosition) {
                case 0://predict
                    picker.setVisibility(View.GONE);
                    svBar.setVisibility(View.GONE);
                    signalLL.setVisibility(View.VISIBLE);
                    signal.setText(((PropertyPredictability)property).getSignal());
                    spTag = UtilSP.EVENT_PROPERTY_PREDICT_DEFAULT;
                    break;
                case 1://importance
                    picker.setVisibility(View.GONE);
                    svBar.setVisibility(View.GONE);
                    signalLL.setVisibility(View.VISIBLE);
                    signal.setText(((PropertyImportance)property).getSignal());
                    spTag = UtilSP.EVENT_PROPERTY_IMPORTANT_DEFAULT;
                    break;
                case 2://type
                    signalLL.setVisibility(View.GONE);
                    picker.addSVBar(svBar);
                    picker.setColor(((PropertyType) property).getColor());
                    //To set the old selected color u can do it like this
                    picker.setOldCenterColor(picker.getColor());
                    picker.setOnColorChangedListener(((PropertyType) property)::setColor);
                    picker.setShowOldCenterColor(false);
                    spTag = UtilSP.EVENT_PROPERTY_TYPE_DEFAULT;
                    break;
                default:
                    throw new RuntimeException("property 类型不合法！");
            }

            MaterialDialog.SingleButtonCallback callbackP = (dialog, which) -> {
                if (TextUtils.isEmpty(text.getText().toString())){
                    Toast.makeText(this, "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                property.setText(text.getText().toString());
                if (property instanceof PropertyPredictability){
                    ((PropertyPredictability) property).setSignal(signal.getText().toString());
                } else if(property instanceof PropertyImportance){
                    ((PropertyImportance) property).setSignal(signal.getText().toString());
                }
                UtilDB.updateProperty(property);
                if (setDefault.isChecked()){
                    mSpSetting.edit().putLong(spTag,property.getId()).apply();
                }
                notifyDataSetChanged(groupPosition);

            };

            UtilDialog.buildDialog(this, "修改", "保存修改", "知道了", callbackP,null , inflate);

            return true;
        });
    }

    public void notifyDataSetChanged(int groupPosition){
        loadData();

        mExpandableListAdapter.notifyDataSetChanged();//只有GroupView 更新??
        mElvAulExpandList.collapseGroup(groupPosition);//Group 的伸缩会引起getChildView(int, int, boolean, View, ViewGroup)  的运行!
        mElvAulExpandList.expandGroup(groupPosition);
    }

}
