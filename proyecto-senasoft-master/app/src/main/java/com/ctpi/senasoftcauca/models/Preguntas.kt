package com.ctpi.senasoftcauca.models

class Preguntas {

    var id = ""
    var codigo = ""
    var pregunta = ""
    var resp1 = ""
    var resp2 = ""
    var resp3 = ""
    var resp4 = ""
    var pista = ""
    var qr = ""


    constructor()
    constructor(
        id: String,
        codigo: String,
        pregunta: String,
        resp1: String,
        resp2: String,
        resp3: String,
        resp4: String,
        pista: String,
        qr: String
    ) {
        this.id = id
        this.codigo = codigo
        this.pregunta = pregunta
        this.resp1 = resp1
        this.resp2 = resp2
        this.resp3 = resp3
        this.resp4 = resp4
        this.pista = pista
        this.qr = qr
    }

    override fun toString(): String {

        return "Jugador(id='$id', codigo='$codigo', pregunta='$pregunta', resp1='$resp1', resp2='$resp2', resp3='$resp3', resp4='$resp4', pista='$pista', qr='$qr')"
    }
}