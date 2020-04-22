package com.demo.lotterynine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.demo.lotterynine.view.LotteryAdapter;
import com.demo.lotterynine.view.LotteryRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LotteryRecyclerView lrv_get_prize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lrv_get_prize = this.findViewById(R.id.lrv_get_prize);
        final List<String> data = getPrize();
        lrv_get_prize.setPrizeList(data);
        lrv_get_prize.setOnBtnClickListener(new LotteryAdapter.OnItemClickListener() {
            @Override
            public void onDrawItem() {
                lrv_get_prize.setWin(data.get(5));
            }

            @Override
            public void onWinPrizeItem(Object o) {
                Toast.makeText(MainActivity.this, data.get(5), Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<String> getPrize() {
        List<String> data = new ArrayList<>();
        data.add("北京7天游");
        data.add("苹果笔记本一台");
        data.add("华为手机一部");
        data.add("女朋友一个");
        data.add("奥迪一辆");
        data.add("双色球彩票一张");
        return data;
    }
}
