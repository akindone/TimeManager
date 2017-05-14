package com.timer.jike.timemanager.utils;

import com.timer.jike.timemanager.bean.BaseProperty;

import java.util.List;
import java.util.Objects;

/**
 * Created by jike on 2017/5/14.
 */

public class UtilList {

    public static int getPosition(List<? extends BaseProperty> list, BaseProperty property){
        if (property == null)
            return -1;
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i).getId(), property.getId())){
                return i;
            }
        }
        return -1;
    }
}
