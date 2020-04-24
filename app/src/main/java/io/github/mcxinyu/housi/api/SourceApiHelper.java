package io.github.mcxinyu.housi.api;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import io.github.mcxinyu.housi.BuildConfig;
import io.github.mcxinyu.housi.bean.GitRepos;
import io.github.mcxinyu.housi.bean.SourceConfig;
import io.github.mcxinyu.housi.util.StaticValues;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by huangyuefeng on 2017/3/19.
 * Contact me : mcxinyu@foxmail.com
 */
public class SourceApiHelper {
    private static final String TAG = SourceApiHelper.class.getSimpleName();

    private static final SourceApi SOURCE_API = ApiFactory.getSourceApi();

    public static Observable<SourceConfig> getSourceConfig() {
        return SOURCE_API.getSourceConfig(BuildConfig.SOURCE_KEY);
    }

    public static Observable<File> getSourceHosts(final Context context, final String source) {
        return SOURCE_API.getSourceHosts(source)
                .map(new Func1<ResponseBody, File>() {
                    @Override
                    public File call(ResponseBody responseBody) {
                        if (responseBody == null) {
                            return null;
                        }

                        InputStream is = null;
                        byte[] buf = new byte[1024];
                        int len = 0;
                        FileOutputStream fos = null;

                        File sourceHostFile = new File(context.getCacheDir().getAbsolutePath() +
                                File.separator + StaticValues.HOSTS_FILE_NAME + (new Date()).getTime());
                        if (BuildConfig.DEBUG) {
                            sourceHostFile = new File(context.getExternalCacheDir().getAbsolutePath() +
                                    File.separator + StaticValues.HOSTS_FILE_NAME + (new Date()).getTime());
                        }
                        try {
                            is = responseBody.byteStream();
                            fos = new FileOutputStream(sourceHostFile);
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                            }
                            fos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (is != null) is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (fos != null) fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return sourceHostFile;
                    }
                });
    }

    public static Observable<File> getMultiSourceHosts(final Context context, final Set<String> sources) {
        return Observable.from(sources)
                .flatMap(new Func1<String, Observable<File>>() {
                    @Override
                    public Observable<File> call(String s) {
                        return getSourceHosts(context, s);
                    }
                })
                .toList()
                .flatMap(new Func1<List<File>, Observable<File>>() {
                    @Override
                    public Observable<File> call(List<File> files) {
                        File sourceHostFile = new File(context.getCacheDir().getAbsolutePath() +
                                File.separator + "merge-" + StaticValues.HOSTS_FILE_NAME + (new Date()).getTime());
                        if (BuildConfig.DEBUG) {
                            sourceHostFile = new File(context.getExternalCacheDir().getAbsolutePath() +
                                    File.separator + "merge-" + StaticValues.HOSTS_FILE_NAME + (new Date()).getTime());
                        }
                        return Observable.just(mergeFiles(sourceHostFile, files));
                    }
                });
    }

    public static Observable<String> getSourceUpdateDate(String repos) {
        return SOURCE_API.getSourceUpdateDate(repos)
                .map(new Func1<List<GitRepos>, String>() {
                    @Override
                    public String call(List<GitRepos> gitRepos) {
                        if (gitRepos == null || gitRepos.size() == 0) {
                            return "n/a";
                        }
                        SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        SimpleDateFormat simpleDateFormatResult = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        try {
                            return simpleDateFormatResult
                                    .format(simpleDateFormatInput
                                            .parse(gitRepos.get(0)
                                                    .getCommit()
                                                    .getCommitter()
                                                    .getDate()
                                                    .replace("Z", "UTC")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return "n/a";
                        }
                    }
                });
    }

    private static File mergeFiles(File outFile, List<File> files) {
        FileChannel outChannel = null;
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for (File file : files) {
                FileChannel fc = new FileInputStream(file).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(1024);
                while (fc.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException ignore) {
            }
        }
        return outFile;
    }
}
