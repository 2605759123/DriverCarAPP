package com.mcz.temperarure_humidity_appproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mcz.temperarure_humidity_appproject.Mydata.Mygatewayid;

import java.util.ArrayList;
import java.util.List;

public class MySettingActivity extends AppCompatActivity {
    private List<MySettingdata> settingList=new ArrayList<>();
    Button back;
    SharedPreferences sp3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        back=(Button)findViewById(R.id.setting_back);
        initMySettingdata();//chu shi hua
        sp3 = PreferenceManager.getDefaultSharedPreferences(this);
        final MyAdapter3 adapter=new MyAdapter3(MySettingActivity.this,R.layout.mydata3,settingList);
        final ListView listView=(ListView)findViewById(R.id.list_view3);
        listView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final MySettingdata z=settingList.get(position);
//                Toast.makeText(MySettingActivity.this,z.getName(),Toast.LENGTH_SHORT).show();
                if (z.getName()=="修改关联设备ID"){
                    final Spinner inputServer = new Spinner(MySettingActivity.this);//这是下拉选择框
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i<Mygatewayid.gatewayId.size(); i++){
                        list.add(Mygatewayid.gatewayId.get(i));
                    }
                    String [] data=list.toArray(new String[0]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MySettingActivity.this, android.R.layout.simple_dropdown_item_1line,data);
                    inputServer.setAdapter(adapter);

                }
                if (z.getName()=="修改紧急联络号码"){
                    final EditText inputServer = new EditText(MySettingActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MySettingActivity.this);
                    inputServer.setText(sp3.getString("sosphone",""));
                    builder.setTitle("请修改紧急联络号码").setIcon(R.drawable.ic_phone_forwarded_black_24dp).setView(inputServer)
                            .setNegativeButton("Cancel", null);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            sp3.edit().putString("sosphone",inputServer.getText().toString()).commit();
                            Toast.makeText(MySettingActivity.this,"修改成功"+inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }


            }
        });
    }





    private void initMySettingdata() {
        settingList.add(new MySettingdata("修改关联设备ID",R.drawable.ic_all_inclusive_black_24dp));
        settingList.add(new MySettingdata("修改紧急联络号码",R.drawable.ic_phone_forwarded_black_24dp));
    }
}
