package com.example.finalapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),UsuarioAdapter.OnItemClicked {
    lateinit var  binding: ActivityMainBinding
    //variable adaptador de ryclerview
    lateinit var adaptador: UsuarioAdapter
    //arreglo almacenador de informacion
    var listaUsuarios = arrayListOf<Usuario>()
    //variable auxiliar
    var usuario = Usuario(-1,"","")

    var isEditando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsuarios.layoutManager=LinearLayoutManager(this)
        setupRecyclerView()
        obtenerUsuarios()
        binding.btnAddUpdated.setOnClickListener {
            val isValido = validarCampos()
            if(isValido){
                if(!isEditando){
                    agregarUsuario()
                }else{
                    actualizarUsuario()
                }
            }else{
                Toast.makeText(this,"Se debe llenar los campos",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setupRecyclerView() {
        adaptador = UsuarioAdapter(this, listaUsuarios)
        adaptador.setOnClick(this@MainActivity)
        binding.rvUsuarios.adapter =adaptador
    }
    fun validarCampos():Boolean{
        return !(binding.etNombre.text.isNullOrEmpty()|| binding.etEmail.text.isNullOrEmpty())
    }
    fun obtenerUsuarios(){
        //corrutinas
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webServices.obtenerUsuarios()
            runOnUiThread {
                if (call.isSuccessful) {
                    listaUsuarios = call.body()!!.listaUsuarios
                    setupRecyclerView()
                }else{
                    Toast.makeText(this@MainActivity, "Error de consulta",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun agregarUsuario(){
        this.usuario.idUsuario =-1
        this.usuario.nombre= binding.etNombre.text.toString()
        this.usuario.email= binding.etEmail.text.toString()
        //corrutinas
        CoroutineScope(Dispatchers.IO).launch {
            val  call = RetrofitClient.webServices.agregarUsuario(usuario)
            runOnUiThread{
                if(call.isSuccessful){
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                    obtenerUsuarios()
                    limpiarCampos()
                    limpiarObjeto()
                }else{
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun actualizarUsuario(){
        this.usuario.nombre= binding.etNombre.text.toString()
        this.usuario.email= binding.etEmail.text.toString()
        //corrutinas
        CoroutineScope(Dispatchers.IO).launch {
            val  call = RetrofitClient.webServices.actualizarUsuario(usuario.idUsuario, usuario)
            runOnUiThread{
                if(call.isSuccessful){
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                    obtenerUsuarios()
                    limpiarCampos()
                    limpiarObjeto()
                    binding.btnAddUpdated.setText("Agregar Usuario")
                    binding.btnAddUpdated.backgroundTintList =resources.getColorStateList(R.color.black)
                    isEditando =false
                }else{
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun limpiarCampos(){
        binding.etNombre.setText("")
        binding.etEmail.setText("")
    }
    fun limpiarObjeto(){
        this.usuario.idUsuario =-1
        this.usuario.nombre = ""
        this.usuario.email =""
    }

    override fun editarUsuario(usuario: Usuario) {
        binding.etNombre.setText(usuario.nombre)
        binding.etEmail.setText(usuario.email)
        binding.btnAddUpdated.setText("Actualizar Usuario")
        binding.btnAddUpdated.backgroundTintList= resources.getColorStateList(R.color.purple_500)
        this.usuario = usuario
        isEditando =true
    }

    override fun borrarUsuario(idUsuario: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webServices.borrarUsuario(idUsuario)
            runOnUiThread {
                if(call.isSuccessful){
                    Toast.makeText(this@MainActivity,call.body().toString(),Toast.LENGTH_SHORT).show()
                    obtenerUsuarios()
                }
            }
        }
    }
}