package com.example.universaltransmitter2.ui.file;

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
import com.example.universaltransmitter2.FilleUp;
import com.example.universaltransmitter2.R;

public class FilleUpFragment extends Fragment {

    View v;

    public static FilleUpFragment newInstance() {
        return new FilleUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fileup, container, false);
        Button button_test = v.findViewById(R.id.button_fileup);
        button_test.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), FilleUp.class);
            startActivity(intent);
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FileUpViewModel mViewModel = new ViewModelProvider(this).get(FileUpViewModel.class);
        // TODO: Use the ViewModel
    }

}