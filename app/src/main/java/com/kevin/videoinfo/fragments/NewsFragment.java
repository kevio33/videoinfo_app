package com.kevin.videoinfo.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevin.videoinfo.R;
import com.kevin.videoinfo.adapter.NewsAdapter;
import com.kevin.videoinfo.entity.NewsEntity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private RefreshLayout refreshLayout;

    private List<NewsEntity> datas = new ArrayList<>();//用来存放请求结果数据

    private NewsAdapter newsAdapter;

    protected LinearLayoutManager mLinearLayoutManager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    public NewsFragment() {
        // Required empty public constructor
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

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = view.findViewById(R.id.recyler_view);

        //下拉刷新控件
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //为RecyclerView添加布局管理器
        mLinearLayoutManager = new LinearLayoutManager(getActivity());//获取这个frag的父类(activity)
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        newsAdapter= new NewsAdapter(getActivity());
//        newsAdapter.setOnItemChildClickListener(this);

        for(int i = 0;i<15;i++){
            int type = i% 3;
            NewsEntity news = new NewsEntity();
            news.setType(type);
            datas.add(news);
        }

        newsAdapter.setNewsEntityList(datas);
        recyclerView.setAdapter(newsAdapter);

//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
////                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
////                pageNum = 1;
//                getNewsOnInternet(true);//进行上拉刷新
//
//            }
//        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
////                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
////                pageNum++;
//                getNewsOnInternet(false);//下拉更新
//            }
//        });
//        getNewsOnInternet(true);
    }
//    private void getNewsOnInternet(boolean isFresh){
//        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit", MODE_PRIVATE);
//        String token = sp.getString("token", "");
//        HashMap<String,Object> params = new HashMap<>();
//        params.put("token",token);
////        params.put("page",pageNum);
////        params.put("limit",5);
////        params.put("categoryId",categoryId);//视频标签
//        HttpRequest.config(ConfigUtils.VIDEO_LIST_BY_CATEGORY,params).getRequest(getActivity(), new TtitCallback() {
//
//            @Override
//            public void onSuccess(String res) {
//                //TODO:主线程更新换为handler
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        VideoListResponse response = new Gson().fromJson(res,VideoListResponse.class);
//                        if(response!=null && response.getCode()==0){
//                            List<VideoEntity> list = response.getPage().getList();
//
//                            if(list!=null && !list.isEmpty()){
//                                if(isFresh){
//                                    //上拉刷新
//                                    refreshLayout.finishRefresh(true);
//                                    datas = list;
//                                }else{
//                                    //下拉更新
//                                    refreshLayout.finishLoadMore(true);
//                                    datas.addAll(list);
//                                }
//
//                                videoFragAdapter.setVideoEntityList(datas);
//                                videoFragAdapter.notifyDataSetChanged();
//                            }else{
//                                //暂时新没有数据
//                                if(isFresh){
//                                    //上拉刷新
//                                    refreshLayout.finishRefresh(true);
//                                }else{
//                                    //下拉更新
//                                    refreshLayout.finishLoadMore(true);
//                                }
//                                ToastUtil.showToast(getActivity(),"暂无更多数据");
//                            }
//
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(isFresh){
//                            //上拉刷新
//                            refreshLayout.finishRefresh(true);
//                        }else{
//                            //下拉更新
//                            refreshLayout.finishLoadMore(true);
//                        }
//                    }
//                });
//
//            }
//        });
//    }


}