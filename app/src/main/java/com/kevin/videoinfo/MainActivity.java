package com.kevin.videoinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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

    }

    /**
     * A native method that is implemented by the 'videoinfo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}