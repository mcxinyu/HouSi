package io.github.mcxinyu.housi.api;

import java.util.List;

import io.github.mcxinyu.housi.bean.GitRepos;
import io.github.mcxinyu.housi.bean.SourceConfig;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by 跃峰 on 2016/9/18.
 * Contact Me : mcxinyu@foxmail.com
 */
public interface SourceApi {

    @GET("{source_key}/raw?blob_name=source.json")
    Observable<SourceConfig> getSourceConfig(@Path("source_key") String sourceKey);

    @GET
    Observable<ResponseBody> getSourceHosts(@Url String source);

    @GET
    Observable<List<GitRepos>> getSourceUpdateDate(@Url String repos);
}
