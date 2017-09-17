package io.github.mcxinyu.housi.api;

/**
 * Created by 跃峰 on 2016/9/18.
 * Contact Me : mcxinyu@foxmail.com
 * 使用单例模式
 */
public class ApiFactory {

    private static final Object monitor = new Object();
    private static SourceApi sourceApi = null;

    public static SourceApi getSourceApi() {
        synchronized (monitor) {
            if (sourceApi == null) {
                sourceApi = new ApiRetrofit().getSourceApi();
            }
            return sourceApi;
        }
    }
}
