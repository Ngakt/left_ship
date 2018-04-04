package com.glandroid.listviewdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mvc 开发模式
        // model 数据模型
        // view 视图
        // controller 控制器
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {

        /**
         *获取列表里面一共有多少条记录
         * @return 返回记录总数
         */
        @Override
        public int getCount() {
            return 1000000;
        }

        /**
         * 返回一个view对象，这个view对象显示在指定的位置
         * @param position
         *          item的位置
         * @param convertView
         *          回收的view
         * @param parent
         *          父容器
         * @return  返回的view对象
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                tv = new TextView(MainActivity.this);
                System.out.println("创建新的view:" + position);
            } else {
                tv = (TextView) convertView;
                System.out.println("使用回收的view:" + position);
            }
            tv.setText("我是文本：" + position);
            tv.setTextColor(Color.RED);
            tv.setTextSize(20);
            return tv;
        }

        /**
         * 获取一条item
         * @param position
         *          item的位置
         * @return  item
         */
        @Override
        public Object getItem(int position) {
            return null;
        }

        /**
         * 获取一条item的id
         * @param position
         *          item的位置
         * @return  item的id
         */
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
