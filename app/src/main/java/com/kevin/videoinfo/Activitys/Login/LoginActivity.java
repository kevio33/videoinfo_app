package com.kevin.videoinfo.Activitys.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kevin.videoinfo.Activitys.Home.HomePageActivity;
import com.kevin.videoinfo.DBhelper.DBOpenHelper;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.Utils.ConfigUtils;
import com.kevin.videoinfo.Utils.UserInfoVlidate;
import com.kevin.videoinfo.databinding.ActivityLoginBinding;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        dbOpenHelper = DBOpenHelper.getDBOpenHelper(getApplicationContext(),"user.db",null,1);

        sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        Click();
    }


    private void Click(){
        activityLoginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                String username = activityLoginBinding.loginEdit.getText().toString().trim();
                String pwd = activityLoginBinding.loginPwdedit.getText().toString().trim();

                boolean isVal = UserInfoVlidate.isValidate(getApplicationContext(),username,pwd);//验证输入的合法性

                if(isVal){
                    //因为没有后台服务器，所有使用sqllite实现
                    //创建游标对象
                    Cursor cursor = sqLiteDatabase.query("user", new String[]{"id","username"}, "username=? and password =?", new String[]{username,pwd}, null, null, null);
                    //利用游标遍历所有数据对象
                    String id = "-1";
                    while(cursor.moveToNext()){
                        id = cursor.getString(cursor.getColumnIndex("id"));
                        @SuppressLint("Range") String useracoount = cursor.getString(cursor.getColumnIndex("username"));
                        Log.i("Loginactivity","result: id="  + id +" username: " + useracoount );
                    }
                    // 关闭游标，释放资源
                    cursor.close();
                    if(!id.equals("-1")){
                        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"用户信息错误",Toast.LENGTH_SHORT).show();
                    }


                    //发起请求
//                    OkHttpClient okHttp = new OkHttpClient.Builder().build();
//                    Map m = new HashMap();
//                    m.put("mobile",username);
//                    m.put("password",pwd);
//                    JSONObject jsonObject = new JSONObject(m);
//                    String jsonStr = jsonObject.toString();
//                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
//                            jsonStr);
//                    Request request = new Request.Builder()
//                            .url(ConfigUtils.BASE_URL+"app/login")
//                            .addHeader("contentType","application/json;charset=utf-8")
//                            .post(requestBody)
//                            .build();
//
//                    final Call call = okHttp.newCall(request);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                            Log.e("onFailure", Objects.requireNonNull(e.getMessage()));
//                        }
//
//                        @Override
//                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                            final String result = response.body().string();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    });


                }
            }
        });
    }

    //验证输入的合法性
//    private boolean isValidate(String username, String pwd) {
//
//        if(username ==null || username.length()==0){
//            Toast.makeText(getApplicationContext(),"请输入用户名",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if(pwd ==null || pwd.length() ==0){
//            Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        return true;
//    }
}