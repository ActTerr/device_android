package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.EndLine;
import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/7/21.
 */

public class LineDetailAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<EndLine> lists;

    public LineDetailAdapter(Context context, ArrayList<EndLine> lists) {
        super();
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
        if (view == null) {
            view = inflater.inflate(R.layout.item_end_line_form, null);
        }
        TextView textView1 = (TextView) view.findViewById(R.id.text1);
        TextView textView2 = (TextView) view.findViewById(R.id.text2);
        TextView textView3 = (TextView) view.findViewById(R.id.text3);
        TextView textView4 = (TextView) view.findViewById(R.id.text4);
        TextView textView5 = (TextView) view.findViewById(R.id.text5);
        TextView textView6 = (TextView) view.findViewById(R.id.text6);
        TextView textView7 = (TextView) view.findViewById(R.id.text7);
        textView1.setText(String.valueOf(endLine.getTemperature()));
        textView2.setText(convert(endLine.getS1()));
        textView3.setText(convert(endLine.getS2()));
        textView4.setText(convert(endLine.getRadio_station()));

        DecimalFormat decimalFormat=new DecimalFormat("0.0");
        textView5.setText(decimalFormat.format(cf(endLine.getBattery())));
        textView6.setText(decimalFormat.format(cf(endLine.getPower())));
        if(endLine.getBattery()<12.5){
            textView5.setTextColor(Color.RED);
        }
        textView7.setText(ConvertUtils.Date2String(new Date(endLine.getTime())));


        view.setBackgroundResource(R.color.gray2);
        textView1.setTextColor(Color.WHITE);
        textView2.setTextColor(Color.WHITE);
        textView3.setTextColor(Color.WHITE);
        textView4.setTextColor(Color.WHITE);
        textView5.setTextColor(Color.WHITE);
        textView6.setTextColor(Color.WHITE);
        textView7.setTextColor(Color.WHITE);
        return view;
    }



    String convert(int i) {
        if (i == 1) {
            return "√";
        } else {
            return "×";
        }
    }

    float cf(float f){
        if(f<1){
          return 0f;
        }
        return f;
    }



}
