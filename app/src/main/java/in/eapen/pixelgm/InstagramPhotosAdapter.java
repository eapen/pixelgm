package in.eapen.pixelgm;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by geapen on 12/1/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    // context data source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        TextView fullName = (TextView) convertView.findViewById(R.id.tvName);
        TextView username = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView caption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView profileImage = (ImageView) convertView.findViewById(R.id.ivProfile);
        ImageView image = (ImageView) convertView.findViewById(R.id.ivPhoto);

        fullName.setText(photo.fullName);
        username.setText("@" + photo.username);
        caption.setText(photo.caption);

        // clear out images if recycled right away
        profileImage.setImageResource(0);
        image.setImageResource(0);

        Picasso.with(getContext()).load(photo.userProfileImage).centerCrop().transform(new CircleTransform()).resize(30, 30).into(profileImage);
        Picasso.with(getContext()).load(photo.imageUrl).resize().fit().centerInside().placeholder(R.mipmap.ic_launcher).into(image);

        return convertView;
    }
}
