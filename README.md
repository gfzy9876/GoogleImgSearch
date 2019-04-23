# GoogleImgSearch

the api of google image search by Regular Expression

##### tap1:set jitpack maven in your Proejct build.gradle

`        maven { url 'https://www.jitpack.io' }
`
##### tab2:add implementation:

`implementation 'com.github.gfzy9876:GoogleImgSearch:1.0.0'`

##### tab3:use api:

    GoogleSearchUtils searchUtils = GoogleSearchUtils.SearchBuilder
                    .newBuilder(this)
                    .query(mEdit.getText().toString())
                    .build();

            searchUtils.enqueue(new GoogleSearchUtils.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, List<String> imgUrlList) throws IOException {
                    //to do what you want,cause you have already get the imgUrls;
            });