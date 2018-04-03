package com.example.huoda.left_ship;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateActivity extends AppCompatActivity {
    public View date_add,date_del;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        date_add = findViewById(R.id.date_add);



        TextView tv_date_tom_add = findViewById(R.id.tv_date_tom_add);

        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        final String users = bundle.getString("users");
        final String ip = bundle.getString("ip");
        final String db = bundle.getString("db");
        final String user = bundle.getString("user");
        final String pwd = bundle.getString("pwd");
        addTomId(ip, db, user, pwd,users);

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        tv_date_tom_add.setText(sf.format(c.getTime()));

        Button bt_add_pre = (Button) findViewById(R.id.bt_add_pre);
        bt_add_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_tom_add_pre = (EditText)findViewById(R.id.et_tom_add_pre);
                String pre = et_tom_add_pre.getText().toString().trim();
                System.out.println(pre);
                submitTomPre(ip, db, user, pwd,pre,users);

            }
        });

        FloatingActionButton fab_back = (FloatingActionButton) findViewById(R.id.fab_back);
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                finish();
            }
        });
        fab_back.setVisibility(View.VISIBLE);
    }

    private void submitTomPre(final String ip, final String db, final String user, final String pwd, final String pre,final String users){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn = DriverManager.getConnection(url, user, pwd);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    // String sql = "insert into student (S_name)values('"+pre+"')";
                    String sql ="insert into notes (date,user,notes) values("+sf.format(c.getTime())+",'"+users+"','"+pre+"')";
                    Statement st = (Statement) cn.createStatement();
                    int Res = st.executeUpdate(sql);
                    // Toast.makeText(DateActivity.this, "霍达提示您添加成功", Toast.LENGTH_SHORT).show();
                    System.out.println(Res > 0 ? "插入数据成功" : "插入数据失败");
                    if (Res>0) {
                        onSuccess2(20, 1);
                    } else {
                        onSuccess2(20, 0);
                    }

                    cn.close();
                    st.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 9:
                    TextView tv_date_add_id = (TextView)findViewById(R.id.tv_date_add_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle9 = msg.getData();
                    int data9 = bundle9.getInt("json");
                    data9++;
                    System.out.println(data9);
                    tv_date_add_id.setText(data9+"");
                    break;
                case 20:
                    Bundle bundle20 = msg.getData();
                    int data20 = bundle20.getInt("json");
                    if (data20==1)
                        Toast.makeText(DateActivity.this, "霍达提示您添加成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DateActivity.this, "霍达提示您添加失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void onSuccess2(int i, int json) {
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("json", json);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    private void addTomId(final String ip, final String db, final String user, final String pwd,final String users) {
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
                        onSuccess2(9, mybook);
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

    public void add_date(View view){
        date_add.setVisibility(View.VISIBLE);
        date_del.setVisibility(View.GONE);
    }
    public void del_date(View view){
        date_add.setVisibility(View.GONE);
        date_del.setVisibility(View.VISIBLE);
    }

}
