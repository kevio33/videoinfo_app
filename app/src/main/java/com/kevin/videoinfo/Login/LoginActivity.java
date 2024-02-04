package com.kevin.videoinfo.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kevin.videoinfo.R;
import com.kevin.videoinfo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        Click();
    }


    private void Click(){
        activityLoginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = activityLoginBinding.loginEdit.getText().toString().trim();
                String pwd = activityLoginBinding.loginPwdedit.getText().toString().trim();

                boolean isVal = isValidate(username,pwd);//验证输入的合法性

                if(isVal){
                    Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //验证输入的合法性
    private boolean isValidate(String username, String pwd) {

        if(username ==null || username.length()==0){
            Toast.makeText(getApplicationContext(),"请输入用户名",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd ==null || pwd.length() ==0){
            Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}