package io.github.mcxinyu.housi.fragment;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.util.QueryPreferences;

/**
 * Created by huangyuefeng on 2017/9/17.
 * Contact me : mcxinyu@gmail.com
 */
public class SourceDiyFragment extends PreferenceFragmentCompat {
    private EditTextPreference mSourceDiyDownloadUrl;

    public static SourceDiyFragment newInstance() {

        Bundle args = new Bundle();

        SourceDiyFragment fragment = new SourceDiyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.source_diy_fragment);
        initPreference();
    }

    private void initPreference() {
        mSourceDiyDownloadUrl = (EditTextPreference) findPreference("source_diy_download_url");
        mSourceDiyDownloadUrl.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (!TextUtils.isEmpty(mSourceDiyDownloadUrl.getText())) {
                    QueryPreferences.setSourceRouting(getActivity(), 1);
                }
                return false;
            }
        });
    }
}
