package com.example.universaltransmitter2.ui.geolocation;

import android.content.Intent;
import android.widget.Button;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.universaltransmitter2.Geolocation;
import com.example.universaltransmitter2.R;

public class GeolocationFragment extends Fragment {

    View v;

    public static GeolocationFragment newInstance() {
        return new GeolocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_geolocation, container, false);
        Button button_test = v.findViewById(R.id.button_geolocation);
        button_test.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), Geolocation.class);
            startActivity(intent);
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GeolocationViewModel mViewModel = new ViewModelProvider(this).get(GeolocationViewModel.class);
        // TODO: Use the ViewModel
    }

}