package pers.zy.imgsearchmodule;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
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
    WeakReference<Activity> activityWeakReference;

    private GoogleSearchUtils(SearchBuilder searchBuilder, Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
        client = new OkHttpClient.Builder().build();
        finalUrl = "";
        for (String key : searchBuilder.fieldMap.keySet()) {
            finalUrl += key + searchBuilder.fieldMap.get(key);
        }
    }

    //start search
    public void enqueue(final Callback callback) {
        client.newCall(new Request.Builder()
                .get()
                .url(SEARCH_URL + finalUrl)
                .header("User-Agent"
                        , "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                .build())
                .enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onFailure(call, e);
                        Log.d("GFZY", "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        String resStr = response.body().string();
                        final ArrayList<String> imgUrlList = new ArrayList<>();
                        Pattern pattern = Pattern.compile("(?=(https|http))(.*?)(?=\")");
                        Matcher matcher = pattern.matcher(resStr);
                        while (matcher.find()) {
                            String group = matcher.group();
                            if (group.endsWith(".jpg") || group.endsWith(".png")) {
                                imgUrlList.add(group);
                                Log.d("GFZY", "onResponse: " + group);
                            }
                        }

                        activityWeakReference.get().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    callback.onResponse(call, imgUrlList);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }

    public interface Callback {
        void onFailure(Call call, IOException e);

        void onResponse(Call call, List<String> imgUrlList) throws IOException;
    }

    public static class SearchBuilder {
        private Map<String, String> fieldMap;
        private WeakReference<Activity> activityWeakReference;

        public static SearchBuilder newBuilder(Activity activity) {
            return new SearchBuilder(activity);
        }

        private SearchBuilder(Activity activity) {
            fieldMap = new HashMap<>();
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * @param query something what you want to search
         * */
        public SearchBuilder query(String query) {
            fieldMap.put(QUERY, query);
            return this;
        }

        public GoogleSearchUtils build() {
            return new GoogleSearchUtils(this, activityWeakReference.get());
        }
    }
}
