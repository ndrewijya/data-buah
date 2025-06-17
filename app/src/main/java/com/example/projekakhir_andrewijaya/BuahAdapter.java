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
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BuahAdapter extends ArrayAdapter<Buah> {

    private Context context;
    private List<Buah> buahList;
    private DatabaseHelper dbHelper;

    public BuahAdapter(Context context, List<Buah> list) {
        super(context, R.layout.activity_list_item_buah, list);
        this.context = context;
        this.buahList = list;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Menggunakan layout item yang sudah Anda buat
            view = inflater.inflate(R.layout.activity_list_item_buah, null);
        }

        // Mengambil data buah pada posisi saat ini
        final Buah buah = buahList.get(position);

        TextView txtInfo = view.findViewById(R.id.txtInfo);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        // Menampilkan informasi buah
        txtInfo.setText(buah.toString());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk membuka EditDataActivity
                Intent intent = new Intent(context, EditDataActivity.class);

                // Mengirim data buah saat ini ke EditDataActivity
                intent.putExtra("buah_id", buah.getId());
                intent.putExtra("buah_nama", buah.getNama());
                intent.putExtra("buah_jenis", buah.getJenis());

                context.startActivity(intent);
            }
        });

        // Tombol Delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Hapus Data")
                        .setMessage("Yakin ingin menghapus data buah '" + buah.getNama() + "'?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Hapus data dari database
                                Integer deletedRows = dbHelper.deleteData(buah.getId());
                                if (deletedRows > 0) {
                                    // Hapus item dari list dan perbarui tampilan
                                    buahList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return view;
    }
}