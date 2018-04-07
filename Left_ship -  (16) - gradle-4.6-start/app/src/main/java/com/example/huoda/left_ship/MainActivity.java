package com.example.huoda.left_ship;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.example.huoda.left_ship.image.SmartImageView;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import imui.jiguang.cn.imuisample.messages.MessageListActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarLayout appBarLayout;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", /*Locale.getDefault()*/Locale.CHINESE);
    private CompactCalendarView compactCalendarView;
    private boolean isExpanded = false;

    private TextView tv,tv_count,tv_date_yes,tv_date_to,tv_date_tom,tv_date_tom_add,tv_date_day;
    public TextView tv_day_id,tv_day_id2,tv_day_list,tv_day_pre;
    private ListView lv,lv_name;
    public View change_add,change_con,change_del,change_wel,change_edit,change_look,change_send,change_share,change_help,change_date_day;
    public View bo_yes,bo_to,bo_tom;
    public View change_add1,content;
    private TextView mTextMessage;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    public Handler nHandler;
    public TextView rot;
    protected static String  users="user1",ip="pcohd.uicp.cn:24967",db0,db="librarydb",db2,user="test",pwd="";
    public int count;
    private BottomNavigationBar bottomNavigationBar;
    private ShapeBadgeItem badgeItem;
    public int state_1=1;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fdv();
        vis();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SetGone();
            }
        }, 20);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vis();
            }
        }, 30);
        onSuccess(102,"");


        verifyStoragePermissions(MainActivity.this);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("公园植物管理系统");

        FloatingActionButton fab_date = (FloatingActionButton) findViewById(R.id.fab_date);
        fab_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,DateActivity.class);
                //用Bundle携带数据
                Bundle bundle=new Bundle();
                //传递name参数为tinyphp
                bundle.putString("users", users);
                bundle.putString("ip", ip);
                bundle.putString("db", db);
                bundle.putString("user", user);
                bundle.putString("pwd", pwd);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        fab_date.setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        appBarLayout = findViewById(R.id.app_bar_layout);
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);
        compactCalendarView.setShouldDrawDaysHeader(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));
                final SimpleDateFormat dateFormat_f = new SimpleDateFormat("yyyyMMdd", /*Locale.getDefault()*/Locale.ENGLISH);
                String day0=dateFormat_f.format(dateClicked);
                onSuccess(101,day0);
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
            }
        });
        setCurrentDate(new Date());
        final ImageView arrow = findViewById(R.id.date_picker_arrow);

        RelativeLayout datePickerButton = findViewById(R.id.date_picker_button);

        datePickerButton.setOnClickListener(v -> {
            float rotation = isExpanded ? 0 : 180;
            ViewCompat.animate(arrow).rotation(rotation).start();
            isExpanded = !isExpanded;

            if (isExpanded) {
                //onSuccess(104,"");
                ifExpanded();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SetGone();
                    }
                }, 50);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ifExpanded();
                    }
                }, 100);
            } else{
                NotExpended();
                //  onSuccess(103,"");
            }
            appBarLayout.setExpanded(isExpanded, true);
        });
    }
    public void SetGone(){
        change_add.setVisibility(View.GONE);
        change_con.setVisibility(View.GONE);
        change_wel.setVisibility(View.GONE);
        change_del.setVisibility(View.GONE);
        change_edit.setVisibility(View.GONE);
        change_look.setVisibility(View.GONE);
        change_send.setVisibility(View.GONE);
        change_share.setVisibility(View.GONE);
        change_help.setVisibility(View.GONE);
        change_date_day.setVisibility(View.GONE);
    }
    public void onClick_Add_Note() {
        Intent intent =new Intent(MainActivity.this,AddNoteActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("users", users);
        bundle.putString("ip", ip);
        bundle.putString("db", db);
        bundle.putString("user", user);
        bundle.putString("pwd", pwd);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void fdv(){
        change_add = findViewById(R.id.change_add);
        change_con = findViewById(R.id.change_con);
        change_del = findViewById(R.id.change_del);
        change_wel = findViewById(R.id.change_wel);
        change_edit = findViewById(R.id.change_edit);
        change_look = findViewById(R.id.change_look);
        change_send = findViewById(R.id.change_send);
        change_share = findViewById(R.id.change_share);
        change_help = findViewById(R.id.change_help);
        change_date_day = findViewById(R.id.change_date_day);
        bo_yes = findViewById(R.id.bo_yes);
        bo_to = findViewById(R.id.bo_to);
        bo_tom = findViewById(R.id.bo_tom);
        tv_date_tom_add = findViewById(R.id.tv_date_tom_add);
        tv_date_tom = findViewById(R.id.tv_date_tom);
        tv_date_to = findViewById(R.id.tv_date_to);
        tv_date_yes = findViewById(R.id.tv_date_yes);
        tv_date_day = findViewById(R.id.tv_date_day);
        tv_day_id =(TextView)findViewById(R.id.tv_day_id);
        tv_day_id2 =(TextView)findViewById(R.id.tv_day_id2);
        tv_day_list =(TextView)findViewById(R.id.tv_day_list);
        tv_day_pre =(TextView)findViewById(R.id.tv_day_pre);
        lv = (ListView) findViewById(R.id.lv);
         rot=(TextView)findViewById(R.id.rot);
    }
    public void vis(){
        change_add.setVisibility(View.GONE);
        change_con.setVisibility(View.GONE);
        change_wel.setVisibility(View.GONE);
        change_del.setVisibility(View.VISIBLE);
        change_edit.setVisibility(View.GONE);
        change_look.setVisibility(View.GONE);
        change_send.setVisibility(View.GONE);
        change_share.setVisibility(View.GONE);
        change_help.setVisibility(View.GONE);
        change_date_day.setVisibility(View.GONE);
        bo_yes.setVisibility(View.GONE);
        bo_to.setVisibility(View.VISIBLE);
        bo_tom.setVisibility(View.GONE);

    }
    public void ifExpanded(){
        SimpleDateFormat sf3 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c3 = Calendar.getInstance();
        tv_date_day.setText(sf3.format(c3.getTime()));
        SimpleDateFormat sf4 = new SimpleDateFormat("yyyyMMdd");
        Calendar c4 = Calendar.getInstance();
        String date_to = sf4.format(c4.getTime());
        if(change_add.getVisibility()==(View.VISIBLE))
        {state_1 = 1;}
        if(change_con.getVisibility()==(View.VISIBLE))
        {state_1 = 2;}
        if(change_wel.getVisibility()==(View.VISIBLE))
        {state_1 = 3;}
        if(change_del.getVisibility()==(View.VISIBLE))
        {state_1 = 4;}
        if(change_edit.getVisibility()==(View.VISIBLE))
        {state_1 = 5;}
        if(change_look.getVisibility()==(View.VISIBLE))
        {state_1 = 6;}
        if(change_send.getVisibility()==(View.VISIBLE))
        {state_1 = 7;}
        if(change_share.getVisibility()==(View.VISIBLE))
        {state_1 = 8;}
        if(change_help.getVisibility()==(View.VISIBLE))
        {state_1 = 9;}

        change_add.setVisibility(View.GONE);
        change_con.setVisibility(View.GONE);
        change_wel.setVisibility(View.GONE);
        change_del.setVisibility(View.GONE);
        change_edit.setVisibility(View.GONE);
        change_look.setVisibility(View.GONE);
        change_send.setVisibility(View.GONE);
        change_share.setVisibility(View.GONE);
        change_help.setVisibility(View.GONE);
        change_date_day.setVisibility(View.VISIBLE);

        getdayId(date_to);
        getdayId2(date_to);
        getdayList(date_to);
        getdayPre(date_to);

    }
    public void NotExpended(){
        SimpleDateFormat sf4 = new SimpleDateFormat("yyyyMMdd");
        Calendar c4 = Calendar.getInstance();
        String date_to = sf4.format(c4.getTime());
        change_date_day.setVisibility(View.GONE);
        System.out.println(state_1);
        switch (state_1){
            case 1:
                change_add.setVisibility(View.VISIBLE);
                break;
            case 2:
                change_con.setVisibility(View.VISIBLE);
                break;
            case 3:
                change_wel.setVisibility(View.VISIBLE);
                break;
            case 4:
                change_del.setVisibility(View.VISIBLE);
                break;
            case 5:
                change_edit.setVisibility(View.VISIBLE);
                break;
            case 6:
                change_look.setVisibility(View.VISIBLE);
                break;
            case 7:
                change_send.setVisibility(View.VISIBLE);
                break;
            case 8:
                change_share.setVisibility(View.VISIBLE);
                break;
            case 9:
                change_help.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(this,"错误代码，无法识别", Toast.LENGTH_SHORT).show();
                break;
        }


    }
    private void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        if (compactCalendarView != null) {
            compactCalendarView.setCurrentDate(date);
        }
    }
    @Override
    public void setTitle(CharSequence title) {
        TextView tvTitle = findViewById(R.id.title);

        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    private void setSubtitle(String subtitle) {
        TextView datePickerTextView = findViewById(R.id.date_picker_text_view);

        if (datePickerTextView != null) {
            datePickerTextView.setText(subtitle);
        }
    }
    public void onSuccess(int i, String json) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    public void onSuccess2(int i, int json) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("json", json);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    private void getYesId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select id from tips where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(0, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                }
            }
        }).start();
    }
    private void getYesList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select tips from tips where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("tips");
                        onSuccess(1, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getYesPre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select notes from notes where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("notes");
                        onSuccess(2, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getTodayId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c2 = Calendar.getInstance();
                    String sql = "select id from tips where user = '"+users+"' and date = "+sf.format(c2.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(3, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                }
            }
        }).start();
    }
    private void getTodayList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    // c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select tips from tips where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("tips");
                        onSuccess(4, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getTodayPre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    //c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select notes from notes where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("notes");
                        onSuccess(5, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getTomId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String sql = "select id from tips where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(6, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                }
            }
        }).start();
    }
    private void getTomList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String sql = "select tips from tips where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("tips");
                        onSuccess(7, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getTomPre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String sql = "select notes from notes where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("notes");
                        onSuccess(8, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getYesId2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select id from notes where user = '"+users+"' and date = "+sf.format(c.getTime());

                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(10, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getTodayId2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c2 = Calendar.getInstance();
                    String sql = "select id from notes where user = '"+users+"' and date = "+sf.format(c2.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(11, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getTomId2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String sql = "select id from notes where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(12, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getdayId(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    // SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    // Calendar c2 = Calendar.getInstance();
                    String sql = "select id from tips where user = '"+users+"' and date = "+date ;
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        String x=mybook+"";
                        if (x!=null)
                            onSuccess2(13, mybook);
                        else
                            onSuccess2(13, 0X00);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                }
            }
        }).start();
    }
    private void getdayId2(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c2 = Calendar.getInstance();
                    String sql = "select id from notes where user = '"+users+"' and date = "+date;
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(16, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getdayList(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    // c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select tips from tips where user = '"+users+"' and date = "+date;
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("tips");
                        onSuccess(14, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getdayPre(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    //c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select notes from notes where user = '"+users+"' and date = "+date;
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("notes");
                        onSuccess(15, mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, Connect.class));
            return true;
        }
        if (id == R.id.action_help) {
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            return true;
        }
        if (id == R.id.action_add) {
            onClick_Add_Note();
            return true;
        }
        if (id == R.id.action_IMUI) {
           qqUI();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Handler handler = new Handler();
        if (id == R.id.nav_camera) {
            //  change_add
            //onSuccess(110,"");
            onSuccess(110,"");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(110,"");
                }
            }, 60);


        } else if (id == R.id.nav_gallery) {
            /*tv.setText("hi");*/

        } else if (id == R.id.nav_slideshow) {
            //  change_del
            onSuccess(105,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(61,"");
                }
            }, 10);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(105,"");
                }
            }, 60);

        } else if (id == R.id.nav_manage) {
            //  change_look
            onSuccess(112,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(112,"");
                }
            }, 60);

        } else if (id == R.id.nav_share) {
            //  startActivity(new Intent(MainActivity.this, Login.class));//红色部分为要打开的新窗口的类名
            //  change_share
            onSuccess(113,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(113,"");
                }
            }, 60);


            final ConstraintLayout cut2=(ConstraintLayout)findViewById(R.id.cut2);
            final ImageView im_logo2=(ImageView)findViewById(R.id.im_logo2);
            //   final TextView tv = (TextView) findViewById(R.id.tv_logo);
            //  tv.setBackgroundColor(Color.GREEN);
            // tv.setDrawingCacheEnabled(true);

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    viewSaveToImage(im_logo2);
                }
            };
            Button button = (Button) findViewById(R.id.bt_share);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new Handler().post(runnable);
                }
            });
            Button button2 = (Button) findViewById(R.id.bt_share2);
            button2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent share_intent = new Intent();
                    share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                    share_intent.setType("text/plain");//设置分享内容的类型
                    share_intent.putExtra(Intent.EXTRA_SUBJECT, "总想祭天");//添加分享内容标题
                    share_intent.putExtra(Intent.EXTRA_TEXT, "我觉得公园植物管理系统非常好用");//添加分享内容
                    share_intent = Intent.createChooser(share_intent, "分享公园植物管理系统");
                    MainActivity.this.startActivity(share_intent);

                   /* Intent share_intent = new Intent();
                    share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                   // share_intent.setType("text/plain");//设置分享内容的类型
                    share_intent.setType("image/*");  //设置分享内容的类型
                    share_intent.putExtra(Intent.EXTRA_SUBJECT, "总想祭天");//添加分享内容标题
                    share_intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("http://20vv455999.iask.in:22467/WebServer/image/share.png"));
                    //share_intent.putExtra(Intent.EXTRA_TEXT, "我觉得公园植物管理系统非常好用");//添加分享内容
                    share_intent = Intent.createChooser(share_intent, "分享公园植物管理系统");
                    MainActivity.this.startActivity(share_intent);*/

                    // WeiXinShareUtil.sharePhotoToWX(MainActivity.this,"不想说话，你们开心就好",android.os.Environment.MEDIA_MOUNTED);

                }
            });

        } else if (id == R.id.nav_send) {
            //  change_wel
            onSuccess(114,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(114,"");
                }
            }, 60);


        } else if (id == R.id.home) {
            //  change_wel
            onSuccess(115,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(115,"");
                }
            }, 60);


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setScrimColor(Color.TRANSPARENT);
        return true;
    }
    public void connect(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
                //  System.out.println("创建新的view:" + position);
            } else {
                tv = (TextView) convertView;
                //  System.out.println("使用回收的view:" + position);
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
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // mTextMessage.setText(R.string.title_home);
                    // Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
                   onSuccess(60,"");
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    onSuccess(61,"");
                    return true;
                case R.id.navigation_notifications:
                    //  mTextMessage.setText(R.string.title_notifications);
                    onSuccess(62,"");
                    return true;
            }
            return false;
        }
    };
    public void savePicture(Bitmap bm, String fileName) {
        Log.i("xing", "savePicture: ------------------------");
        if (null == bm) {
            Log.i("xing", "savePicture: ------------------图片为空------");
            return;
        }
        // File foder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test");
        //DCIM/camera
        File foder = new File("/DCIM/camera");
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(foder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //压缩保存到本地
            bm.compress(Bitmap.CompressFormat.PNG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "霍达提示您保存成功!", Toast.LENGTH_SHORT).show();
    }
    private Bitmap getImageFromAssetsFile(String fileName){
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is=am.open(fileName);
            image= BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();    }
        return image;
    }
    private static Uri saveBitmap(Bitmap bm, String picName) {
        try {
            String dir=Environment.getExternalStorageDirectory().getAbsolutePath()+"/renji/"+picName+".jpg";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Uri uri = Uri.fromFile(f);
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();    }
        return null;
    }
    private static String insertImageToSystem(Context context, String imagePath) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "test", "你对图片的描述");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        // 添加水印
        Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp,
                "HD"));

        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
                File file = new File(sdRoot, "test2.PNG");
                fos = new FileOutputStream(file);
            } else
                throw new Exception("创建文件失败!");

            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        view.destroyDrawingCache();
        Toast.makeText(this, " 霍达提示您保存成功!", Toast.LENGTH_SHORT).show();

    }
    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }
    // 为图片target添加水印
    private Bitmap createWatermarkBitmap(Bitmap target, String str) {
        int w = target.getWidth();
        int h = target.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint p = new Paint();

        // 水印的颜色
        p.setColor(Color.BLUE);

        // 水印的字体大小
        p.setTextSize(30);

        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(target, 0, 0, p);

        // 在中间位置开始添加水印
        canvas.drawText(str, w-8*w / 9, h / 9, p);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }
    public void addInfo(View view){
        EditText et_1 = (EditText)findViewById(R.id.et_1);
        String et1 = et_1.getText().toString().trim();
        EditText et_2 = (EditText)findViewById(R.id.et_2);
        String et2 = et_2.getText().toString().trim();
        EditText et_3 = (EditText)findViewById(R.id.et_3);
        String et3 = et_3.getText().toString().trim();
        EditText et_4 = (EditText)findViewById(R.id.et_4);
        String et4 = et_4.getText().toString().trim();
        add(et1,et2,et3,et4);
        hintKbTwo();
    }
    private void add(final String et1,final String et2,final String et3,final String et4) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    String sql1 = "insert into student (S_name)values('"+et1+et2+et3+et4+"')";
                    Statement st1 = (Statement) cn.createStatement();
                    int Res1 = st1.executeUpdate(sql1);
                    //System.out.println(Res1 > 0 ? "插入数据成功" : "插入数据失败");
                    // Toast.makeText(MainActivity.this, "霍达提示您上报成功", Toast.LENGTH_SHORT).show();
                    onSuccess2(21,1);
                    cn.close();
                    st1.close();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(21,0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(21,0);
                }
            }
        }).start();
    }
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    private void qqUI()
    {
        //startActivity(new Intent(MainActivity.this, MessageListActivity.class));
        onSuccess(70,"");
    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 70:
                    startActivity(new Intent(MainActivity.this, MessageListActivity.class));
                    break;

                case 60://设置昨天的
                    bo_yes.setVisibility(View.VISIBLE);
                    bo_to.setVisibility(View.GONE);
                    bo_tom.setVisibility(View.GONE);
                    getYesList();
                    getYesPre();
                    getYesId();
                    getYesId2();
                    break;
                case 61://设置今天的
                    bo_yes.setVisibility(View.GONE);
                    bo_to.setVisibility(View.VISIBLE);
                    bo_tom.setVisibility(View.GONE);
                    getTodayList();
                    getTodayPre();
                    getTodayId();
                    getTodayId2();
                    break;
                case 62://设置明天的
                    bo_yes.setVisibility(View.GONE);
                    bo_to.setVisibility(View.GONE);
                    bo_tom.setVisibility(View.VISIBLE);
                    getTomList();
                    getTomPre();
                    getTomId();
                    getTomId2();
                    break;
                case 233:
//                    Toast.makeText(this,"nihao", Toast.LENGTH_SHORT).show();
                    break;
                case 110:
                    change_add.setVisibility(View.VISIBLE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.GONE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);
                    change_send.setVisibility(View.GONE);
                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 111:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.GONE);
                    change_del.setVisibility(View.VISIBLE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);
                    change_send.setVisibility(View.GONE);
                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 112:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.GONE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.VISIBLE);
                    change_send.setVisibility(View.GONE);
                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url = "jdbc:mysql://" + ip + "/" + db;
                                //  System.out.println(url);
                                Class.forName("com.mysql.jdbc.Driver");
                                Connection cn = DriverManager.getConnection(url, user, pwd);
                                String sql = "select * from student";
                                Statement st = (Statement) cn.createStatement();
                                ResultSet rs = st.executeQuery(sql);
                                System.out.println("成功读取数据库！！");
                                while (rs.next()) {
                                    String mybook = rs.getString("S_Name");
                                    //  System.out.println(mybook);
                                    int id = rs.getInt("id");
                                    String s = String.valueOf(id);
                                    tv_count.setText(s);
                                    System.out.println(id);
                                }
                                cn.close();
                                st.close();
                                rs.close();
                                System.out.println("数据库关闭");
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    lv.setAdapter(new MyAdapter());
                    lv_name.setAdapter(new MyAdapter());
                    break;
                case 113:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.GONE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);
                    change_send.setVisibility(View.GONE);
                    change_share.setVisibility(View.VISIBLE);
                    onSuccess(100,"");
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 114:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.VISIBLE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);
                    change_send.setVisibility(View.GONE);
                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 115:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.VISIBLE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);
                    change_send.setVisibility(View.GONE);
                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 116:
                    break;
                case 117:
                    break;
                case 118:
                    break;
                case 105:
                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            vis();
//                        }
//                    }, 50);
                     vis();
//                    change_add.setVisibility(View.GONE);
//                    change_con.setVisibility(View.GONE);
//                    change_wel.setVisibility(View.GONE);
//                    change_del.setVisibility(View.GONE);
//                    change_edit.setVisibility(View.GONE);
//                    change_look.setVisibility(View.GONE);
//                    change_send.setVisibility(View.GONE);
//                    change_share.setVisibility(View.GONE);
//                    change_help.setVisibility(View.GONE);
//                    change_date_day.setVisibility(View.GONE);
//                    bo_yes.setVisibility(View.GONE);
//                    bo_to.setVisibility(View.GONE);
//                    bo_tom.setVisibility(View.GONE);

                    break;
                case 104:
                    ifExpanded();
                    break;
                case 103:
                    NotExpended();
                    break;
                case 102:
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    tv_date_to.setText(sf.format(c.getTime()));
                    //  System.out.println(“当前日期：               ”+sf.format(c.getTime()));
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    //   System.out.println(“增加一天后日期 ：  ”+sf.format(c.getTime()));
                    tv_date_tom.setText(sf.format(c.getTime()));
                    //  tv_date_tom_add.setText(sf.format(c.getTime()));
                    c.add(Calendar.DAY_OF_MONTH, -2);
                    tv_date_yes.setText(sf.format(c.getTime()));
                    break;
                case 101:
                    Bundle bundle101 = msg.getData();
                    String day0 = bundle101.getString("json");
                    tv_date_day.setText(day0);
                    //refresh
                    tv_day_id.setText("id");
                    tv_day_id2.setText("id");
                    tv_day_list.setText("系统提示");
                    tv_day_pre.setText("待办事项");
                    //get
                    getdayId(day0);
                    getdayId2(day0);
                    getdayList(day0);
                    getdayPre(day0);
                    break ;
                case 100:
                    SmartImageView siv = (SmartImageView) findViewById(R.id.smart_v);
                    siv.setImageUrl("http://20vv455999.iask.in:22467/WebServer/image/share.png", R.drawable.ic_menu_camera);
                    break;
                case 99:
                    // vis();
                    break;
                case 0:
                    TextView tv_yes_id = (TextView)findViewById(R.id.tv_yes_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle0 = msg.getData();
                    int data0 = bundle0.getInt("json");
                    tv_yes_id.setText(data0+"");
                    break;
                case 1:
                    TextView tv_yes_list = (TextView)findViewById(R.id.tv_yes_list);
                    //完成主界面更新,拿到数据
                    Bundle bundle1 = msg.getData();
                    String data1 = bundle1.getString("json");
                    tv_yes_list.setText(data1);
                    break;
                case 2:
                    TextView tv_yes_pre = (TextView)findViewById(R.id.tv_yes_pre);
                    //完成主界面更新,拿到数据
                    Bundle bundle2 = msg.getData();
                    String data2 = bundle2.getString("json");
                    tv_yes_pre.setText(data2);
                    break;
                case 3:
                    TextView tv_today_id = (TextView)findViewById(R.id.tv_day_id);
                    System.out.println("shoudao");
                    //完成主界面更新,拿到数据
                    Bundle bundle3 = msg.getData();
                    int data3 = bundle3.getInt("json");
                    tv_today_id.setText(data3+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
                    break;
                case 4:
                    TextView tv_today_list = (TextView)findViewById(R.id.tv_today_list);
                    //完成主界面更新,拿到数据
                    Bundle bundle4 = msg.getData();
                    String data4 = bundle4.getString("json");
                    tv_today_list.setText(data4);
                    break;
                case 5:
                    TextView tv_today_pre = (TextView)findViewById(R.id.tv_today_pre);
                    //完成主界面更新,拿到数据
                    Bundle bundle5 = msg.getData();
                    String data5 = bundle5.getString("json");
                    tv_today_pre.setText(data5);
                    break;
                case 6:
                    TextView tv_tom_id = (TextView)findViewById(R.id.tv_tom_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle6 = msg.getData();
                    int data6 = bundle6.getInt("json");
                    tv_tom_id.setText(data6+"");
                    break;
                case 7:
                    TextView tv_tom_list = (TextView)findViewById(R.id.tv_tom_list);
                    //完成主界面更新,拿到数据
                    Bundle bundle7 = msg.getData();
                    String data7 = bundle7.getString("json");
                    tv_tom_list.setText(data7);
                    break;
                case 8:
                    TextView tv_tom_pre = (TextView)findViewById(R.id.tv_tom_pre);
                    //完成主界面更新,拿到数据
                    Bundle bundle8 = msg.getData();
                    String data8 = bundle8.getString("json");
                    tv_tom_pre.setText(data8);
                    break;
                case 9:
                    TextView tv_date_add_id = (TextView)findViewById(R.id.tv_date_add_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle9 = msg.getData();
                    int data9 = bundle9.getInt("json");
                    System.out.println(data9);
                    tv_date_add_id.setText(data9+"");
                    break;
                case 10:
                    TextView tv_yes_id2 = (TextView)findViewById(R.id.tv_yes_id2);
                    //完成主界面更新,拿到数据
                    Bundle bundle10 = msg.getData();
                    int data10 = bundle10.getInt("json");
                    tv_yes_id2.setText(data10+"");
                    break;
                case 11:
                    TextView tv_today_id2 = (TextView)findViewById(R.id.tv_today_id2);
                    //完成主界面更新,拿到数据
                    Bundle bundle11 = msg.getData();
                    int data11 = bundle11.getInt("json");
                    tv_today_id2.setText(data11+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
                    break;
                case 12:
                    TextView tv_tom_id2 = (TextView)findViewById(R.id.tv_tom_id2);
                    //完成主界面更新,拿到数据
                    Bundle bundle12 = msg.getData();
                    int data12 = bundle12.getInt("json");
                    tv_tom_id2.setText(data12+"");
                    break;
                case 13:
                    TextView tv_day_id = (TextView)findViewById(R.id.tv_day_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle13 = msg.getData();
                    int data13 = bundle13.getInt("json");
                    tv_day_id.setText(data13+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
                    break;
                case 14:
                    TextView tv_day_list = (TextView)findViewById(R.id.tv_day_list);
                    //完成主界面更新,拿到数据
                    Bundle bundle14 = msg.getData();
                    String data14 = bundle14.getString("json");
                    tv_day_list.setText(data14);
                    break;
                case 15:
                    TextView tv_day_pre = (TextView)findViewById(R.id.tv_day_pre);
                    //完成主界面更新,拿到数据
                    Bundle bundle15 = msg.getData();
                    String data15 = bundle15.getString("json");
                    tv_day_pre.setText(data15);
                    break;
                case 16:
                    TextView tv_day_id2 = (TextView)findViewById(R.id.tv_day_id2);
                    //完成主界面更新,拿到数据
                    Bundle bundle16 = msg.getData();
                    int data16 = bundle16.getInt("json");
                    tv_day_id2.setText(data16+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
                    break;
                case 20:
                    Bundle bundle20 = msg.getData();
                    int data20 = bundle20.getInt("json");
                    if (data20==1)
                        Toast.makeText(MainActivity.this, "霍达提示您连接成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "霍达提示您连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case 21:
                    Bundle bundle21 = msg.getData();
                    int data21 = bundle21.getInt("json");
                    if (data21==1)
                        Toast.makeText(MainActivity.this, "霍达提示您上报成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "霍达提示您上报失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
