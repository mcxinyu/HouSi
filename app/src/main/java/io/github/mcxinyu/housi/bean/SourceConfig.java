package io.github.mcxinyu.housi.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by huangyuefeng on 2017/9/17.
 * Contact me : mcxinyu@gmail.com
 */
public class SourceConfig {

    @SerializedName("status")
    private String status;
    @SerializedName("server_time")
    private String serverTime;
    @SerializedName("result")
    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {

        @SerializedName("name")
        private String name;
        @SerializedName("url")
        private String url;
        @SerializedName("value")
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public String[] getNameArray() {
        String[] nameArray = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            nameArray[i] = result.get(i).getName();
        }
        return nameArray;
    }

    public String[] getValueArray() {
        String[] valueArray = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            valueArray[i] = result.get(i).getValue();
        }
        return valueArray;
    }
}
