package io.github.mcxinyu.housi.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.github.mcxinyu.housi.util.LogUtils;

/**
 * Created by huangyuefeng on 2017/10/16.
 * Contact me : mcxinyu@gmail.com
 */
public class FCMInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMInstanceIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.d(TAG, refreshToken);
    }
}
