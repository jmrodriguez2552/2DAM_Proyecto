package com.example.expressapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expressapp.models.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

/**
 * Esta clase gestiona todo lo relativo a la solicitud de permisos por varios mótivos
 * @author Jose Manuel Rodríguez Madrid
 */
class PermisosActivity : AppCompatActivity() {

    //Creamos variables iniciales
    private lateinit var btnDateInicio: Button
    private lateinit var btnDateFin: Button
    private lateinit var btnHora: Button
    private lateinit var btnSolicitar: Button
    private lateinit var textoFechaInicio: TextView
    private lateinit var textoFechaFin: TextView
    private lateinit var textoHora: TextView
    private lateinit var spinerMotivo: Spinner
    private lateinit var fabVolver: FloatingActionButton
    private var nombre:String? = null
    private var apellidos:String? = null
    private var NIF:String? = null
    private var nombreCompleto:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_permisos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.permisos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Asociamos las variables con su id correspondiente
        btnDateInicio = findViewById(R.id.btnFechaInicio)
        btnDateFin = findViewById(R.id.btnFechaFin)
        btnHora = findViewById(R.id.btnHoraInicio)
        btnSolicitar = findViewById(R.id.btnSolicitar)
        textoFechaInicio = findViewById(R.id.txtFechaInicio)
        textoFechaFin = findViewById(R.id.txtFechaFin)
        textoHora = findViewById(R.id.txtHoraInicio)
        spinerMotivo = findViewById(R.id.SpinnerMotivo)
        fabVolver = findViewById(R.id.fabBack)
        //Asociamos el valor del intent a las variables
        nombre = intent.getStringExtra("nombre")
        NIF = intent.getStringExtra("NIF")
        apellidos = intent.getStringExtra("apellidos")
        nombreCompleto = "$nombre $apellidos"

        /**
         * Este botón establece a traves de la función Calendar.getInstance(), las variables
         * anio, mes y día, para posteriormente asignar los valores de esas variables al TextView
         * del layout.
         * @param anio
         * @param mes
         * @param dia
         * @return textoFechaInicio.
         */
        btnDateInicio.setOnClickListener {
            val calendar = Calendar.getInstance()
            val anio = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@PermisosActivity,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val fecha = "$dayOfMonth / ${month + 1} / $year"
                    textoFechaInicio.text = fecha

                },
                anio,
                mes,
                dia
            )
            datePicker.show()
        }
        /**
         * Este botón establece a traves de la función Calendar.getInstance(), las variables
         * anio, mes y día, para posteriormente asignar los valores de esas variables al TextView
         * del layout.
         * @param anio
         * @param mes
         * @param dia
         * @return textoFechaFin.
         */
        btnDateFin.setOnClickListener {
            val calendar = Calendar.getInstance()
            val anio = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@PermisosActivity,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val fecha = "$dayOfMonth / ${month + 1} / $year"
                    textoFechaFin.text = fecha

                },
                anio,
                mes,
                dia
            )
            datePicker.show()
        }

        /**
         * Este botón establece a traves de la función Calendar.getInstance(), las variables
         * hora, mes y día, para posteriormente asignar los valores de esas variables al TextView
         * del layout.
         * @param hora
         * @param minuto
         * @return textoHora.
         */
        btnHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hora = calendar.get(Calendar.HOUR_OF_DAY)
            val minuto = calendar.get(Calendar.MINUTE)
            val time = TimePickerDialog(
                this@PermisosActivity,
                TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    if (minute < 10) {
                        textoHora.text = "$hour : 0$minute"
                    } else {
                        textoHora.text = "$hour : $minute"
                    }

                }, hora, minuto, false
            )
            time.show()
        }

        /**
         * Este botón nos devuelve a la actividad Menu y finaliza la actividad
         * PermisosActivity.
         * @param this
         * @param MenuActivity::class.java
         * @return MenuActivity
         */

        fabVolver.setOnClickListener {
            val volver = Intent(this, MenuActivity::class.java)
            volver.putExtra("nombre", nombre)
            volver.putExtra("apellidos", apellidos)
            volver.putExtra("NIF", NIF)
            startActivity(volver)
            finish()
        }

    }

    /**
     * Método para recoger los valores de fecha de inicio, fecha fin, hora de inicio y motivo
     * para posteriormente enviarlos a una dirección de correo electrónico mediante su gestor
     * de correo predeterminado.
     * @param view
     * @return Intent de envío de correo electrónico (Intent.ACTION_SENDTO).
     */
    fun solicitarPermiso(view: View) {
        //Correo de la empresa donde se envía
        val correo = "padacaexpress@outlook.com"
        val motivo = spinerMotivo.selectedItem.toString() //Recogemos el motivo seleccionado
        //Contenido del mensaje
        val sms =
            "Empleado: ${nombreCompleto}\n"+
            "Fecha de inicio: ${textoFechaInicio.text}\n" +
                    "Fecha fin: ${textoFechaFin.text}\n" +
                    "Hora inicio: ${textoHora.text}\n" +
                    "Motivo: $motivo"
        //Intent para abrir aplicación de correo electrónico
        val email = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, "$correo")
            putExtra(Intent.EXTRA_SUBJECT, "Solicitud de permiso")
            putExtra(Intent.EXTRA_TEXT, "$sms")

        }
        startActivity(email)
    }
}