package mx.fei.prointesomovil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_citas.*
import mx.fei.prointesomovil.Interfaz.ClicLista
import mx.fei.prointesomovil.adaptador.AdaptadorListaCitas
import mx.fei.prointesomovil.pojos.Citas
import mx.fei.prointesomovil.util.Constantes

class CitasActivity : AppCompatActivity(), ClicLista {

    private var citas = ArrayList<Citas>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citas)
        title = "Mis citas"
        descargaListaWS()
    }

    fun descargaListaWS(){
        val bundle = intent.extras
        val idPaciente = bundle?.getInt("idpaciente")
        Ion.with(this@CitasActivity)
            .load("POST", Constantes.URL_WS+"ProyectoIntegracion/ws/fitNuutrition/citaPaciente")
            .setHeader("Content-Type", "application/x-www-form-urlencoded")
            .setBodyParameter("idPaciente", idPaciente.toString())
            .asString()
            .setCallback { e, result ->
                if (e != null){
                    e.printStackTrace()
                    Toast.makeText(this@CitasActivity, e.message, Toast.LENGTH_LONG).show()
                }else{
                    Log.d("RESPUESTA WS", result)
                    val gson = Gson()
                    val arrType = object: TypeToken<ArrayList<Citas>>() {}.type
                    citas = gson.fromJson(result, arrType)
                    cargaElementosLista()
                }
            }
    }

    fun cargaElementosLista(){
        val adaptadorCitas = AdaptadorListaCitas()
        adaptadorCitas.citas = citas
        adaptadorCitas.listener = this
        val layoutManager = LinearLayoutManager(this@CitasActivity)
        listaCitas.layoutManager = layoutManager
        listaCitas.adapter = adaptadorCitas
    }

    override fun clicElementoEditar(posicion: Int) {
        val edicion = citas[posicion]
        val gson = Gson()
        val datoEdicion = gson.toJson(edicion)
        val intent = Intent(this@CitasActivity, InformacionCitasActivity::class.java)
        intent.putExtra("isEdicion", true)
        intent.putExtra("datos", datoEdicion)
        startActivity(intent)
    }

    override fun clicElementoEliminar(posicion: Int) {
        TODO("Not yet implemented")
    }
}
