package com.pab.android_client;
import com.google.gson.annotations.SerializedName;
public class User {
    @SerializedName("id")
    private int id;
    @SerializedName("Nim")
    private String Nim;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("prodi")
    private String prodi;
    // Konstruktor untuk membuat objek User baru
    public User(int id, String name, String email) {
        this.id = id;
        this.Nim = Nim;
        this.name = name;
        this.email = email;
        this.prodi = prodi;
    }
    // Konstruktor untuk membuat objek User tanpa id (misalnya, untuk menambahkan user baru)
    public User(String Nim, String name, String email, String prodi) {
        this.Nim= Nim;
        this.name = name;
        this.email = email;
        this.prodi = prodi;
    }
    // Getter untuk mendapatkan id user
    public int getId() {
        return id;
    }
    // Setter untuk mengatur id user
    public void setId(int id) {
        this.id = id;
    }
    //getter untuk mendapatkan nim
    public int getNim() {
        return Nim;
    }
    // Setter untuk mengatur nim
    public void setNim(String Nim) {
        this.Nim = Nim;
    }
    // Getter untuk mendapatkan nama user
    public String getName() {
        return name;
    }
    // Setter untuk mengatur nama user
    public void setName(String name) {
        this.name = name;
    }
    // Getter untuk mendapatkan email user
    public String getEmail() {
        return email;
    }
    // Setter untuk mengatur email user
    public void setEmail(String email) {
        this.email = email;
    }
    // Getter untuk mendapatkan prodi
    public String getProdi() {
        return prodi;
    }
    // Setter untuk mengatur prodi
    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

}

