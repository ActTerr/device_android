package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Check;
import mac.yk.devicemanagement.ui.holder.CheckViewHolder;
import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/3/8.
 */

public class CheckAdapter extends RecyclerView.Adapter<CheckViewHolder> {
    ArrayList<Check> checkList=new ArrayList<>();
    Context context;
    public void changeData(ArrayList<Check> list ){
        synchronized (checkList){
            if (checkList!=null){
                checkList.clear();
            }
            checkList.addAll(list);

            notifyDataSetChanged();
        }
    }

    public CheckAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CheckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_check, parent, false);
         CheckViewHolder viewHolder=new CheckViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CheckViewHolder holder, int position) {
        Check check =checkList.get(position);
        holder.checkDate.setText(ConvertUtils.Date2String(check.getDate()));
        holder.user.setText(check.getUser());
        holder.status.setText(check.getStatus());
        if (check.getStatus().equals("异常")){
            holder.cause.setText(check.getCause());
        }else {
            holder.cause.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }
}
