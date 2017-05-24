package mac.yk.devicemanagement.ui.activity;

import android.view.View;

public class SaveActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {

    }
//    String id;
//
//    PopupWindow pop;
//    View v;
//    View v2;
//    @BindView(R.id.name)
//    TextView name;
//    @BindView(R.id.nameSelect)
//    ImageView nameSelect;
//    @BindView(R.id.chuchang)
//    TextView chuchang;
//    @BindView(R.id.chuchangBtn)
//    ImageView chuchangBtn;
//    @BindView(R.id.check)
//    TextView check;
//    @BindView(R.id.check_btn)
//    ImageView checkBtn;
//    @BindView(R.id.status)
//    TextView status;
//    @BindView(R.id.btn_save)
//    Button btnSave;
//    @BindView(R.id.statusSelect)
//    ImageView statusSelect;
//    boolean isBattery = false;
//    boolean isSelected = false;
//    Context context;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_save);
//        ButterKnife.bind(this);
//        context=this;
//        id = getIntent().getStringExtra("id");
//        if (id == null) {
//            MFGT.finish((Activity) context);
//        }
//        v = inflate(this, R.layout.popu_name_, null);
//        v2 = inflate(this, R.layout.popu_status, null);
//
//    }
//
//    public void onSave(View view) {
//
//        L.e("main", "save:" + name.getText().toString());
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.show();
//
//        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
//        subscription=wrapper.targetClass(ServerAPI.class).getAPI().
//                saveDevice(MyMemory.getInstance().getUser().getName(),"")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(wrapper.<String>applySchedulers())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        progressDialog.dismiss();
//                        if ( ExceptionFilter.filter(context,e)){
//                            ToastUtil.showToast(context,"请求失败!");
//                        }
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        progressDialog.dismiss();
//                        Toast.makeText(SaveActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
////                        MFGT.gotoDetailActivity(SaveActivity.this, deviceOld);
//                        MFGT.finish((Activity) context);
//                    }
//                });
//    }
//
//
//    private void showPopuWindow(View v) {
//        pop = new PopupWindow(v, ConvertUtils.dp2px(this, 100), ConvertUtils.dp2px(this, 140));
//        pop.setOutsideTouchable(true);
//        pop.setTouchable(true);
//        pop.setFocusable(true);
//        pop.setBackgroundDrawable(new BitmapDrawable());
//        pop.showAsDropDown(nameSelect);
//    }
//
//
//    class nameHolder{
//        public nameHolder() {
//            ButterKnife.bind(this,v);
//
//        }
//        @OnClick({R.id.transceiver, R.id.machine_controller, R.id.zone_controller, R.id.battery})
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case transceiver:
//                    name.setText("电台");
//                    break;
//                case R.id.machine_controller:
//                    name.setText("机控器");
//                    break;
//                case R.id.zone_controller:
//                    name.setText("区控器");
//                    break;
//                case R.id.battery:
//                    name.setText("电池");
//                    isBattery = true;
//                    break;
//            }
//            isSelected = true;
//            pop.dismiss();
//            statusHolder holder = new statusHolder();
//        }
//    }
//
//    class statusHolder {
//        @BindView(R.id.service)
//        TextView service;
//
//        public statusHolder() {
//            ButterKnife.bind(this, v2);
//            if (isBattery){
//                service.setText("充电");
//            }
//        }
//
//        @OnClick({R.id.spare, R.id.inactive, R.id.function, R.id.service, R.id.yonghou})
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.spare:
//                    status.setText("备用");
//                    break;
//                case R.id.inactive:
//                    status.setText("待用");
//                    break;
//                case R.id.function:
//                    status.setText("运行");
//                    break;
//                case R.id.service:
//                    if (isBattery) {
//                        status.setText("充电");
//                    } else {
//                        status.setText("维修");
//                    }
//                    break;
//                case R.id.yonghou:
//                    status.setText("用后");
//                    break;
//            }
//            pop.dismiss();
//        }
//    }
//
//    @OnClick({R.id.nameSelect, R.id.chuchangBtn, R.id.check_btn, R.id.btn_save, R.id.statusSelect})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.nameSelect:
//                nameHolder n=new nameHolder();
//                showPopuWindow(v);
//                break;
//            case R.id.chuchangBtn:
//                pickDate(chuchang);
//                break;
//            case R.id.check_btn:
//                pickDate(check);
//                break;
//            case R.id.btn_save:
//                onSave(view);
//                btnSave.setEnabled(false);
//                break;
//            case R.id.statusSelect:
//                if (isSelected) {
//                    showStatusWindow(v2);
//                } else {
//                    Toast.makeText(this, "请先选择设备名称", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//        }
//    }
//
//    private void showStatusWindow(View v2) {
//        int width= ConvertUtils.dp2px(this, 100);
//        int height=ConvertUtils.dp2px(this, 140);
//        L.e("main","w:"+width+" h:"+height);
//        pop = new PopupWindow(v2, width,height);
//        pop.setOutsideTouchable(true);
//        pop.setTouchable(true);
//        pop.setFocusable(true);
//        pop.setBackgroundDrawable(new BitmapDrawable());
//        pop.showAsDropDown(statusSelect);
//    }
//
//    private void pickDate(final TextView tv) {
//        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                tv.setText(String.format("%d-%d-%d", year, month+1, dayOfMonth));
//                //月份从0开始的，所以正常显示要加1
//            }
//        }, 2017, 1, 12).show();
//    }
}
