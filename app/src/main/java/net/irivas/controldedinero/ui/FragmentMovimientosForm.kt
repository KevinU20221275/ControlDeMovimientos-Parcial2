package net.irivas.controldedinero.ui

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.irivas.controldedinero.R
import android.widget.*
import net.irivas.controldedinero.data.ConexionSQLite
import net.irivas.controldedinero.data.Movimiento
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FragmentMovimientosForm : Fragment() {

    private lateinit var db: ConexionSQLite
    private var movimientoId: Int? = null

    private var fechaSeleccionada : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movimientos_form, container, false)
        db = ConexionSQLite(requireContext())
        movimientoId = arguments?.getInt("MOVIMIENTO_ID")

        val spTipo = view.findViewById<Spinner>(R.id.txtTipoMovimiento)
        val spCategoria = view.findViewById<Spinner>(R.id.txtCategoria)
        val etDescripcion = view.findViewById<EditText>(R.id.txtDescripcion)
        val etMonto = view.findViewById<EditText>(R.id.txtMonto)
        val btnEnviar = view.findViewById<Button>(R.id.btnEnviar)
        val btnDatePicker = view.findViewById<Button>(R.id.btnSeleccionarFecha)
        val labelFecha = view?.findViewById<TextView>(R.id.labelFecha)
        val formTitle = view.findViewById<TextView>(R.id.txtTituloMovimientoForm)

        formTitle.setText("Agregar Movimiento")

        val adapterTipo = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            listOf("Ingreso", "Gasto")
        )
        adapterTipo.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spTipo.adapter = adapterTipo

        val adapterCategoria = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            listOf("Venta", "Devolucion s/ compra", "Devolucion s/ venta", "Transporte", "Comida", "Salud", "Servicios", "Otro")
        )
        adapterCategoria.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spCategoria.adapter = adapterCategoria

        // Si es edición, cargar datos
        movimientoId?.let { id ->
            val mov = db.obtenerMovimientoPorId(id)
            spTipo.setSelection(if (mov?.tipo == "Ingreso") 0 else 1)
            spCategoria.setSelection(
                when (mov?.categoria) {
                    "Venta" -> 0
                    "Devolucion s/ compra" -> 1
                    "Devolucion s/ venta" -> 2
                    "Transporte" -> 3
                    "Comida" -> 4
                    "Salud" -> 5
                    "Servicios" -> 6
                    else -> 7
                })
            etDescripcion.setText(mov?.descripcion)
            etMonto.setText(mov?.monto.toString())
            formTitle.setText("Actualizar Movimiento")
            labelFecha?.setText("Fecha: ${mov?.fecha}")
        }

        btnDatePicker.setOnClickListener { mostrarDatePicker() }

        btnEnviar.setOnClickListener {
            val tipo = spTipo.selectedItem.toString()
            val categoria = spCategoria.selectedItem.toString()
            val descripcion = etDescripcion.text.toString()
            val monto = etMonto.text.toString().toDoubleOrNull() ?: 0.0
            val fechaAGuardar = fechaSeleccionada ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (tipo.isNullOrBlank() || categoria.isNullOrBlank() || descripcion.isNullOrBlank() || monto.isNaN()) {
                Toast.makeText(requireContext(), "Por favor llene los campos requeridos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val mov = Movimiento(
                id = movimientoId ?: 0,
                tipo = tipo,
                categoria = categoria,
                descripcion = descripcion,
                monto = monto,
                fecha = fechaAGuardar
            )

            if (movimientoId == null) {
                db.insertarMovimiento(mov)
                Toast.makeText(requireContext(), "Movimiento agregado con exito", Toast.LENGTH_SHORT).show()
            } else{
                db.actualizarMovimiento(mov)
                Toast.makeText(requireContext(), "Movimiento actualizado con exito", Toast.LENGTH_SHORT).show()
            }

            // Volver a la lista
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.contenedorFragmentos, FragmentListaMovimientos())
                ?.commit()
        }

        return view
    }

    private fun mostrarDatePicker() {
        val labelFecha = view?.findViewById<TextView>(R.id.labelFecha)

        val cal = Calendar.getInstance()
        val hoy = Calendar.getInstance() // para comparar la fecha seleccionada

        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_DeviceDefault_Dialog,
            { _, year, month, day ->
                val seleccion = Calendar.getInstance()
                seleccion.set(year, month, day)

                if (seleccion.after(hoy)) {
                    Toast.makeText(requireContext(), "No se puede seleccionar una fecha futura", Toast.LENGTH_SHORT).show()
                    return@DatePickerDialog
                }

                val m = month + 1
                fechaSeleccionada = String.format("%04d-%02d-%02d", year, m, day)
                labelFecha?.setText("Fecha: ${fechaSeleccionada}")
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        // Limitar fecha máxima al día de hoy
        dialog.datePicker.maxDate = hoy.timeInMillis

        dialog.show()
    }

}
