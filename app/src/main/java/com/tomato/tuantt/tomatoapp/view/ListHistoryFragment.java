package com.tomato.tuantt.tomatoapp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.adapter.HistoryAdapter;
import com.tomato.tuantt.tomatoapp.adapter.HistoryPagerAdapter;
import com.tomato.tuantt.tomatoapp.controller.ListHistoryController;
import com.tomato.tuantt.tomatoapp.model.MessageEvent;
import com.tomato.tuantt.tomatoapp.model.OrderData;
import com.tomato.tuantt.tomatoapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class ListHistoryFragment extends Fragment implements ListHistoryController.ListHistoryCallback, HistoryAdapter.ItemListener {

    public static final String TYPE = "TYPE";
    private static final int REQUEST_IMAGE_CAPTURE = 1010;
    private static final int REQUEST_CAMERA = 1011;

    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Unbinder mUnbinder;
    private HistoryAdapter mAdapter;
    private List<OrderData> mList = new ArrayList<>();
    private ListHistoryController mController;

    public static ListHistoryFragment newInstance(int historyType) {

        Bundle args = new Bundle();
        args.putInt(TYPE, historyType);
        ListHistoryFragment fragment = new ListHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new ListHistoryController(getActivity(), this);
        int type = HistoryPagerAdapter.TAB_TODO;
        if (getArguments() != null && getArguments().containsKey(ListHistoryFragment.TYPE)) {
            type = getArguments().getInt(ListHistoryFragment.TYPE);
        }
        mAdapter = new HistoryAdapter(getActivity(), mList, type, this);

        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mController.loadData(getArguments());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rvHistory.setLayoutManager(lm);
        rvHistory.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void setDataHistory(List<OrderData> list) {
        if (mAdapter != null && !Utils.isEmptyList(list)) {
            mList.clear();
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void deleteItem(int id) {
        if (!Utils.isEmptyList(mList)) {
            for (OrderData model : mList) {
                if (model != null && model.getId() == id) {
                    mList.remove(model);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    public void reloadHistoryList(int id) {
        if (!Utils.isEmptyList(mList)) {
            for (int i = 0; i < mList.size(); i++) {
                OrderData model = mList.get(i);
                if (model != null && model.getId() == id) {
                    mAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    @Override
    public void saveTimeChanged(int id) {
        if (mAdapter != null) {
            mAdapter.saveTimeChanged(id);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent != null) {
            switch (messageEvent.getEvent()) {
                case DELETE_ORDER:
                    if (!Utils.isEmptyList(mList)) {
                        for (OrderData data : mList) {
                            if (data != null && data.getId() == messageEvent.getId()) {
                                mController.deleteOrder(messageEvent.getId(), messageEvent.getPhoneNumber());
                                return;
                            }
                        }
                    }
                    break;
                case UPDATE_ORDER:
                    if (!Utils.isEmptyList(mList)) {
                        for (OrderData data : mList) {
                            if (data != null && data.getId() == messageEvent.getId()) {
                                mController.updateOrder(messageEvent.getId(), messageEvent.getHashMap());
                                return;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClickCamera(int position) {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                // TODO permission denied
            }
        }
    }
}
