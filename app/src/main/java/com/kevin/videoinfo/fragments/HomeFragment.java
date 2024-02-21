package com.kevin.videoinfo.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.Utils.ConfigUtils;
import com.kevin.videoinfo.Utils.HttpRequest;
import com.kevin.videoinfo.Utils.StringUtils;
import com.kevin.videoinfo.Utils.ToastUtil;
import com.kevin.videoinfo.Utils.TtitCallback;
import com.kevin.videoinfo.adapter.HomeFragPageAdapter;
import com.kevin.videoinfo.adapter.MyPagerAdapter;
import com.kevin.videoinfo.entity.CategoryEntity;
import com.kevin.videoinfo.entity.VideoCategoryResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private  String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = v.findViewById(R.id.homefrag_vp);
        slidingTabLayout = v.findViewById(R.id.homefrag_tl_1);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getVideoCategory();
    }

    /**
     * 请求视频标签
     */
    public void getVideoCategory(){
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit", MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (!StringUtils.isEmpty(token)){
            HashMap<String,Object> params = new HashMap<>();
            params.put("token",token);
            Log.i("HomeFrag",token);
            HttpRequest.config(ConfigUtils.VIDEO_CATEGORY_LIST,params).getRequest(getActivity(), new TtitCallback() {
                @Override
                public void onSuccess(String res) {

                    //TODO:更新主线程使用Handler
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VideoCategoryResponse response = new Gson().fromJson(res,VideoCategoryResponse.class);
                            if(response!=null && response.getCode() == 0){
                                Log.i("HomeFrag","请求成功:"+res);
                                List<CategoryEntity> list = response.getPage().getList();
                                if (list!=null && !list.isEmpty()){
                                    mTitles = new String[list.size()];
                                    for(int i = 0;i<list.size();i++){
                                        mTitles[i] = list.get(i).getCategoryName();//将类别赋值给字符串数组
                                        mFragments.add(VideoFragment.newInstance(list.get(i).getCategoryId()));//每一个tab对应的category
                                    }
                                    viewPager.setOffscreenPageLimit(mFragments.size());//预加载，不用切换的时候销毁frags
                                    viewPager.setAdapter(new HomeFragPageAdapter(getFragmentManager(),mFragments,mTitles));
                                    slidingTabLayout.setViewPager(viewPager);
                                }
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(getActivity(),"请求视频标签失败，请重试");
                        }
                    });
                }
            });
        }


    }




}