package net.irivas.controldedinero.data

data class Movimiento(
    val id: Int = 0,
    val tipo: String,
    val categoria: String,
    val descripcion: String,
    val monto : Double,
    val fecha: String
)
