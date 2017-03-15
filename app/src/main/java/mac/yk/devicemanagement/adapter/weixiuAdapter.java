package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.ui.holder.WxViewHolder;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class weixiuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Weixiu> weixius=new ArrayList<>();
    Context context;


    public weixiuAdapter(Context context) {

        this.context = context;
    }


    public void initwData(ArrayList<Weixiu> list ){
        if (weixius!=null){
            weixius.clear();
        }
        L.e("main","list:"+list.toString());
        if (weixius!=null){
            weixius.addAll(list);
        }
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

            id=R.layout.item_weixiu;
            View view=View.inflate(context,id,null);
            /**
             * 如果切换的话(复用），item绝对不能传入parent;
             */
            viewHolder=new WxViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            WxViewHolder wxViewHolder= (WxViewHolder) holder;
            Weixiu weixiu=weixius.get(position);
            wxViewHolder.xjDate.setText(String.valueOf(weixiu.getXjData()));
            wxViewHolder.wxDate.setText(String.valueOf(weixiu.getWxDate()));
            wxViewHolder.wxUser.setText(weixiu.getControlUser());
            wxViewHolder.wxRemark.setText(weixiu.getRemark());
            wxViewHolder.translate.setText(getTranText(weixiu.isTranslate()));

    }

    private String getTranText(boolean translate) {
        if (translate){
            return "已更换";
        }else {
            return "未更换";
        }
    }

    @Override
    public int getItemCount() {
        return weixius.size();
    }

}
