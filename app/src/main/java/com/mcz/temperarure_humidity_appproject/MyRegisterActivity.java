package com.mcz.temperarure_humidity_appproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyRegisterActivity extends AppCompatActivity {

    EditText textusername;
    EditText textPassword;//数据库端
    TextView toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_register);

        Button button2=(Button)findViewById(R.id.mylogin2);
        textusername=(EditText)findViewById(R.id.username2);
        textPassword=(EditText)findViewById(R.id.password2);
        toLogin=(TextView)findViewById(R.id.SignIn2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    RegisterRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyRegisterActivity.this,MyActivity1.class);
                startActivity(intent);
            }
        });
    }

    public void RegisterRequest() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                String account=textusername.getText().toString();
                String password=textPassword.getText().toString();
                if (account.equals("")){
                    toast("请输入用户名");
                }else if (password.equals("")){
                    toast("请输入密码");
                }else{
                    try{
                        String url1 = "http://47.102.201.183:8080/test/RegisterServlet?account="+account+"&password="+password;
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
                        result=RegisterJSON(response.toString());
                        Log.i("result",result);
                        if (result.equals("")){
                            //数据库连接不上
                            toast("验证账号密码失败，请检查网络或联系我们");//一般不会这样，在catch处会捕获异常
                        }else if (result.equals("注册成功")){
                            //数据库验证注册成功
                            Log.i("poi6","123");
                            toast("注册成功，快去登陆吧~");
                        }else if(result.equals("账号已存在")){
                            //数据库验证账号已经存在
                            toast("此账号已经被注册了，换一个试试吧~");
                        }
                    }catch (Exception e){
                        toast("验证账号密码失败，请检查网络或联系我们");
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
                Toast.makeText(MyRegisterActivity.this,toast,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String RegisterJSON(String jsondata) {
        try {
            Log.i("jsondata", jsondata);
            JSONObject js = new JSONObject(jsondata);
            String jsonObject = js.getJSONObject("结果").toString();
            Log.i("3232:", jsonObject);
            JSONObject object = new JSONObject(jsonObject);
            String ob = object.optString("Result");
            Log.i("ob", ob);
            return ob;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
