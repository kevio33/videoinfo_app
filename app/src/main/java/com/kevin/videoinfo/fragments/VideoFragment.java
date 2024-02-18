package com.kevin.videoinfo.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.Utils.ConfigUtils;
import com.kevin.videoinfo.Utils.HttpRequest;
import com.kevin.videoinfo.Utils.TtitCallback;
import com.kevin.videoinfo.adapter.VideoFragAdapter;
import com.kevin.videoinfo.databinding.FragmentVideoBinding;
import com.kevin.videoinfo.entity.VideoEntity;
import com.kevin.videoinfo.entity.VideoListResponse;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String title;

    private FragmentVideoBinding fragmentVideoBinding;


    private RecyclerView recyclerView;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1) {
        VideoFragment fragment = new VideoFragment();

        fragment.title = param1;
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
        View v = inflater.inflate(R.layout.fragment_video,container,false);
        // Inflate the layout for this fragment
        fragmentVideoBinding = FragmentVideoBinding.inflate(inflater,container,false);
        recyclerView = v.findViewById(R.id.video_recyler_view);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //为RecyclerView添加布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//获取这个frag的父类(activity)
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        getVideoOnInternet();

    }

    private void getVideoOnInternet(){
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit", MODE_PRIVATE);
        String token = sp.getString("token", "");
        HashMap<String,Object> params = new HashMap<>();
        params.put("token",token);

        HttpRequest.config(ConfigUtils.VIDEO_LIST_ALL,params).getRequest(getActivity(), new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Log.i("VideoFrag",res);

                VideoListResponse  response = new Gson().fromJson(res,VideoListResponse.class);
                if(response!=null && response.getCode()==0){
                    List<VideoEntity> list = response.getPage().getList();
                    VideoFragAdapter videoFragAdapter = new VideoFragAdapter(getActivity(),list);
                    recyclerView.setAdapter(videoFragAdapter);
                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}