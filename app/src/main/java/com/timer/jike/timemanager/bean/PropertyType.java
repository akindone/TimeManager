package com.timer.jike.timemanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by jike on 2017/5/14.
 */
@Entity(indexes = {
        @Index(value = "id", unique = true)
})
public class PropertyType extends BaseProperty {
    @Id
    private Long id;
    private Integer color;
    private String text;

    @Generated(hash = 895355453)
    public PropertyType() {
    }

    @Generated(hash = 1767717324)
    public PropertyType(Long id, Integer color, String text) {
        this.id = id;
        this.color = color;
        this.text = text;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getColor() {
        return this.color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "PropertyType{" +
                "id=" + id +
                ", color=" + color +
                ", text='" + text + '\'' +
                '}';
    }
}
