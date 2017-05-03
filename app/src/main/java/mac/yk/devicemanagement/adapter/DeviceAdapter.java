package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.DeviceOld;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/3/13.
 */

public class DeviceAdapter extends RecyclerView.Adapter {
    ArrayList<DeviceOld> deviceOlds = new ArrayList<>();
    Context context;

    public DeviceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_device, null);
        DeviceHolder holder = new DeviceHolder(view);
        return holder;
    }

    public void addData(ArrayList<DeviceOld> list){
        L.e("main","datper"+list.size());
        deviceOlds.addAll(list);
        notifyDataSetChanged();
    }

   public void changeData(ArrayList<DeviceOld> list){
       if (deviceOlds !=null){
           deviceOlds.clear();
       }
       deviceOlds.addAll(list);
       notifyDataSetChanged();
   }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DeviceOld deviceOld = deviceOlds.get(position);
        DeviceHolder deviceHolder= (DeviceHolder) holder;
        deviceHolder.Did.setText(deviceOld.getDid()+"");
        deviceHolder.Dname.setText(ConvertUtils.getDname(deviceOld.getDname()));
        deviceHolder.chuchangDate.setText(ConvertUtils.Date2String(deviceOld.getChuchang()));
        deviceHolder.xunjianDate.setText(ConvertUtils.Date2String(deviceOld.getXunjian()));
        boolean isDianchi= deviceOld.getDname()== I.DNAME.DIANCHI;
        deviceHolder.status.setText(ConvertUtils.getStatus(isDianchi, deviceOld.getStatus()));
    }

    @Override
    public int getItemCount() {
        return deviceOlds.size();
    }

    public void clear() {
        if (deviceOlds !=null){
            deviceOlds.clear();
        }
    }

    public void refresh(){
        notifyDataSetChanged();
    }
    class DeviceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Did)
        TextView Did;
        @BindView(R.id.Dname)
        TextView Dname;
        @BindView(R.id.chuchangDate)
        TextView chuchangDate;
        @BindView(R.id.xunjianDate)
        TextView xunjianDate;
        @BindView(R.id.status)
        TextView status;
        public DeviceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
