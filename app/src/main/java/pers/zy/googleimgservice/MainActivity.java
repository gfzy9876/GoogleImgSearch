package pers.zy.googleimgservice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import pers.zy.pixabay.PixabayUtils;


public class MainActivity extends AppCompatActivity {

    ImgAdapter imgAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recycler = findViewById(R.id.img_recycler_view);
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        imgAdapter = new ImgAdapter(this);
        recycler.setAdapter(imgAdapter);

    }

    private void initView() {
        Button mSearch = findViewById(R.id.search);
        EditText mEdit = findViewById(R.id.img_search_edit);

        mSearch.setOnClickListener(v -> {

            PixabayUtils.search();

            /*GoogleSearchUtils searchUtils = GoogleSearchUtils.SearchBuilder
                    .newBuilder(this)
                    .query(mEdit.getText().toString())
                    .build();

            searchUtils.enqueue(new GoogleSearchUtils.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, List<String> imgUrlList) throws IOException {
                    imgAdapter.notifyData(imgUrlList);
                }
            });*/
        });
    }
}
