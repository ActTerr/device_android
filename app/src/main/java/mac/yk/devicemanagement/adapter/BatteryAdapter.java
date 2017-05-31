package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Battery;
import mac.yk.devicemanagement.util.MFGT;

/**
 * Created by mac-yk on 2017/5/19.
 */

public class BatteryAdapter extends RecyclerView.Adapter<BatteryAdapter.BatteryViewHolder> {
    Context context;
    ArrayList<Battery> list;


    public BatteryAdapter(Context context, ArrayList<Battery> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BatteryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_battery, null);
        return new BatteryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BatteryViewHolder holder, int position) {
        long hour=60*60*1000;
        final Battery battery=list.get(position);
        holder.position.setText(position+1+".");
        holder.batteryType.setText(battery.getType());
        holder.theory.setText((int) (battery.getTheory_duration()/hour)+"小时");
        long usedDuration=System.currentTimeMillis()-battery.getStart_time()+battery.getUsed_duration();
        holder.usedTime.setText((int) (usedDuration/hour)+"小时");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDetailActivity(context,true,false,battery.getDid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class BatteryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.position)
        TextView position;
        @BindView(R.id.battery_type)
        TextView batteryType;
        @BindView(R.id.theory)
        TextView theory;
        @BindView(R.id.used_time)
        TextView usedTime;
        public BatteryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
