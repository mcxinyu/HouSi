package io.github.mcxinyu.housi.services;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import io.github.mcxinyu.housi.activity.MainActivity;

/**
 * Created by huangyuefeng on 2017/10/30.
 * Contact me : mcxinyu@gmail.com
 */
public class AuthenticationService extends Service {
    private static final String ACCOUNT_TYPE = "io.github.mcxinyu.housi.account.anonymous";

    private AccountAuthenticator mAuthenticator;

    private AccountAuthenticator getAuthenticator() {
        if (mAuthenticator == null) {
            mAuthenticator = new AccountAuthenticator(this);
        }
        return mAuthenticator;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getAuthenticator().getIBinder();
    }

    class AccountAuthenticator extends AbstractAccountAuthenticator {

        public AccountAuthenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response,
                                 String accountType, String authTokenType,
                                 String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(AuthenticationService.this, MainActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response,
                                         Account account, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response,
                                   Account account, String authTokenType,
                                   Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response,
                                        Account account, String authTokenType,
                                        Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response,
                                  Account account, String[] features) throws NetworkErrorException {
            return null;
        }
    }
}
