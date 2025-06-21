package com.example.projekakhir_andrewijaya;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuahAdapter extends RecyclerView.Adapter<BuahAdapter.BuahViewHolder> implements Filterable {

    private Context context;
    private List<Buah> listBuah;
    private List<Buah> listBuahFull;
    private DatabaseHelper dbHelper;

    public BuahAdapter(Context context, List<Buah> list) {
        this.context = context;
        this.listBuah = list;
        this.listBuahFull = new ArrayList<>(list);
        this.dbHelper = new DatabaseHelper(context);
    }

    //PERUBAHAN: Deklarasi TextView baru di ViewHolder
    public static class BuahViewHolder extends RecyclerView.ViewHolder {
        TextView tvBuahId, tvBuahNama, tvBuahJenis;
        Button btnEdit, btnDelete;

        public BuahViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBuahId = itemView.findViewById(R.id.tv_buah_id);
            tvBuahNama = itemView.findViewById(R.id.tv_buah_nama);
            tvBuahJenis = itemView.findViewById(R.id.tv_buah_jenis);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public BuahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_list_item_buah, parent, false);
        return new BuahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuahViewHolder holder, int position) {
        Buah buah = listBuah.get(position);

        // Mengatur teks untuk setiap TextView
        holder.tvBuahId.setText("ID: " + buah.getId());
        holder.tvBuahNama.setText("Nama: " + buah.getNama());
        holder.tvBuahJenis.setText("Jenis: " + buah.getJenis());

        // Listener untuk tombol Edit dan Delete
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditDataActivity.class);
            intent.putExtra("buah_id", String.valueOf(buah.getId()));
            intent.putExtra("buah_nama", buah.getNama());
            intent.putExtra("buah_jenis", buah.getJenis());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Hapus Data")
                    .setMessage("Yakin ingin menghapus data buah '" + buah.getNama() + "'?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        Integer deletedRows = dbHelper.deleteBuah(String.valueOf(buah.getId()));
                        if (deletedRows > 0) {
                            int currentPosition = holder.getAdapterPosition();
                            listBuahFull.remove(buah);
                            listBuah.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listBuah.size();
    }

    public void updateData(ArrayList<Buah> dataBuahBaru) {
        listBuah.clear();
        listBuah.addAll(dataBuahBaru);
        listBuahFull.clear();
        listBuahFull.addAll(dataBuahBaru);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return buahFilter;
    }

    private Filter buahFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Buah> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listBuahFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Buah item : listBuahFull) {
                    if (item.getNama().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listBuah.clear();
            listBuah.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}