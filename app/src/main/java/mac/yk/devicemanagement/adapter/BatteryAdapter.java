package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mac.yk.devicemanagement.bean.Battery;

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
        return null;
    }

    @Override
    public void onBindViewHolder(BatteryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BatteryViewHolder extends RecyclerView.ViewHolder{

        public BatteryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
