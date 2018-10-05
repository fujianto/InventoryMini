package com.septianfujianto.inventorymini.ui.backup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.models.realm.RealmBackupRestoreJson;
import com.septianfujianto.inventorymini.ui.product.ProductPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Septian A. Fujianto on 2/8/2017.
 */

public class BackupFragment extends Fragment implements ProductPresenter.ProductPresenterListener{
    @BindView(R.id.btnBackup) Button btnBackup;
    @BindView(R.id.backupDetail) TextView backupDetail;
    //@BindView(R.id.btnBackupRealm) Button btnBackupRealm;
    @BindView(R.id.btnDeleteAll) Button btnDeleteAll;
    private RealmBackupRestoreJson realmBackupRestoreJson;
    private ProductPresenter productPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backup, container, false);
        ButterKnife.bind(this, view);
        realmBackupRestoreJson = new RealmBackupRestoreJson(getActivity());


        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realmBackupRestoreJson.backupProduct();
                realmBackupRestoreJson.backupCategory();
                realmBackupRestoreJson.backupLocation();
                realmBackupRestoreJson.backupAll();
                Toast.makeText(getActivity(), getString(R.string.backup_created), Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productPresenter = new ProductPresenter(getActivity(), this);
    }

    private void deleteAll() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getResources().getString(R.string.question_delete_all_product));
        alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                productPresenter.deleteAllTable();
            }
        });

        alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.show();
    }

    @Override
    public void productLoaded() {

    }

    @Override
    public void productUpdated() {

    }

    @Override
    public void productDeleted() {

    }

    @Override
    public Product createdProduct() {
        return null;
    }

    @Override
    public void productSearched() {

    }
}
