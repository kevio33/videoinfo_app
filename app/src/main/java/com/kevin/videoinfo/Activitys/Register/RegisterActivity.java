package com.kevin.videoinfo.Activitys.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kevin.videoinfo.DBhelper.DBOpenHelper;
import com.kevin.videoinfo.Activitys.Login.LoginActivity;
import com.kevin.videoinfo.Utils.ConfigUtils;
import com.kevin.videoinfo.Utils.HttpRequest;
import com.kevin.videoinfo.Utils.ToastUtil;
import com.kevin.videoinfo.Utils.TtitCallback;
import com.kevin.videoinfo.Utils.UserInfoVlidate;
import com.kevin.videoinfo.databinding.ActivityRegisterBinding;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding activityRegisterBinding;

    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());

//        dbOpenHelper = DBOpenHelper.getDBOpenHelper(getApplicationContext(),"user.db",null,1);
//        sqLiteDatabase = dbOpenHelper.getWritableDatabase();

        context = this;
        Click();

    }

    private void Click() {
        activityRegisterBinding.regisBtn.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                String mobile = activityRegisterBinding.regisEdit.getText().toString().trim();
                String pwd = activityRegisterBinding.regisPwdedit.getText().toString().trim();

                boolean isVal = UserInfoVlidate.isValidate(getApplicationContext(),mobile,pwd);//验证输入的合法性
                if(isVal){
//                    //因为没有后台服务器，所有使用sqllite实现
//                    //创建游标对象
//                    Cursor cursor = sqLiteDatabase.query("user", new String[]{"id","username"}, "username=? and password =?", new String[]{username,pwd}, null, null, null);
//                    //利用游标遍历所有数据对象
//                    String id = "-1";
//                    while(cursor.moveToNext()){
//                        id = cursor.getString(cursor.getColumnIndex("id"));
//                        if(!id.equals("-1")){
//                            break;
//                        }
//                    }
//                    // 关闭游标，释放资源
//                    cursor.close();
//                    if(id.equals("-1")){
//                        //创建存放数据的contentvalue对象
//                        ContentValues values = new ContentValues();
//                        values.put("username",username);
//                        values.put("password",pwd);
//                        sqLiteDatabase.insert("user",null,values);
//                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(getApplicationContext(),"用户已存在",Toast.LENGTH_SHORT).show();
//                    }
                    /**
                     * 使用本地部署的后端代码作为请求服务器接口
                     */
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("mobile",mobile);
                    params.put("password",pwd);
                    HttpRequest.config(ConfigUtils.REGISTER,params).postRequest(context, new TtitCallback() {
                        @Override
                        public void onSuccess(String res) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   ToastUtil.showToast(context,"注册成功");
                                }
                            });
                            //注册成功就跳转到登录界面
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }
            }
        });
    }


}