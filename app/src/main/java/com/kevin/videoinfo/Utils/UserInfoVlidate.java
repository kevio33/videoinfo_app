package com.kevin.videoinfo.Utils;

import android.content.Context;
import android.widget.Toast;

public class UserInfoVlidate {
    public static boolean isValidate(Context context,String username, String pwd) {

        if(username ==null || username.length()==0){
            Toast.makeText(context,"请输入用户名",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd ==null || pwd.length() ==0){
            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
