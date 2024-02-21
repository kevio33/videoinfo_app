package com.kevin.videoinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kevin.videoinfo.Listener.OnItemClickListener;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.entity.NewsEntity;
import com.kevin.videoinfo.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<NewsEntity> newsEntityList;

    private Context context;
    public List<NewsEntity> getNewsEntityList() {
        return newsEntityList;
    }

    public void setNewsEntityList(List<NewsEntity> newsEntities) {
        this.newsEntityList = newsEntities;
    }

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public NewsAdapter(List<NewsEntity> newsEntities, Context context) {
        this.newsEntityList = newsEntities;
        this.context = context;
    }


    private OnItemClickLis onItemClickLis;

    //view的点击事件
    public void setOnItemClickListener(OnItemClickLis onItemClickListener){
        this.onItemClickLis = onItemClickListener;
    }


    //返回数据对应的view类型标号
    @Override
    public int getItemViewType(int position){
        int viewType = newsEntityList.get(position).getType();
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //根据不同数据的view标号加载对应viewholder
        if(viewType == 1){
            return new NewsOneHolder(LayoutInflater.from(context).inflate(R.layout.news_item_one,parent,false));
        }else if(viewType == 2){
            return new NewsTwoHolder(LayoutInflater.from(context).inflate(R.layout.news_item_two,parent,false));
        }else{
            return new NewsThreeHolder(LayoutInflater.from(context).inflate(R.layout.news_item_three,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        NewsEntity news = newsEntityList.get(position);
        if(viewType == 1){
            NewsOneHolder newsOneHolder = (NewsOneHolder) holder;
            newsOneHolder.title.setText(news.getNewsTitle());
            newsOneHolder.author.setText(news.getAuthorName());
            newsOneHolder.comment.setText(news.getCommentCount() + "评论 .");
            newsOneHolder.time.setText(news.getReleaseDate());
            newsOneHolder.newsEntity = news;
            Picasso.with(context)
                    .load(news.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(newsOneHolder.header);

            Picasso.with(context)
                    .load(news.getThumbEntities().get(0).getThumbUrl())
                    .into(newsOneHolder.thumb);
        }else if(viewType ==2){
            NewsTwoHolder newsTwoHolder = (NewsTwoHolder) holder;
            newsTwoHolder.title.setText(news.getNewsTitle());
            newsTwoHolder.author.setText(news.getAuthorName());
            newsTwoHolder.comment.setText(news.getCommentCount() + "评论 .");
            newsTwoHolder.time.setText(news.getReleaseDate());
            newsTwoHolder.newsEntity = news;
            Picasso.with(context)
                    .load(news.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(newsTwoHolder.header);

            Picasso.with(context)
                    .load(news.getThumbEntities().get(0).getThumbUrl())
                    .into(newsTwoHolder.pic1);
            Picasso.with(context)
                    .load(news.getThumbEntities().get(1).getThumbUrl())
                    .into(newsTwoHolder.pic2);
            Picasso.with(context)
                    .load(news.getThumbEntities().get(2).getThumbUrl())
                    .into(newsTwoHolder.pic3);
        }else{
            NewsThreeHolder newsThreeHolder = (NewsThreeHolder) holder;

            newsThreeHolder.title.setText(news.getNewsTitle());
            newsThreeHolder.author.setText(news.getAuthorName());
            newsThreeHolder.comment.setText(news.getCommentCount() + "评论 .");
            newsThreeHolder.time.setText(news.getReleaseDate());
            newsThreeHolder.newsEntity = news;
            Picasso.with(context)
                    .load(news.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(newsThreeHolder.header);

            Picasso.with(context)
                    .load(news.getThumbEntities().get(0).getThumbUrl())
                    .into(newsThreeHolder.thumb);
        }
    }

    @Override
    public int getItemCount() {
        return newsEntityList.size();
    }

    public class NewsOneHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView thumb;
        private NewsEntity newsEntity;
        public NewsOneHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLis.onClick(newsEntity);
                }
            });
        }
    }

    public class NewsTwoHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView pic1, pic2, pic3;
        private NewsEntity newsEntity;

        public NewsTwoHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            pic1 = view.findViewById(R.id.pic1);
            pic2 = view.findViewById(R.id.pic2);
            pic3 = view.findViewById(R.id.pic3);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLis.onClick(newsEntity);
                }
            });

        }
    }


    public class NewsThreeHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView thumb;
        private NewsEntity newsEntity;


        public NewsThreeHolder(@NonNull View view) {
            super(view);

            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLis.onClick(newsEntity);
                }
            });
        }
    }


    public interface OnItemClickLis{
        public void onClick(Serializable obj);
    }

}
