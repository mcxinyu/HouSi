package io.github.mcxinyu.housi.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.util.LogUtils;
import io.github.mcxinyu.housi.util.StaticValues;

/**
 * Created by huangyuefeng on 2017/9/17.
 * Contact me : mcxinyu@gmail.com
 */
public class SourceFileFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SourceFileFragment";

    private static final int SELECT_FILE_REQUEST_CODE = 1024;

    private PreferenceScreen mSourceFilePath;

    public static SourceFileFragment newInstance() {

        Bundle args = new Bundle();

        SourceFileFragment fragment = new SourceFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Callbacks mCallbacks;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (activity instanceof Callbacks) {
                mCallbacks = (Callbacks) activity;
            } else {
                throw new RuntimeException(activity.toString()
                        + " must implement Callbacks");
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks) {
            mCallbacks = (Callbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callbacks");
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.source_file_fragment);
        initPreferences();
    }

    private void initPreferences() {
        mSourceFilePath = (PreferenceScreen) findPreference("source_file_path");
        mSourceFilePath.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

                try {
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.select_a_file_to_apply)), SELECT_FILE_REQUEST_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), getString(R.string.please_install_a_file_manager), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                Uri uri = intent.getData();
                try {
                    String path = getUriFilePath(uri);
                    mSourceFilePath.setSummary(path);
                    new HandleHosts(path).execute();
                } catch (Exception e) {
                    mSourceFilePath.setSummary(getString(R.string.error));
                    e.printStackTrace();
                }
            }
        }
    }

    private String getUriFilePath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    class HandleHosts extends AsyncTask<Void, Void, Void> {
        private String mFilePath;
        ProgressDialog dialog = null;
        boolean suAvailable = false;
        List<String> suResult = null;

        public HandleHosts(String filePath) {
            mFilePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.do_on_reset_hosts));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            suAvailable = Shell.SU.available();
            if (!suAvailable) {
                return null;
            }

            suResult = Shell.SU.run(new String[]{
                    StaticValues.MOUNT_SYSTEM_RW,
                    "cp " + mFilePath + " " + StaticValues.SYSTEM_HOSTS_FILE_PATH,
                    StaticValues.MOUNT_SYSTEM_RO
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            if (!suAvailable) {
                Toast.makeText(getActivity(), getString(R.string.no_su), Toast.LENGTH_SHORT).show();
                // mCallbacks.hasRoot(suAvailable);
            } else if (suResult != null) {
                mSourceFilePath.setSummary(mFilePath + " " + getString(R.string.update_hosts_success));
                for (int i = 0; i < suResult.size(); i++) {
                    LogUtils.d(TAG, "suResult line " + i + " : " + suResult.get(i));
                }
                Toast.makeText(getActivity(),
                        suResult.size() == 0 ? getString(R.string.reset_hosts_success) : getString(R.string.reset_hosts_failure),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface Callbacks {
        // void hasRoot(boolean hasRoot);
    }
}
