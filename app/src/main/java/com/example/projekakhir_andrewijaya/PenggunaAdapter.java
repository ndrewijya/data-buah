package com.example.projekakhir_andrewijaya;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import java.util.List;

public class PenggunaAdapter extends ArrayAdapter<Pengguna> {
    private Context context;
    private List<Pengguna> penggunaList;
    private DatabaseHelper dbHelper;

    public PenggunaAdapter(Context context, List<Pengguna> list) {
        super(context, R.layout.activity_list_item_pengguna, list);
        this.context = context;
        this.penggunaList = list;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_list_item_pengguna, parent, false);
        }

        final Pengguna pengguna = penggunaList.get(position);
        TextView txtInfo = view.findViewById(R.id.txtInfoPengguna);
        Button btnEdit = view.findViewById(R.id.btnEditPengguna);
        Button btnDelete = view.findViewById(R.id.btnDeletePengguna);

        txtInfo.setText("ID: " + pengguna.getId() + "\nUsername: " + pengguna.getUsername());

        btnEdit.setOnClickListener(v -> {
            // Fitur Edit bisa ditambahkan di sini nanti
            Toast.makeText(context, "Fitur Edit untuk pengguna belum dibuat.", Toast.LENGTH_SHORT).show();
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Hapus Pengguna")
                    .setMessage("Yakin ingin menghapus pengguna '" + pengguna.getUsername() + "'?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        Integer deletedRows = dbHelper.deleteUser(pengguna.getId());
                        if (deletedRows > 0) {
                            penggunaList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Pengguna berhasil dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Gagal menghapus pengguna", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });

        return view;
    }
}