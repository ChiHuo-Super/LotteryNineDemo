package com.demo.lotterynine.view;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.lotterynine.R;

import java.util.List;

public class LotteryRecyclerView extends ConstraintLayout {
    private Context context;
    private Handler handler;
    private View view_bj_one, view_bj_two;
    private RecyclerView rv_draw;
    private LotteryAdapter rvAdapter;
    private LotteryAdapter.OnItemClickListener onBtnClickListener;


    public LotteryRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_lottery, this, true);
        handler = new Handler();
        view_bj_one = this.findViewById(R.id.view_bj_one);
        view_bj_two = this.findViewById(R.id.view_bj_two);
        rv_draw = this.findViewById(R.id.rv_draw);
        initRecyclerView();
    }

    private void initRecyclerView() {
        if (rv_draw != null) {
            rv_draw.setLayoutManager(new GridLayoutManager(context, 3));
            rv_draw.addItemDecoration(new GridSpacingItemDecoration(3, 16, false));
            rvAdapter = new LotteryAdapter(context, null);
            rvAdapter.setOnBtnClickListener(new LotteryAdapter.OnItemClickListener() {
                @Override
                public void onDrawItem() {
                    if (onBtnClickListener != null) onBtnClickListener.onDrawItem();
                }

                @Override
                public void onWinPrizeItem(Object o) {
                    if (onBtnClickListener != null) onBtnClickListener.onWinPrizeItem(o);
                }
            });
            rv_draw.setAdapter(rvAdapter);
            setViewHeight();
        }
    }

    /**
     * 设置控件的适配高度
     */
    private void setViewHeight() {
        ViewTreeObserver vto = rv_draw.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rv_draw.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int viewHeight = rv_draw.getHeight();
                rvAdapter.setRecyclerViewHeight(viewHeight);
                rv_draw.setAdapter(rvAdapter);
                System.out.println("Height:" + viewHeight);
            }
        });
    }

    /**
     * 设置第一层背景
     */
    public void setOneBackground(int resid) {
        if (view_bj_one != null) view_bj_one.setBackgroundResource(resid);
    }

    /**
     * 设置第层层背景
     */
    public void setTwoBackground(int resid) {
        if (view_bj_two != null) view_bj_two.setBackgroundResource(resid);
    }

    /**
     * 设置中奖奖品
     *
     * @param prizeName
     */
    public void setWin(String prizeName) {
        if (rv_draw != null && rvAdapter != null) rvAdapter.setWin(prizeName);
    }

    /**
     * 设置奖品
     */
    public void setPrizeList(List<String> dataList) {
        if (rv_draw != null && rvAdapter != null) rvAdapter.update(dataList);
    }

    /**
     * 设置抽奖按钮显示
     *
     * @param btnStr
     */
    public void setLuckDrawBtnStr(String btnStr) {
        if (rv_draw != null && rvAdapter != null) rvAdapter.setbtnStr(btnStr);
    }


    /**
     * 设置点击按钮回调
     *
     * @param onBtnClickListener
     */
    public void setOnBtnClickListener(LotteryAdapter.OnItemClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

}
