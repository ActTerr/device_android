package mac.yk.devicemanagement.controller.adapter;

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
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/3/13.
 */

public class DeviceAdapter extends RecyclerView.Adapter {
    ArrayList<Device> devices = new ArrayList<>();
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

    public void addData(ArrayList<Device> list){
        L.e("main","datper"+list.size());
        devices.addAll(list);
        L.e("main",devices.get(3).toString());
        notifyDataSetChanged();
    }

   public void changeData(ArrayList<Device> list){
       if (devices!=null){
           devices.clear();
       }
       devices.addAll(list);
       notifyDataSetChanged();
   }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Device device=devices.get(position);
        DeviceHolder deviceHolder= (DeviceHolder) holder;
        L.e("main","id:"+device.getDid());
        deviceHolder.Did.setText(device.getDid()+"");
        deviceHolder.Dname.setText(ConvertUtils.getDname(device.getDname()));
        deviceHolder.chuchangDate.setText(String.valueOf(device.getChuchang()));
        deviceHolder.xunjianDate.setText(String.valueOf(device.getXunjian()));
        boolean isDianchi=device.getDname()== I.DNAME.DIANCHI;
        deviceHolder.status.setText(ConvertUtils.getStatus(isDianchi,device.getStatus()));
    }

    @Override
    public int getItemCount() {
        return devices.size();
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
