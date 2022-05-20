package edu.pe.idat.appcontrolesbasicoskotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import edu.pe.idat.appcontrolesbasicoskotlin.commom.AppMensaje
import edu.pe.idat.appcontrolesbasicoskotlin.commom.TipoMensaje
import edu.pe.idat.appcontrolesbasicoskotlin.databinding.ActivityMainBinding


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
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        estadocivil = if(p2 > 0){
            p0!!.getItemAtPosition(p2).toString()
        }else{
            ""
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onClick(p0: View) {
        if (p0 is CheckBox) {
            agregarQuitarPreferenciaSeleccionadas(p0)
        }else{
            when(p0.id){
                R.id.btnlistar -> irListaPersonas()
                R.id.btnregistrar -> registrarPersona()
            }
        }
    }
    //13.
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
    //12.
    private fun registrarPersona(){
        if(validarFormulario()){
            var infousuario = binding.etnombre.text.toString() + " " +
                    binding.etapellido.text.toString() + " " +
                    obtenerGeneroSeleccinado() + " " +
                    obtenerPreferenciasSeleccionadas()+" "+
                    estadocivil + " " +
                    binding.swemail.isChecked
            listausuarios.add(infousuario)
            AppMensaje.enviarMensaje(binding.root,
                getString(R.string.msjregistrocorrecto),
                TipoMensaje.SUCCESSFULL
            )
            setearControles()
        }
    }
    //11.
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
    //10.
    fun obtenerPreferenciasSeleccionadas():String{
        var preferencias = ""
        for (pref in listapreferencias){
            preferencias += "$pref -"
        }
        return preferencias
    }
    //9.
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
    //8.
    fun validarFormulario(): Boolean {
        var respuesta = false
        if(!validarNombreApellido()){
            AppMensaje.enviarMensaje(binding.root,
                getString(R.string.errorNombreApellido),
                TipoMensaje.ERROR
            )
        }else if(!validarGenero()){
            AppMensaje.enviarMensaje(binding.root,
                "Seleccione un género",
                TipoMensaje.ERROR
            )
        } else if(!validarEstadoCivil()){
            AppMensaje.enviarMensaje(binding.root,
                "Seleccione un estado civil",
                TipoMensaje.ERROR
            )
        } else if(!validarPreferencias()){
            AppMensaje.enviarMensaje(binding.root,
                "Seleccione una preferencia",
                TipoMensaje.ERROR
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


}