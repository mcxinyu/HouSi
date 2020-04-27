package io.github.mcxinyu.housi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.fragment.ABaseFragment;
import io.github.mcxinyu.housi.fragment.ReadEditHostsFragment;

/**
 * Created by huangyuefeng on 2017/9/19.
 * Contact me : mcxinyu@gmail.com
 */
public class ReadEditHostsActivity extends BaseAppCompatActivity
        implements ABaseFragment.Callbacks {

    public static String sHostsString;

    private static final String EXTRA_EDIT = "extra_edit";

    private FragmentManager mFragmentManager;
    private boolean mBooleanExtra;

    public static Intent newIntent(Context context) {
        return newIntent(context, false);
    }

    public static Intent newIntent(Context context, boolean edit) {

        Intent intent = new Intent(context, ReadEditHostsActivity.class);
        intent.putExtra(EXTRA_EDIT, edit);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout_container);
        if (getIntent() != null) {
            mBooleanExtra = getIntent().getBooleanExtra(EXTRA_EDIT, false);
        }

        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = ReadEditHostsFragment.newInstance(mBooleanExtra);
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void setDrawerMenuClicked(int item) {

    }

    @Override
    public void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sHostsString = null;
    }
}
