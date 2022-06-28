package com.example.universaltransmitter2.ui.zipfile;

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
import com.example.universaltransmitter2.FilleUp;
import com.example.universaltransmitter2.R;
import com.example.universaltransmitter2.ZipFile;

public class ZipFileFragment extends Fragment {

    View v;

    public static ZipFileFragment newInstance() {
        return new ZipFileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_zipfile, container, false);
        Button button_test = v.findViewById(R.id.button_zipfile);
        button_test.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), ZipFile.class);
            startActivity(intent);
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ZipFileViewModel mViewModel = new ViewModelProvider(this).get(ZipFileViewModel.class);
        // TODO: Use the ViewModel
    }

}