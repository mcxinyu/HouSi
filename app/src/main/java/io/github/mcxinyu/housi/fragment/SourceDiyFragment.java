package io.github.mcxinyu.housi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.text.TextUtils;
import android.widget.Toast;

import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.net.MalformedURLException;
import java.net.URL;

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
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.source_diy_fragment);
        initPreference();
    }

    private void initPreference() {
        mSourceDiyDownloadUrl = (EditTextPreference) findPreference("source_diy_download_url");
        mSourceDiyDownloadUrl.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (!TextUtils.isEmpty(mSourceDiyDownloadUrl.getEditText().getText())) {
                    URL address;
                    try {
                        address = new URL((String) o);
                    } catch (MalformedURLException e) {
                        Toast.makeText(getContext(), getString(R.string.malformed_url_exception), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return false;
                    }
                    QueryPreferences.setSourceRouting(getContext(), 1);
                    preference.setSummary(address.toString());
                }
                return true;
            }
        });

        String text = QueryPreferences.getSourceDiyDownloadUrl(getContext());
        if (!TextUtils.isEmpty(text)) {
            mSourceDiyDownloadUrl.setText(text);
            mSourceDiyDownloadUrl.setSummary(text);
        }
    }
}
