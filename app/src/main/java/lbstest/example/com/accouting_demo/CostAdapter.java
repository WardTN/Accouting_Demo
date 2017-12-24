package lbstest.example.com.accouting_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chen on 17-12-24.
 */

public class CostAdapter extends BaseAdapter{

    private Context mContext;
    private List<CostModel> mList;
    private LayoutInflater inflater;

    public CostAdapter(Context mContext, List<CostModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder = null;
      if (convertView == null){
          viewHolder =  new ViewHolder();
          convertView = inflater.inflate(R.layout.list_item,null);
          viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
          viewHolder.tv_date = convertView.findViewById(R.id.tv_date);
          viewHolder.tv_money = convertView.findViewById(R.id.tv_money);
          convertView.setTag(viewHolder);
      }else {
          viewHolder = (ViewHolder) convertView.getTag();
      }

      CostModel model = mList.get(position);

      viewHolder.tv_title.setText(model.getTitle());
      viewHolder.tv_date.setText(model.getDate());
      viewHolder.tv_money.setText(model.getMoney());

      return convertView;
    }


    class ViewHolder{
        private TextView tv_title;
        private TextView tv_date;
        private TextView tv_money;
    }
}
