package com.example.administrator.recyclerviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.administrator.recyclerviewdemo.adapter.NewsAdapter;
import com.example.administrator.recyclerviewdemo.bean.NewsBean;
import com.example.administrator.recyclerviewdemo.view.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ObservableScrollView mScrollView;
    private RecyclerView         mRecyclerView;
    private SwipeRefreshLayout   mSwipeRefreshLayout;
    private Handler handler = new Handler();
    boolean isLoading = false;
    private List<NewsBean> mNewsData = new ArrayList<NewsBean>();
    private NewsAdapter          mAdapter;
    private LinearLayoutManager  mLayoutManger;
    private ObservableScrollView mStikcyView;
    private float                currentScrollX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mStikcyView = (ObservableScrollView) findViewById(R.id.view_header_copy);

    }

    private void initData() {
        mLayoutManger = new LinearLayoutManager(this);
//        mLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManger);
        getDataForNet();
        mAdapter = new NewsAdapter(mNewsData);
//        mAdapter.setOnStickyListener(this);
        mRecyclerView.setAdapter(mAdapter);

//      mRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
    }

    /***
     * ---------------------------模拟拉取网络数据-------------------------- -------------
     *
     *
     * 这里设置 kfootView
     *
     * 如果没有更多数据 对footView进行View.setVisiblitexity(View.GONE);
     *
     */
    private void getDataForNet() {
        for (int i = 0; i < 10; i++) {
            NewsBean bean = new NewsBean();
            bean.title = "2015-12-11 12:00";
            bean.content = "视线好转,0729出口开通,0621进口开通。视线好转,0729出口开通,0621进口开通。";
            mNewsData.add(bean);
        }
//        if ()
//        mAdapter.notifyDataSetChanged();
//        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
    }
    private int srcollviewScrollFlag = 0;
    private void setScrollViewListener(){

        /**-------监听是不是滑到mScrollView是不是滑到顶部start----- **/
        if (mScrollView == null) {//mSrcollView 初始化一次后只要activity不消失 他就不会是null
            if(mLayoutManger.findViewByPosition(1) instanceof ObservableScrollView)
                mScrollView = (ObservableScrollView) mLayoutManger.findViewByPosition(1);
        }
        Log.e("srcollviewScrollFlag",srcollviewScrollFlag+"");
        Log.e("mScrollView.getTop()",mScrollView.getTop()+"");

        //如果第0个或者第1个条目是 ObservableScrollView 则进行下面的操作
        //else 说明mScrollView 已经不再页面上，不显示
        if (mLayoutManger.findViewByPosition(1) instanceof ObservableScrollView || mLayoutManger.findViewByPosition(0) instanceof ObservableScrollView) {
            mScrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                @Override
                public void onScrollChanged(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    currentScrollX = scrollX;
                }
            });
            float y = mScrollView.getTop();
            if (y <= 0) {
                mStikcyView.scrollTo((int) currentScrollX, 0);
                mStikcyView.setVisibility(View.VISIBLE);
            } else {
                mScrollView.scrollTo((int) currentScrollX, 0);
                mStikcyView.setVisibility(View.GONE);
            }
        } else {
            Log.e(TAG,"mScrollView is null2");
            /**防止mStikcyView（mLayoutManger.findViewByPosition(1)） 在recyclerView快速滑到顶部的时候为null 则立即显示**/
            mStikcyView.scrollTo((int) currentScrollX, 0);
            mStikcyView.setVisibility(View.VISIBLE);
        }
        srcollviewScrollFlag = mScrollView.getTop();
        /**-------监听是不是滑到mScrollView是不是滑到顶部end-----**/
    }
    private void initListener() {
        mStikcyView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                currentScrollX = scrollX;
                //                Log.e("scrollX",""+scrollX);
            }
        });
        mStikcyView.setVisibility(View.GONE);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //解决sticky头闪烁的问题
                //
                setScrollViewListener();
            }
        });
//        mScrollView = (ObservableScrollView ) mLayoutManger.findViewByPosition(1);
//        mScrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
//            @Override
//            public void onScrollChanged(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                currentScrollX = scrollX;
//                mStikcyView.scrollTo((int) currentScrollX, 0);
//            }
//        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("test", "refresh executed");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mNewsData.clear();
                        getDataForNet();
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.d("test", "refresh completed");
                    }
                }, 5000);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                setScrollViewListener();
                //最后一个可视的view的position
                int lastVisibleItemPosition = mLayoutManger.findLastVisibleItemPosition();
                //                最后一个可视的position + 1 == 总的item数   (滑动到正在加载的item)
                if (lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {
                    Log.d("test", "loading executed");
                    //是否正在刷新
                    boolean isRefreshing = mSwipeRefreshLayout.isRefreshing();
                    //如果正在刷新
                    if (isRefreshing) {
                        //删除最后一个item 正在加载
                        Log.d("test", "notifyItemRemoved");
                        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getDataForNet();
                                mAdapter.notifyDataSetChanged();
                                Log.d("test", "load more completed");
                                isLoading = false;
                            }
                        }, 3000);
                    }
                }
            }
        });
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_LONG).show();
            }
        });
    }

}
