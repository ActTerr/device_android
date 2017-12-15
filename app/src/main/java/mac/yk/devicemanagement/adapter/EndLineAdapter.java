package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.EndLine;
import mac.yk.devicemanagement.bean.EndLine2;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/7/18.
 */

public class EndLineAdapter extends RecyclerView.Adapter<EndLineAdapter.LineHolder> {
    Context context;
    ArrayList<EndLine> lines;
    ArrayList<EndLine2> line2s;

    public EndLineAdapter(Context context, ArrayList<EndLine> lines, ArrayList<EndLine2> line2s) {
        this.context = context;
        this.lines=lines;
        this.line2s=line2s;
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_endline2, parent, false);
        LineHolder holder = new LineHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(LineHolder holder, int position) {
        if(position==0){
            L.e("kunsiwole");
            EndLine line= lines.get(0);
            if (checkStatus(line) == false) {
                holder.ll.setBackgroundColor(context.getResources().getColor(R.color.red));
            }else{
                holder.ll.setBackgroundColor(context.getResources().getColor(R.color.gray2));
            }
            holder.type2_ll.setVisibility(View.GONE);
            holder.id.setText(position + 1 + "");
            holder.sensor1.setText(convert(line.getS1()));
            holder.sensor2.setText(convert(line.getS2()));
            holder.battery.setText(String.valueOf(line.getBattery()));
            String s=ConvertUtils.Date2String(new Date(line.getTime()));
            holder.time.setText(s);
            holder.radio.setText(convert(line.getRadio_station()));
            holder.temperature.setText(line.getTemperature() + "");
            holder.power.setText(String.valueOf(line.getPower()));
            holder.itemView.setTag(1);
        }else if(lines.size()!=0&&position>=lines.size()||lines.size()==0){
            EndLine2 line= line2s.get(0);
            holder.type2_ll.setVisibility(View.VISIBLE);
            holder.id.setText(String.valueOf(position+1));
            holder.sensor1.setText(convert(line.getS1()));
            holder.sensor2.setText(convert(line.getS2()));
            holder.sensor3.setText(convert(line.getS3()));
            holder.sensor4.setText(convert(line.getS4()));
            holder.battery.setText(String.valueOf(line.getBattery()));
            String s=ConvertUtils.Date2String(new Date(line.getTime()));
            holder.time.setText(s);
            holder.radio.setText(convert(line.getRadio_station()));
            holder.temperature.setText(line.getTemperature() + "");
            holder.power.setText(String.valueOf(line.getPower()));
            holder.itemView.setTag(2);
        }


        //float的转换

    }

    private boolean checkStatus(EndLine line) {
        if (line.getS1() == 0 || line.getRadio_station() == 0 || line.getS2() == 0
//                ||line.getBattery()<12.5
                ) {
            return false;
        } else {
            return true;
        }
    }

    String convert(int i) {
        if (i == 1) {
            return "√";
        } else {
            return "×";
        }
    }

    public void clear(){
        lines.clear();
    }

    @Override
    public int getItemCount() {
        return lines.size()+line2s.size();
    }

    public class LineHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id)
        public TextView id;
        @BindView(R.id.temperature)
        TextView temperature;
        @BindView(R.id.radio)
        TextView radio;
        @BindView(R.id.sensor1)
        TextView sensor1;
        @BindView(R.id.sensor2)
        TextView sensor2;
        @BindView(R.id.sensor3)
        TextView sensor3;
        @BindView(R.id.sensor4)
        TextView sensor4;
        @BindView(R.id.battery)
        TextView battery;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.ll)
        LinearLayout ll;
        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.battery_ll)
        LinearLayout batteryLl;
        @BindView(R.id.type2_ll)
        LinearLayout type2_ll;
        @BindView(R.id.power)
        TextView power;

        public LineHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
