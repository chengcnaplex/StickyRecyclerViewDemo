package com.example.administrator.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.recyclerviewdemo.R;
import com.example.administrator.recyclerviewdemo.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 Administrator
 * @创建时间 2016/3/30 9:32
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEAD = 2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_STICKY = 3;
    private static final int TYPE_FOOTER = 1;

    private List<NewsBean> mNewList;

    /**--------------构造----------------**/
    public  NewsAdapter(List<NewsBean> beans){
        mNewList = beans;
    }



    /**--------------监听----------------**/
    public interface StickyListener{
        void getX(float x);
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

//        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener onItemClickListener;
    private StickyListener mStickyListener;
    public void setOnStickyListener(StickyListener stickyListener){
        mStickyListener = stickyListener;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /**  填充view  放进holder**/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent,
                    false);
            return new FootViewHolder(view);
        }else if (viewType == TYPE_HEAD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent,
                    false);
            return new HeadViewHolder(view);
        }else if(viewType == TYPE_STICKY){
            View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.view_header, parent, false);
            
            StickyHolder stickyHolder = new StickyHolder(view);
//            mStickyListener.getX(view.getX());
                    return stickyHolder ;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
//        Log.d("count",""+getItemCount());
//        Log.d("position",""+position);
        //position + 1 不是 position + 2
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (position == 0){
            return TYPE_HEAD;
        }else if (position == 1){
            return TYPE_STICKY;
        } else {
            return TYPE_ITEM;
        }
    }

    /**取出holder绑定数据**/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof ItemViewHolder) {
        /**-----------------------绑定数据------------------------**/
            //holder.tv.setText(data.get(position));
//            if (onItemClickListener != null) {
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int position = holder.getLayoutPosition();
//                        onItemClickListener.onItemClick(holder.itemView, position);
//                    }
//                });
//
//                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        int position = holder.getLayoutPosition();
//                        onItemClickListener.onItemLongClick(holder.itemView, position);
//                        return false;
//                    }
//                });

//        }
    }

//    @Override
//    public long getHeaderId(int position) {
//        if (position == 0){
//            return 0;
//        }else{
//            return 1;
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
//
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.view_header, parent, false);
//        return new StickyHolder(view) ;
//    }
//
//    @Override
//    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
//        TextView textView = (TextView) holder.itemView;
//
//        textView.setText("ok");
//        holder.itemView.setBackgroundColor(getRandomColor());
//        if (position == 0) {
//            ((TextView) holder.itemView).setHeight(0);
//        }
//    }
//    private int getRandomColor() {
//        SecureRandom rgen = new SecureRandom();
//        return Color.HSVToColor(150, new float[]{
//                rgen.nextInt(359), 1, 1
//        });
//    }
    @Override
    public int getItemCount() {
        if (mNewList != null){
            return mNewList.size() + 3;
        }
        return  0;
    }

    public class StickyHolder extends RecyclerView.ViewHolder {

        public StickyHolder(View view) {
            super(view);

//            ((HorizontalScrollView)view)
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public ItemViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_date);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null){
                        Log.d("click","is click");
                        onItemClickListener.onItemClick(v ,getLayoutPosition()-1);
                    }
                }
            });
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
   public class HeadViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        //轮播图
        private ViewPager mViewPager;
        private LinearLayout mPonintContainer;		// 装静态点的容器
        private ImageView mIvPointFocus;			// 动态的点
        private int	mPointSpace;			// 两个点的距离
        private List<String> mPagerDatas;
        public HeadViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            mViewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
            mPonintContainer = (LinearLayout)view.findViewById(R.id.guide_point_container);
            mIvPointFocus = (ImageView) view.findViewById(R.id.guide_iv_pointfocus);
            mIvPointFocus.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // 布局发生改变时的回调
                    mPointSpace = mPonintContainer.getChildAt(1).getLeft() - mPonintContainer.getChildAt(0).getLeft();

                    mIvPointFocus.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
            mPagerDatas = new ArrayList<String>();
            mPagerDatas.add("1");
            mPagerDatas.add("2");
            mPagerDatas.add("3");
            mPagerDatas.add("4");
            mPagerDatas.add("5");

            //设置静态点
            for (int i = 0; i < mPagerDatas.size(); i++)
            {
                ImageView point = new ImageView(view.getContext());
                point.setImageResource(R.drawable.point_normal);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i != 0)
                {
                    params.leftMargin = 10;
                }
                // 动态加载点
                mPonintContainer.addView(point, params);
            }
            class TopNewsPagerAdapter extends PagerAdapter {

                @Override
                public int getCount() {
                    if (mPagerDatas != null) {
                        return mPagerDatas.size();
                    }
                    return 0;
                }

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {

                    TextView tv = new TextView(mContext);
                    tv.setText(mPagerDatas.get(position));

                    // 给imageview加载网络图片

                    container.addView(tv);

                    return tv;
                }
                @Override
                public void destroyItem(ViewGroup container, int position, Object object)
                {
                    container.removeView((View) object);
                }
            }
            mViewPager.setAdapter(new TopNewsPagerAdapter());
            class GuidePagerListener implements ViewPager.OnPageChangeListener
            {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                {
                    // 1.position: 在那个页面滑动
                    // 2.positionOffset: 滑动的像素值/页面的宽度 滑动的百分比
                    // 3.positionOffsetPixels: 滑动的像素值
                    // 当页面滚动时的回调
                    // Log.d(TAG, "onPageScrolled : " + position + "  " + positionOffset
                    // + "  " + positionOffsetPixels);
                    // 两个点间的距离--->
                    // 两个点间的距离 * 滑动的比值positionOffset
                    int marginLeft = (int) (mPointSpace * positionOffset + position * mPointSpace + 0.5f);

                    // 动态去设置 点的marginLeft
                    RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mIvPointFocus.getLayoutParams();
                    params.leftMargin = marginLeft;
                    mIvPointFocus.setLayoutParams(params);
                    Log.d("params", marginLeft + "");
                }

                @Override
                public void onPageSelected(int position)
                {
                    // 当页面选中时的回调
                    // Log.d(TAG, "onPageSelected:" + position);
                    // if (position == mImageDatas.size() - 1)
                    // {
                    // mBtnStart.setVisibility(View.VISIBLE);
                    // }
                    // else
                    // {
                    // mBtnStart.setVisibility(View.GONE);
                    // }
                    //mBtnStart.setVisibility((position == mImageDatas.size() - 1) ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onPageScrollStateChanged(int state)
                {
                    // 页面滑动状态改变时的回调
                    // * @param state The new scroll state.
                    // * @see ViewPager#SCROLL_STATE_IDLE : 闲置
                    // * @see ViewPager#SCROLL_STATE_DRAGGING : 拖拽状态
                    // * @see ViewPager#SCROLL_STATE_SETTLING: 固定选中状态

                    // 由一种状态变为另外一种状态
                    //Log.d(TAG, "onPageScrollStateChanged:" + state);

                }

            }
            mViewPager.addOnPageChangeListener(new GuidePagerListener());
        }
    }
}
