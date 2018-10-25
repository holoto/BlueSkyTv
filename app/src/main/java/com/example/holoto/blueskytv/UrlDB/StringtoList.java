package com.example.holoto.blueskytv.UrlDB;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class StringtoList implements PropertyConverter<List<String>,String>
{
    @Override
    public List<String> convertToEntityProperty(String databaseValue)
    {
        if (databaseValue==null)
        {
            return null;
        }
        else {
            List<String> list= Arrays.asList(databaseValue.split(","));
            return list;
        }
    }
    @Override
    public String convertToDatabaseValue(List<String> entityProerty)
    {
        if (entityProerty==null)
        {
            return null;
        }
        else{
            StringBuilder sb=new StringBuilder();
            for (String link:entityProerty)
            {
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
    }
    }
}
