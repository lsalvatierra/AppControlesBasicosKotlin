package com.qbo.appcontrolesbasicoskotlin

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.qbo.appcontrolesbasicoskotlin.databinding.ActivityListaBinding
import com.qbo.appcontrolesbasicoskotlin.databinding.ActivityMainBinding

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