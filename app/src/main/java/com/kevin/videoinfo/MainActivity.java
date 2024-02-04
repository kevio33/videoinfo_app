package com.kevin.videoinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kevin.videoinfo.Login.LoginActivity;
import com.kevin.videoinfo.Register.RegisterActivity;
import com.kevin.videoinfo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'videoinfo' library on application startup.
    static {
        System.loadLibrary("videoinfo");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method

        Click();//点击事件

    }


    private void Click(){
        //登录按钮点击事件
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //注册按钮点击事件
        binding.regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * A native method that is implemented by the 'videoinfo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}