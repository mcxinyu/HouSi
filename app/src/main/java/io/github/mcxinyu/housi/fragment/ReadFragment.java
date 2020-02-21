package io.github.mcxinyu.housi.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import androidx.core.widget.NestedScrollView;
import androidx.core.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.chainfire.libsuperuser.Shell;
import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.util.StaticValues;

/**
 * Created by huangyuefeng on 2017/9/20.
 * Contact me : mcxinyu@gmail.com
 */
public class ReadFragment extends ABaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.parent_view)
    CoordinatorLayout mParentView;
    private Unbinder unbinder;

    public static ReadFragment newInstance() {

        Bundle args = new Bundle();

        ReadFragment fragment = new ReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readHosts();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readHosts();
    }

    @Override
    protected Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    protected int getMenuItemId() {
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void readHosts() {
        new AsyncTask<Void, Void, Uri>() {
            boolean suAvailable;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!mSwipeRefresh.isRefreshing())
                    mSwipeRefresh.setRefreshing(true);
            }

            @Override
            protected Uri doInBackground(Void... voids) {
                suAvailable = Shell.SU.available();
                if (!suAvailable) {
                    return null;
                }

                File hostsFile = new File(StaticValues.SYSTEM_HOSTS_FILE_PATH);

                return Uri.fromFile(hostsFile);
            }

            @Override
            protected void onPostExecute(Uri uri) {
                super.onPostExecute(uri);
                if (!suAvailable) {
                    mSwipeRefresh.setRefreshing(false);
                    Snackbar.make(mParentView, getString(R.string.no_su), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                mToolbar.setSubtitle(uri.toString());

                WebSettings settings = mWebView.getSettings();
                settings.setJavaScriptEnabled(false);
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

                mWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        mSwipeRefresh.setRefreshing(false);
                        mSwipeRefresh.setEnabled(false);
                    }
                });
                mWebView.loadUrl(uri.toString());
            }
        }.execute();
    }
}
