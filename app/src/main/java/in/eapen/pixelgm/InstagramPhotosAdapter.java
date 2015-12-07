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

    // our ViewHolder.
    // caches our TextView
    static class ViewHolderItem {
        TextView fullName;
        TextView username;
        TextView caption;
        TextView likeCount;
        TextView timePosted;
        ImageView profileImage;
        ImageView image;
    }

    // context data source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        roboto = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Bold.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;

        InstagramPhoto photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            // setup viewholder
            viewHolder = new ViewHolderItem();
            viewHolder.fullName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.likeCount = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.profileImage = (ImageView) convertView.findViewById(R.id.ivProfile);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.timePosted = (TextView) convertView.findViewById(R.id.tvTimePosted);

            // set the holder with the view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        viewHolder.fullName.setTypeface(roboto);
        viewHolder.username.setTypeface(roboto);

        if (photo.fullName != null && !photo.fullName.isEmpty()) {
            viewHolder.fullName.setText(photo.username);
        } else {
            viewHolder.fullName.setText(photo.fullName.trim());
        }
        viewHolder.username.setText(photo.username);

        if (photo.caption != null && !photo.caption.isEmpty()) {
            viewHolder.caption.setText(photo.caption.trim());
        }

        if (!photo.isImage) {
            viewHolder.caption.setText("[VIDEO] " + photo.caption.trim());
        }
        viewHolder.likeCount.setText(photo.likeCount);

        viewHolder.timePosted.setText(DateUtils.getRelativeTimeSpanString(photo.relativeTimestamp * 1000,
                System.currentTimeMillis(),
                0,
                DateUtils.FORMAT_ABBREV_RELATIVE));

        // clear out images if recycled right away
        viewHolder.profileImage.setImageResource(0);
        viewHolder.image.setImageResource(0);

        Picasso.with(getContext()).load(photo.userProfileImage).transform(new CircleTransform()).resize(100, 100).centerCrop().into(viewHolder.profileImage);
        Picasso.with(getContext()).load(photo.imageUrl).fit().centerInside().placeholder(R.mipmap.image_placeholder).into(viewHolder.image);

        return convertView;
    }
}
