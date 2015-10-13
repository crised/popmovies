package telematic.cl.popmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        if (intent == null || !intent.hasExtra("title")) return null;

        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView title = (TextView) root.findViewById(R.id.detail_title);
        TextView plot = (TextView) root.findViewById(R.id.detail_plot);
        TextView rating = (TextView) root.findViewById(R.id.detail_rating_date);
        ImageView imageView = (ImageView) root.findViewById(R.id.detail_view);

        Picasso.with(getContext()).load(intent.getStringExtra("uri")).into(imageView);
        title.setText(intent.getStringExtra("title"));
        plot.setText(intent.getStringExtra("plot"));
        rating.setText("User Rating: " + intent.getStringExtra("rating") + "   "
                + "Release Date: " + intent.getStringExtra("date"));

        return root;
    }
}