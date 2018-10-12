package com.tomato.tuantt.tomatoapp.createorder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Anh Nguyen on 10/12/2018.
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//            if (onKeyBack !=null) {
//                onKeyBack.onKeyback();
//            }
//        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onKeyBack !=null) {
                onKeyBack.onKeyback();
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    OnKeyBack onKeyBack;
    public void setOnKeyBack(OnKeyBack onKeyBack){
        this.onKeyBack = onKeyBack;
    }
    public interface OnKeyBack{
        void onKeyback();
    }
}
