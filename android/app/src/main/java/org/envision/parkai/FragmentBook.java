package org.envision.parkai;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;

/**
 * Created by root on 10/3/19.
 */

public class FragmentBook extends Fragment {

    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<News, NewsActivity.NewsViewHolder> mPeopleRVAdapter;


    public static FragmentBook newInstance() {

        return new FragmentBook();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_news, container, false);
        getActivity().setTitle("Book your Space!");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String a=user.getUid().toString();


        if(!isNetworkAvailable())
        {
            // Toast.makeText(MainActivity.this, "Connect to Internet to Download Contents",
            //       Toast.LENGTH_LONG).show();

            CookieBar.build(getActivity())
                    .setTitle("Device not connected to Internet")
                    .setMessage("Connect to get the Live Updates! ")
                    .setDuration(5000)
                    .setBackgroundColor(R.color.red)
                    .show();
        }


        //setTitle("News");
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);


        //"News" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("host");
        mDatabase.keepSynced(true);

        mPeopleRV = (RecyclerView) v.findViewById(R.id.myRecycleView);
        // progressBar.setVisibility(ProgressBar.VISIBLE);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("host");
        Query personsQuery = personsRef.orderByKey();

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<News>().setQuery(personsQuery, News.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<News, NewsActivity.NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(final NewsActivity.NewsViewHolder holder, final int position, final News model) {

                progressBar.setVisibility(ProgressBar.INVISIBLE);







                holder.setTitle(model.getName());
               // holder.setDesc("Hosted by: \nRohan Banerjee");

                holder.setDesc(model.getHostuid());
                holder.setTime(model.getStatus());
                holder.setImage(getActivity(), model.getImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String url = model.getPhone();
                        final String img = model.getImage();
                        final String title = model.getName();
                        final String time = model.getCharge();
                        final String desc = model.getAddress();

                        Intent intent = new Intent(getActivity(), LiveTileDescription.class);
                        intent.putExtra("id", url);
                        intent.putExtra("context", img);
                        intent.putExtra("title", title);
                        intent.putExtra("time", time);
                        intent.putExtra("desc", desc);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public NewsActivity.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_row, parent, false);

                return new NewsActivity.NewsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);




        return  v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();


    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NewsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }
        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void setTime(String time){
            TextView time_desc = (TextView)mView.findViewById(R.id.time_desc);
            time_desc.setText(time);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
