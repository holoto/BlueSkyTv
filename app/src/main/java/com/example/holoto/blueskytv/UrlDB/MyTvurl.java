package com.example.holoto.blueskytv.UrlDB;

import android.util.ArraySet;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

@Entity
public class MyTvurl
{

    @NotNull
    private String name;

    @Convert(columnType = String.class,converter = StringtoList.class)
    private List<String> url=null;


    public List<String> getUrl() {
        return url;
    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    @Keep
    public MyTvurl(@NotNull String name, List<String> url) {
        this.name = name;
        this.url = url;
    }



    @Generated(hash = 1880961644)
    public MyTvurl() {
    }
}
