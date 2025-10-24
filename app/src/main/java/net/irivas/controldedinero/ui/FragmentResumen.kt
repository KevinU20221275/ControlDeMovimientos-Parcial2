package net.irivas.controldedinero.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import net.irivas.controldedinero.R
import net.irivas.controldedinero.data.ConexionSQLite
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FragmentResumen : Fragment() {
    private lateinit var db: ConexionSQLite
    private lateinit var tvFecha : TextView
    private lateinit var tvIngresos: TextView
    private lateinit var tvGastos : TextView
    private lateinit var tvSaldo : TextView
    private lateinit var btnCambiarFecha : Button
    private lateinit var btnActualizar : Button

    private var fechaSeleccionada : String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_resumen, container, false)
        db = ConexionSQLite(requireContext())

        tvFecha = view.findViewById(R.id.tvFecha)
        tvIngresos = view.findViewById(R.id.tvIngresos)
        tvGastos = view.findViewById(R.id.tvGastos)
        tvSaldo = view.findViewById(R.id.tvSaldo)
        btnCambiarFecha = view.findViewById(R.id.btnCambiarFecha)

        // la fecha por defecto sera el dia actual
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fechaSeleccionada = sdf.format(Date())
        tvFecha.text = fechaSeleccionada

        btnCambiarFecha.setOnClickListener { mostrarDatePicker() }

        // Mostrar resumen inicial
        mostrarResumen(fechaSeleccionada)

        return view
    }

    private fun mostrarDatePicker() {
        val cal = Calendar.getInstance()
        val hoy = Calendar.getInstance() // para comparar fechas
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_DeviceDefault_Dialog,
            { _, year, month, day ->
                val m = month + 1
                fechaSeleccionada = String.format("%04d-%02d-%02d", year, m, day)
                tvFecha.text = fechaSeleccionada
                mostrarResumen(fechaSeleccionada)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        // Limitar fecha máxima al día de hoy
        dialog.datePicker.maxDate = hoy.timeInMillis

        dialog.show()
    }

    private fun mostrarResumen(fecha: String) {
        val ingresos = db.obtenerTotalPorTipo("Ingreso", fecha)
        val gastos = db.obtenerTotalPorTipo("Gasto", fecha)
        val saldo = ingresos - gastos

        tvIngresos.text = "$${"%.2f".format(ingresos)}"
        tvGastos.text = "$${"%.2f".format(gastos)}"
        tvSaldo.text = "Saldo: $${"%.2f".format(saldo)}"
    }
}