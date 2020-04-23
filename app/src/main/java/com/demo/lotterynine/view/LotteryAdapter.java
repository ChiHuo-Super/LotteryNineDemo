package com.demo.lotterynine.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.lotterynine.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LotteryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> list;
    private OnItemClickListener onBtnClickListener;
    private Random random = new Random();
    private String btnStr = "抽奖";
    private String[] defaultFailStr = {"谢谢惠顾", "谢谢惠顾", "谢谢惠顾", "谢谢惠顾", "", "谢谢惠顾", "谢谢惠顾", "谢谢惠顾", "谢谢惠顾"};
    private int[] prizeSubscript = {1, 2, 3, 8, -1, 4, 7, 6, 5};//奖品下标列表
    private int mRepeatCount = 8; // 转的圈数
    private int durationTime = 6 * 1000;//设置抽奖动画变化时长
    private boolean mShouldStartNextTurn = true; // 标记是否应该开启下一轮抽奖
    private int mStartLuckPosition = 0; // 开始抽奖的位置
    private int mCurrentPosition = -1; // 当前转圈所在的位置
    private int[] turnClockwiseArray = {0, 1, 2, 5, 8, 7, 6, 3};//顺时针转圈
    private int win = -1;//中奖下标
    private int mRecyclerViewHeight = 0;//列表控件的高度

    public LotteryAdapter(Context context, List<String> DataLists) {
        this.context = context;
        if (DataLists != null) {
            this.list = getPrizeSort(DataLists);
        }
    }

    /**
     * 更新奖品列表
     *
     * @param DataLists
     */
    public void update(List<String> DataLists) {
        if (DataLists != null) {
            this.list = getPrizeSort(DataLists);
        }
        notifyDataSetChanged();
    }

    /**
     * 奖品排序
     */
    private List<String> getPrizeSort(List<String> list) {
        switch (list.size()) {
            case 1:
                defaultFailStr[0] = list.get(0);
                break;
            case 2:
                defaultFailStr[0] = list.get(0);
                defaultFailStr[2] = list.get(1);
                break;
            case 3:
                defaultFailStr[0] = list.get(0);
                defaultFailStr[2] = list.get(1);
                defaultFailStr[6] = list.get(2);
                break;
            case 4:
                defaultFailStr[0] = list.get(0);
                defaultFailStr[2] = list.get(1);
                defaultFailStr[6] = list.get(2);
                defaultFailStr[8] = list.get(3);
                break;
            case 5:
                defaultFailStr[0] = list.get(0);
                defaultFailStr[1] = list.get(1);
                defaultFailStr[2] = list.get(2);
                defaultFailStr[6] = list.get(3);
                defaultFailStr[8] = list.get(4);
                break;
            case 6:
                defaultFailStr[0] = list.get(0);
                defaultFailStr[1] = list.get(1);
                defaultFailStr[2] = list.get(2);
                defaultFailStr[6] = list.get(3);
                defaultFailStr[7] = list.get(4);
                defaultFailStr[8] = list.get(5);
                break;
            case 7:
                defaultFailStr[0] = list.get(0);
                defaultFailStr[1] = list.get(1);
                defaultFailStr[2] = list.get(2);
                defaultFailStr[5] = list.get(3);
                defaultFailStr[6] = list.get(4);
                defaultFailStr[7] = list.get(5);
                defaultFailStr[8] = list.get(6);
                break;
        }
        return Arrays.asList(defaultFailStr);
    }

    /**
     * 设置抽奖按钮显示
     *
     * @param btnStr
     */
    public void setbtnStr(String btnStr) {
        this.btnStr = btnStr;
        notifyDataSetChanged();
    }

    /**
     * 设置中奖奖品
     *
     * @param prizeName 奖品名称
     */
    public void setWin(String prizeName) {
        if (prizeName == null || prizeName.isEmpty()) prizeName = "谢谢惠顾";
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isEmpty() && prizeName.equals(list.get(i))) {
                win = i;
            }
        }
        turnPosition = -1;
        mCurrentPosition = -1;
        startAnim(win);
    }

    /**
     * 传入承载控件高度
     * @param mRecyclerViewHeight 控件高度
     */
    public void setRecyclerViewHeight(int mRecyclerViewHeight) {
        this.mRecyclerViewHeight = mRecyclerViewHeight;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_lottery_item, viewGroup, false);
       //item高度自适应
        if (mRecyclerViewHeight != 0)
            view.getLayoutParams().height = (mRecyclerViewHeight - 16 * 2) / 3;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (i != 4) {//前4个，后4个
            holder.txt_ranks.setText(list.get(i));
            if (mCurrentPosition == i) {
                holder.txt_ranks.setBackgroundResource(R.drawable.bg_item_checked);
            } else {
                holder.txt_ranks.setBackgroundResource(R.drawable.bg_item);
            }
        } else {//下标4，第5个中奖按钮
            holder.txt_ranks.setText(btnStr);
            if (mShouldStartNextTurn) {
                holder.txt_ranks.setBackgroundResource(R.drawable.bg_button);
                holder.txt_ranks.setEnabled(true);
            } else {
                holder.txt_ranks.setBackgroundResource(R.drawable.bg_button_disabled);
                holder.txt_ranks.setEnabled(false);
            }
            holder.txt_ranks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBtnClickListener != null) onBtnClickListener.onDrawItem();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        else return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_ranks;

        ViewHolder(View view) {
            super(view);
            txt_ranks = view.findViewById(R.id.txt_ranks);
        }
    }

    /**
     * 开始抽奖动画
     *
     * @param rankNum 中奖下标
     */
    private int animation_int = -1;//判断去重

    private void startAnim(final int rankNum) {
        if (!mShouldStartNextTurn) return;
        animation_int = -1;

        //设置开始值和结束值
        ValueAnimator animator = ValueAnimator.ofInt(mStartLuckPosition, mRepeatCount * 8 + prizeSubscript[rankNum]).setDuration(durationTime);// 设置变化时长

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {//动画更新
                // 检查一下这个方法会调用多少次
                final int position = (int) animation.getAnimatedValue();//获取计算出来的当前属性值。
                Log.i("animation", "position：" + position);
                if (animation_int != position) animation_int = position;
                else return;
                mCurrentPosition = getTurnClockwisePosition();
                mShouldStartNextTurn = false;
                notifyDataSetChanged();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {//动画结束
                mShouldStartNextTurn = true;
                mStartLuckPosition = 0;
                mCurrentPosition = rankNum;
                notifyDataSetChanged();
                if (onBtnClickListener != null) onBtnClickListener.onWinPrizeItem(null);
            }
        });

        animator.start();
    }

    /**
     * 获取随机下标
     *
     * @return
     */
    private int getRandomPosition() {
        return random.nextInt(8);//生成 [0,8) 的随机整数
    }

    /**
     * 获取顺时针下标
     *
     * @return
     */
    private int turnPosition = -1;//顺时针转圈统计值

    private int getTurnClockwisePosition() {
        if (turnPosition < turnClockwiseArray.length - 1) {
            turnPosition++;
        } else {
            turnPosition = 0;
        }
        return turnClockwiseArray[turnPosition];
    }

    /**
     * 设置点击中奖按钮回调
     *
     * @param onBtnClickListener
     */
    public void setOnBtnClickListener(OnItemClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }


    public interface OnItemClickListener<T> {
        void onDrawItem();//点击中奖按钮回调

        void onWinPrizeItem(T t);//中奖动画执行完毕回调
    }
}
