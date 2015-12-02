package in.eapen.pixelgm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> instagramPhotos;
    private InstagramPhotosAdapter instagramPhotosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        instagramPhotos = new ArrayList<>();
        instagramPhotosAdapter = new InstagramPhotosAdapter(this, instagramPhotos);
        ListView lv = (ListView) findViewById(R.id.lvPhotos);
        lv.setAdapter(instagramPhotosAdapter);
        // fetch popular photos
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        AsyncHttpClient client = new AsyncHttpClient();
        // https://api.instagram.com/v1/media/popular?client_id=e05c462ebd86446ea48a5af73769b602

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // log the data
                //Log.i("debug", response.toString());
                /*
                data.type == image || video
                */
                JSONArray photosJson = null;
                try {
                    photosJson = response.getJSONArray("data");
                    for (int i=0; i<photosJson.length(); i++) {
                        JSONObject photoObject = photosJson.getJSONObject(i);

                        InstagramPhoto photo = new InstagramPhoto();

                        if (photoObject.optJSONObject("caption") != null) {
                            photo.caption = photoObject.getJSONObject("caption").getString("text");
                        }
                        photo.fullName = photoObject.getJSONObject("user").getString("full_name");
                        photo.username = photoObject.getJSONObject("user").getString("username");
                        photo.userProfileImage = photoObject.getJSONObject("user").getString("profile_picture");
                        photo.commentCount = photoObject.getJSONObject("comments").getLong("count");
                        photo.imageUrl = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getLong("height");
                        photo.imageWidth = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getLong("width");
                        if (photoObject.optJSONObject("likes") != null) {
                            photo.likeCount = photoObject.getJSONObject("likes").getLong("count");
                        }
                        photo.relativeTimestamp = photoObject.getString("created_time");

                        instagramPhotos.add(photo);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("debug", String.valueOf(instagramPhotos.size()) + " elements in array");
                instagramPhotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // do something
            }
        });
    }

}
