package com.example.projekakhir_andrewijaya;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/*Anggota Kelompok:
 * 1. Andre Wijaya (221011400791)
 * 2. Iqbal Isya Fathurrohman (221011401657)
 * 3. Novandra Anugrah (221011400778)
 */

public class PenggunaAdapter extends RecyclerView.Adapter<PenggunaAdapter.PenggunaViewHolder> {

    private Context context;
    private List<Pengguna> listPengguna;
    private DatabaseHelper dbHelper;

    public PenggunaAdapter(Context context, List<Pengguna> list) {
        this.context = context;
        this.listPengguna = list;
        this.dbHelper = new DatabaseHelper(context);
    }

    public static class PenggunaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPenggunaId, tvPenggunaUsername;
        Button btnEditPassword, btnDeletePengguna;

        public PenggunaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPenggunaId = itemView.findViewById(R.id.tv_pengguna_id);
            tvPenggunaUsername = itemView.findViewById(R.id.tv_pengguna_username);
            btnEditPassword = itemView.findViewById(R.id.btn_edit_password);
            btnDeletePengguna = itemView.findViewById(R.id.btn_delete_pengguna);
        }
    }

    @NonNull
    @Override
    public PenggunaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_list_item_pengguna, parent, false);
        return new PenggunaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenggunaViewHolder holder, int position) {
        Pengguna pengguna = listPengguna.get(position);
        holder.tvPenggunaId.setText("ID: " + pengguna.getId());
        holder.tvPenggunaUsername.setText("Username: " + pengguna.getUsername());

        holder.btnDeletePengguna.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Hapus Pengguna")
                    .setMessage("Yakin ingin menghapus pengguna '" + pengguna.getUsername() + "'?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        Integer deletedRows = dbHelper.deleteUser(String.valueOf(pengguna.getId()));
                        if (deletedRows > 0) {
                            int currentPosition = holder.getAdapterPosition();
                            listPengguna.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            Toast.makeText(context, "Pengguna berhasil dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Gagal menghapus pengguna", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        holder.btnEditPassword.setOnClickListener(v -> {
            showEditPasswordDialog(pengguna);
        });
    }

    private void showEditPasswordDialog(Pengguna pengguna) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ubah Password untuk " + pengguna.getUsername());

        // Buat EditText untuk input password baru di dalam dialog
        final EditText inputPassword = new EditText(context);
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputPassword.setHint("Masukkan password baru");
        builder.setView(inputPassword);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String newPassword = inputPassword.getText().toString().trim();
            if (newPassword.isEmpty()) {
                Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean isUpdated = dbHelper.updateUser(String.valueOf(pengguna.getId()), newPassword);
            if(isUpdated) {
                Toast.makeText(context, "Password berhasil diperbarui", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Gagal memperbarui password", Toast.LENGTH_SHORT).show();
            }
        });

        // Atur tombol "Batal"
        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    @Override
    public int getItemCount() {
        return listPengguna.size();
    }

    public void updateData(ArrayList<Pengguna> dataPenggunaBaru) {
        listPengguna.clear();
        listPengguna.addAll(dataPenggunaBaru);
        notifyDataSetChanged();
    }
}