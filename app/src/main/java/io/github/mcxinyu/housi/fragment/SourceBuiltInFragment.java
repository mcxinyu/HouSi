package io.github.mcxinyu.housi.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.Toast;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.api.SourceApiHelper;
import io.github.mcxinyu.housi.bean.SourceConfig;
import io.github.mcxinyu.housi.util.QueryPreferences;
import io.github.mcxinyu.housi.util.ValidatedEditTextPreference;
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
    ValidatedEditTextPreference mSourceBuiltInDownloadUrl;
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
        mSourceBuiltInDownloadUrl = (ValidatedEditTextPreference) findPreference("source_built_in_download_url");
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
        QueryPreferences.setSourceRouting(getActivity(), 0);

        mSourceBuiltInUpdate.setSummary(getString(R.string.source_built_in_update_refresh_when) + " " + mSourceConfig.getServerTime());

        mSourceBuiltInList.setEntryValues(mSourceConfig.getValueArray());
        mSourceBuiltInList.setEntries(mSourceConfig.getNameArray());
        mSourceBuiltInList.setValue(mSourceConfig.getValueArray()[0]);
        mSourceBuiltInList.setSummary(mSourceConfig.getNameArray()[0]);
        mSourceBuiltInList.setSelectable(true);
        mSourceBuiltInList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                mSourceBuiltInList.setSummary(mSourceConfig.getNameArray()[Integer.parseInt((String) o)]);
                mSourceBuiltInDownloadUrl.setText(mSourceConfig.getResult().get(Integer.parseInt((String) o)).getUrl());
                mSourceBuiltInDownloadUrl.setSummary(mSourceConfig.getResult().get(Integer.parseInt((String) o)).getUrl());
                return true;
            }
        });

        mSourceBuiltInDownloadUrl.setText(mSourceConfig.getResult().get(0).getUrl());
        mSourceBuiltInDownloadUrl.setSummary(mSourceConfig.getResult().get(0).getUrl());
        mSourceBuiltInDownloadUrl.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newRawUri(preference.getSummary(),
                        Uri.parse(preference.getSummary().toString()));
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(), getString(R.string.clipboard_hint), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mSourceBuiltInDownloadUrl.setSelectable(true);
    }
}
