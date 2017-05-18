package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.DeviceResume;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.MFGT;

/**
 * Created by mac-yk on 2017/5/17.
 */

public class DeviceResumeAdapter extends RecyclerView.Adapter<DeviceResumeAdapter.DResumeHolder> {

    ArrayList<DeviceResume> list;
    Context context;



    public DeviceResumeAdapter(ArrayList<DeviceResume> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public DResumeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
        return new DResumeHolder(view);
    }

    @Override
    public void onBindViewHolder(DResumeHolder holder, int position) {
        final DeviceResume deviceResume = list.get(position);
        holder.useDate.setText(ConvertUtils.Date2String(deviceResume.getUse_date()));
        holder.useLocal.setText(deviceResume.getUse_local());
        holder.checkDate.setText(ConvertUtils.Date2String(deviceResume.getCheck_date()));
        holder.remark.setText(deviceResume.getRemark());
        holder.useUnit.setText(deviceResume.getUse_unit());
        holder.scrapDate.setText(ConvertUtils.Date2String(deviceResume.getScrap_date()));
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoDetailActivity(context,true,deviceResume.getDid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class DResumeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.use_date)
        TextView useDate;
        @BindView(R.id.scrap_date)
        TextView scrapDate;
        @BindView(R.id.use_unit)
        TextView useUnit;
        @BindView(R.id.use_local)
        TextView useLocal;
        @BindView(R.id.check_date)
        TextView checkDate;
        @BindView(R.id.remark)
        TextView remark;
        @BindView(R.id.ll)
        LinearLayout ll;
        public DResumeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
