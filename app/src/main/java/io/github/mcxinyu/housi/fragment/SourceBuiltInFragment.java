package io.github.mcxinyu.housi.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.api.SourceApiHelper;
import io.github.mcxinyu.housi.bean.SourceConfig;
import io.github.mcxinyu.housi.util.LogUtils;
import io.github.mcxinyu.housi.util.QueryPreferences;
import moe.shizuku.preference.ListPreference;
import moe.shizuku.preference.MultiSelectListPreference;
import moe.shizuku.preference.Preference;
import moe.shizuku.preference.PreferenceFragment;
import moe.shizuku.preference.PreferenceScreen;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangyuefeng on 2017/9/17.
 * Contact me : mcxinyu@gmail.com
 */
public class SourceBuiltInFragment extends PreferenceFragment {
    private static final String TAG = SourceBuiltInFragment.class.getSimpleName();

    private SourceConfig mSourceConfig;

    PreferenceScreen mSourceBuiltInUpdate;
    MultiSelectListPreference mSourceBuiltInMultiList;
    ListPreference mSourceBuiltInMultiDownloadUrl;
    PreferenceScreen mSourceBuiltInDate;

    public static SourceBuiltInFragment newInstance() {

        Bundle args = new Bundle();

        SourceBuiltInFragment fragment = new SourceBuiltInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.source_built_in_fragment);
        initPreferences();
    }

    private void initPreferences() {
        mSourceBuiltInUpdate = (PreferenceScreen) findPreference("source_built_in_update");
        mSourceBuiltInMultiList = (MultiSelectListPreference) findPreference("source_built_in_item_multi");
        mSourceBuiltInMultiDownloadUrl = (ListPreference) findPreference("source_built_in_download_url_multi");
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

        mSourceBuiltInMultiList.setEntryValues(mSourceConfig.getValueArray());
        mSourceBuiltInMultiList.setEntries(mSourceConfig.getNameArray());
        Set<String> valuesSet = new HashSet<>();
        valuesSet.add(mSourceConfig.getValueArray()[0]);
        mSourceBuiltInMultiList.setValues(valuesSet);
        mSourceBuiltInMultiList.setSummary(mSourceConfig.getNameArray()[0]);
        mSourceBuiltInMultiList.setSelectable(true);
        mSourceBuiltInMultiList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String[] select = new String[((Set<String>) o).size()];
                ((Set<String>) o).toArray(select);
                if (select.length == 0) {
                    return false;
                } else if (select.length == 1) {
                    preference.setSummary(mSourceConfig.getNameArray()[Integer.parseInt(select[0])]);
                } else {
                    preference.setSummary(String.format(getString(R.string.selected_sources), select.length));
                }
                String[] urlEntries = new String[select.length];
                for (int i = 0; i < select.length; i++) {
                    urlEntries[i] = mSourceConfig.getUrlArray()[Integer.parseInt(select[i])];
                }
                QueryPreferences.setSourceBuiltInMultiDownloadUrl(getContext(),
                        new HashSet<>(Arrays.asList(urlEntries)));
                mSourceBuiltInMultiDownloadUrl.setEntryValues(select);
                mSourceBuiltInMultiDownloadUrl.setEntries(urlEntries);
                return true;
            }
        });

        String[] values = new String[valuesSet.size()];
        valuesSet.toArray(values);
        String[] urlEntries = new String[valuesSet.size()];
        for (int i = 0; i < values.length; i++) {
            urlEntries[i] = mSourceConfig.getUrlArray()[Integer.parseInt(values[i])];
        }
        QueryPreferences.setSourceBuiltInMultiDownloadUrl(getContext(),
                new HashSet<>(Arrays.asList(urlEntries)));
        mSourceBuiltInMultiDownloadUrl.setEntryValues(values);
        mSourceBuiltInMultiDownloadUrl.setEntries(urlEntries);
        mSourceBuiltInMultiDownloadUrl.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                LogUtils.d(TAG + ": " + "onPreferenceChange", newValue.toString());
                String[] urlEntries = (String[]) ((ListPreference) preference).getEntries();

                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newRawUri("url", Uri.parse(urlEntries[Integer.parseInt((String) newValue)]));
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(), getString(R.string.clipboard_hint), Toast.LENGTH_SHORT).show();
                LogUtils.d(TAG + ": " + "onPreferenceChange", urlEntries[Integer.parseInt((String) newValue)]);
                return true;
            }
        });
        mSourceBuiltInMultiDownloadUrl.setSelectable(true);
    }
}
