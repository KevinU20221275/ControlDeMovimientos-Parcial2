package net.irivas.controldedinero.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import net.irivas.controldedinero.R
import net.irivas.controldedinero.data.ConexionSQLite

class FragmentEliminarMovimiento : Fragment() {

    private lateinit var db: ConexionSQLite
    private var movimientoId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_eliminar_movimiento, container, false)
        db = ConexionSQLite(requireContext())

        val txtDetalle = view.findViewById<TextView>(R.id.txtDetalle)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelar)

        movimientoId = arguments?.getInt("MOVIMIENTO_ID") ?: 0

        // Mostrar detalle del movimiento
        val movimiento = db.obtenerMovimientoPorId(movimientoId)
        movimiento?.let {
            txtDetalle.text = """
                Tipo: ${it.tipo}
                Categoría: ${it.categoria}
                Descripción: ${it.descripcion}
                Monto: ${it.monto} USD
                Fecha: ${it.fecha}
            """.trimIndent()
        }

        btnConfirmar.setOnClickListener {
            db.eliminarMovimiento(movimientoId)
            Toast.makeText(requireContext(), "Movimiento eliminado correctamente", Toast.LENGTH_SHORT).show()

            // Redirigir al listado
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragmentos, FragmentListaMovimientos())
                .commit()
        }

        btnCancelar.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}

