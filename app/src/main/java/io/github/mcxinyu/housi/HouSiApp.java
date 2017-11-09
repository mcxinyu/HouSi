package io.github.mcxinyu.housi;

import android.app.Application;

import com.google.firebase.iid.FirebaseInstanceId;

import io.github.mcxinyu.housi.util.LogUtils;

/**
 * Created by huangyuefeng on 2017/9/13.
 * Contact me : mcxinyu@gmail.com
 */
public class HouSiApp extends Application {
    private static final String TAG = "HouSiApp";
    private static HouSiApp houSiApp;

    public static HouSiApp getInstance() {
        return houSiApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        houSiApp = this;
        LogUtils.d(TAG, FirebaseInstanceId.getInstance().getToken() + "");
    }
}
