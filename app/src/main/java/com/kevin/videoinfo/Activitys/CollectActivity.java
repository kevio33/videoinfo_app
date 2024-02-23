package com.kevin.videoinfo.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.kevin.videoinfo.Listener.OnItemChildClickListener;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.Utils.ConfigUtils;
import com.kevin.videoinfo.Utils.HttpRequest;
import com.kevin.videoinfo.Utils.Tag;
import com.kevin.videoinfo.Utils.TtitCallback;
import com.kevin.videoinfo.Utils.Utils;
import com.kevin.videoinfo.adapter.CollectAdapter;
import com.kevin.videoinfo.adapter.VideoFragAdapter;
import com.kevin.videoinfo.databinding.ActivityCollectBinding;
import com.kevin.videoinfo.entity.MyCollectResponse;
import com.kevin.videoinfo.entity.VideoEntity;

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
import xyz.doikki.videoplayer.player.VideoView;
import xyz.doikki.videoplayer.player.VideoViewManager;

public class CollectActivity extends AppCompatActivity implements OnItemChildClickListener {


    private ActivityCollectBinding activityCollectBinding;

//    private RecyclerView recyclerView;

    private List<VideoEntity> datas = new ArrayList<>();//用来存放请求结果数据

    protected LinearLayoutManager mLinearLayoutManager;

    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;

    private CollectAdapter collectAdapter;

    private VideoView videoView;

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityCollectBinding = ActivityCollectBinding.inflate(getLayoutInflater());
        setContentView(activityCollectBinding.getRoot());


        initView();
        initVideo();

        getVideoList();
    }

    private void initView() {

        mLinearLayoutManager = new LinearLayoutManager(this);//获取这个frag的父类(activity)
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityCollectBinding.recyclerView.setLayoutManager(mLinearLayoutManager);
        collectAdapter= new CollectAdapter(this);
        collectAdapter.setOnItemChildClickListener(this);
        activityCollectBinding.recyclerView.setAdapter(collectAdapter);
        activityCollectBinding.recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == v && !videoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });
    }

    private void initVideo() {
        videoView = new VideoView(this);
        videoView.setOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(videoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(this);
        mErrorView = new ErrorView(this);
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(this);
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(this);
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(this));
        mController.addControlComponent(new GestureView(this));
        mController.setEnableOrientation(true);
        videoView.setVideoController(mController);
    }

    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
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
        AssetManager am = getAssets();
        try {
            AssetFileDescriptor afd = am.openFd("1.mp4");// 注意这里的区别

            videoView.setAssetFileDescriptor(afd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = mLinearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoFragAdapter.MyViewHolder viewHolder = (VideoFragAdapter.MyViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isDissociate此处只能为true, 请点进去看isDissociate的解释
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(videoView);
        viewHolder.playerConatainer.addView(videoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(videoView, Tag.LIST);
        videoView.start();
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
    private void releaseVideoView() {
        videoView.release();
        if (videoView.isFullScreen()) {
            videoView.stopFullScreen();
        }
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }

    private void getVideoList() {
        HashMap<String, Object> params = new HashMap<>();
        HttpRequest.config(ConfigUtils.VIDEO_MYCOLLECT, params).getRequest(this, new TtitCallback() {

            @Override
            public void onSuccess(final String res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyCollectResponse response = new Gson().fromJson(res, MyCollectResponse.class);
                        if (response != null && response.getCode() == 0) {
                            List<VideoEntity> list = response.getList();
                            if (list != null && list.size() > 0) {
                                datas = list;
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }


}