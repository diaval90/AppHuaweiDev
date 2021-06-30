package com.ctpi.senasoftcauca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ctpi.senasoftcauca.models.Preguntas
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import java.util.*

class AdminPregunatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pregunats)
        insertar()
    }

    fun insertar () {

        var fireBase = FirebaseDatabase.getInstance()
        var myRef = fireBase.getReference()

        var preguntas: Preguntas = Preguntas()

        preguntas.id = UUID.randomUUID().toString()
        preguntas.codigo = "c002"
        preguntas.pregunta = "¿Dónde se encuentra la famosa Torre Eiffel?"
        preguntas.resp1 = "Paris"
        preguntas.resp2 = "Mexico"
        preguntas.resp3 = "Canada"
        preguntas.resp4 = "Italia"
        preguntas.pista = "https://www.facebook.com/SENACauca"
        myRef.child("Preguntas").child(preguntas.id).setValue(preguntas)
        Toast.makeText(this, "Insertado", Toast.LENGTH_SHORT).show()
    }

}