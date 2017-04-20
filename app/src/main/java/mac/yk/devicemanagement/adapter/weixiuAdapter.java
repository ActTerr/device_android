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
import mac.yk.devicemanagement.bean.Weixiu;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class weixiuAdapter extends RecyclerView.Adapter<weixiuAdapter.WxViewHolder> {
    ArrayList<Weixiu> weixius = new ArrayList<>();
    Context context;

    final static String TAG="weixiuAdapter";
    boolean isLast=false;
    public weixiuAdapter(Context context) {

        this.context = context;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public void changeData(ArrayList<Weixiu> list) {
            synchronized (weixius){
                if (weixius != null) {
                    weixius.clear();
                }
                L.e(TAG,"init:被执行");
                L.e(TAG, "list:" + list.size());

                weixius.addAll(list);

            }
        }

    public void addwData(ArrayList<Weixiu> list) {
        L.e("main","add被执行");
        for (Weixiu weixiu:list){
            L.e(TAG,weixiu.toString());
        }
        synchronized (weixius){
            weixius.addAll(list);
        }
    }
    public void refresh(){
        notifyDataSetChanged();
    }


    @Override
    public WxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weixiu, parent, false);
        /**
         * 如果切换的话(复用），item绝对不能传入parent;
         */
       WxViewHolder viewHolder = new WxViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WxViewHolder holder, int position) {
        L.e(TAG,"execute bind");
       Weixiu weixiu = weixius.get(position);
        holder.wxDate.setText(ConvertUtils.Date2String(weixiu.getWxDate()));
        if (weixiu.getXjDate() != null) {
            holder.xjDate.setText(ConvertUtils.Date2String(weixiu.getXjDate()));
            holder.wxUser.setText(weixiu.getControlUser());
            holder.wxRemark.setText(weixiu.getRemark());
            holder.translate.setText(getTranText(weixiu.isTranslate()));
        }else {
            holder.xjDate.setText("");
            holder.wxUser.setText("");
            holder.wxRemark.setText("维修中......");
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
        return weixius.size();
    }

    class WxViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.xjDate)
        TextView xjDate;

        @BindView(R.id.wxDate)
        TextView wxDate;

        @BindView(R.id.name)
        TextView wxUser;

        @BindView(R.id.remark)
        TextView wxRemark;

        @BindView(R.id.translate)
        TextView translate;

        public WxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
