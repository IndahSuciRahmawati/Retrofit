package com.pab.android_client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indah.android_client.ApiClient;
import com.indah.android_client.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<com.pab.android_client.User> userList;
    private Context context;
    private com.pab.android_client.MainActivity mainActivity; // Referensi ke MainActivity

    // Konstruktor UserAdapter yang menerima List<User> dan Context
    public UserAdapter(List<com.pab.android_client.User> userList, Context context) {
        this.userList = userList;
        this.context = context;

        // Check if context is an instance of MainActivity and set mainActivity
        if (context instanceof com.pab.android_client.MainActivity) {
            this.mainActivity = (com.pab.android_client.MainActivity) context;
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final com.pab.android_client.User user = userList.get(position);
        holder.Nim.setText(user.getNim());
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.prodi.setText(user.getProdi());

        // Mengatur OnClickListener pada itemView untuk menangani tap pada item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity != null) {
                    mainActivity.showUpdateDialog(user);
                }
            }
        });

        // Mengatur OnClickListener pada tombol delete untuk menangani tap pada tombol delete
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(user.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Metode untuk menampilkan dialog konfirmasi penghapusan
    private void showDeleteConfirmationDialog(final int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete this user?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser(userId);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    // Metode untuk menghapus pengguna dari daftar dan server
    private void deleteUser(int userId) {
        com.pab.android_client.ApiService apiService = ApiClient.getClient().create(com.pab.android_client.ApiService.class);
        Call<Void> call = apiService.deleteUser(userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getId() == userId) {
                            userList.remove(i);
                            notifyItemRemoved(i);
                            break;
                        }}
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    // Refresh user list after deletion
                    mainActivity.refreshData();
                } else {
                    Toast.makeText(context, "Failed to delete user: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed to delete user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ViewHolder untuk UserAdapter
    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView Nim,name, email, prodi;
        public Button buttonDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            Nim = itemView.findViewById(R.id.textViewNim);
            name = itemView.findViewById(R.id.textViewName);
            email = itemView.findViewById(R.id.textViewEmail);
            prodi = itemView.findViewById(R.id.textViewProdi);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    // Metode untuk mengatur MainActivity yang terkait
    public void setMainActivity(com.pab.android_client.MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

}