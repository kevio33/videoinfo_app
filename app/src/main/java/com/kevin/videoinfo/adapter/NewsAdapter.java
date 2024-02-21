package com.kevin.videoinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kevin.videoinfo.R;
import com.kevin.videoinfo.entity.NewsEntity;

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

        if(viewType == 1){
            NewsOneHolder newsOneHolder = (NewsOneHolder) holder;
        }else if(viewType ==2){
            NewsTwoHolder newsTwoHolder = (NewsTwoHolder) holder;
        }else{
            NewsThreeHolder newsThreeHolder = (NewsThreeHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return newsEntityList.size();
    }

    public class NewsOneHolder extends RecyclerView.ViewHolder{


        public NewsOneHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class NewsTwoHolder extends RecyclerView.ViewHolder{

        public NewsTwoHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public class NewsThreeHolder extends RecyclerView.ViewHolder{

        public NewsThreeHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
