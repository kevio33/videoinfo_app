package com.kevin.videoinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kevin.videoinfo.Listener.OnItemChildClickListener;
import com.kevin.videoinfo.Listener.OnItemClickListener;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.entity.VideoEntity;
import com.kevin.videoinfo.view.CircleTransform;
import com.squareup.picasso.Picasso;

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

//        if (videoEntity.getVideoSocialEntity() != null) {
            myViewHolder.collect.setText(""+videoEntity.getCollectNum());
            myViewHolder.dz.setText(""+videoEntity.getLikeNum());
                myViewHolder.comment.setText(""+videoEntity.getCommentNum());
//        }

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


        public FrameLayout playerConatainer;

        public int position;

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
//            mThumb = mPrepareView.findViewById(R.id.thumb);


            if(onItemChildClickListener !=null){
                playerConatainer.setOnClickListener(this);
            }

            if(mOnItemClickListener != null){
                itemView.setOnClickListener(this);
            }

            //通过tag将ViewHolder和itemView绑定
            itemView.setTag(this);
        }

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

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
