package io.github.mcxinyu.housi.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.activity.ReadEditHostsActivity;
import io.github.mcxinyu.housi.util.StaticValues;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by huangyuefeng on 2017/9/20.
 * Contact me : mcxinyu@gmail.com
 */
public class ReadEditHostsFragment extends ABaseFragment {
    private static final String TAG = ReadEditHostsFragment.class.getSimpleName();
    private static final String ARGS_EDIT = "args_edit";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.parent_view)
    CoordinatorLayout mParentView;
    @BindView(R.id.rich_editor)
    RichEditor mRichEditor;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_spinner)
    AppCompatSpinner mToolbarSpinner;
    @BindView(R.id.relative_layout_title)
    RelativeLayout mRelativeLayoutTitle;
    private Unbinder unbinder;

    private boolean mEditStatus;
    private String mHostsHtml;

    public static ReadEditHostsFragment newInstance(boolean edit) {

        Bundle args = new Bundle();
        args.putBoolean(ARGS_EDIT, edit);
        ReadEditHostsFragment fragment = new ReadEditHostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mEditStatus = getArguments().getBoolean(ARGS_EDIT, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
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
        initToolbar();
        readHosts();
    }

    private void initToolbar() {
        if (mToolbar != null) {
            if (mEditStatus) {
                mToolbar.setTitle("");
                mRelativeLayoutTitle.setVisibility(View.VISIBLE);
                mToolbarTitle.setText("编辑 hosts");
            } else {
                mRelativeLayoutTitle.setVisibility(View.GONE);
            }
        }
        String[] lineOptional = new String[]{"100行/页", "200行/页", "300行/页"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item_theme_color, lineOptional);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_theme_color);

        mToolbarSpinner.setAdapter(adapter);
        mToolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    protected int getNavMenuItemId() {
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void readHosts() {
        if (mEditStatus) {
            mRichEditor.setHtml(ReadEditHostsActivity.sHostsString);
        } else {
            mRichEditor.getSettings().setJavaScriptEnabled(true);
            mRichEditor.addJavascriptInterface(new HostsJavaScriptInterFace(), "hosts_script");
            mRichEditor.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (!mSwipeRefresh.isRefreshing())
                        mSwipeRefresh.setRefreshing(true);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    view.loadUrl("javascript:window.hosts_script.getSource(document.documentElement.outerHTML);void(0)");
                    if (mToolbar != null && url != null) {
                        mToolbar.setSubtitle(url);
                    }
                    mSwipeRefresh.setRefreshing(false);
                    mSwipeRefresh.setEnabled(false);
                }
            });

            File hostsFile = new File(StaticValues.SYSTEM_HOSTS_FILE_PATH);
            mRichEditor.loadUrl(Uri.fromFile(hostsFile).toString());
        }
    }

    public final class HostsJavaScriptInterFace {

        @JavascriptInterface
        public void getSource(String html) {
            if (!mEditStatus) {
                mHostsHtml = html;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mEditStatus) {
            inflater.inflate(R.menu.edit_menu, menu);
        } else {
            inflater.inflate(R.menu.read_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_hosts:
                ReadEditHostsActivity.sHostsString = mHostsHtml;
                startActivity(ReadEditHostsActivity.newIntent(getContext(), true));
                return true;
            case R.id.action_save_hosts:
                // TODO: 2019/1/8 save hosts
                Toast.makeText(getContext(), "do save", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
