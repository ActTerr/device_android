package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.application.MyMemory;

/**
 * Created by mac-yk on 2017/11/9.
 */

public class UnitAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    String [] units=new String[]{
            "yuquan","chengdu","wuhan","C"
    };
    Context context;

    String memory="";

    public UnitAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return units.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = new TextView(context);
        }
        final TextView tv= (TextView) convertView;
         tv.setText(units[position]);
        if(units[position+1].equals(memory)){
            tv.setBackgroundColor(context.getResources().getColor(R.color.google_yellow));
        }else{
            tv.setBackgroundColor(context.getResources().getColor(R.color.gray_white));
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memory=units[position];
                notifyDataSetChanged();
                MyMemory.getInstance().setUnit(memory);
            }
        });
        return convertView;
    }
}