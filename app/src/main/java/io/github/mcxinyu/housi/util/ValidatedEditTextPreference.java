package io.github.mcxinyu.housi.util;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * Created by huangyuefeng on 2017/9/19.
 * Contact me : mcxinyu@gmail.com
 */
public class ValidatedEditTextPreference extends EditTextPreference {

    public ValidatedEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ValidatedEditTextPreference(Context context) {
        super(context);
    }

    @Override
    protected void showDialog(Bundle state) {
        // super.showDialog(state);
    }
}
