package io.github.mcxinyu.housi.util;

/**
 * Created by huangyuefeng on 2017/9/15.
 * Contact me : mcxinyu@gmail.com
 */
public class StaticValues {
    public static final String MOUNT_SYSTEM_RW = "mount -o rw,remount /system";
    public static final String MOUNT_SYSTEM_RO = "mount -o ro,remount /system";
    public static final String SYSTEM_HOST_FILE_PATH = "/system/etc/hosts";
    public static final String EMPTY_HOST_NAME = "empty_hosts";
    public static final String EMPTY_HOST_VALUE = "127.0.0.1\tlocalhost\n" + "::1\tlocalhost\n" +
            "::1\tip6-localhost\n" + "::1\tip6-loopback";
}
