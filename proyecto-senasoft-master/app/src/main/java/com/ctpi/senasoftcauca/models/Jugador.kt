package com.ctpi.senasoftcauca.models

class Jugador {
    var id = ""
    var nikName = ""
    var puntaje = 0
    var codigo = ""

    constructor()
    constructor(id: String, nikName: String, tirmpo: String, puntaje: Int, codigo: String) {
        this.id = id
        this.nikName = nikName
        this.puntaje = puntaje
        this.codigo = codigo
    }

    override fun toString(): String {
        return nikName + " : " +puntaje
    }




}