package io.github.mcxinyu.housi.fragment;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.util.QueryPreferences;

/**
 * Created by huangyuefeng on 2017/9/17.
 * Contact me : mcxinyu@gmail.com
 */
public class SourceDiyFragment extends PreferenceFragment {
    private EditTextPreference mSourceDiyDownloadUrl;

    public static SourceDiyFragment newInstance() {

        Bundle args = new Bundle();

        SourceDiyFragment fragment = new SourceDiyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
