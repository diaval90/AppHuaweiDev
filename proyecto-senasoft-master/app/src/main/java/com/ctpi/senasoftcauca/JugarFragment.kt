package com.ctpi.senasoftcauca

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.ctpi.senasoftcauca.models.Jugador
import com.ctpi.senasoftcauca.models.Preguntas
import com.ctpi.senasoftcauca.models.SesionUsuario
import com.ctpi.senasoftcauca.models.Usuario
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.acticity_juego.view.*
import kotlinx.android.synthetic.main.activity_reto_final.view.*
import kotlinx.android.synthetic.main.fragment_jugar.view.*
import kotlinx.android.synthetic.main.fragment_retar.view.*
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class JugarFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var contexto = MenuActivity()
    lateinit var inflate: View
    var usuario = Usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflate = inflater.inflate(R.layout.fragment_jugar, container, false)
        var sesion = SesionUsuario.getInstance()
        usuario = sesion.usuario
        inflate.btnJugarSolo.setOnClickListener {
            cargarCodigo()
        }

        return inflate
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JugarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun cargarCodigo() {
        var lista: MutableList<Preguntas> = ArrayList()
        var fireBase = FirebaseDatabase.getInstance()
        var myRef = fireBase.getReference()

        var query: Query = myRef.child("Preguntas")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (objSnaptshot in dataSnapshot.children) {
                    val pregunta: Preguntas? = objSnaptshot.getValue(Preguntas::class.java)
                    lista.add(pregunta!!)
                }
                var num = (Math.random()*lista.size +0)
                val infView = layoutInflater.inflate(R.layout.acticity_juego, null)
                showDialog(infView,lista)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }

    private fun showDialog(infView: View, preguntas: List<Preguntas>) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(contexto)
        var contador = 0
        var pregunta = infView.txtPregunta
        var opc1 = infView.opc1
        var opc2 = infView.opc2
        var opc3 = infView.opc3
        var opc4 = infView.opc4
        var btn = infView.btnSiguiente
        var chipGroup = infView.radioGroup
        var cronometro = infView.cronometro
        cronometro.start()
        var respuesta = ""
        cargarInformacion(opc1,opc2,opc3,opc4,pregunta,preguntas.get(contador))
        dialog.setView(infView)
        var creardo = dialog.create()
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId){
                R.id.opc1 ->{
                    if (preguntas.get(contador).resp1.equals(infView.opc1.text)) {
                        respuesta = "Respuesta Correcta"
                        usuario.puntaje+=3
                    }else {
                        respuesta = "Respuesta Incorrecta"
                    }
                }
                R.id.opc2 ->{
                    if (preguntas.get(contador).resp1.equals(infView.opc2.text)) {
                        respuesta = "Respuesta Correcta"
                        usuario.puntaje+=3
                    }else {
                        respuesta = "Respuesta Incorrecta"
                    }
                }
                R.id.opc3 ->{
                    if (preguntas.get(contador).resp1.equals(infView.opc3.text)) {
                        respuesta = "Respuesta Correcta"
                        usuario.puntaje+=3
                    }else {
                        respuesta = "Respuesta Incorrecta"
                    }
                }
                R.id.opc4 -> {
                    if (preguntas.get(contador).resp1.equals(infView.opc4.text)) {
                        respuesta = "Respuesta Correcta"
                        usuario.puntaje+=3
                    }else {
                        respuesta = "Respuesta Incorrecta"
                    }
                }
            }
        }
        btn.setOnClickListener {
            Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show()
            if (respuesta == "Respuesta Correcta"){
                contexto.BtnClick()
                Toast.makeText(context, "Lee el Codigpo Qr: "+preguntas.get(contador).qr, Toast.LENGTH_SHORT).show()
            }
            if (contador > 1) {
                creardo.cancel()
                val infView = layoutInflater.inflate(R.layout.activity_reto_final, null)
                showRetoFinal(infView, cronometro)
            } else {
                contador++
                cargarInformacion(opc1,opc2,opc3,opc4,pregunta,preguntas.get(contador))
            }
        }
        creardo.show()
    }

    fun showRetoFinal(infView: View, cornometro: Chronometer){
        val dialog = androidx.appcompat.app.AlertDialog.Builder(contexto)
        val texto = infView.txtRespFinal
        val btn = infView.btnEnviarResp
        dialog.setView(infView)
        var creardo = dialog.create()
        creardo.show()
        btn.setOnClickListener {
            if (texto.text.toString().toLowerCase() == "mariposa") {
                usuario.puntaje+=5
                var  intent = Intent(contexto, PruebaAr2::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(context, "Intentalo de Nuevo", Toast.LENGTH_SHORT).show()
            }
            insertar()
            creardo.cancel()
        }
    }

    fun cargarInformacion(opc1: RadioButton, opc2: RadioButton, opc3: RadioButton, opc4: RadioButton, texto: TextView, pregunta: Preguntas) {
        var listaResp: MutableList<String> = ArrayList()
        listaResp.add(pregunta.resp1)
        listaResp.add(pregunta.resp2)
        listaResp.add(pregunta.resp3)
        listaResp.add(pregunta.resp4)

        Collections.shuffle(listaResp)

        texto.setText(pregunta.pregunta)
        opc1.setText(listaResp.get(0))
        opc2.setText(listaResp.get(1))
        opc3.setText(listaResp.get(2))
        opc4.setText(listaResp.get(3))
    }
    fun insertar () {

        var fireBase = FirebaseDatabase.getInstance()
        var myRef = fireBase.getReference()

        var jugador: Jugador = Jugador()

        jugador.id = UUID.randomUUID().toString()
        jugador.codigo = usuario.codigo
        jugador.nikName = usuario.nikName
        jugador.puntaje = usuario.puntaje
        myRef.child("Jugador").child(jugador.id).setValue(jugador)
        Toast.makeText( contexto, "Insertado", Toast.LENGTH_SHORT).show()

    }

}