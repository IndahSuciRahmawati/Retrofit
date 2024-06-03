package com.pab.android_client;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;
    // Konstruktor UserAdapter yang menerima List<User> dan Context
    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.Nim.setText(user.getNim());
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.prodi.setText(user.getProdi());
        // Mengatur OnClickListener pada itemView untuk menangani tap pada item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).showUpdateDialog(user);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
    // ViewHolder untuk UserAdapter
    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            email = itemView.findViewById(R.id.textViewEmail);
        }
    }
}
    private void updateUser(int id, String name, String email) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        User user = new User(id, name, email);
        Call<Void> call = apiService.updateUser(user);
        Log.d("MainActivity", "Updating user: " + id + ", " + name + ", " + email);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "User updated successfully");
                    Toast.makeText(MainActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Log.e("MainActivity", "Response error: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Failed to update user: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "Fetch error: ", t);
                Toast.makeText(MainActivity.this, "Failed to update user: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
