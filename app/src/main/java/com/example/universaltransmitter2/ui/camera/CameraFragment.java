package com.example.universaltransmitter2.ui.camera;

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
import com.example.universaltransmitter2.Camera;
import com.example.universaltransmitter2.R;

public class CameraFragment extends Fragment {

    View v;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_camera, container, false);
        Button button_test = v.findViewById(R.id.button_camera);
        button_test.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), Camera.class);
            startActivity(intent);
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CameraViewModel mViewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        // TODO: Use the ViewModel
    }

}