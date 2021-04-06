package com.qbo.appcontrolesbasicoskotlin
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.qbo.appcontrolesbasicoskotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener,
    View.OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private val listapreferencias = ArrayList<String>()
    private val listausuarios = ArrayList<String>()
    private var estadocivil = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //2.
        ArrayAdapter.createFromResource(
            this,
            R.array.estado_civil_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spestadocivil.adapter = adapter
        }
        binding.spestadocivil.onItemSelectedListener = this
        binding.btnregistrar.setOnClickListener(this)
        binding.btnlistar.setOnClickListener(this)
        binding.chkdeporte.setOnClickListener(this)
        binding.chkdibujo.setOnClickListener(this)
        binding.chkotros.setOnClickListener(this)

    }

    fun obtenerGeneroSeleccinado():String{
        var genero = ""
        when(binding.rggenero.checkedRadioButtonId){
            R.id.rbtnmasculino -> {
                genero = binding.rbtnmasculino.text.toString()
            }
            R.id.rbtnfemenino -> {
                genero = binding.rbtnfemenino.text.toString()
            }
        }
        return genero
    }
    fun obtenerPreferenciasSeleccionadas():String{
        var preferencias = ""
        for (pref in listapreferencias){
            preferencias += "$pref -"
        }
        return preferencias
    }
    fun agregarQuitarPreferenciaSeleccionadas(view: View) {
        val checkBox = view as CheckBox
        if(checkBox.isChecked){
            when(checkBox.id){
                R.id.chkdeporte -> listapreferencias.add(checkBox.text.toString())
                R.id.chkdibujo -> listapreferencias.add(checkBox.text.toString())
                R.id.chkotros -> listapreferencias.add(checkBox.text.toString())
            }
        }else{
            when(checkBox.id){
                R.id.chkdeporte -> listapreferencias.remove(checkBox.text.toString())
                R.id.chkdibujo -> listapreferencias.remove(checkBox.text.toString())
                R.id.chkotros -> listapreferencias.remove(checkBox.text.toString())
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        estadocivil = if(position > 0){
            parent!!.getItemAtPosition(position).toString()
        }else{
            ""
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        if (v!! is CheckBox) {
            agregarQuitarPreferenciaSeleccionadas(v!!)
        }else{
            when(v!!.id){
                R.id.btnlistar -> irListaPersonas()
                R.id.btnregistrar -> registrarPersona(v!!)
            }
        }
    }

    fun irListaPersonas(){
        val intentlista = Intent(
            this,
            ListaActivity::class.java
        ).apply {
            putExtra(
                "listausuarios",
                listausuarios
            )
        }
        startActivity(intentlista)
    }
    fun registrarPersona(vista: View){
        if(validarFormulario(vista)){
            var infousuario = binding.etnombre.text.toString() + " " +
                    binding.etapellido.text.toString() + " " +
                    obtenerGeneroSeleccinado() + " " +
                    obtenerPreferenciasSeleccionadas()+" "+
                    estadocivil + " " +
                    binding.swemail.isChecked
            listausuarios.add(infousuario)
            setearControles()
        }
    }
    //8.
    fun validarFormulario(vista: View): Boolean {
        var respuesta = false
        if(!validarNombreApellido()){
            enviarMensajeError(
                vista,
                getString(R.string.errorNombreApellido)
            )
        }else if(!validarGenero()){
            enviarMensajeError(
                vista,
                "Seleccione un género"
            )
        } else if(!validarEstadoCivil()){
            enviarMensajeError(
                vista,
                "Seleccione un estado civil"
            )
        } else if(!validarPreferencias()){
            enviarMensajeError(
                vista,
                "Seleccione una preferencia"
            )
        }  else{
            respuesta = true
        }
        return respuesta
    }
    //7.
    fun validarPreferencias():Boolean{
        var respuesta = false
        if(binding.chkdeporte.isChecked || binding.chkdibujo.isChecked
            || binding.chkotros.isChecked){
            respuesta = true
        }
        return respuesta
    }
    //6.
    fun validarEstadoCivil():Boolean{
        var respuesta = true
        if(estadocivil == ""){
            respuesta = false
        }
        return respuesta
    }
    //5.
    fun validarGenero():Boolean{
        var respuesta = true
        if(binding.rggenero.checkedRadioButtonId == -1){
            respuesta = false
        }
        return respuesta
    }
    //4.
    fun validarNombreApellido():Boolean{
        var respuesta = true
        if(binding.etnombre.text.toString().trim().isEmpty()){
            binding.etnombre.isFocusableInTouchMode = true
            binding.etnombre.requestFocus()
            respuesta = false
        } else if(binding.etapellido.text.toString().trim().isEmpty()){
            binding.etapellido.isFocusable = true
            binding.etapellido.isFocusableInTouchMode = true
            binding.etapellido.requestFocus()
            respuesta = false
        }
        return respuesta
    }
    //3
    fun setearControles(){
        listapreferencias.clear()
        binding.etnombre.setText("")
        binding.etapellido.setText("")
        binding.swemail.isChecked = false
        binding.chkdeporte.isChecked = false
        binding.chkdibujo.isChecked = false
        binding.chkotros.isChecked = false
        binding.rggenero.clearCheck()
        binding.etnombre.isFocusableInTouchMode = true
        binding.etnombre.requestFocus()
        binding.spestadocivil.setSelection(0)
    }
    //1.
    fun enviarMensajeError(vista: View, mensajeError: String){
        val snackbar = Snackbar.make(vista, mensajeError, Snackbar.LENGTH_LONG)
        val snackBarView: View = snackbar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this,
            R.color.snackbarerror))
        snackbar.show()
    }
}