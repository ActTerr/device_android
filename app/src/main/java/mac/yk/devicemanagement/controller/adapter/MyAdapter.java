package mac.yk.devicemanagement.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.bean.Xunjian;
import mac.yk.devicemanagement.view.holder.WxViewHolder;
import mac.yk.devicemanagement.view.holder.XjViewHolder;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Weixiu> weixius;
    Context context;
    List<Xunjian> xjlist;
    boolean isWexiu;

    public MyAdapter(Context context, List<Weixiu> weixius, List<Xunjian> xjlist) {
        this.weixius = weixius;
        this.context = context;
        this.xjlist = xjlist;
        isWexiu=weixius==null?false:true;
    }
    public void initxData(ArrayList<Xunjian> list ){
        if (xjlist!=null){
            xjlist.clear();
        }
        xjlist.addAll(list);
        notifyDataSetChanged();
    }

    public void addxData(ArrayList<Xunjian> list){
        xjlist.addAll(list);
        notifyDataSetChanged();
    }

    public void initwData(ArrayList<Weixiu> list ){
        if (weixius!=null){
            weixius.clear();
        }
        weixius.addAll(list);
        notifyDataSetChanged();
    }

    public void addwData(ArrayList<Weixiu> list){
        weixius.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        int id;
        if (isWexiu){
            id=R.layout.item_weixiu;
            View view=View.inflate(context,id,parent);
            viewHolder=new WxViewHolder(view);
        }else {
            id=R.layout.item_xunjian;
            View view=View.inflate(context,id,parent);
            viewHolder=new XjViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isWexiu){
            WxViewHolder wxViewHolder= (WxViewHolder) holder;
            Weixiu weixiu=weixius.get(position);
            wxViewHolder.xjDate.setText(weixiu.getXjData());
            wxViewHolder.wxDate.setText(weixiu.getWxDate());
            wxViewHolder.wxUser.setText(weixiu.getControlUser());
            wxViewHolder.wxRemark.setText(weixiu.getRemark());
        }else {
            XjViewHolder xjViewHolder= (XjViewHolder) holder;
          Xunjian xunjian=xjlist.get(position);
            xjViewHolder.xJianDate.setText(xunjian.getXjDate());
            xjViewHolder.xjUser.setText(xunjian.getXjUser());
            xjViewHolder.status.setText(xunjian.getStatus());
            if (xunjian.getStatus().equals("异常")){
                xjViewHolder.cause.setText(xunjian.getCause());
            }else {
                xjViewHolder.cause.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return weixius==null?xjlist.size():weixius.size();
    }

}
