package com.mcz.temperarure_humidity_appproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.mcz.temperarure_humidity_appproject.MyUtils.ViewUtils;
import com.mcz.temperarure_humidity_appproject.Mydata.Mygatewayid;
import com.mcz.temperarure_humidity_appproject.Mydialog.BottomPopUpDialog;
import com.mcz.temperarure_humidity_appproject.Myview.HealthView;
import com.mcz.temperarure_humidity_appproject.Myview.SwitcherView;
import com.mcz.temperarure_humidity_appproject.R;
import com.mcz.temperarure_humidity_appproject.app.HistoricaldataActivity;
import com.mcz.temperarure_humidity_appproject.app.model.DataInfo;
import com.mcz.temperarure_humidity_appproject.app.model.PullrefreshListviewAdapter2;
import com.mcz.temperarure_humidity_appproject.app.utils.Config;
import com.mcz.temperarure_humidity_appproject.app.utils.DataManager;
import com.mcz.temperarure_humidity_appproject.app.view.view.IPullToRefresh;
import com.mcz.temperarure_humidity_appproject.app.view.view.LoadingLayout;
import com.mcz.temperarure_humidity_appproject.app.view.view.PullToRefreshBase;
import com.mcz.temperarure_humidity_appproject.app.view.view.PullToRefreshFooter;
import com.mcz.temperarure_humidity_appproject.app.view.view.PullToRefreshHeader;
import com.mcz.temperarure_humidity_appproject.app.view.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyHistoryActivity extends AppCompatActivity {
    String deviceId="";
    String gatewayId="";
    String token2 = "";
    TextView makedataText2;
    Button myhistoryshuaxin;
    //Button viewhistory;
    Button tosetting;
    Button returndenglu;
    SharedPreferences sp2;
    int returndata=0;//控制
    int returndata2=0;
    private MyAdapter2 adapter2;
    private Handler handler=null;
    private List<DataInfo> mlist2 = null;
    private String time;
    private List<String> gatewayId2=new ArrayList<String>();
    private String[] DialogData;//deviceId 组合 用于下拉选择框的内容
    private int soscishu=0;
    String account;
    String password;
    private HealthView mQQHealthView;//xinlv
    private HealthView mQQHealthView2;//xueya
    private HealthView mQQHealthView3;//tiwen
    private SwitcherView switcherView;//gun dong tiao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        makedataText2=(TextView)findViewById(R.id.makedatatext2);
        myhistoryshuaxin=(Button)findViewById(R.id.myhistory_shuaxin);
        //创建属于主线程的handler
        handler=new Handler();
        returndenglu=(Button)findViewById(R.id.person2);

        sp2 = PreferenceManager.getDefaultSharedPreferences(this);
        mQQHealthView= (HealthView) findViewById(R.id.qq2HealthView);
        mQQHealthView2= (HealthView) findViewById(R.id.qq2HealthView2);
        mQQHealthView3= (HealthView) findViewById(R.id.qq2HealthView3);
        mQQHealthView.setThemeColor(ViewUtils.getThemeColorPrimary(MyHistoryActivity.this));
        mQQHealthView2.setThemeColor(ViewUtils.getThemeColorPrimary(MyHistoryActivity.this));
        mQQHealthView3.setThemeColor(ViewUtils.getThemeColorPrimary(MyHistoryActivity.this));
        mQQHealthView.settoptext("实时心率");
        mQQHealthView.setSteps(new double[]{000,000,000,000,000,000,000});
        mQQHealthView2.settoptext("实时血压");
        mQQHealthView2.setSteps(new double[]{000,000,000,000,000,000,000});
        mQQHealthView3.settoptext("实时体温");
        mQQHealthView3.setSteps(new double[]{000,000,000,000,000,000,000});
        mQQHealthView.setDanwei("次/min");
        mQQHealthView2.setDanwei("mmHg");
        mQQHealthView3.setDanwei("℃");

        account=sp2.getString("account","");
        password=sp2.getString("LoginPassword","");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏


        switcherView = (SwitcherView) findViewById(R.id.switcherView);
//        ArrayList<String> strs = new ArrayList<>();
//        strs.add("双十一购物节");
//        strs.add("双十二购物节");
//        strs.add("京东购物节");
//        strs.add("买买买买买买买买买买买买买买买买买买买买买买买买买买买买买买买买买买买买");
//        switcherView.setResource(strs);
//        switcherView.startRolling();
//        switcherView.setAnimationBottom2Top();



//        viewhistory=(Button)findViewById(R.id.img_viewhistroy);
//        viewhistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MyHistoryActivity.this,MyHistoryActivity2.class);
//                intent.putExtra("deviceId", deviceId);
//                intent.putExtra("gatewayId", gatewayId);
//                startActivity(intent);
//            }
//        });
        tosetting=(Button)findViewById(R.id.tosetting);
        tosetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyHistoryActivity.this,MySettingActivity.class);
                startActivity(intent);
            }
        });

        returndenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp2.edit().putBoolean("AUTO_ISCHECK", false).commit();//取消自动登陆
                Intent intent=new Intent(MyHistoryActivity.this,MyActivity1.class);
                startActivity(intent);
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    init();
                    try {
                        Thread.sleep(10000);//设置10s// 延迟，10s刷新一次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        myhistoryshuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }
    private void init(){
        token2 = sp2.getString("token", "");

        if (sp2.getString("nowgatewayid","").equals("")){
            //没有选择过车载设备当前的id
            try {
                SearchRequest();//连接数据库获取当前账户下的所有id
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int k=0;
                    while (true){
                        if (k==0){
                            if (returndata2==2){

                                k=1;
                                List<String> list = new ArrayList<String>();
                                for (int i = 0; i<gatewayId2.size(); i++){
                                    list.add(gatewayId2.get(i));
                                }
                                DialogData=list.toArray(new String[0]);
                                //下面弹出选择框的方式
//                                new BottomPopUpDialog.Builder()
//                                        .setDialogData(DialogData)
//                                        .setItemTextColor(2, R.color.colorAccent)
//                                        .setItemTextColor(4, R.color.colorAccent)
//                                        .setCallBackDismiss(true)
//                                        .setItemLineColor(R.color.line_color)
//                                        .setItemOnListener(new BottomPopUpDialog.BottomPopDialogOnClickListener() {
//                                            @Override
//                                            public void onDialogClick(String tag) {
//                                                gatewayId=deviceId=tag;
//                                                Log.i("gatewayll",gatewayId);
//                                                sp2.edit().putString("nowgatewayid",tag).commit();//修改关联的设备ID
//                                                Toast.makeText(MyHistoryActivity.this,"选择"+tag,Toast.LENGTH_SHORT).show();
//                                                gatewayId=deviceId=tag;
//                                            }
//                                        })
//                                        .show(getSupportFragmentManager(), "tag");
                                //下面是下拉选择框的方式 但是改成了调用子函数

//                                final Spinner inputServer = new Spinner(MyHistoryActivity.this);//这是下拉选择框
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyHistoryActivity.this, android.R.layout.simple_dropdown_item_1line,DialogData);
//                                inputServer.setAdapter(adapter);
//                                AlertDialog.Builder builder = new AlertDialog.Builder(MyHistoryActivity.this);
//                                builder.setTitle("请修改关联的设备ID").setIcon(R.drawable.ic_all_inclusive_black_24dp).setView(inputServer)
//                                        .setNegativeButton("Cancel", null);
//                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//                                    public void onClick(DialogInterface dialog, int which) {
//                                          gatewayId=deviceId=tag;
//                                        sp2.edit().putString("nowgatewayid", (String) inputServer.getSelectedItem()).commit();
//                                        Toast.makeText(MyHistoryActivity.this,"修改成功"+inputServer.getSelectedItem(),Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                builder.show();
                                handler.post(xialaUi);
                            }
                        }

                    }

                }
            }).start();


        }else {
            gatewayId=deviceId=sp2.getString("nowgatewayid","");
            try {
                SearchRequest();//连接数据库获取当前账户下的所有id  这里为什么也要获取呢：因为这里获取是为了给设置页面的修改gatewayid提供数据
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (sp2.getString("nowgatewayid","").equals("")) {

                    }else {
                        if (!(gatewayId.equals(""))){
                            ListviewADD_Data();
                            break;
                        }

                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    if (sp2.getString("nowgatewayid","").equals("")) {

                    }else{
                        if (returndata==0) {
                            Log.i("z2mlista:", "nodata");
                            handler.post(nodataUi);
                            try {
                                Thread.sleep(200);//设置延迟，不然不行
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            //Log.i("mlista:","eee");
                            //Log.i("m2lista:",mlist2.get(0).getDevicetemperature());
                            handler.post(dataUi);
                            //tem.setText("mlist.get(0).getDevicetemperature()");
                            break;
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }


    //xia 构建Runnable对象，在runnable中更新界面
    Runnable xialaUi=new Runnable() {
        @Override
        public void run() {
            final Spinner inputServer = new Spinner(MyHistoryActivity.this);//这是下拉选择框
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyHistoryActivity.this, android.R.layout.simple_dropdown_item_1line,DialogData);
            inputServer.setAdapter(adapter);
            AlertDialog.Builder builder = new AlertDialog.Builder(MyHistoryActivity.this);
            builder.setTitle("请修改关联的设备ID").setIcon(R.drawable.ic_all_inclusive_black_24dp).setView(inputServer);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    sp2.edit().putString("nowgatewayid", (String) inputServer.getSelectedItem()).commit();
                    gatewayId=deviceId=(String) inputServer.getSelectedItem();
                    Toast.makeText(MyHistoryActivity.this,"修改成功"+inputServer.getSelectedItem(),Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();

        }
    };




    Runnable   dataUi=new  Runnable(){
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            if(mlist2.size()!=0){
                //更新界面
            /*Historydata.setText(null);
            for (i=0;i<mlist2.size();i++){
                Historydata.append(i+1+".");
                Historydata.append("心率："+mlist2.get(i).getDevicetemperature());//添加心率
                Historydata.append("血压："+mlist2.get(i).getDevicehumidity());//添加血压
                Historydata.append("体温："+mlist2.get(i).getDevicetiwen());//添加体温
                //CharSequence dateText = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date());
                //时间格式转换
                time=mlist2.get(i).getDevicetimestamp();
                int b = Integer.parseInt(time.substring(9,11));
                b=b+8;
                String TIME= time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+"  "+b+":"+time.substring(11,13)+":"+time.substring(13,15);
                Historydata.append("操作时间："+TIME+"\n");
            }*/
                adapter2 = new MyAdapter2( MyHistoryActivity.this);//创建下面的数据视图
                adapter2.clearItem();
                Log.i("mlistsize", String.valueOf(mlist2.size()));
                for (int j = 0; j < mlist2.size(); j++) {
                    adapter2.addItem(mlist2.get(j));
                    //mlist集合是用于存放界面中的值  并在跳转时传入item界面
                }
                double[] xinlv=new double[]{000,000,000,000,000,000,000};
                double[] xueya=new double[]{000,000,000,000,000,000,000};
                double[] tiwen=new double[]{000,000,000,000,000,000,000};
                if (mlist2.size()>=7){
                    for (int i=0;i<7;i++){
                        xinlv[i]= Double.parseDouble(mlist2.get(7-i-1).getDevicetemperature());//xinlv
                        String time2=mlist2.get(7-i-1).getDevicetimestamp();
                        Log.i("time23:",time2);
                        //xLabel1[i]=time2.substring(12);
                        xueya[i]= Double.parseDouble(mlist2.get(7-i-1).getDevicehumidity());//xueya
                        tiwen[i]= Double.parseDouble((mlist2.get(7-i-1).getDevicetiwen()));//tiwen
                    }
                }else{
                    int j;
                    int i;
                    for (i = 0; i<mlist2.size(); i++){
                        xinlv[i]= Double.parseDouble(mlist2.get(mlist2.size()-i-1).getDevicetemperature());//xinlv
                        String time2=mlist2.get(mlist2.size()-i-1).getDevicetimestamp();
                        //xLabel1[i]=time2.substring(12);
                        xueya[i]= Double.parseDouble(mlist2.get(mlist2.size()-i-1).getDevicehumidity());//xueya
                        tiwen[i]= Double.parseDouble(mlist2.get(mlist2.size()-i-1).getDevicetiwen());
                    }
                    j=i;
                    j--;
                    for(;i<7;i++){
                        xinlv[i]= Double.parseDouble(mlist2.get(mlist2.size()-j-1).getDevicetemperature());//xinlv
                        String time2=mlist2.get(mlist2.size()-j-1).getDevicetimestamp();
                        //xLabel1[i]=time2.substring(12);
                        xueya[i]= Double.parseDouble(mlist2.get(mlist2.size()-j-1).getDevicehumidity());//xueya
                        tiwen[i]= Double.parseDouble(mlist2.get(mlist2.size()-j-1).getDevicetiwen());//tiwen
                    }
                }
                //ListView listView=(ListView)findViewById(R.id.list_view2);
                //listView.setAdapter(adapter2);
                for (int i=0;i<mlist2.size();i++){
                    Log.i("xinlv67",mlist2.get(i).getDevicetemperature());
                }
                mQQHealthView.setSteps(xinlv);
                mQQHealthView2.setSteps(xueya);
                mQQHealthView3.setSteps(tiwen);

                //最下面的wen's'd
                ArrayList<String> strs = new ArrayList<>();
                strs.add("温度："+mlist2.get(0).getDevicewendu()+"℃ "+" 湿度："+mlist2.get(0).getDeviceshidu()+"%");
                switcherView.setResource(strs);
                switcherView.startRolling();

                mQQHealthView.setThemeColor(ViewUtils.getThemeColorPrimary(MyHistoryActivity.this));
                mQQHealthView2.setThemeColor(ViewUtils.getThemeColorPrimary(MyHistoryActivity.this));
                mQQHealthView3.setThemeColor(ViewUtils.getThemeColorPrimary(MyHistoryActivity.this));
                //switcherView.setAnimationBottom2Top();

                //mQQHealthView.settoptext("kkkkkk");
//                mQQHealthView.setavastar(R.drawable.green);//设置视图 但是显示不好 暂时取消  //绿色代表正常
//                mQQHealthView2.setavastar(R.drawable.red2);
                mQQHealthView.setDanwei("次/min");
                mQQHealthView2.setDanwei("mmHg");
                mQQHealthView3.setDanwei("℃");
                if (Double.parseDouble(mlist2.get(0).getDevicetemperature())>120.0||Double.parseDouble(mlist2.get(0).getDevicetemperature())<40.0){
                    mQQHealthView.setNowzhuangtai("当前状态异常");
                }
                if (Double.parseDouble(mlist2.get(0).getDevicehumidity())>150.0||Double.parseDouble(mlist2.get(0).getDevicehumidity())<80.0){
                    mQQHealthView2.setNowzhuangtai("当前状态异常");
                }
                if (Double.parseDouble(mlist2.get(0).getDevicetiwen())>39.0||Double.parseDouble(mlist2.get(0).getDevicetiwen())<36.0){
                    mQQHealthView3.setNowzhuangtai("当前状态异常");
                }

                mQQHealthView.invalidate();//刷新试图

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (soscishu==0){
                            //如果没有呼叫过sos联络人
                            if (mlist2.size()>=7&&
                                    (mQQHealthView.getmAverageStep()>120||mQQHealthView.getmAverageStep()<40||mQQHealthView2.getmAverageStep()>150||mQQHealthView2.getmAverageStep()<80||mQQHealthView3.getmAverageStep()>39||mQQHealthView3.getmAverageStep()<36)){
                                toast("准备呼叫紧急联络人");
                                if (sp2.getString("sosphone","").equals("")){
                                    toast("您没有设置紧急联络人");
                                }else{
                                    if (ActivityCompat.checkSelfPermission(MyHistoryActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {//如果已经获得权限
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                                                + sp2.getString("sosphone","")));//拨打紧急电话
                                        startActivity(intent);     //拨打电话
                                        soscishu++;//不要再重复打电话了
                                    }else {
                                        toast("请允许电话权限，以便设置紧急联络人");
                                        ActivityCompat.requestPermissions(MyHistoryActivity.this, new String[]{Manifest.permission.CALL_PHONE},10111);//请求电话权限
                                    }
                                }


                            }
                        }
                    }
                }).start();


                CharSequence dateText = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date());
                String TIME=dateText.toString().substring(11);
                makedataText2.setText(TIME);
            }
            else {
                makedataText2.setText("没有记录");
            }


        }
    };
    Runnable nodataUi=new Runnable() {
        @Override
        public void run() {
            if (makedataText2.getText().length()==8){
                makedataText2.setText("收取中.");
            }else if(makedataText2.getText().length()==4){
                makedataText2.setText("收取中..");
            }else if(makedataText2.getText().length()==5){
                makedataText2.setText("收取中...");
            }else if(makedataText2.getText().length()==6){
                makedataText2.setText("收取中.");
            }
            //Toast.makeText(MyMainActivity.this,"正在获取数据",Toast.LENGTH_SHORT).show();
        }
    };

    private List<DataInfo> ListviewADD_Data() {
        returndata=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String login_appid = sp2.getString("appId","");
                    ///////////////////////////************************2.3.2 查询单个设备信息***********************////////////////////////////
//            String add_url = Config.all_url + "/iocm/app/dm/v1.3.0/devices?appId=" + login_appid
//                    + "&pageNo=" + Fnum + "&pageSize=" + Onum;
                    ////////////////////////////////////////////************************查询设备历史数据*****************/////////////////////////////////////
                    String add_url = Config.all_url + "/iocm/app/data/v1.1.0/deviceDataHistory?deviceId="+deviceId+"&gatewayId="+gatewayId;
                    Log.i("zzza",add_url);
                    String json = DataManager.Txt_REQUSET(MyHistoryActivity.this, add_url, login_appid, token2);
                    Log.i("bbbbbbbbbbbbbbbbbbbbbbb", "josn1" + json);
                    mlist2 = new ArrayList<DataInfo>();

                    JSONObject jo = new JSONObject(json);
                    JSONArray jsonArray = jo.getJSONArray("deviceDataHistoryDTOs");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        DataInfo dataInfo = new DataInfo();
                        String timestamp=jsonArray.getJSONObject(i).getString("timestamp");
                        String timestamps=dataTextc(timestamp);
                        Log.i("timestamp",timestamp);
                        Log.i("timestamps",timestamps);
                        dataInfo.setDevicetimestamp(timestamps);
                        String ser_data = jsonArray.getJSONObject(i).getString("data");
                        //   Log.i("bbbbbbbbbbbbbbbbbbbbbb","timestamp            "+timestamp);
//                dataInfo.setDevicetimestamp(timestamp);
                        JSONObject jsonObject = new JSONObject(ser_data);
                        dataInfo.setDevicehumidity(jsonObject.optString("xueya"));
                        dataInfo.setDevicetemperature(jsonObject.optString("xinlv"));
                        dataInfo.setDevicetiwen(jsonObject.optString("tiwen"));
                        dataInfo.setDevicewendu(jsonObject.optString("wendu"));
                        dataInfo.setDeviceshidu(jsonObject.optString("shidu"));
                        mlist2.add(dataInfo);
                    }
                    returndata=1;

                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }).start();
        return mlist2;
    }
    private String dataTextc(String datatext){
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;
        String years;
        String months;
        String days;
        String hours;
        String minutes;
        String seconds;
        year= Integer.parseInt(datatext.substring(0,4));
        month= Integer.parseInt(datatext.substring(4,6));
        day= Integer.parseInt(datatext.substring(6,8));
        hour= Integer.parseInt(datatext.substring(9,11));
        minute= Integer.parseInt(datatext.substring(11,13));
        second= Integer.parseInt(datatext.substring(13,15));
        second=second+30;
        if (second>=60){
            second=second-60;
            minute++;
        }
        if (second==0){
            seconds="00";
        }else if (second<10){
            seconds="0"+second;
        }else {
            seconds= String.valueOf(second);
        }
        minute=minute+4;
        if (minute>=60){
            minute=minute-60;
            hour++;
        }
        if (minute==0){
            minutes="00";
        }else if (minute<10) {
            minutes = "0" + minute;
        }else {
            minutes=String.valueOf(minute);
        }
        hour=hour+8;
        if (hour>=24){
            hour=hour-24;
            day++;
        }
        if (hour==0){
            hours="00";
        }else if (hour<10){
            hours="0"+hour;
        }else {
            hours=String.valueOf(hour);
        }
        if (day>31){
            day=day-31;
            month++;
        }else if (day>30 && (month==4 || month==6 ||month==9 ||month==11)){
            day=day-30;
            month++;
        }else if (day>28 && month==2){
            day=day-28;
            month++;
        }
        if (day<10){
            days="0"+day;
        }else {
            days=String.valueOf(day);
        }
        if (month>12){
            month=month-12;
            year++;
        }
        if (month<10){
            months="0"+month;
        }else {
            months= String.valueOf(month);
        }
        years=String.valueOf(year);
        String TIME=years+"-"+months+"-"+days+"  "+hours+":"+minutes+":"+seconds;
        return TIME;
    }









    //------------以下为数据库登陆验证的地方

    public void SearchRequest() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("poi2","123");
                HttpURLConnection connection=null;
                BufferedReader reader=null;

                Log.i("1user",account);
                if (account.equals("")){
                    Log.i("poi3","123");
                    toast("请输入用户名");
                }else if (password.equals("")){
                    Log.i("poi4","123");
                    toast("请输入密码");
                }else{
                    Log.i("poi5","123");
                    try{
                        String url1 = "http://47.102.201.183:8080/test/SearchServlet?account="+account+"&password="+password;
                        //String tag = "Login";
                        URL url=new URL(url1);
                        connection=(HttpURLConnection)url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream in=connection.getInputStream();
                        //下面对获取到的输入流进行读取
                        reader=new BufferedReader(new InputStreamReader(in));
                        StringBuilder response=new StringBuilder();
                        String line;
                        while ((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        String result="";
                        result=IntentionJSON(response.toString());
                        Log.i("result",result);
                        //gatewayId.clear();
                        gatewayId2 = Arrays.asList(result.split(","));
                        if (gatewayId2.size()==0){
                            returndata2=1;//代表该用户下没有设备
                        }else if (gatewayId2.get(0).equals("")){
                            Log.i("tsize=1","test");
                            returndata2=1;//代表该用户下没有设备
                        }else{

                            for (int k=0;k<gatewayId2.size();k++){
                                Log.i("results",gatewayId2.get(k));
                            }
                            Mygatewayid.gatewayId=gatewayId2;//把ID放到全局变量中 在设置页面设置gateid的时候用
                            returndata2=2;//代表数据库gateID获取成功
                        }


                    }catch (Exception e){
                        toast("数据库连接失败，请联系我们");
                        Log.i("debugg",Log.getStackTraceString(e));
                        e.printStackTrace();
                    }finally {
                        if(reader!=null){
                            try {
                                reader.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        if (connection!=null){
                            connection.disconnect();
                        }
                    }
                }

            }
        }).start();
    }
    private void toast(final String toast){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyHistoryActivity.this,toast,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String IntentionJSON(String jsondata){
        try{
            Log.i("jsondata",jsondata);
            JSONObject js=new JSONObject(jsondata);
            String jsonObject=js.getJSONObject("1").toString();
            Log.i("3232:",jsonObject);
            JSONObject object=new JSONObject(jsonObject);
            String ob=object.optString("Intention");
            Log.i("ob",ob);
            return ob;

        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


    private Boolean jianchagateid(String id){
        for (int i=0;i<gatewayId2.size();i++){
            Log.i("gateideee",gatewayId2.get(i));
            Log.i("gateideeeid",id);
            if (id.equals(gatewayId2.get(i))){
                Log.i("gateideeetrue","test");
                return true;
            }
        }
        return false;
    }






}
