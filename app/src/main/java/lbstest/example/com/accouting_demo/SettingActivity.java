package lbstest.example.com.accouting_demo;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mList = new ArrayList<>();
    private ListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getSupportActionBar().setElevation(0);
        }
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);

        mList.add("作者:蓝莲花WSG");
        mList.add("如有建议请联系QQ：1516961816");
        mList.add("博客:http://my.csdn.net/qq_29375837");
        mList.add("GitHub:https://github.com/stevenwsg");
        mList.add("学习自刘桂林：GitHub:https://github.com/LiuGuiLinAndroid/SimpleProject/tree/master/TallyBook");

        mList.add("作者:蓝莲花WSG");
        mList.add("如有建议请联系QQ：1516961816");
        mList.add("博客:http://my.csdn.net/qq_29375837");
        mList.add("GitHub:https://github.com/stevenwsg");
        mList.add("学习自刘桂林：GitHub:https://github.com/LiuGuiLinAndroid/SimpleProject/tree/master/TallyBook");


        mAdapter = new lbstest.example.com.accouting_demo.ListAdapter(this,mList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
