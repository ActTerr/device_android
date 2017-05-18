package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.MFGT;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class FormStatusAdapter extends BaseAdapter {

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
    public View getView(final int index, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final ArrayList<String> list = lists.get(index);
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
        TextView textView7= (TextView) view.findViewById(R.id.text7);

        textView1.setText(list.get(0));
        textView2.setText(list.get(1));
        textView3.setText(list.get(2));
        textView4.setText(list.get(3));
        textView5.setText(list.get(4));
        textView6.setText(list.get(5));
        textView7.setText(list.get(6));

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"0",index/5+1,
                        list.get(0),"备用");
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"0",index/5+1,
                        list.get(0),"待用");

            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"0",index/5+1,
                        list.get(0),"运行");
            }
        });
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"0",index/5+1
                        ,list.get(0),"待修");
            }
        });
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"0",index/5+1
                        ,list.get(0),"维修");
            }
        });
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"0",index/5+1
                        ,list.get(0),"修竣");
            }
        });
        return view;
    }


}
