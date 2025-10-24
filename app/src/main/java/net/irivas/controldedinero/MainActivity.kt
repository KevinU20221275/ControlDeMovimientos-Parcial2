package net.irivas.controldedinero

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import net.irivas.controldedinero.ui.FragmentListaMovimientos
import net.irivas.controldedinero.ui.FragmentMovimientosForm
import net.irivas.controldedinero.ui.FragmentResumen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = "Control de Movimientos \uD83D\uDCB5"
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragmentos, FragmentResumen())
            .commit()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menu?.let {
            for (i in 0 until it.size()) {
                val item = it.getItem(i)
                val spanString = SpannableString(item.title)
                spanString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.blanco)),
                    0,
                    spanString.length,
                    0
                )
                item.title = spanString
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId){
            R.id.action_home -> FragmentResumen()
            R.id.action_agregar_movimiento -> FragmentMovimientosForm()
            R.id.action_lista_movimientos -> FragmentListaMovimientos()
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragmentos, it)
                .commit()
        }

        return true
    }
}