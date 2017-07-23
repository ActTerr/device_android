package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.EndLine;
import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/7/21.
 */

public class LineDetailAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<EndLine> lists;

    public LineDetailAdapter(Context context, ArrayList<EndLine> lists) {
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
        EndLine endLine = lists.get(index);
        if(view == null){
            view = inflater.inflate(R.layout.item_total_form, null);
        }
        TextView textView1 = (TextView) view.findViewById(R.id.text1);
        TextView textView2 = (TextView) view.findViewById(R.id.text2);
        TextView textView3 = (TextView) view.findViewById(R.id.text3);
        TextView textView4 = (TextView) view.findViewById(R.id.text4);
        TextView textView5 = (TextView) view.findViewById(R.id.text5);
        TextView textView6 = (TextView) view.findViewById(R.id.text6);
        if (index==0){
            textView1.setText("序列号");
            textView2.setText("温度");
            textView3.setText("传感器状态");
            textView4.setText("电台状态");
            textView5.setText("电池状态");
            textView6.setText("上传日期");
        }else {
            textView1.setText(String.valueOf(endLine.getNumber()));
            textView2.setText(String.valueOf(endLine.getTemperature()));
            textView3.setText(convert(endLine.getSensor()));
            textView4.setText(convert(endLine.getRadio_station()));
            textView5.setText(convert(endLine.getBattery()));
            textView6.setText(ConvertUtils.Date2String(new Date(endLine.getTime())));
        }

        view.setBackgroundResource(R.color.gray2);
        textView1.setTextColor(Color.WHITE);
        textView2.setTextColor(Color.WHITE);
        textView3.setTextColor(Color.WHITE);
        textView4.setTextColor(Color.WHITE);
        textView5.setTextColor(Color.WHITE);
        textView6.setTextColor(Color.WHITE);


        return view;
    }
    String convert(int i){
        if (i==1){
            return "√";
        }else {
            return "×";
        }
    }

}
