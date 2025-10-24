package net.irivas.controldedinero.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import net.irivas.controldedinero.data.Movimiento
import net.irivas.controldedinero.R


class MovimientosAdapter(
    private val context: Context,
    private val lista: List<Movimiento>,
    private val onActionClick: (Movimiento, String) -> Unit
) : BaseAdapter() {

    override fun getCount() = lista.size
    override fun getItem(position: Int) = lista[position]
    override fun getItemId(position: Int) = lista[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_movimiento, parent, false)
        val movimiento = lista[position]

        view.findViewById<TextView>(R.id.txtDescripcion).text = movimiento.descripcion
        view.findViewById<TextView>(R.id.txtMonto).text = "$${movimiento.monto}"

        view.findViewById<Button>(R.id.btnEditar).setOnClickListener {
            onActionClick(movimiento, "editar")
        }
        view.findViewById<Button>(R.id.btnEliminar).setOnClickListener {
            onActionClick(movimiento, "eliminar")
        }

        return view
    }
}

