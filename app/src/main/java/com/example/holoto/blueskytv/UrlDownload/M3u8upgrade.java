package com.example.holoto.blueskytv.UrlDownload;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.Utils;
import com.example.holoto.blueskytv.MainActivity;
import com.example.holoto.blueskytv.UrlDB.DaoSession;
import com.example.holoto.blueskytv.UrlDB.MyTvurl;
import com.example.holoto.blueskytv.UrlDB.MyTvurlDao;
import com.example.holoto.blueskytv.UrlDB.TvDbApp;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class M3u8upgrade extends IntentService {
private final static String action_updateurldbfromjson="action_updateurldbfromjson";
private final static String action_downm3u8="action_download_m3u8";
private final static String action_updateurldb="action_updateurldb";
    public M3u8upgrade() {
        super("M3u8upgrade");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action==action_updateurldbfromjson)
            {
                UpdateUrlFromJson();
            }else if (action==action_downm3u8){
            downm3u8();
            }else if (action==action_updateurldb){
                updateurldb();
            }
        }
    }

    public static void getAction_updateurldbfromjson(Context context) {
        Intent intent=new Intent(context,M3u8upgrade.class);
        intent.setAction(action_updateurldbfromjson);

        context.startService(intent);

    }
    public static void getAction_downm3u8(Context context)
    {

        Intent intent=new Intent(context,M3u8upgrade.class);
        intent.setAction(action_downm3u8);

        context.startService(intent);
    }

    public static void getAction_updateurldb(Context context) {
        Intent intent=new Intent(context,M3u8upgrade.class);
        intent.setAction(action_updateurldb);

        context.startService(intent);

    }

    private  void UpdateUrlFromJson()
    {
        DaoSession daoSession=((TvDbApp) getApplication()).getDaoSession();
        MyTvurlDao myTvurlDao=daoSession.getMyTvurlDao();
        Log.e("dd","action_firstupdate");
        Gson gson=new Gson();
        String m3u8=null;
        try {
            m3u8=FileIOUtils.readFile2String(CreateFile.myfilen);
        }catch (Exception e)
        {

            e.printStackTrace();
        };
        Log.e("dd", "frist_update: "+m3u8.trim().toString() );
//        mybody mym3u8json=new mybody();
        mybody mym3u8json=gson.fromJson(m3u8,mybody.class);
//        Log.e("dd", "frist_update: "+mym3u8json.getChannels().get(7).getUrl() );
//        Log.e("dd", "frist_update: "+mym3u8json.getChannels().size() );
        myTvurlDao.deleteAll();
        myTvurlDao.queryBuilder().build().list().clear();
        int size=mym3u8json.getChannels().size();
        for (int max=0;max<size;max++){
            List<String> a=new ArrayList<>();
            a.add(mym3u8json.getChannels().get(max).getUrl());
            a.add("empty");
            a.add("empty2");
            myTvurlDao.insert(new MyTvurl(mym3u8json.getChannels().get(max).getName(),a));
    }

    }
    public interface DownloadJson2
    {
        @Streaming
        @GET("{filename}")
        Call<mybody> Download(@Path("filename") String filename);
    }
    private void downm3u8()
    {
        final String url;
        final String filename1;
//        http://pfyi37wqq.bkt.clouddn.com/2000.m3u8
//        https://raw.githubusercontent.com/GL8666/m3u8_channels/master/channels.json
//        url="http://pfyi37wqq.bkt.clouddn.com/";
//        filename1="2000.m3u8";

        url="https://raw.githubusercontent.com/GL8666/m3u8_channels/master/";
        filename1="channels.json";
        OkHttpClient.Builder builder=new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request().newBuilder().addHeader("Accept-Encoding","*").build());
                    }
                })
                .cache(new Cache(new File(getCacheDir() ,"https"),50*1024*1024));
//        .readTimeout(20,TimeUnit.SECONDS).writeTimeout(20,TimeUnit.SECONDS)
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .client(new OkHttpClient.Builder()
                        .cache(new Cache(new File(getCacheDir(),"https"),50*1024*1024)).build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        DownService downService=retrofit.create(DownService.class);
        DownloadJson2 downloadJson2=retrofit.create(DownloadJson2.class);
//        Call<ResponseBody> bodyCall=downService.Download(filename1);
        Call<mybody> bodyCall=downloadJson2.Download(filename1);
        final   Gson gson=new Gson();
        bodyCall.enqueue(new Callback<mybody>() {
            @Override
            public void onResponse(Call<mybody> call, retrofit2.Response<mybody> response) {
                Log.e("dd", "onResponse: "+response.body().getChannels().size() );
//                for (int a=0;a<response.body().getChannels().size();a++)
//                {
//                    Log.e("adad", "onResponse: "+response.body().getChannels().get(a).getName() );
//                }
                try {
                    FileIOUtils.writeFileFromString(CreateFile.myfilen,gson.toJson(response.body(),mybody.class));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<mybody> call, Throwable t) {
                t.printStackTrace();
            }
        });


//        bodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                try {
//                    InputStream inputStream=response.body().byteStream();
//                    String filen;
//                  filen=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"2000.m3u8";
//
//
//                    Log.e("dd", "onResponse: "+response.code() );
//                    Log.e("dd", "onResponse: "+response.isSuccessful() );
//                    Log.e("dd", "onResponse: "+response.headers().toString() );
//                    Log.e("dd", "onResponse: "+response.errorBody().string() );
//                    CreateFile.CWFIle(filen,inputStream);
//                }catch (Throwable e){
//                    Log.e("d", "onResponse: error" );
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("dd", "onFailure: f" );
//                t.printStackTrace();
//            }
//        });
    }
    private  void  updateurldb()
    {
        DaoSession daoSession=((TvDbApp) getApplication()).getDaoSession();
        MyTvurlDao myTvurlDao=daoSession.getMyTvurlDao();
        String m3u8=null;
        try {
            m3u8=FileIOUtils.readFile2String(CreateFile.myfilen);
        }catch (Exception e)
        {

            e.printStackTrace();
        }
        String streamurl="^https?://.*";
        String tvname="\\\"\\,.*";
        String tvname2="\\\"[\\w]*\\,.*";
//         \"[\w]*\,.*
        String pic_url="(?=h).*?://[\\w\\d\\/\\.\\-\\%\\@\\!\\+\\:\\*\\_\\~\\=\\?\\>\\>\\&\\$\\(\\)]*(?=\")";

        Pattern patternstreamurl=Pattern.compile(streamurl,Pattern.MULTILINE);
        Pattern patterntvname=Pattern.compile(tvname,Pattern.MULTILINE);
        Pattern patternpic_url=Pattern.compile(pic_url,Pattern.MULTILINE);
        Matcher matchersreamurl=patternstreamurl.matcher(m3u8);
        Matcher matcherpic_url=patternpic_url.matcher(m3u8);
        Matcher matchertvname=patterntvname.matcher(m3u8);
        while (matchersreamurl.find()){
            if (matcherpic_url.find()&&matchertvname.find()){
                List<String> a=new ArrayList<>();
                a.add(matchersreamurl.group());
                a.add(matcherpic_url.group());
                a.add("empty");
                myTvurlDao.insert(new MyTvurl(matchertvname.group(),a));
            }
        }

    }
}
