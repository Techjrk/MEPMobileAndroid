package com.lecet.app.data.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class BindableString extends BaseObservable {
    private String value;
    private int size;

    //Constructer sets up value size relation
    public BindableString(String value){
        super();
        this.value = value;
        size = getValue().length();
        notifyChange();
    }

    @Bindable//@Bindable makes an Observable object visible to dataBinded xml
    public String getValue() {
      if(value.equals("") || value == null){//logic to avoid null strings giving errors
          return "";
      }else {
          return value;
      }
    }

    //Method called when the
    public void setValue(String value) {
      if (!this.value.equals(value)) {
          this.value = value;
          size = getValue().length();
          notifyChange();
      }

    }

    @Bindable
    public String getSize(){return String.valueOf(size);}
}