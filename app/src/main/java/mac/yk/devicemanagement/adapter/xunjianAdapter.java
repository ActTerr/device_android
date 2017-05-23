package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class xunjianAdapter extends RecyclerView.Adapter<XjViewHolder> {
    ArrayList<Xunjian> xjlist=new ArrayList<>();
    Context context;
    public void changeData(ArrayList<Xunjian> list ){
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

    @Override
    public XjViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_xunjian, parent, false);
         XjViewHolder  viewHolder=new XjViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(XjViewHolder holder, int position) {
        Xunjian xunjian=xjlist.get(position);
        holder.xJianDate.setText(ConvertUtils.Date2String(xunjian.getXjDate()));
        holder.xjUser.setText(xunjian.getXjUser());
        holder.status.setText(xunjian.getStatus());
        if (xunjian.getStatus().equals("异常")){
            holder.cause.setText(xunjian.getCause());
        }else {
            holder.cause.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return xjlist.size();
    }
}
