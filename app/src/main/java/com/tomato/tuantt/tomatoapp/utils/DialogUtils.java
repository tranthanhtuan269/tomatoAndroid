package com.tomato.tuantt.tomatoapp.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tomato.tuantt.tomatoapp.R;

public class DialogUtils {

    public static void showDialogConfirmDeleteOrder(Context context, MaterialDialog.SingleButtonCallback onNegativeCallback) {
        new MaterialDialog.Builder(context)
                .content(R.string.msg_delete_order)
                .negativeText(R.string.txt_sure)
                .onNegative(onNegativeCallback)
                .positiveText(R.string.cancel)
                .show();
    }
}
