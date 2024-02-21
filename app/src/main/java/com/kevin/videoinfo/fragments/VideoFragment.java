package com.kevin.videoinfo.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.google.gson.Gson;
import com.kevin.videoinfo.Listener.OnItemChildClickListener;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.Utils.ConfigUtils;
import com.kevin.videoinfo.Utils.HttpRequest;
import com.kevin.videoinfo.Utils.Tag;
import com.kevin.videoinfo.Utils.ToastUtil;
import com.kevin.videoinfo.Utils.TtitCallback;
import com.kevin.videoinfo.Utils.Utils;
import com.kevin.videoinfo.adapter.VideoFragAdapter;
import com.kevin.videoinfo.entity.VideoEntity;
import com.kevin.videoinfo.entity.VideoListResponse;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.GestureView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.ijk.RawDataSourceProvider;
import xyz.doikki.videoplayer.player.VideoView;
import xyz.doikki.videoplayer.player.VideoViewManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment implements OnItemChildClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String title;

    private int categoryId;

//    private FragmentVideoBinding fragmentVideoBinding;


    private RecyclerView recyclerView;

    private RefreshLayout refreshLayout;

    private int pageNum = 1;

    private List<VideoEntity> datas = new ArrayList<>();//用来存放请求结果数据

    private VideoFragAdapter videoFragAdapter;

    private VideoView mVideoView;

    protected LinearLayoutManager mLinearLayoutManager;

    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(int categoryId) {
        VideoFragment fragment = new VideoFragment();

        fragment.categoryId = categoryId;//通过categoryid加载不同类别视频
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
//        fragmentVideoBinding = FragmentVideoBinding.inflate(inflater,container,false);
        recyclerView = v.findViewById(R.id.video_recyler_view);

        //下拉刷新控件
        refreshLayout = v.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //为RecyclerView添加布局管理器
        mLinearLayoutManager = new LinearLayoutManager(getActivity());//获取这个frag的父类(activity)
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        videoFragAdapter= new VideoFragAdapter(getActivity());
        videoFragAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(videoFragAdapter);

        /**
         * 设置，当第i个videoview滑出屏幕时候停止播放
         */
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });

        initVideoView();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageNum = 1;
                getVideoOnInternet(true);//进行上拉刷新

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageNum++;
                getVideoOnInternet(false);//下拉更新
            }
        });
        getVideoOnInternet(true);

    }


    /**
     * 获取视频信息
     * @param isFresh
     */
    private void getVideoOnInternet(boolean isFresh){
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit", MODE_PRIVATE);
        String token = sp.getString("token", "");
        HashMap<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("page",pageNum);
        params.put("limit",5);
        params.put("categoryId",categoryId);//视频标签
        HttpRequest.config(ConfigUtils.VIDEO_LIST_BY_CATEGORY,params).getRequest(getActivity(), new TtitCallback() {

            @Override
            public void onSuccess(String res) {
                //TODO:主线程更新换为handler
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        VideoListResponse  response = new Gson().fromJson(res,VideoListResponse.class);
                        if(response!=null && response.getCode()==0){
                            List<VideoEntity> list = response.getPage().getList();

                            if(list!=null && !list.isEmpty()){
                                if(isFresh){
                                    //上拉刷新
                                    refreshLayout.finishRefresh(true);
                                    datas = list;
                                }else{
                                    //下拉更新
                                    refreshLayout.finishLoadMore(true);
                                    datas.addAll(list);
                                }

                                videoFragAdapter.setVideoEntityList(datas);
                                videoFragAdapter.notifyDataSetChanged();
                            }else{
                                //暂时新没有数据
                                if(isFresh){
                                    //上拉刷新
                                    refreshLayout.finishRefresh(true);
                                }else{
                                    //下拉更新
                                    refreshLayout.finishLoadMore(true);
                                }
                                ToastUtil.showToast(getActivity(),"暂无更多数据");
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
                        if(isFresh){
                            //上拉刷新
                            refreshLayout.finishRefresh(true);
                        }else{
                            //下拉更新
                            refreshLayout.finishLoadMore(true);
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * 初始化videoView
     */
    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }


    /**
     * 开始播放
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = datas.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

//        mVideoView.setUrl(videoEntity.getPlayurl());//播放网络视频

        //播放assets的视频
        AssetManager am = getActivity().getAssets();
        try {
            AssetFileDescriptor afd = am.openFd("1.mp4");// 注意这里的区别

            mVideoView.setAssetFileDescriptor(afd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = mLinearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoFragAdapter.MyViewHolder viewHolder = (VideoFragAdapter.MyViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isDissociate此处只能为true, 请点进去看isDissociate的解释
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.playerConatainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    @Override
    public void onPause() {
        super.onPause();
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseVideoView();
    }

    /**
     * 释放正在播放的video
     */
    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if(getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }

}