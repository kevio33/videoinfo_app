package com.kevin.videoinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kevin.videoinfo.R;
import com.kevin.videoinfo.entity.VideoEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * video_frag中的recyclerview的adapter
 */
public class VideoFragAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoEntity> videoEntityList;

    private Context context;

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
        ((MyViewHolder) holder).title.setText(videoEntity.getVtitle());
        ((MyViewHolder) holder).author.setText(videoEntity.getAuthor());

        if (videoEntity.getVideoSocialEntity() != null) {
            ((MyViewHolder) holder).collect.setText(""+videoEntity.getVideoSocialEntity().getCollectnum());
            ((MyViewHolder) holder).dz.setText(""+videoEntity.getVideoSocialEntity().getLikenum());
            ((MyViewHolder) holder).comment.setText(""+videoEntity.getVideoSocialEntity().getCommentnum());
        }

        Picasso.with(context).load(R.mipmap.header).into(((MyViewHolder) holder).imageHeader);//返回的图片链接访问不了，统一使用本地的图片
    }

    @Override
    public int getItemCount() {
        return videoEntityList.size();
    }


    //缓存子项视图的持有者
    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView collect;
        private TextView dz;
        private ImageView imageHeader;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comment);
            collect = itemView.findViewById(R.id.collect);
            dz = itemView.findViewById(R.id.dz);
            imageHeader = itemView.findViewById(R.id.img_header);
        }
    }
}
