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
import mac.yk.devicemanagement.util.MFGT;

/**
 * Created by mac-yk on 2017/5/17.
 */

public class FormServiceAdapter extends BaseAdapter{

        private Context context;
        private LayoutInflater inflater;
        private ArrayList<ArrayList<String>> lists;

        public FormServiceAdapter(Context context, ArrayList<ArrayList<String>> lists) {
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
            view = inflater.inflate(R.layout.item_service_form, null);
        }
        TextView station= (TextView) view.findViewById(R.id.tvStation);
        if(index%5==0){
            station.setVisibility(View.VISIBLE);
            station.setText(ConvertUtils.getServiceStation(index/5+1));
        }else {
            station.setVisibility(View.GONE);
        }
        TextView textView1 = (TextView) view.findViewById(R.id.text1);
        TextView textView2 = (TextView) view.findViewById(R.id.text2);
        TextView textView3 = (TextView) view.findViewById(R.id.text3);
        TextView textView4 = (TextView) view.findViewById(R.id.text4);

        textView1.setText(list.get(0));
        textView2.setText(list.get(1));
        textView3.setText(list.get(2));
        textView4.setText(list.get(3));
        textView1.setTextColor(Color.WHITE);
        textView2.setTextColor(Color.WHITE);
        textView3.setTextColor(Color.WHITE);
        textView4.setTextColor(Color.WHITE);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"1",index/5+1
                        ,list.get(0),"待修");
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"1",index/5+1
                        ,list.get(0),"维修");
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDeviceListActivity(context,"1",index/5+1
                        ,list.get(0),"修竣");
            }
        });

        return view;
    }


}
