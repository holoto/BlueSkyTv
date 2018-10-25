package com.example.holoto.blueskytv.UrlDownload;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.example.holoto.blueskytv.UrlDB.DaoSession;
import com.example.holoto.blueskytv.UrlDB.MyTvurlDao;
import com.example.holoto.blueskytv.UrlDB.TvDbApp;
import com.shuyu.gsyvideoplayer.render.effect.InvertColorsEffect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class CreateFile
{

    public final static String myfilen=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"2000.m3u8";
    public static void CWFIle(final String filenamepath, final InputStream inputStream)
    {
                    if(FileUtils.isFileExists(filenamepath)){
                        FileIOUtils.writeFileFromIS(filenamepath,inputStream);
                    }else{
                        File file=new File(filenamepath);
                        try {
                            file.createNewFile();
                        }catch (IOException e){
                            Log.e("error",e+"a");
                            return;
                        }
                        FileIOUtils.writeFileFromIS(filenamepath,inputStream);

                }
    }
    public  void WriteResponseBodyToDisk(String filenamepath, ResponseBody responseBody)
    {

    }
    private static String getExtendedMemoryPath(Context mContext) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {

            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
