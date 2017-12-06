package com.example.mrschmitz.jobs.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.database.Users;
import com.example.mrschmitz.jobs.misc.Utils;
import com.example.mrschmitz.jobs.pojos.Review;

import java.util.ArrayList;

import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by Noah on 11/30/2017.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    private static class ViewHolder {
        AvatarView avatarView;
        TextView reviewTextView;
        RatingBar ratingBar;
    }

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        super(context, R.layout.list_item_review, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();

        } else {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_review, parent, false);
            convertView.setTag(viewHolder);

            viewHolder.avatarView = convertView.findViewById(R.id.avatar);
            viewHolder.reviewTextView = convertView.findViewById(R.id.review);
            viewHolder.ratingBar = convertView.findViewById(R.id.rating);
        }

        final Review review = getItem(position);
        Users.loadUser(review.getReviewerUid(), user -> {
            Utils.loadProfileImage(getContext(), user, viewHolder.avatarView);
            viewHolder.reviewTextView.setText(review.getReview());
            viewHolder.ratingBar.setRating(review.getRating());
        });

        return convertView;
    }


}
