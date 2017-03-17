package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Xunjian;
import mac.yk.devicemanagement.ui.holder.XjViewHolder;
import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/3/8.
 */

public class xunjianAdapter extends RecyclerView.Adapter {
    ArrayList<Xunjian> xjlist=new ArrayList<>();
    Context context;
    public void initxData(ArrayList<Xunjian> list ){
        synchronized (xjlist){
            if (xjlist!=null){
                xjlist.clear();
            }
            xjlist.addAll(list);

            notifyDataSetChanged();
        }
    }

    public xunjianAdapter(Context context) {
        this.context = context;
    }

    public void addxData(ArrayList<Xunjian> list){
        xjlist.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int id= R.layout.item_xunjian;
        View view=View.inflate(context,id,null);
        RecyclerView.ViewHolder  viewHolder=new XjViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        XjViewHolder xjViewHolder= (XjViewHolder) holder;
        Xunjian xunjian=xjlist.get(position);
        xjViewHolder.xJianDate.setText(ConvertUtils.Date2String(xunjian.getXjDate()));
        xjViewHolder.xjUser.setText(xunjian.getXjUser());
        xjViewHolder.status.setText(ConvertUtils.getXunjianStatus(xunjian.getStatus()));
        if (xunjian.getStatus()==0){
            xjViewHolder.cause.setText(xunjian.getCause());
        }else {
            xjViewHolder.cause.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return xjlist.size();
    }
}
