package com.example.admin.labeq;

/**
 * Created by Admin on 27.3.2017..
 */

import com.google.gson.annotations.SerializedName;

public class Podaci {
    // @SerializedName kako bi GSON shvatio o cemu je rijec
    @SerializedName("id")
    public String id;
    @SerializedName("vrsta")
    public String vrsta;
    @SerializedName("opis")
    public String opis;
}