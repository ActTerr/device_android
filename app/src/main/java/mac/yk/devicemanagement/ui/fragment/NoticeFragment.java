package mac.yk.devicemanagement.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.Notice;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.observable.Update;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.Subscriber;

/**
 * 公告列表
 */

public class NoticeFragment extends BaseFragment implements java.util.Observer {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    Context context;
    NoticeAdapter adapter;
    ArrayList<Notice> data;
    CustomDialog pd;
    int memory;

    @BindView(R.id.btn_down)
    Button btnDown;

    String TAG = "NoticeFragment";
    Update update = new Update();
    boolean isMore = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notice, container, false);
        ButterKnife.bind(this, view);
        update.addObserver(this);
        MyMemory.getInstance().setUpdate(update);
        context = getContext();
        showIv();
        data = new ArrayList<>();
        adapter = new NoticeAdapter(context, data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));
        pd = CustomDialog.create(getContext(),"加载中...",false,null);
        btnDown.setVisibility(View.GONE);
        initData();
        return view;
    }


    private void showIv() {
        if (MyMemory.getInstance().getUser().getGrade() == 0) {
            ivAdd.setVisibility(View.VISIBLE);
        } else {
            ivAdd.setVisibility(View.GONE);
        }
    }

    public void refresh() {
        data.clear();
        initData();
    }

    private void initData() {
        pd.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getNotices(memory)
                .compose(wrapper.<ArrayList<Notice>>applySchedulers())
                .subscribe(new Observer<ArrayList<Notice>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showException(context);
                        }
                        isMore = false;
                    }

                    @Override
                    public void onNext(ArrayList<Notice> notices) {
                        pd.dismiss();
                        data.addAll(notices);
                        adapter.notifyDataSetChanged();
                        if (notices.size() < 8) {
                            isMore = false;
                        }
                    }
                });
    }

    @OnClick({R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_add:
                MFGT.gotoNoticeDetail(context, true, null);
                break;
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//    }

    @Override
    public void update(Observable o, Object arg) {
        L.e(TAG, "refresh");
        if (update.getType() == Update.updateData) {
            refresh();
        }
    }

    int mark=-1;
    RecyclerView.ViewHolder memoryHolder;

    private void setUnMark(){
        if (memoryHolder!=null){
            memoryHolder.itemView.setBackgroundResource(R.color.gray2);
            NoticeAdapter.NoticeViewHolder holder= (NoticeAdapter.NoticeViewHolder) memoryHolder;
            holder.ivDelete.setVisibility(View.GONE);
            holder.ivEdit.setVisibility(View.GONE);
            memoryHolder=null;
        }
        mark=-1;
    }
    public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        String TAG = "NoticeAdapter";
        ArrayList<Notice> list;

        User user;
        CustomDialog dialog;


        private int footer = 1;
        private int item = 2;

        public NoticeAdapter(Context context, ArrayList<Notice> list) {
            this.context = context;
            this.list = list;
            dialog= CustomDialog.create(getContext(),"加载中...",false,null);
            user = MyMemory.getInstance().getUser();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            if (viewType == item) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false);
                holder = new NoticeViewHolder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.item_footer, parent, false);
                holder = new FooterViewHolder(view);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder.getItemViewType() == footer) {
                return;
            }

            final NoticeViewHolder nHolder = (NoticeViewHolder) holder;
            final Notice notice = list.get(position);

            if (user.getGrade() == 0) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        L.e("attachment","long click");
                        if (mark!=-1){
                            setUnMark();
                        }
                        mark=position;
                        memoryHolder=holder;
                        holder.itemView.setBackgroundResource(R.drawable.long_click_bg);
                        nHolder.ivDelete.setVisibility(View.VISIBLE);
                        nHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog dialog = new AlertDialog.Builder(context)
                                        .setTitle("确定删除该条公告吗？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                postDelete(notice);
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create();
                                dialog.show();
                            }
                        });
                        nHolder.ivEdit.setVisibility(View.VISIBLE);
                        nHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MFGT.gotoNoticeDetail(context, true, notice);
                            }
                        });
                        return true;
                    }
                });

            }
            nHolder.noticePosition.setText(position + 1 + ".");
            nHolder.noticeTitle.setText(notice.getTitle());
            nHolder.noticeTime.setText(ConvertUtils.Date2String(new Date(notice.getNid())));
            nHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.e("attachment","short click");
                    if (mark!=-1){
                        if (mark!=position){
                            setUnMark();
                    }} else {
                        MFGT.gotoNoticeDetail(context, false, notice);
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return isMore ? list.size() + 1 : list.size();
        }

        @Override
        public int getItemViewType(int position) {

            return position == list.size() ? footer : item;
        }

        private void postDelete(final Notice notice) {
            dialog.show();
            ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
            wrapper.targetClass(ServerAPI.class).getAPI().deleteNotice(notice.getNid())
                    .compose(wrapper.<String>applySchedulers())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            if (ExceptionFilter.filter(context, e)) {
                                ToastUtil.showException(context);
                            }
                        }

                        @Override
                        public void onNext(String s) {
                            dialog.dismiss();
                            list.remove(notice);
                            notifyDataSetChanged();
                            ToastUtil.showToast(context, "删除成功!");
                        }
                    });
        }

        class NoticeViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.notice_position)
            TextView noticePosition;
            @BindView(R.id.notice_title)
            TextView noticeTitle;
            @BindView(R.id.notice_time)
            TextView noticeTime;
            @BindView(R.id.iv_edit)
            ImageView ivEdit;
            @BindView(R.id.iv_delete)
            ImageView ivDelete;

            public NoticeViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        class FooterViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.add_history)
            TextView addHistory;
            public FooterViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                addHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        memory+=8;
                        initData();
                    }
                });
            }

        }
    }
}
