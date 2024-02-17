package com.kevin.videoinfo.Activitys.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kevin.videoinfo.Activitys.Home.HomePageActivity;
import com.kevin.videoinfo.DBhelper.DBOpenHelper;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.Utils.ConfigUtils;
import com.kevin.videoinfo.Utils.HttpRequest;
import com.kevin.videoinfo.Utils.ToastUtil;
import com.kevin.videoinfo.Utils.TtitCallback;
import com.kevin.videoinfo.Utils.UserInfoVlidate;
import com.kevin.videoinfo.databinding.ActivityLoginBinding;
import com.kevin.videoinfo.entity.LoginResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
//        dbOpenHelper = DBOpenHelper.getDBOpenHelper(getApplicationContext(),"user.db",null,1);
//
//        sqLiteDatabase = dbOpenHelper.getReadableDatabase();

        context = this;
        Click();
    }


    private void Click(){
        activityLoginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                String username = activityLoginBinding.loginEdit.getText().toString().trim();
                String pwd = activityLoginBinding.loginPwdedit.getText().toString().trim();

                boolean isVal = UserInfoVlidate.isValidate(getApplicationContext(),username,pwd);//验证输入的合法性

                if(isVal){
                    //使用sqllite本地存储用户信息
                    //因为没有后台服务器，所有使用sqllite实现
                    //创建游标对象
//                    Cursor cursor = sqLiteDatabase.query("user", new String[]{"id","username"}, "username=? and password =?", new String[]{username,pwd}, null, null, null);
//                    //利用游标遍历所有数据对象
//                    String id = "-1";
//                    while(cursor.moveToNext()){
//                        id = cursor.getString(cursor.getColumnIndex("id"));
//                        @SuppressLint("Range") String useracoount = cursor.getString(cursor.getColumnIndex("username"));
//                        Log.i("Loginactivity","result: id="  + id +" username: " + useracoount );
//                    }
//                    // 关闭游标，释放资源
//                    cursor.close();
//                    if(!id.equals("-1")){
//                        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(getApplicationContext(),"用户信息错误",Toast.LENGTH_SHORT).show();
//                    }
                    /**
                     * 使用封装的okhttp请求数据
                     */
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("mobile", username);
                    params.put("password", pwd);
                    HttpRequest.config(ConfigUtils.LOGIN,params).postRequest(context, new TtitCallback() {
                        @Override
                        public void onSuccess(String res) {
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                            if (loginResponse.getCode() == 0) {
                                String token = loginResponse.getToken();
                                insertVal("token", token);//保存token，他这里是一个设备一个token
                                Intent intent = new Intent(context, HomePageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(context,"未查询到用户");
                                }
                            });

                        }
                    });


                }
            }
        });
    }

    //保存返回的token，保存到sharedpreference
    protected void insertVal(String key, String val) {
        SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

}