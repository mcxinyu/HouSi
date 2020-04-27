package io.github.mcxinyu.housi.util;

import android.content.Context;
import android.util.AttributeSet;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by huangyuefeng on 2019/1/12.
 * Contact me : mcxinyu@foxmail.com
 */
public class ObservableRichEditor extends RichEditor {

    public ObservableRichEditor(Context context) {
        super(context);
    }

    public ObservableRichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableRichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallbacks != null) {
            mOnScrollChangedCallbacks.onScroll(l, t, oldl, oldt);
        }
    }

    private OnScrollChangedCallbacks mOnScrollChangedCallbacks;

    public void setOnScrollChangedCallbacks(OnScrollChangedCallbacks onScrollChangedCallbacks) {
        mOnScrollChangedCallbacks = onScrollChangedCallbacks;
    }

    public interface OnScrollChangedCallbacks {
        void onScroll(int x, int y, int oldX, int oldY);
    }
}
