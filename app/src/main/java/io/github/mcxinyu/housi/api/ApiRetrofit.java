package io.github.mcxinyu.housi.api;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.github.mcxinyu.housi.BuildConfig;
import io.github.mcxinyu.housi.HouSiApp;
import io.github.mcxinyu.housi.util.LogUtils;
import io.github.mcxinyu.housi.util.StateUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by 跃峰 on 2016/9/18.
 * Contact Me : mcxinyu@foxmail.com
 */
public class ApiRetrofit {
    private static final String TAG = "ApiRetrofit";

    private SourceApi mSourceApi;

    public static final String BASE_CONFIG_URL = "https://housi.mcxinyu.github.io/";
    public static final String BASE_TEMP_URL = "https://raw.githubusercontent.com/googlehosts/hosts/master/hosts-files/hosts";

    public SourceApi getSourceApi() {
        return mSourceApi;
    }

    public ApiRetrofit() {
        // 缓存 url
        File httpCacheDirectory = new File(HouSiApp.getInstance().getCacheDir(), "cacheData");
        int cacheSize = 1 * 1024 * 1024; // 1 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            // loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                // .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();

        Retrofit retrofitHosts = new Retrofit.Builder()
                .baseUrl(BASE_CONFIG_URL)
                .client(client)
                // .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mSourceApi = retrofitHosts.create(SourceApi.class);
    }

    // 缓存
    private Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(7, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();

            Request request = chain.request();
            if (!StateUtils.isNetworkAvailable(HouSiApp.getInstance())) {
                // 网络不可用时，读取缓存
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
                LogUtils.d(TAG, "Cache Interceptor No Network");
            }

            Response originalResponse = chain.proceed(request);

            if (StateUtils.isNetworkAvailable(HouSiApp.getInstance())) {
                int maxAge = 0; // 这里设置为 0 就是说不进行缓存，我们也可以设置缓存时间
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24; // tolerate 1-day stale
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
}
