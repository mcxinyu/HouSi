package io.github.mcxinyu.housi.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.api.SourceApiHelper;
import io.github.mcxinyu.housi.bean.SourceConfig;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangyuefeng on 2017/9/17.
 * Contact me : mcxinyu@gmail.com
 */
public class SourceBuiltInFragment extends PreferenceFragment {
    private SourceConfig mSourceConfig;

    PreferenceScreen mSourceBuiltInUpdate;
    ListPreference mSourceBuiltInList;
    EditTextPreference mSourceBuiltInDownloadUrl;
    PreferenceScreen mSourceBuiltInDate;

    public static SourceBuiltInFragment newInstance() {

        Bundle args = new Bundle();

        SourceBuiltInFragment fragment = new SourceBuiltInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.source_built_in_fragment);
        initPreferences();
    }

    private void initPreferences() {
        mSourceBuiltInUpdate = (PreferenceScreen) findPreference("source_built_in_update");
        mSourceBuiltInList = (ListPreference) findPreference("source_built_in_item");
        mSourceBuiltInDownloadUrl = (EditTextPreference) findPreference("source_built_in_download_url");
        mSourceBuiltInDate = (PreferenceScreen) findPreference("source_built_in_date");

        mSourceBuiltInUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mSourceBuiltInUpdate.setSummary(getString(R.string.source_built_in_update_refresh));
                getSourceConfig();
                return false;
            }
        });
    }

    public void getSourceConfig() {
        SourceApiHelper.getSourceConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SourceConfig>() {
                    @Override
                    public void onCompleted() {
                        if (mSourceConfig == null) {
                            mSourceBuiltInUpdate.setSummary(getString(R.string.source_built_in_update_refresh_failure));
                            return;
                        }
                        updateUI();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSourceBuiltInUpdate.setSummary(getString(R.string.source_built_in_update_refresh_failure));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SourceConfig sourceConfig) {
                        if (sourceConfig != null)
                            mSourceConfig = sourceConfig;
                    }
                });
    }

    private void updateUI() {
        mSourceBuiltInUpdate.setSummary(getString(R.string.source_built_in_update_refresh_success));

        mSourceBuiltInList.setEntryValues(mSourceConfig.getValueArray());
        mSourceBuiltInList.setEntries(mSourceConfig.getNameArray());
        mSourceBuiltInList.setDefaultValue(mSourceConfig.getNameArray()[0]);
        mSourceBuiltInList.setSummary(mSourceConfig.getNameArray()[0]);
        mSourceBuiltInList.setSelectable(true);
        mSourceBuiltInList.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        mSourceBuiltInDownloadUrl.setText(mSourceConfig.getResult().get(0).getUrl());
        mSourceBuiltInDownloadUrl.setSummary(mSourceConfig.getResult().get(0).getUrl());
        mSourceBuiltInDownloadUrl.setSelectable(true);
    }
}
