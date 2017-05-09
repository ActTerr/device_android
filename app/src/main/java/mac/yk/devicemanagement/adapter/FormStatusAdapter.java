package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class FormStatusAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ArrayList<String>> lists;

    public FormStatusAdapter(Context context, ArrayList<ArrayList<String>> lists) {
        super();
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int index, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ArrayList<String> list = lists.get(index);
        if(view == null){
            view = inflater.inflate(R.layout.item_status_form, null);
        }
        view.setBackgroundResource(R.color.gray2);
        TextView station= (TextView) view.findViewById(R.id.tvStation);
        if(index%5==0){
            station.setVisibility(View.VISIBLE);
            station.setText(ConvertUtils.getUnitName(index/5+1));
        }else {
            station.setVisibility(View.GONE);
        }
        TextView textView1 = (TextView) view.findViewById(R.id.text1);
        TextView textView2 = (TextView) view.findViewById(R.id.text2);
        TextView textView3 = (TextView) view.findViewById(R.id.text3);
        TextView textView4 = (TextView) view.findViewById(R.id.text4);
        TextView textView5 = (TextView) view.findViewById(R.id.text5);
        TextView textView6 = (TextView) view.findViewById(R.id.text6);

        textView1.setText(list.get(0));
        textView2.setText(list.get(1));
        textView3.setText(list.get(2));
        textView4.setText(list.get(3));
        textView5.setText(list.get(4));
        textView6.setText(list.get(5));

        textView1.setTextColor(Color.WHITE);
        textView2.setTextColor(Color.WHITE);
        textView3.setTextColor(Color.WHITE);
        textView4.setTextColor(Color.WHITE);
        textView5.setTextColor(Color.WHITE);
        textView6.setTextColor(Color.WHITE);


        return view;
    }

}
