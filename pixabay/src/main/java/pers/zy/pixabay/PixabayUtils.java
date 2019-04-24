package pers.zy.pixabay;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PixabayUtils {
    public static void search() {
        final OkHttpClient client = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder()
                .header("User-Agent"
                        , "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                .url("https://pixabay.com/images/search/anim/")
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("GFZY", "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.i("GFZY", "onResponse: " + response.body().string());
                        Log.i("GFZY", "onResponse: ");
//                        FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory()
//                                , "AAAG.html"));
//                        fileOutputStream.write(response.body().string().getBytes());
//
//                        fileOutputStream.close();
                        String resStr = response.body().string();
                        Pattern pattern = Pattern.compile("(?<=href=\").*?(?=/\")");
                        Matcher matcher = pattern.matcher(resStr);

                        ArrayList<String> detailsUrlList = new ArrayList<>();
                        while (matcher.find()) {

                            String group = matcher.group();
                            if (group.startsWith("/photos")
                                    && group.charAt(group.length() - 1) >= '0'
                                    && group.charAt(group.length() - 1) <= '9') {
                                detailsUrlList.add("https://pixabay.com" + group);
                            }
                        }

                        for (String s : detailsUrlList) {
//                            Log.i("GFZY", "onResponse: " + s);
                            Request detailRequest = new Request.Builder()
                                    .url(s)
                                    .get()
                                    .header("User-Agent"
                                            , "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                                    .url("https://pixabay.com/images/search/anim/")
                                    .build();

                            client.newCall(detailRequest)
                                    .enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.e("GFZY", "onFailure: " + e.getMessage());
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String resStr = response.body().string();
//                                            Log.i("GFZY", "onResponse: " + resStr);
                                            Pattern detailPattern = Pattern.compile("(?<=src=\").*?\\.jpg(?=\")");
                                            Matcher detailMatcher = detailPattern.matcher(resStr);
                                            while (detailMatcher.find()) {
                                                Log.i("GFZY", "onResponse: " + detailMatcher.group());
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
