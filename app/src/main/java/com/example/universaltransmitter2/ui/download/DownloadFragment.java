package com.example.universaltransmitter2.ui.download;

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
import com.example.universaltransmitter2.Download;
import com.example.universaltransmitter2.R;

public class DownloadFragment extends Fragment {

    View v;

    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_download, container, false);
        Button button_test = v.findViewById(R.id.button_download);
        button_test.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), Download.class);
            startActivity(intent);
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DownloadMainViewModel mViewModel = new ViewModelProvider(this).get(DownloadMainViewModel.class);
        // TODO: Use the ViewModel
    }

}