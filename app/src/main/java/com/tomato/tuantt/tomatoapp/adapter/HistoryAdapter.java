package com.tomato.tuantt.tomatoapp.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.SharedPreferenceConfig;
import com.tomato.tuantt.tomatoapp.model.Event;
import com.tomato.tuantt.tomatoapp.model.MessageEvent;
import com.tomato.tuantt.tomatoapp.model.OrderData;
import com.tomato.tuantt.tomatoapp.model.Service;
import com.tomato.tuantt.tomatoapp.utils.DialogUtils;
import com.tomato.tuantt.tomatoapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private static final String TAG = HistoryAdapter.class.getSimpleName();

    private List<OrderData> mList;

    private Context mContext;
    private Calendar mTime;
    private boolean isTmp;
    private int mType;
    private ItemListener mItemListener;

    public HistoryAdapter(Context context, List<OrderData> list, int type, ItemListener itemListener) {
        this.mList = list;
        this.mContext = context;
        this.mType = type;
        this.mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderData orderData = mList.get(position);
        if (holder == null || orderData == null) {
            return;
        }
        holder.tvAddress.setText(orderData.getAddress());
        holder.tvPrice.setText(getPrice(orderData));
        holder.tvStartTime.setText(getStartTime(orderData.getStart_time()));
        holder.tvHouseNumber.setText(getHouseNumber(orderData.getNumber_address()));
        holder.ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemListener != null) {
                    mItemListener.onClickCamera(position);
                }
            }
        });

        Service service = orderData.getService();
        if (service != null) {
            holder.tvJobName.setText(service.getName());
            int drawableId = mContext.getResources().getIdentifier(service.getIcon(), "drawable", mContext.getPackageName());
            if (drawableId == 0) {
                drawableId = R.drawable.donvesinh;
            }
            holder.ivType.setImageResource(drawableId);
        }
        if (mType == HistoryPagerAdapter.TAB_TODO) {
            holder.ivMore.setImageResource(R.drawable.ic_more_vert_gray_24dp);
            holder.tvJobStatus.setText(R.string.txt_new_post);
            holder.tvJobStatus.setTextColor(ContextCompat.getColor(mContext, R.color.colorNumberBuy));
            holder.lnlStatus.setVisibility(View.VISIBLE);
        } else {
            holder.ivMore.setImageResource(R.drawable.ic_delete_gray_24dp);
            holder.tvJobStatus.setText(R.string.txt_accepted);
            holder.tvJobStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_gray));
            holder.lnlStatus.setVisibility(View.GONE);
        }

        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mType == HistoryPagerAdapter.TAB_TODO) {
                        mTime = Calendar.getInstance();
                        mTime.setTime(new Date(Long.parseLong(orderData.getStart_time())));
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(mContext, holder.ivMore);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.menu_option_history);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.action_change_time:
                                        showDatePicker(orderData.getStart_time(), holder.tvStartTime, position);
                                        return true;
                                    case R.id.action_delete:
                                        DialogUtils.showDialogConfirmDeleteOrder(mContext, new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                EventBus.getDefault().post(new MessageEvent(Event.DELETE_ORDER, orderData.getId(), orderData.getUser() != null ? orderData.getUser().getPhone() : ""));
                                            }
                                        });
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();
                    } else {
                        DialogUtils.showDialogConfirmDeleteOrder(mContext, new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                EventBus.getDefault().post(new MessageEvent(Event.DELETE_ORDER, orderData.getId(), orderData.getUser() != null ? orderData.getUser().getPhone() : ""));
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        });
    }



    private String getHouseNumber(String houseNumber) {
        if (!TextUtils.isEmpty(houseNumber) && !"null".equals(houseNumber)) {
            return houseNumber;
        }
        return "";
    }

    private String getStartTime(String startTime) {
        try {
            Date date = new Date(Long.parseLong(startTime));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyy", Locale.getDefault());
            return sdf.format(date);
        } catch (NumberFormatException ne) {
            android.util.Log.e(TAG, android.util.Log.getStackTraceString(ne));
        }
        return "";
    }

    private String getStartTime(Date startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyy", Locale.getDefault());
        return sdf.format(startTime);
    }

    @Override
    public int getItemCount() {
        if (!Utils.isEmptyList(mList)) {
            return mList.size();
        }
        return 0;
    }

    private String getPrice(OrderData model) {
        if (model == null) {
            return "";
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Long.valueOf(model.getPrice())) + " " + mContext.getString(R.string.txt_vnd);
    }

    private void showDatePicker(final String startTime, final TextView tvStartTime, final int position) {
        try {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(Long.parseLong(startTime)));
            Locale.setDefault(new Locale("vi"));
            DatePickerDialog dialog = new DatePickerDialog(mContext, R.style.MyDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    isTmp = false;
                    mTime.set(year, month, dayOfMonth);
                    showTimePicker(startTime, tvStartTime, position);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            Calendar calendar1 = (Calendar) Calendar.getInstance().clone();
            calendar1.add(Calendar.DAY_OF_YEAR, 7);
            dialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());
            calendar1.add(Calendar.DAY_OF_YEAR, -7);
            dialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
            dialog.setTitle("");
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void showTimePicker(String startTime, final TextView tvStartTime, final int position) {
        try {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(Long.parseLong(startTime)));
            TimePickerDialog dialog = new TimePickerDialog(mContext, R.style.MyDatePickerStyle, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    isTmp = false;
                    mTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mTime.set(Calendar.MINUTE, minute);
                    updateTimeData(tvStartTime, position);
                }
            }, mTime.get(Calendar.HOUR_OF_DAY), mTime.get(Calendar.MINUTE), true);
            dialog.setTitle("");
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    updateTimeData(tvStartTime, position);
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void updateTimeData(TextView tvStartTime, int position) {
        if (tvStartTime != null) {
            tvStartTime.setText(getStartTime(mTime.getTime()));
        }

        if (!Utils.isEmptyList(mList) && mList.size() > position) {
            OrderData model = mList.get(position);
            if (model == null) {
                return;
            }
            SharedPreferenceConfig preferenceConfig = SharedPreferenceConfig.getInstance(mContext);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("address", model.getAddress());
            params.put("start_time", String.valueOf(mTime.getTimeInMillis()));
            params.put("end_time", model.getEnd_time());
            params.put("price", model.getPrice());
            params.put("phone", model.getUser().getPhone());
            params.put("access_token", preferenceConfig.getToken());
            JSONArray jsonArray = new JSONArray(model.getPackages());
            params.put("list_packages", jsonArray.toString());

            EventBus.getDefault().post(new MessageEvent(Event.UPDATE_ORDER, model.getId(), params));
        }
    }

    public void saveTimeChanged(int id) {
        if (!Utils.isEmptyList(mList)) {
            for (int i = 0; i < mList.size(); i++) {
                OrderData model = mList.get(i);
                if (model != null && model.getId() == id) {
                    model.setStart_time(String.valueOf(mTime.getTimeInMillis()));
                    notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_type)
        ImageView ivType;
        @BindView(R.id.iv_camera)
        ImageView ivCamera;
        @BindView(R.id.tv_job_name)
        TextView tvJobName;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.tv_job_status)
        TextView tvJobStatus;
        @BindView(R.id.tv_start_time)
        TextView tvStartTime;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_house_number)
        TextView tvHouseNumber;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_payments)
        TextView tvPayments;
        @BindView(R.id.lnl_status_worker)
        LinearLayout lnlStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        void onClickCamera(int position);
    }
}
