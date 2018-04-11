package jijiehao.minhua.com.sidesliplistview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jijiehao.minhua.com.sidesliplistview.adapter.CommonAdapter;
import jijiehao.minhua.com.sidesliplistview.adapter.ViewHolder;

/**
 * @author moodstrikerdd
 * @date 2018/4/10
 * @label
 */

public class MainActivity extends AppCompatActivity {

    private RelativeSwipeRefreshLayout relativeSwipeRefreshLayout;
    private SideslipListView sideslipListView;

    private CommonAdapter<String> adapter;
    private List<String> data = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            data.add("item" + (i + 1));
        }
        adapter.notifyDataSetChanged();
        relativeSwipeRefreshLayout.setRefreshing(false);
    }

    private void initView() {
        relativeSwipeRefreshLayout = this.findViewById(R.id.refresh);
        relativeSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        sideslipListView = this.findViewById(R.id.slvContent);
        adapter = new CommonAdapter<String>(this, R.layout.item_main, data) {

            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.tvItemName, s);
                holder.setOnClickListener(R.id.tvDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        sideslipListView.turnToNormal();
                    }
                });
                holder.setOnClickListener(R.id.tvPay, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "已换", Toast.LENGTH_SHORT).show();
                        sideslipListView.turnToNormal();
                    }
                });
            }
        };
        sideslipListView.setAdapter(adapter);
        sideslipListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
}
