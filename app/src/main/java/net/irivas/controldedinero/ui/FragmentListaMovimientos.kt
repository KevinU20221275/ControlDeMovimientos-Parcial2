package net.irivas.controldedinero.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import net.irivas.controldedinero.R
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import net.irivas.controldedinero.data.ConexionSQLite

class FragmentListaMovimientos : Fragment() {

    private lateinit var db: ConexionSQLite

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_lista_movimientos, container, false)
        db = ConexionSQLite(requireContext())

        val contenedor = root.findViewById<LinearLayout>(R.id.contenedorLista)
        contenedor.removeAllViews()

        val lista = db.obtenerMovimientos()
        lista.forEach { m ->
            val view = inflater.inflate(R.layout.item_movimiento, contenedor, false)
            view.findViewById<TextView>(R.id.tvDescripcion).text =
                "Tipo: ${m.tipo} - Categoria: ${m.categoria} \nDescirpcion: ${m.descripcion} \nMonto: ${m.monto} USD \nFecha: ${m.fecha}"
            view.findViewById<Button>(R.id.btnEditar).setOnClickListener {
                val frag = FragmentMovimientosForm()
                val bundle = Bundle()
                bundle.putInt("MOVIMIENTO_ID", m.id)
                frag.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.contenedorFragmentos, frag)
                    ?.addToBackStack(null)
                    ?.commit()
            }
            view.findViewById<Button>(R.id.btnEliminar).setOnClickListener {
                val frag = FragmentEliminarMovimiento()
                val bundle = Bundle()
                bundle.putInt("MOVIMIENTO_ID", m.id)
                frag.arguments = bundle

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.contenedorFragmentos, frag)
                    ?.addToBackStack(null)
                    ?.commit()
            }
            contenedor.addView(view)
        }

        return root
    }
}
