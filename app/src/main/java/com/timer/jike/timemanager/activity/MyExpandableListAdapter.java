package com.timer.jike.timemanager.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.BaseProperty;
import com.timer.jike.timemanager.bean.PropertyImportance;
import com.timer.jike.timemanager.bean.PropertyPredictability;
import com.timer.jike.timemanager.bean.PropertyType;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilDialog;

import java.util.List;


/**
 * Created by jike on 2017/5/2.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    public String[] groupStrings;
    public List<? extends BaseProperty>[] childStrings;
    public Context mContext;


    public MyExpandableListAdapter(String[] groupStrings, List<? extends BaseProperty>[] childStrings, Context context) {
        this.groupStrings = groupStrings;
        this.childStrings = childStrings;
        mContext = context;
    }

    //        获取分组的个数
    @Override
    public int getGroupCount() {
        return groupStrings.length;
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childStrings[groupPosition].size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupStrings[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childStrings[groupPosition].get(childPosition);
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //        获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_item_parent, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
            groupViewHolder.btnAdd = (TextView) convertView.findViewById(R.id.btn_eip_add);

            groupViewHolder.btnAdd.setOnClickListener(v -> {
                Toast.makeText(mContext, "onClickAdd"+ groupPosition, Toast.LENGTH_SHORT).show();

                View inflate = View.inflate(mContext, R.layout.dialog_event_property, null);

                EditText text = (EditText) inflate.findViewById(R.id.et_dcs_text);
                EditText signal = (EditText) inflate.findViewById(R.id.et_dcs_signal);
                ColorPicker picker = (ColorPicker) inflate.findViewById(R.id.picker);
                SVBar svBar = (SVBar) inflate.findViewById(R.id.svbar);

                BaseProperty property = null;

                switch (groupPosition){
                    case 0://predict
                        picker.setVisibility(View.GONE);
                        svBar.setVisibility(View.GONE);
                        property = new PropertyPredictability();
                        break;
                    case 1://importance
                        picker.setVisibility(View.GONE);
                        svBar.setVisibility(View.GONE);
                        property = new PropertyImportance();
                        break;
                    case 2://type
                        picker.addSVBar(svBar);
                        picker.setOldCenterColor(picker.getColor());
                        property = new PropertyType();
                        picker.setOnColorChangedListener(((PropertyType) property)::setColor);
                        picker.setShowOldCenterColor(false);
                        break;
                    default:
                        throw new RuntimeException("property 类型不合法！");
                }

                BaseProperty finalProperty = property;
                MaterialDialog.SingleButtonCallback callbackP = (dialog, which) -> {
                    if (TextUtils.isEmpty(text.getText().toString())){
                        Toast.makeText(mContext, "请输入名称", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    finalProperty.setText(text.getText().toString());
                    if (finalProperty instanceof PropertyPredictability){
                        ((PropertyPredictability) finalProperty).setSignal(signal.getText().toString());
                    } else if(finalProperty instanceof PropertyImportance){
                        ((PropertyImportance) finalProperty).setSignal(signal.getText().toString());
                    } else if(finalProperty instanceof PropertyType){
                        ((PropertyType) finalProperty).setColor(picker.getColor());
                    }
                    UtilDB.insertProperty(finalProperty);
                    if (mContext instanceof UIExpandableLvActivity){
                        ((UIExpandableLvActivity) mContext).notifyDataSetChanged(groupPosition);
                    }

                };

                UtilDialog.buildDialog(mContext, "添加", "添加", "取消", callbackP, null, inflate);

            });
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupStrings[groupPosition]);
        return convertView;
    }

    //        获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_item_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (getChild(groupPosition,childPosition) instanceof BaseProperty)
        childViewHolder.tvTitle.setText(((BaseProperty) getChild(groupPosition,childPosition)).getText());
        return convertView;
    }

    //        指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewHolder {
        TextView tvTitle;
        TextView btnAdd;
    }

    private class ChildViewHolder {
        TextView tvTitle;
    }
}
