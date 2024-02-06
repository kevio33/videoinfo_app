package com.kevin.videoinfo.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kevin.videoinfo.DBhelper.DBOpenHelper;
import com.kevin.videoinfo.Login.LoginActivity;
import com.kevin.videoinfo.MainActivity;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.Utils.UserInfoVlidate;
import com.kevin.videoinfo.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding activityRegisterBinding;

    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());

        dbOpenHelper = DBOpenHelper.getDBOpenHelper(getApplicationContext(),"user.db",null,1);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        Click();

    }

    private void Click() {

        activityRegisterBinding.regisBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                String username = activityRegisterBinding.regisEdit.getText().toString().trim();
                String pwd = activityRegisterBinding.regisPwdedit.getText().toString().trim();

                boolean isVal = UserInfoVlidate.isValidate(getApplicationContext(),username,pwd);//验证输入的合法性
                if(isVal){
                    //因为没有后台服务器，所有使用sqllite实现
                    //创建游标对象
                    Cursor cursor = sqLiteDatabase.query("user", new String[]{"id","username"}, "username=? and password =?", new String[]{username,pwd}, null, null, null);
                    //利用游标遍历所有数据对象
                    String id = "-1";
                    while(cursor.moveToNext()){
                        id = cursor.getString(cursor.getColumnIndex("id"));
//                        @SuppressLint("Range") String useracoount = cursor.getString(cursor.getColumnIndex("username"));
//                        @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
//                        Log.i("Registeractivity","result: id="  + id +" username: " + useracoount +"  password:" + password);
                        if(!id.equals("-1")){
                            break;
                        }
                    }
                    // 关闭游标，释放资源
                    cursor.close();
                    if(id.equals("-1")){
                        //创建存放数据的contentvalue对象
                        ContentValues values = new ContentValues();
                        values.put("username",username);
                        values.put("password",pwd);
                        sqLiteDatabase.insert("user",null,values);
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"用户已存在",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}