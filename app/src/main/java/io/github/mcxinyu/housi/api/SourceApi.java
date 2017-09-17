package io.github.mcxinyu.housi.api;

import io.github.mcxinyu.housi.bean.BaseConfig;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by 跃峰 on 2016/9/18.
 * Contact Me : mcxinyu@foxmail.com
 */
public interface SourceApi {

    @GET
    Observable<BaseConfig> getBaseConfig();


    @GET
    Observable<ResponseBody> getSourceHosts(@Url String source);
}
