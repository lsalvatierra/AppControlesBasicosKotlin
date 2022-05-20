package edu.pe.idat.appcontrolesbasicoskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.R
import edu.pe.idat.appcontrolesbasicoskotlin.databinding.ActivityListaBinding

class ListaActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listusuarios = intent.getSerializableExtra("listausuarios")
                as ArrayList<String>
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_list_item_1,
            listusuarios
        )
        binding.lvpersonas.adapter = adapter
    }
}