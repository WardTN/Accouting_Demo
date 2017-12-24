package lbstest.example.com.accouting_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView mListView;
    private CostAdapter costAdapter;
    private RadioButton checkRadioButton;
    private List<CostModel> mList = new ArrayList<>();

    private boolean quit = false; //设置退出标识


    //数据库对象
    private DBHelper db;

    //悬浮按钮
    private FloatingActionButton mFab;
    //提示框
    private Dialog dialog;

    private RadioGroup radioGroup;
    private EditText et_money;
    private Button btn_add_data;
    private RadioButton output;
    private RadioButton income;

    //没有数据
    private TextView tv_no_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    //初始化数据
    private void initData() {
        db = new DBHelper(this);
        //查询
        Cursor cursor = db.queryAlllData();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CostModel model = new CostModel();
                model.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                model.setDate(cursor.getString(cursor.getColumnIndex("date")));
                model.setMoney(cursor.getString(cursor.getColumnIndex("money")));
                mList.add(model);
            }
        }
    }

    private void initView() {
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        mListView = (ListView) findViewById(R.id.mListView);
        //倒序
        Collections.reverse(mList);

        //适配器
        costAdapter = new CostAdapter(this, mList);
        mListView.setAdapter(costAdapter);


        mFab = (FloatingActionButton) findViewById(R.id.mFab);
        mFab.setOnClickListener(this);

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("账单详情")
                        .setMessage("说明：" + mList.get(position).getTitle() + "\n"
                                + "时间：" + mList.get(position).getDate() + "\n"
                                + "金额：" + mList.get(position).getMoney())
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //数据库的也删除
                                db.deleteData(mList.get(position).getDate());
                                mList.remove(position);
                                costAdapter.notifyDataSetChanged();

                                //没有数据
                                if (mList.size() == 0) {
                                    mListView.setVisibility(View.GONE);
                                    tv_no_data.setVisibility(View.VISIBLE);
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });

        //没有数据
        if (mList.size() == 0) {
            mListView.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }

    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.mFab:
                //CrashReport.testJavaCrash();
                dialog = new Dialog(this);
                dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.dialog_item, null));
                radioGroup = (RadioGroup) dialog.findViewById(R.id.radiogroup);
                output = dialog.findViewById(R.id.output);
                income = dialog.findViewById(R.id.income);
                et_money = (EditText) dialog.findViewById(R.id.et_money);
                btn_add_data = (Button) dialog.findViewById(R.id.btn_add_data);
                btn_add_data.setOnClickListener(this);
                //屏幕外点击无效
                //dialog.setCancelable(false);
                dialog.show();
                break;

            case R.id.btn_add_data://添加数据

                String title;
                //获取title
               if (output.isChecked()){
                    title = "output";
               }else {
                   title ="income";
               }

                String money = et_money.getText().toString().trim();

                //title和text都不为空
//                Toast.makeText(MainActivity.this,"Title"+title,Toast.LENGTH_LONG).show();
                if (title != "") {
                    if (!TextUtils.isEmpty(money)) {
                        //当前时间
                        SimpleDateFormat  formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String date = formatter.format(curDate);

//                          date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        //封装成对象
                        CostModel model = new CostModel();
                        model.setTitle(title);
                        model.setMoney(money + " 元");
                        model.setDate(date);
                        //数据库添加
                        db.insertData(model);
                        //当前列表添加
                        mList.add(0, model);
                        //数据适配器
                        costAdapter.notifyDataSetChanged();
                        dialog.dismiss();

                        if (mListView.getVisibility() != View.VISIBLE) {
                            mListView.setVisibility(View.VISIBLE);
                        }
                        if (tv_no_data.getVisibility() != View.GONE) {
                            tv_no_data.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(this, "金额不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    //选择菜单键
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:     //设置
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_clear_all:   //清除全部
                if (mList.size() != 0) {
                    new AlertDialog.Builder(this)
                            .setTitle("警告")
                            .setMessage("如果点击确定，将删除所有账单数据且不能恢复")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.deleteAllDate();
                                    mList.clear();
                                    costAdapter.notifyDataSetChanged();
                                    //没有数据
                                    if (mList.size() == 0) {
                                        mListView.setVisibility(View.GONE);
                                        tv_no_data.setVisibility(View.VISIBLE);
                                    }
                                }
                            })
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();   //弹出框消失
                                }
                            }).show();
                } else {
                    Toast.makeText(this, "你都还没有记账！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    //点击两次返回退出

    @Override public void onBackPressed() {
        if (quit == false) {
            //询问退出程序
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            new Timer(true).schedule(new TimerTask() {
                                         //启动定时任务
                                         @Override public void run() {
                                             quit = false;
                                             //重置退出标识
                                         }
                                     },
                    2000);
            //2秒后运行run()方法
            quit = true;
        } else {
            //确认退出程序
            super.onBackPressed();
            finish();
        }
    }


}