package in.eapen.pixelgm.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import in.eapen.pixelgm.R;
import in.eapen.pixelgm.adapters.InstagramPhotosAdapter;
import in.eapen.pixelgm.models.InstagramPhoto;

public class PhotosActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> instagramPhotos;
    private InstagramPhotosAdapter instagramPhotosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
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
        // url = "https://api.instagram.com/v1/tags/kerala/media/recent?client_id=" + CLIENT_ID;
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // log the data
                Log.i("debug", response.toString());
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
                        photo.commentCount = photoObject.getJSONObject("comments").getString("count");
                        photo.imageUrl = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.imageWidth = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.isImage = photoObject.getString("type").equalsIgnoreCase("image");
                        if (photoObject.optJSONObject("likes") != null) {
                            photo.likeCount = NumberFormat.getInstance().format(photoObject.getJSONObject("likes").getLong("count")) + " likes";
                        } else {
                            photo.likeCount = "Be the first to like";
                        }
                        photo.relativeTimestamp = photoObject.getLong("created_time");

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
