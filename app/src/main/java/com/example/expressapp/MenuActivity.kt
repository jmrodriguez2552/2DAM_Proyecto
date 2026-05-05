package com.example.expressapp

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.example.expressapp.models.Usuario

/**
 * Esta clase gestiona todo el ménu de la interfaz llamando a una función u otra dependiendo de la
 * opción que se escoja.
 * @author Jose Manuel Rodríguez Madrid
 */
class MenuActivity : AppCompatActivity() {
    //Variable iniciales
    private lateinit var saludo: TextView
    private lateinit var salarios: ImageView
    private lateinit var fragmento: FragmentContainerView
    private val fragNomina = NominasFragment()
    private val fragDocument = OtrosDocumentosFragment()
    private lateinit var bundle: Bundle
    private var NIF: String? = null
    private var nombre: String? = null
    private var apellidos: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_app)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Asignamos las variables a sus id correspondientes y recogemos variables pasadas en el
        //intent del LoginActivity
        nombre = intent.getStringExtra("nombre")
        apellidos = intent.getStringExtra("apellidos")
        NIF = intent.getStringExtra("NIF")
        saludo = findViewById(R.id.txtSaludo)
        saludo.text = "Bienvenido/a, $nombre"
        salarios = findViewById(R.id.imgNominas)
        fragmento = findViewById(R.id.Container)
        //Esta variable de tipo Bundle es para pasar un dato almacenado en otra variable a un fragmento
        bundle = Bundle()
        bundle.putString("NIF", NIF)
        fragNomina.arguments = bundle

    }

    /**
     * Esta función llama a la clase NominasFragment() reemplazando en el FragmentContainerView
     * lo que hay en fragNomina
     * @param view
     * @return contenido del fragmento fragNomina
     */
    fun salario(view: View) {

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.Container, fragNomina)
            addToBackStack(null)
            commit()
        }

    }

    /**
     * Esta función llama a la clase OtrosDocumentosFragment() reemplazando en el FragmentContainerView
     * lo que hay en fragDocument
     * @param view
     * @return contenido del fragmento fragDocument
     */
    fun document(view: View) {

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.Container, fragDocument)
            addToBackStack(null)
            commit()
        }
    }

    /**
     * Esta función inicia una nueva actividad y pasa a la misma los datos almacenados en las
     * variables nombre, apellidos y NIF para su posterior uso
     * @param view
     * @return PermisosActivity
     */
    fun permiso(view: View) {

        val intent = Intent(this@MenuActivity, PermisosActivity::class.java)
        intent.putExtra("nombre", nombre)
        intent.putExtra("apellidos", apellidos)
        intent.putExtra("NIF", NIF)
        startActivity(intent)
        finish()


    }

    /**
     * Esta función crea un dialogo de alerta indicando si deseas salir de la aplicación pudiendo
     * elegir entre CANCELAR y permanecer con la sesión inciada o ACEPTAR y cerrar la sesión y la
     * aplicación.
     * @param view
     * @return AlertDialog
     */
    fun salir(view: View) {
        val alerta = AlertDialog.Builder(this)
        alerta.apply {
            setIcon(R.drawable.salida)
            setTitle("EXIT")
            setMessage("¿Estás seguro de que deseas salir?")
            setCancelable(false)
            setPositiveButton("ACEPTAR", DialogInterface.OnClickListener { dialogInterface, _ ->
                finish() //Cerramos la actividad
                System.exit(0) // Finalizamos la ejecución de la aplicación

            })
            setNegativeButton("CANCELAR", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss() // Cierra el diálogo
            })
        }.create().show()

    }
}