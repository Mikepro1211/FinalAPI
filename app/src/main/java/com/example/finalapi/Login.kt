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

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //firebase
        auth = Firebase.auth
        //instancias
        val NoTengoCuenta = findViewById<Button>(R.id.tvNoTengoCuenta)
        val correo = findViewById<EditText>(R.id.txt_correo)
        val contrasena =  findViewById<EditText>(R.id.txt_password)
        val btnIngresar = findViewById<Button>(R.id.btn_Ingresar)
        NoTengoCuenta.setOnClickListener {
            Toast.makeText(this,"Crea tu cuenta", Toast.LENGTH_LONG).show()
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }
        btnIngresar.setOnClickListener {
            //llamado de funcion
            performLogin()
        }
    }
    private fun performLogin(){
        //instancias la info
        val correo = findViewById<EditText>(R.id.txt_correo)
        val contrasena =  findViewById<EditText>(R.id.txt_password)

        if(correo.text.isEmpty()|| contrasena.text.isEmpty()){
            Toast.makeText(this,"Porfavor rellene los campos", Toast.LENGTH_LONG).show()
            return
        }
        val emailInput= correo.text.toString()
        val passwordInput = contrasena.text.toString()

        auth.signInWithEmailAndPassword(emailInput,passwordInput)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    //sign in success update UI with the signed-in user's information
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"Se ha iniciado  con exito", Toast.LENGTH_LONG).show()
                }else{
                    //if fails , display message
                    Toast.makeText(this,"Fallo autenticacion", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Fallo autenticacion. ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }
    }