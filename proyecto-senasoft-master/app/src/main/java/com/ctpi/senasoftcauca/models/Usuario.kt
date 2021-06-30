package com.ctpi.senasoftcauca.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import com.orm.SugarRecord
import java.io.ByteArrayOutputStream

class Usuario(): SugarRecord<Usuario>() {
    var nikName: String = ""
    var foto: ByteArray = ByteArray(0)
    var codigo: String = ""
    var puntaje: Int = 0
    var tiempo: Long = 0

    constructor(nikName: String, foto: Bitmap, codigo: String, puntaje: Int): this(){
        this.nikName = nikName
        this.foto = byteConvertir(foto)
        this.codigo = codigo
        this.puntaje = puntaje
    }

    override fun toString(): String {
        var gson  = Gson()
        var usuario = Usuario()
        usuario.nikName = nikName
        usuario.foto = foto
        usuario.codigo = codigo
        usuario.puntaje = puntaje
        return gson.toJson(usuario)
    }

    fun byteConvertir(bmp: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun byteGet(byteArrayToBeCOnvertedIntoBitMap: ByteArray): Bitmap {
        val bitMapImage = BitmapFactory.decodeByteArray(
            byteArrayToBeCOnvertedIntoBitMap, 0,
            byteArrayToBeCOnvertedIntoBitMap.size
        )
        return bitMapImage;
    }

    fun insertar(usuario: Usuario): String {
        if (usuario.nikName.isEmpty() || usuario.codigo.isEmpty() || usuario.foto.isEmpty()) {
            return "Falta Informacion"
        }
        usuario.save()
        return "Usuario Creado"
    }

    fun iniciarSesion(usuario: Usuario): String {
        var usuarioRegistrado = SugarRecord.find(Usuario::class.java,"codigo = ?", usuario.codigo)
        if (usuarioRegistrado.isEmpty()) {
            return "El Codigo no existe"
        } else if (usuario.nikName != usuarioRegistrado[0].nikName) {
            return "El Nombre del Usuario Invalido"
        } else {
            return "Login"
        }
    }

    fun obteberLista(): List<Usuario> {
        return listAll(Usuario::class.java)
    }

    fun obtenerCodigo(codigo: String): Usuario {
        return SugarRecord.find(Usuario::class.java,"codigo = ?",codigo)[0]
    }

    fun obtenerNikName(nikName: String): Usuario {
        return SugarRecord.find(Usuario::class.java,"nickname = ?", nikName)[0]
    }

    fun subirPuntaje(puntaje: Int, id: Long): String {
        var usuario = SugarRecord.findById(Usuario::class.java, id)
        usuario.puntaje = puntaje
        usuario.save()
        return "Has subido "+puntaje+" de puntaje"
    }
}