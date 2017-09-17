package io.github.mcxinyu.housi.api;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.github.mcxinyu.housi.util.StaticValues;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by huangyuefeng on 2017/3/19.
 * Contact me : mcxinyu@foxmail.com
 */
public class SourceApiHelper {

    private static final SourceApi SOURCE_API = ApiFactory.getSourceApi();

    public static Observable<File> getSourceHosts(final Context context, String source) {
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
                                File.separator + StaticValues.HOSTS_FILE_NAME);
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
}
