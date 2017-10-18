package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Service;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder> {
    ArrayList<Service> services = new ArrayList<>();
    Context context;

    final static String TAG="ServiceAdapter";
    boolean isLast=false;
    public ServiceAdapter(Context context) {

        this.context = context;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public void changeData(ArrayList<Service> list) {
            synchronized (services){
                if (services != null) {
                    services.clear();
                }
                L.e(TAG,"init:被执行");
                L.e(TAG, "list:" + list.size());

                services.addAll(list);

            }
        }

    public void addwData(ArrayList<Service> list) {
        L.e(TAG,"add被执行");
        for (Service service :list){
            L.e(TAG, service.toString());
        }
        synchronized (services){
            services.addAll(list);
        }
    }
    public void refresh(){
        notifyDataSetChanged();
    }


    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        /**
         * 如果切换的话(复用），item绝对不能传入parent;
         */
       ServiceHolder viewHolder = new ServiceHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceHolder holder, int position) {
        L.e(TAG,"execute bind");
       Service service = services.get(position);
        holder.serviceDate.setText(ConvertUtils.Date2String(service.getServiceDate()));
        if (service.getRepairDate() != null) {
            holder.repairDate.setText(ConvertUtils.Date2String(service.getRepairDate()));
            holder.wxUser.setText(service.getUser());
            holder.remark.setText(service.getRemark());
            holder.translate.setText(getTranText(service.isTranslate()));
        }else {
            holder.repairDate.setText("");
            holder.wxUser.setText("");
            holder.remark.setText("维修中......");
            holder.translate.setText("");
        }

    }

    private String getTranText(boolean translate) {
        if (translate) {
            return "已更换";
        } else {
            return "未更换";
        }
    }
    @Override
    public int getItemCount() {
        return services.size();
    }

    class ServiceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.repairDate)
        TextView repairDate;

        @BindView(R.id.serviceDate)
        TextView serviceDate;

        @BindView(R.id.name)
        TextView wxUser;

        @BindView(R.id.remark)
        TextView remark;

        @BindView(R.id.translate)
        TextView translate;

        public ServiceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
