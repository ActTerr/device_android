package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;

/**
 * Created by mac-yk on 2017/5/3.
 */

public class DetailAdapter extends RecyclerView.Adapter {
    String[] data;
    Context context;
    int count=0;
    String TAG=getClass().getName();
    String[] titles = new String[]{
            "设备序号：", "锁版本：","设备分类：","生产厂家：","型号：","出厂日期：","使用日期：","使用单位：","使用地点：","序列号：" +
            "","编码：", "设备运行状态：","备注：","单价：","设备图片：","大类：","所属单位：","创建人：","创建时间：","创建单位：",
            "使用人：", "设备类型：","残值：","去向：","频率：","亚音频","调拨状态",
            "巡检天数：","到期日期：","巡检日期："};


    public DetailAdapter(Context context, String[] data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_detail, null);
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

        myHolder.content.setText(data[2*position]);
        myHolder.title.setText(titles[2*position]);
            myHolder.title2.setText(titles[2*position+1]);
            myHolder.content2.setText(data[2*position+ 1]);
    }

    @Override
    public int getItemCount() {
        return data.length/2;
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
            ButterKnife.bind(this,itemView);
        }
    }



}
