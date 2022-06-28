package com.example.universaltransmitter2.ui.nfc;

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
import com.example.universaltransmitter2.NfcTag;
import com.example.universaltransmitter2.R;

public class NfcFragment extends Fragment {

    View v;

    public static NfcFragment newInstance() {
        return new NfcFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_nfc, container, false);
        Button button_test = v.findViewById(R.id.button_nfc);
        button_test.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), NfcTag.class);
            startActivity(intent);
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NfcViewModel mViewModel = new ViewModelProvider(this).get(NfcViewModel.class);
        // TODO: Use the ViewModel
    }

}