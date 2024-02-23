package com.kevin.videoinfo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kevin.videoinfo.Listener.OnItemChildClickListener;
import com.kevin.videoinfo.Listener.OnItemClickListener;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.Utils.ConfigUtils;
import com.kevin.videoinfo.Utils.HttpRequest;
import com.kevin.videoinfo.Utils.TtitCallback;
import com.kevin.videoinfo.entity.BaseResponse;
import com.kevin.videoinfo.entity.VideoEntity;
import com.kevin.videoinfo.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import xyz.doikki.videocontroller.component.PrepareView;

/**
 * video_frag中的recyclerview的adapter
 */
public class VideoFragAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<VideoEntity> getVideoEntityList() {
        return videoEntityList;
    }

    public void setVideoEntityList(List<VideoEntity> videoEntityList) {
        this.videoEntityList = videoEntityList;
    }

    private OnItemChildClickListener onItemChildClickListener;

    private OnItemClickListener mOnItemClickListener;


    private List<VideoEntity> videoEntityList;

    private Context context;

    public VideoFragAdapter(Context context){
        this.context = context;
    }

    public VideoFragAdapter(@NonNull Context context,List<VideoEntity> videoEntityList){
        this.context = context;
        this.videoEntityList = videoEntityList;
    }


    /**
     * 创建试图并返回viewholder
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video_layout,parent,false));
    }

    /**
     * 绑定试图和数据
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        VideoEntity videoEntity = videoEntityList.get(position);
        myViewHolder.title.setText(videoEntity.getVtitle());
        myViewHolder.author.setText(videoEntity.getAuthor());

        myViewHolder.collect.setText(""+videoEntity.getCollectNum());
        myViewHolder.dz.setText(""+videoEntity.getLikeNum());
        myViewHolder.comment.setText(""+videoEntity.getCommentNum());

        boolean flagLike = videoEntity.isFlagLike();
        boolean flagCollect = videoEntity.isFlagCollect();
        if (flagLike) {
            myViewHolder.dz.setTextColor(Color.parseColor("#E21918"));
            myViewHolder.imgDizan.setImageResource(R.mipmap.dianzan_select);
        }
        if (flagCollect) {
            myViewHolder.collect.setTextColor(Color.parseColor("#E21918"));
            myViewHolder.imgCollect.setImageResource(R.mipmap.collect_select);
        }
        myViewHolder.flagCollect = flagCollect;
        myViewHolder.flagLike = flagLike;

        myViewHolder.position = position;

        Picasso.with(context).load(R.mipmap.header).transform(new CircleTransform()).into(((MyViewHolder) holder).imageHeader);//返回的图片链接访问不了，统一使用本地的图片
    }

    @Override
    public int getItemCount() {

        if(videoEntityList!=null && !videoEntityList.isEmpty()){
            return videoEntityList.size();
        }

        return 0;
    }


    //缓存子项视图的持有者
    //不是静态很有可能内存泄漏啊
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView collect;
        private TextView dz;
        private ImageView imageHeader;

        public PrepareView mPrepareView;

        public ImageView mThumb;//视频封面

        private ImageView imgCollect;


        public FrameLayout playerConatainer;

        public int position;

        private boolean flagCollect;

        private boolean flagLike;

        private ImageView imgDizan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comment);
            collect = itemView.findViewById(R.id.collect);
            dz = itemView.findViewById(R.id.dz);
            imageHeader = itemView.findViewById(R.id.img_header);
            playerConatainer = itemView.findViewById(R.id.player_container);
            mPrepareView = itemView.findViewById(R.id.prepare_view);
            imgCollect = itemView.findViewById(R.id.img_collect);
            imgDizan = itemView.findViewById(R.id.img_like);

            imgCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int collectNum = Integer.parseInt(collect.getText().toString());
                    if (flagCollect) { //已收藏
                        if (collectNum > 0) {
                            collect.setText(String.valueOf(--collectNum));
                            collect.setTextColor(Color.parseColor("#161616"));
                            imgCollect.setImageResource(R.mipmap.collect);
                            updateCount(videoEntityList.get(position).getVid(), 1, !flagCollect);
                        }
                    } else {//未收藏
                        collect.setText(String.valueOf(++collectNum));
                        collect.setTextColor(Color.parseColor("#E21918"));
                        imgCollect.setImageResource(R.mipmap.collect_select);
                        updateCount(videoEntityList.get(position).getVid(), 1, !flagCollect);
                    }
                    flagCollect = !flagCollect;
                }
            });
            imgDizan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int likeNum = Integer.parseInt(dz.getText().toString());
                    if (flagLike) { //已点赞
                        if (likeNum > 0) {
                            dz.setText(String.valueOf(--likeNum));
                            dz.setTextColor(Color.parseColor("#161616"));
                            imgDizan.setImageResource(R.mipmap.dianzan);
                            updateCount(videoEntityList.get(position).getVid(), 2, !flagLike);
                        }
                    } else {//未点赞
                        dz.setText(String.valueOf(++likeNum));
                        dz.setTextColor(Color.parseColor("#E21918"));
                        imgDizan.setImageResource(R.mipmap.dianzan_select);
                        updateCount(videoEntityList.get(position).getVid(), 2, !flagLike);
                    }
                    flagLike = !flagLike;
                }
            });

            if(onItemChildClickListener !=null){
                playerConatainer.setOnClickListener(this);
            }

            if(mOnItemClickListener != null){
                itemView.setOnClickListener(this);
            }

            //通过tag将ViewHolder和itemView绑定
            itemView.setTag(this);
        }



        //视频播放软件的功能
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.player_container){
                if(onItemChildClickListener!=null){
                    onItemChildClickListener.onItemChildClick(position);
                }
            }else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        }
    }



    //点赞收藏进行更新
    private void updateCount(int vid, int type, boolean flag) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("vid", vid);
        params.put("type", type);
        params.put("flag", flag);
        HttpRequest.config(ConfigUtils.VIDEO_UPDATE_COUNT, params).postRequest(context, new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(res, BaseResponse.class);
                if (baseResponse.getCode() == 0) {

                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
