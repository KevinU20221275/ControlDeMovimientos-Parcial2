package net.irivas.controldedinero.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLite(context: Context) : SQLiteOpenHelper(context, "movimientos.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE movimientos(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tipo TEXT," +
                    "categoria TEXT," +
                    "descripcion TEXT," +
                    "monto REAL," +
                    "fecha TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS movimientos")
        onCreate(db)
    }

    fun insertarMovimiento(m: Movimiento) : Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("tipo", m.tipo)
            put("categoria", m.categoria)
            put("descripcion", m.descripcion)
            put("monto", m.monto)
            put("fecha", m.fecha)
        }
        return db.insert("movimientos", null, values)
    }

    fun obtenerMovimientos(): List<Movimiento> {
        val lista = mutableListOf<Movimiento>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM movimientos ORDER BY fecha DESC", null)
        while (cursor.moveToNext()) {
            lista.add(
                Movimiento(
                    id = cursor.getInt(0),
                    tipo = cursor.getString(1),
                    categoria = cursor.getString(2),
                    descripcion = cursor.getString(3),
                    monto = cursor.getDouble(4),
                    fecha = cursor.getString(5)
                )
            )
        }
        cursor.close()
        return lista
    }

    fun actualizarMovimiento(m: Movimiento): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("tipo", m.tipo)
            put("categoria", m.categoria)
            put("descripcion", m.descripcion)
            put("monto", m.monto)
            put("fecha", m.fecha)
        }
        return db.update("movimientos", values, "id=?", arrayOf(m.id.toString()))
    }

    fun eliminarMovimiento(id: Int): Int {
        val db = writableDatabase
        return db.delete("movimientos", "id=?", arrayOf(id.toString()))
    }

    fun obtenerTotalPorTipo(tipo: String, fecha: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM(monto) FROM movimientos WHERE tipo = ? AND fecha = ?",
            arrayOf(tipo, fecha)
        )
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return total
    }

    fun obtenerMovimientoPorId(id: Int): Movimiento? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, tipo, descripcion, categoria, monto, fecha FROM movimientos WHERE id = ?",
            arrayOf(id.toString())
        )
        var movimiento: Movimiento? = null
        if (cursor.moveToFirst()) {
            movimiento = Movimiento(
                id = cursor.getInt(0),
                tipo = cursor.getString(1),
                descripcion = cursor.getString(2),
                categoria = cursor.getString(3),
                monto = cursor.getDouble(4),
                fecha = cursor.getString(5)
            )
        }
        cursor.close()
        db.close()
        return movimiento
    }
}