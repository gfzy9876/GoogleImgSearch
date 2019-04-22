package pers.zy.imgsearchmodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleSearchUtils {
    public static final String SEARCH_URL = "https://www.google.com/search?tbm=isch";
    public static final String QUERY = "&q=";
    String finalUrl;
    final OkHttpClient client;

    private GoogleSearchUtils(SearchBuilder searchBuilder) {
        client = new OkHttpClient.Builder().build();
        finalUrl = "";
        for (String key : searchBuilder.fieldMap.keySet()) {
            finalUrl += key + searchBuilder.fieldMap.get(key);
        }
    }

    public void enqueue(final Callback callback) {
        client.newCall(new Request.Builder()
                .get()
                .url(SEARCH_URL + finalUrl)
                .build())
                .enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onFailure(call, e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resStr = response.body().string();
                        ArrayList<String> imgUrlList = new ArrayList<>();
                        Pattern pattern = Pattern.compile("(?<=\")+.*?(?=\")");
                        Matcher matcher = pattern.matcher(resStr);
                        while (matcher.find()) {
                            imgUrlList.add(matcher.group());
                        }
                        callback.onResponse(call, imgUrlList);
                    }
                });
    }

    public interface Callback {
        void onFailure(Call call, IOException e);

        void onResponse(Call call, List<String> response) throws IOException;
    }

    public static class SearchBuilder {
        private Map<String, String> fieldMap;

        public static SearchBuilder newBuilder() {
            return new SearchBuilder();
        }

        private SearchBuilder() {
            fieldMap = new HashMap<>();
        }

        public SearchBuilder query(String query) {
            fieldMap.put(QUERY, query);
            return this;
        }

        public GoogleSearchUtils build() {
            return new GoogleSearchUtils(this);
        }
    }
}
