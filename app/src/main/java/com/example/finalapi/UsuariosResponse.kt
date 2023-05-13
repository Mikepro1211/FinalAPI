package com.example.finalapi

import com.google.gson.annotations.SerializedName

data class UsuariosResponse (
    //respuesta de empoint recibe usuarios
    @SerializedName("listaUsuarios") var listaUsuarios: ArrayList<Usuario>
)