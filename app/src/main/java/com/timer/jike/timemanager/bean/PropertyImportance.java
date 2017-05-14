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
public class PropertyImportance extends BaseProperty{
    @Id
    private Long id;
    private String signal;
    private String text;


    @Generated(hash = 657660875)
public PropertyImportance() {
}

@Generated(hash = 357397111)
public PropertyImportance(Long id, String signal, String text) {
    this.id = id;
    this.signal = signal;
    this.text = text;
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getSignal() {
    return this.signal;
}

public void setSignal(String signal) {
    this.signal = signal;
}

public String getText() {
    return this.text;
}

public void setText(String text) {
    this.text = text;
}

    @Override
    public String toString() {
        return "PropertyImportance{" +
                "id=" + id +
                ", signal='" + signal + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
