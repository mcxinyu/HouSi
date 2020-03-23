package io.github.mcxinyu.housi.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import androidx.core.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
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
        if (mWebView != null) {
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();

            try {
                mWebView.destroy();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        unbinder.unbind();
    }

    private void readHosts() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!mSwipeRefresh.isRefreshing())
                    mSwipeRefresh.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mToolbar != null && url != null) {
                    mToolbar.setSubtitle(url);
                }
                mSwipeRefresh.setRefreshing(false);
                mSwipeRefresh.setEnabled(false);
            }
        });
        File hostsFile = new File(StaticValues.SYSTEM_HOSTS_FILE_PATH);

        mWebView.loadUrl(Uri.fromFile(hostsFile).toString());
    }
}
