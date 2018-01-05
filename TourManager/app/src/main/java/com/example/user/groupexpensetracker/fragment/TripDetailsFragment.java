package com.example.user.groupexpensetracker.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.activity.MainActivity;


public class TripDetailsFragment extends Fragment {


    Button signInButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trip_details, container, false);
        signInButton = (Button) v.findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).signIn();
            }
        });

        return v;
    }


}
