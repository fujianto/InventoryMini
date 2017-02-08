package com.septianfujianto.inventorymini.ui.backup;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.RealmBackupRestoreJson;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.septianfujianto.inventorymini.R.id.btnRestore;

/**
 * Created by Septian A. Fujianto on 2/8/2017.
 */

public class RestoreFragment extends Fragment {
    @BindView(R.id.btnRestore) Button btnRestore;
    private RealmBackupRestoreJson realmBackupRestoreJson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restore, container, false);
        realmBackupRestoreJson = new RealmBackupRestoreJson(getActivity());
        ButterKnife.bind(this, view);

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withRequestCode(1)
                        .withActivity(getActivity())
                        .withPath(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)))
                        .withFilterDirectories(true) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
