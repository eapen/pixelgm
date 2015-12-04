package in.eapen.pixelgm;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by geapen on 12/1/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    private Typeface roboto;
    // context data source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        roboto = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Bold.ttf");
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
        TextView likeCount = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView profileImage = (ImageView) convertView.findViewById(R.id.ivProfile);
        ImageView image = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView timePosted = (TextView) convertView.findViewById(R.id.tvTimePosted);

        fullName.setTypeface(roboto);
        username.setTypeface(roboto);

        if (photo.fullName != null && !photo.fullName.isEmpty()) {
            fullName.setText(photo.username);
        } else {
            fullName.setText(photo.fullName.trim());
        }
        username.setText(photo.username);

        if (photo.caption != null && !photo.caption.isEmpty()) {
            caption.setText(photo.caption.trim());
        }
        
        if (!photo.isImage) {
            caption.setText("[VIDEO] " + photo.caption.trim());
        }
        likeCount.setText(photo.likeCount);

        timePosted.setText(DateUtils.getRelativeTimeSpanString(photo.relativeTimestamp * 1000,
                System.currentTimeMillis(),
                0,
                DateUtils.FORMAT_ABBREV_RELATIVE));

        // clear out images if recycled right away
        profileImage.setImageResource(0);
        image.setImageResource(0);

        Picasso.with(getContext()).load(photo.userProfileImage).transform(new CircleTransform()).resize(100, 100).centerCrop().into(profileImage);
        Picasso.with(getContext()).load(photo.imageUrl).fit().centerInside().placeholder(R.mipmap.lab_beta_loading).into(image);

        return convertView;
    }
}
