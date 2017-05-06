package com.timer.jike.timemanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by jike on 2017/5/6.
 */
@Entity(indexes = {
        @Index(value = "id DESC", unique = true)
})
public class ColorRule {
    @Id
    private Long id;
    private String text;
    private int color;
    @Generated(hash = 1697257565)
public ColorRule() {
}
@Generated(hash = 2073383135)
public ColorRule(Long id, String text, int color) {
    this.id = id;
    this.text = text;
    this.color = color;
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getText() {
    return this.text;
}
public void setText(String text) {
    this.text = text;
}
public int getColor() {
    return this.color;
}
public void setColor(int color) {
    this.color = color;
}
}
