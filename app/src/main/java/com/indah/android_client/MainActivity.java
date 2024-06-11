package com.pab.android_client;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.indah.android_client.ApiClient;
import com.indah.android_client.R;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.pab.android_client.UserAdapter userAdapter;
    private List<com.pab.android_client.User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new com.pab.android_client.UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);

        // Set the MainActivity reference in the adapter
        userAdapter.setMainActivity(this);

        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });

        fetchUsers();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        final EditText editTextNim = view.findViewById(R.id.editTextNim);
        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        final EditText editTextProdi = view.findViewById(R.id.editTextProdi);

        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Nim = editTextNim.getText().toString();
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String prodi = editTextProdi.getText().toString();
                addUser(Nim, name, email, prodi);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void addUser(String Nim, String name, String email, String prodi) {
        com.pab.android_client.ApiService apiService = ApiClient.getClient().create(com.pab.android_client.ApiService.class);
        com.pab.android_client.User user = new com.pab.android_client.User(Nim, name, email, prodi);
        Call<Void> call = apiService.insertUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add user: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to add user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchUsers() {
        com.pab.android_client.ApiService apiService = ApiClient.getClient().create(com.pab.android_client.ApiService.class);
        Call<List<com.pab.android_client.User>> call = apiService.getUsers();

        call.enqueue(new Callback<List<com.pab.android_client.User>>() {
            @Override
            public void onResponse(Call<List<com.pab.android_client.User>> call, Response<List<com.pab.android_client.User>> response) {
                if (response.isSuccessful()) {
                    userList.clear();
                    userList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "Response error: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<com.pab.android_client.User>> call, Throwable t) {
                Log.e("MainActivity", "Fetch error: ", t);
                Toast.makeText(MainActivity.this, "Failed to fetch users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(int id,String Nim, String name, String email, String prodi) {
        com.pab.android_client.ApiService apiService = ApiClient.getClient().create(com.pab.android_client.ApiService.class);
        com.pab.android_client.User user = new com.pab.android_client.User(id, Nim, name, email, prodi);
        Call<Void> call = apiService.updateUser(user);

        Log.d("MainActivity", "Updating user: " + id + ", " + Nim + ", " + name + ", " + email +", " + prodi);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "User updated successfully");
                    Toast.makeText(MainActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Log.e("MainActivity", "Response error: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Failed to update user: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "Fetch error: ", t);
                Toast.makeText(MainActivity.this, "Failed to update user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showUpdateDialog(final com.pab.android_client.User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update User");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_update_user, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText inputNim = viewInflated.findViewById(R.id.editTextNim);
        final EditText inputName = viewInflated.findViewById(R.id.editTextName);
        final EditText inputEmail = viewInflated.findViewById(R.id.editTextEmail);
        final EditText inputProdi = viewInflated.findViewById(R.id.editTextProdi);

        inputNim.setText(user.getNim());
        inputName.setText(user.getName());
        inputEmail.setText(user.getEmail());
        inputProdi.setText(user.getProdi());

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String Nim = inputNim.getText().toString();
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String prodi = inputProdi.getText().toString();
               updateUser(user.getId(), Nim, name, email, prodi);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void refreshData() {
        fetchUsers();
    }
}