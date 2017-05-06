package com.timer.jike.timemanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.ColorRule;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilDialog;
import com.timer.jike.timemanager.utils.UtilLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    @BindView(R.id.btn_sa_add) Button mBtnAdd;
    @BindView(R.id.rv_sa_rules) RecyclerView mRvRules;
    private List<ColorRule> mList;
    int pickedColor;
    private MyRvAdapter mMyRvAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mList = UtilDB.queryColorRules().list();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvRules.setLayoutManager(layoutManager);
        mMyRvAdapter = new MyRvAdapter(mList,this);
        mRvRules.setAdapter(mMyRvAdapter);


    }

    @OnClick(R.id.btn_sa_add)
    void onClickAdd(){
        View inflate = View.inflate(this, R.layout.dialog_color_selector, null);

        EditText text = (EditText) inflate.findViewById(R.id.et_dcs_text);

        ColorPicker picker = (ColorPicker) inflate.findViewById(R.id.picker);
        SVBar svBar = (SVBar) inflate.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) inflate.findViewById(R.id.opacitybar);
        SaturationBar saturationBar = (SaturationBar) inflate.findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) inflate.findViewById(R.id.valuebar);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        //To get the color
        pickedColor = picker.getColor();

        //To set the old selected color u can do it like this
        picker.setOldCenterColor(picker.getColor());
        picker.setOnColorChangedListener(color -> pickedColor = color);

        //to turn of showing the old color
        picker.setShowOldCenterColor(false);

//        //adding onChangeListeners to bars
//        opacityBar.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
//            @Override
//            public void onOpacityChanged(int opacity) {
//                pickedColor = color;
//
//            }
//        });
//        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
//            @Override
//            public void onValueChanged(int value) {
//                pickedColor = color;
//
//            }
//        });
//        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
//            @Override
//            public void onSaturationChanged(int saturation) {
//                pickedColor = color;
//
//            }
//        });


        MaterialDialog.SingleButtonCallback callbackP = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (TextUtils.isEmpty(text.getText().toString())){
                    Toast.makeText(SettingsActivity.this, "请输入文字", Toast.LENGTH_SHORT).show();
                    return;
                }
                ColorRule colorRule = new ColorRule();
                colorRule.setText(text.getText().toString());
                colorRule.setColor(pickedColor);
                UtilDB.getColorRuleDao().insert(colorRule);
                Toast.makeText(SettingsActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                mList.add(colorRule);
                mMyRvAdapter.notifyDataSetChanged();

            }
        };
        UtilDialog.buildDialog(this, "选择颜色", "确定", "取消", callbackP, (dialog, which) -> dialog.dismiss(), inflate, false);


    }

    class MyRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ColorRule> list;
        private final LayoutInflater mLayoutInflater;
        Context mContext;

        public MyRvAdapter(List<ColorRule> list, Context context) {
            this.list = list;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(mLayoutInflater.inflate(R.layout.cardview_color_rule,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ColorRule colorRule = list.get(position);
            MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.mivColor.setBackgroundColor(colorRule.getColor());
            holder1.mTvText.setText(colorRule.getText());
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "敬请期待！", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.iv_ccr_left_line) ImageView mivColor;
            @BindView(R.id.tv_ccr_text) TextView mTvText;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
