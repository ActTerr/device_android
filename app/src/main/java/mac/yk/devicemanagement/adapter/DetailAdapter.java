package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;

/**
 * Created by mac-yk on 2017/5/3.
 */

public class DetailAdapter extends RecyclerView.Adapter {
    List<String> data;
    Context context;
    int count=0;
    String[] titles = new String[]{
            "设备序号：", "设备分类：","生产厂家：","型号：","出厂日期：","使用日期：","使用单位：","使用地点：","序列号：","编码：",
            "设备运行状态：","备注：","单价：","设备图片：","大类：","所属单位：","创建人：","创建时间：","创建单位：","使用人：",
            "设备类型：","残值：","去向：","频率：","亚音频","调拨状态",
            "巡检天数：","到期日期：","巡检日期："
    };
    /**
     * 用于存储对应索引
     */
    HashMap<Integer,Integer> memory=new HashMap<>();


    public DetailAdapter(Context context, List<String> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_detail, null);
//            view.setTag(viewType);
        return new myHolder(view);
    }

    /**
     * 排序待优化
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        myHolder myHolder = (DetailAdapter.myHolder) holder;
        int type = myHolder.getItemViewType();
        int realPosition;
        if (memory.get(position)==null){
            if (position==0){
                realPosition=0;
            }else {
                realPosition=memory.get(position-1)+1;
                if (type==1){
                    memory.put(position,realPosition+1);
                }else{
                    memory.put(position,realPosition);
                }
            }
        }else {
            realPosition=memory.get(position);
        }
        myHolder.content.setText(data.get(realPosition));
        myHolder.title.setText(titles[realPosition]);
        if (type == 1) {
            myHolder.title2.setText(titles[realPosition+1]);
            myHolder.content2.setText(data.get(realPosition + 1));
        } else {
            myHolder.ll2.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).length() > 15) {
            return 2;
        } else {
            return 1;
        }
    }

    class myHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.title2)
        TextView title2;
        @BindView(R.id.content2)
        TextView content2;
        @BindView(R.id.ll2)
        LinearLayout ll2;

        public myHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }


}
