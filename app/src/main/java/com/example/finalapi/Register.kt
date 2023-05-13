package com.example.finalapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    //instancia de google authenticator
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //variables del formulario
        val correo = findViewById<EditText>(R.id.txt_correo)
        val contrasena = findViewById<EditText>(R.id.tpassword)
        val btnregistrar = findViewById<Button>(R.id.btn_registrar)
        //fire base
        //inicializando fire base auth

        auth = Firebase.auth
        btnregistrar.setOnClickListener {
            //pendiente agregar funcion
            performSingUp()
        }
    }
    private fun performSingUp() {
        val email = findViewById<EditText>(R.id.txt_correo)
        val password = findViewById<EditText>(R.id.tpassword)
        //validacion
        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Llena todo los campos", Toast.LENGTH_LONG).show()
            return
        }
        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //sing in successful
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(baseContext, "Informacion agregada con exito", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Ocurrio un error ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }
}