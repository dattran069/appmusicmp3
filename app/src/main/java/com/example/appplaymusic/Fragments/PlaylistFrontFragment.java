package com.example.appplaymusic.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFrontFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFrontFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imageView;
    public void setImgPlaylist(int res){
        if(imageView!=null){
            imageView.setImageResource(res);
        }
    }
    public PlaylistFrontFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFrontFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static PlaylistFrontFragment newInstance(String param1, String param2) {
        PlaylistFrontFragment fragment = new PlaylistFrontFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void update(){
        //Glide.with(playSongActivity.getApplicationContext()).load(playSongActivity.getSong().getSongImage()).into(circleImageView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("setSongNums", "onCreateView");
        view=inflater.inflate(R.layout.fragment_playlist_front, container, false);
        imageView=view.findViewById(R.id.img_playlist);
        Glide.with(getActivity()).load(Common.getPlaylist().getImage()).into(imageView);
        //imageView.setImageResource(activity.getPlaylist().getImgResId());
        TextView txt_playlist_name=view.findViewById(R.id.txt_playlist_name);
        TextView txt_playlist_num_songs=view.findViewById(R.id.txt_total_songs);
        txt_playlist_num_songs.setText("200");
        txt_playlist_name.setText(Common.getPlaylist().getNamePlaylist());
        return view;
    }




}