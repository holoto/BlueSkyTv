package com.example.holoto.blueskytv;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.example.holoto.blueskytv.TvListView.TvlistAdpater;
import com.example.holoto.blueskytv.UrlDB.DaoSession;
import com.example.holoto.blueskytv.UrlDB.MyTvurl;
import com.example.holoto.blueskytv.UrlDB.MyTvurlDao;
import com.example.holoto.blueskytv.UrlDB.TvDbApp;
import com.example.holoto.blueskytv.UrlDownload.CreateFile;
import com.example.holoto.blueskytv.UrlDownload.M3u8upgrade;
import com.example.holoto.blueskytv.myplayer.myTvPlayer;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;


public class MainActivity extends AppCompatActivity {

//    StandardGSYVideoPlayer videoPlayer;
    myTvPlayer  videoPlayer;
    OrientationUtils orientationUtils;
    GSYVideoManager gsyVideoManager;
    Context context_now;
    Thread mthread;
    TextView textView1=null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String savefilepath=null;
    public String mmmvideourl=null;
    MyTvurlDao myTvurlDao;
    TvlistAdpater tvlistAdpater;
    private RecyclerView recyclerView;
    private int posss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(getApplicationContext());
        Toast_net();
        DaoSession daoSession=((TvDbApp) getApplication()).getDaoSession();
        myTvurlDao=daoSession.getMyTvurlDao();
        sharedPreferences=getSharedPreferences("tv",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putBoolean("firstupdate",false);
        editor.commit();
        checksdcard();
        textView1=(TextView) findViewById(R.id.test_text1);
        if(!FileUtils.isFileExists(CreateFile.myfilen))
        {
            M3u8upgrade.getAction_downm3u8(this);
            if (FileIOUtils.readFile2String(CreateFile.myfilen).trim()==""){
                M3u8upgrade.getAction_updateurldbfromjson(this);
            }

        }


        if (myTvurlDao.queryBuilder().build().list().size()<1)
        {
            M3u8upgrade.getAction_updateurldbfromjson(this);

        }
        try {
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("sleep", "onCreate: 2000" );
//        M3u8upgrade.getAction_downm3u8(this);
//        M3u8upgrade.getAction_firstupdate(this);
        initView();
        initrecycleview();
//        startActivity(new Intent(getApplicationContext().getApplicationContext(),Main2Activity.class));
//        M3u8upgrade.getAction_downm3u8(this);
    }
    private void initrecycleview()
    {
        final List<MyTvurl> myTvurlList=myTvurlDao.queryBuilder().list();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView=(RecyclerView) findViewById(R.id.Tv_List_menu_all2);
        recyclerView.setItemAnimator(null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(linearLayoutManager);
        tvlistAdpater=new TvlistAdpater(myTvurlList);
        tvlistAdpater.clicklister(new TvlistAdpater.mylistenerjk() {
            @Override
            public void listclick1(View view, int pos) {
                posss=pos;
                updowntv();
                Log.d("dd", "listclick1: "+myTvurlList.get(pos).getName());
//                view.setBackgroundColor(getColor(R.color.colorPrimaryDark));

//                view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                textView1.setText(sharedPreferences.getString("tvname","empty").substring(2));

            }
        });
        recyclerView.setAdapter(tvlistAdpater);
    }
    private void testb()
    {

        String filename=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"2000.m3u8";
        String m3u8=FileIOUtils.readFile2String(filename);
        String streamurl="^https?://.*";
        String tvname="\\\"\\,.*";
        String tvname2="(?=\")[\\w]*\\\"\\,.*";
        String pic_url="(?=h).*?://[\\w\\d\\/\\.\\-\\%\\@\\!\\+\\:\\*\\_\\~\\=\\?\\>\\>\\&\\$\\(\\)]*(?=\")";


        //        (?=h).*?://[\w\d\/\.\-\%\@\!\+\:\*\_\~\=\?\>\>\&\$\(\)]*(?=")
        Pattern patternstreamurl=Pattern.compile(streamurl,Pattern.MULTILINE);
        Pattern patterntvname=Pattern.compile(tvname,Pattern.MULTILINE);
        Pattern patternpic_url=Pattern.compile(pic_url,Pattern.MULTILINE);
        Matcher matchersreamurl=patternstreamurl.matcher(m3u8);
        Matcher matcherpic_url=patternpic_url.matcher(m3u8);
        Matcher matchertvname=patterntvname.matcher(m3u8);


        int testbba=0;
        while (matchersreamurl.find())
        {
            testbba++;
//            textView1.append("\n");
//            textView1.append(matcher.group());
        }
        Toast.makeText(this,testbba+"dd",Toast.LENGTH_LONG).show();
//        textView1.setText(testbba+"dd");
    }



    private void test1()
    {
        String filename="key11111";
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+filename);
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            textView1.setText("mount");
        }
            try {
            file.createNewFile();
        }catch (IOException e){
            textView1.setText(e.toString());
            return;
        }
        if(FileUtils.isFileExists(file))
        {
//            FileIOUtils.writeFileFromString(file,new Random().nextInt()+"ddd\n");

            textView1.setText(FileIOUtils.readFile2String(file));
        }else {
            textView1.setText("file create fail");
        }



    }
    private void test2()
    {
        mthread=new Thread(new Runnable() {
            @Override
            public void run() {
                textView1.append("\n");
                textView1.append("Environment.getExternalStorageState()"+Environment.getExternalStorageState());
                textView1.append("\n");
                textView1.append("Environment.getDownloadCacheDirectory().getAbsolutePath()"+Environment.getDownloadCacheDirectory().getAbsolutePath());
                textView1.append("\n");
                textView1.append("Environment.getExternalStorageDirectory().getAbsolutePath()"+Environment.getExternalStorageDirectory().getAbsolutePath());
                textView1.append("\n");
                textView1.append("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()\n"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                textView1.append("\n");
                textView1.append("getCacheDir().getAbsolutePath()"+getCacheDir().getAbsolutePath());
                textView1.append("\n");
                textView1.append("getFilesDir().getAbsolutePath()"+getFilesDir().getAbsolutePath());
                textView1.append("\n");
                textView1.append(Environment.getDataDirectory().getAbsolutePath());
                textView1.append("\n");
                textView1.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getParent()+"/dd");
                textView1.append("\n");
                textView1.append(getExternalCacheDir().getAbsolutePath());
                textView1.append("\n");
                textView1.append(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                textView1.append("\n");
            }
        });
        mthread.start();
    }
    private void checksdcard()
    {
        if (Build.VERSION.SDK_INT>=23){
int REQUEST_CODE_CONTACT=101;
String[] permissons={Manifest.permission.WRITE_EXTERNAL_STORAGE};
for (String str:permissons)
{
    if (this.checkSelfPermission(str)!=PackageManager.PERMISSION_GRANTED){
        this.requestPermissions(permissons,REQUEST_CODE_CONTACT);
        return;
    }
}
        }
    }

    private void initView()
    {

        videoPlayer=(myTvPlayer) findViewById(R.id.activity_player_view);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        GSYVideoType.setShowType(1);

        mmmvideourl=sharedPreferences.getString("tv_url","dasdas");
        videoPlayer.setFullHideActionBar(true);
        videoPlayer.setFullHideStatusBar(true);
        videoPlayer.setKeepScreenOn(true);
        videoPlayer.setIfCurrentIsFullscreen(true);
        videoPlayer.setAutoFullWithSize(true);
        videoPlayer.setDrawingCacheEnabled(true);
        videoPlayer.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        videoPlayer.setIsTouchWigetFull(true);
//        videoPlayer.setNeedShowWifiTip(true);
//        ImageView imageView=new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.mipmap.ic_launcher_round);
//        videoPlayer.setThumbImageView(imageView);



//        videoPlayer.setIsTouchWiget(true);
        orientationUtils=new OrientationUtils(this,videoPlayer);

        videoPlayer.setUp(sharedPreferences.getString("tv_url","dada"),true,sharedPreferences.getString("tvname","empty").substring(2));
        textView1.setText(sharedPreferences.getString("tvname","empty").substring(2));
        videoPlayer.startPlayLogic();

        videoPlayer.setVV(new myTvPlayer.setDoubleclick() {
            @Override
            public void setvvd() {
                swithRecycleView();
            }
        });
        Log.e("dd", "initView: " );


    }
    @Override
    protected void onResume() {
        super.onResume();
//        mmmvideourl="http://54.255.155.24:1935//Live/_definst_/amlst:sweetbcha1novD235L240P/playlist.m3u8";
//        videoPlayer.setUp(sharedPreferences.getString("tv_url","dd"),true,sharedPreferences.getString("tvname","ddd"));
//        videoPlayer.startPlayLogic();
        Log.e("mm", "onResume: " );
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("dd", "onPause: " );
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("dd", "onDestroy: " );
        orientationUtils.releaseListener();
        GSYVideoManager.releaseAllVideos();

        videoPlayer.clearCurrentCache();
        videoPlayer.destroyDrawingCache();


    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getVisibility()==View.VISIBLE)
        {
            recyclerView.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                    Recycle_set_on();
                break;
            case KeyEvent.KEYCODE_ENTER:
                Recycle_set_on();
                break;
            case KeyEvent.KEYCODE_5:
                Recycle_set_on();
                break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (recyclerView.getVisibility()==View.GONE){
                        if (posss==myTvurlDao.queryBuilder().build().list().size()-1){
                            posss=0;
                        }else {
                            posss=posss+1;
                        }
                        updowntv();
                    }
                    break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if (recyclerView.getVisibility()==View.GONE) {
                            if (posss == 0) {
                                posss = myTvurlDao.queryBuilder().build().list().size()-1;
                            } else {
                                posss = posss - 1;
                            }
                            updowntv();
                        }
                        break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (recyclerView.getVisibility()==View.GONE){
                    if (posss==myTvurlDao.queryBuilder().build().list().size()-1){
                        posss=0;
                    }else {
                        posss=posss+1;
                    }
                    updowntv();
                }
                break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (recyclerView.getVisibility()==View.GONE) {
                        if (posss == 0) {
                            posss = myTvurlDao.queryBuilder().build().list().size()-1;
                        } else {
                            posss = posss - 1;
                        }
                        updowntv();
                    }
                    break;
                        default:
                        return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Long mlasttime;
                Long mcurtime=System.currentTimeMillis();
                break;
                    default:
                        return super.onTouchEvent(event);

        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    private void Toast_net()
    {
        if(!NetworkUtils.isAvailableByPing()){
            Toast.makeText(this,"Network err please check it",Toast.LENGTH_LONG).show();
        }
    }
    public void swithRecycleView()
    {
        int opendd=recyclerView.getVisibility();
        if (opendd==8){
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
        }

            return;

    }
    public void Recycle_set_on()
    {
        if (recyclerView.getVisibility()==View.GONE)
        {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    private void updowntv(){
        editor.putString("tvname",myTvurlDao.queryBuilder().build().list().get(posss).getName());
        editor.putString("tv_url",myTvurlDao.queryBuilder().build().list().get(posss).getUrl().get(0));
        editor.commit();
        videoPlayer.onVideoPause();
        textView1.setText(sharedPreferences.getString("tvname","empty"));
        videoPlayer.setUp(sharedPreferences.getString("tv_url","dd"),true,sharedPreferences.getString("tvname","ddd"));
        videoPlayer.startPlayLogic();

    }
}
