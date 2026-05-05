package com.example.expressapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.expressapp.models.Usuario
import org.json.JSONObject
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

/**
 * Clase que gestiona el inicio de sesión del usuario/empleado
 * @author Jose Manuel Rodríguez Madrid
 *
 */

class LoginActivity : AppCompatActivity() {
    //Variable iniciales
    private lateinit var usuario: EditText
    private lateinit var contraseña: EditText
    private lateinit var iniciar: Button
    private lateinit var recuperar: TextView
    private lateinit var requestQueue: RequestQueue
    private var user: String = ""
    private var password: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Asignamos las variables a sus id correspondientes
        usuario = findViewById(R.id.txtUser)
        contraseña = findViewById(R.id.txtPass)
        iniciar = findViewById(R.id.btnIniciarSesion)
        recuperar = findViewById(R.id.txtRecuperar)
        requestQueue = Volley.newRequestQueue(this)
        //Botón de inicio de sesión
        iniciar.setOnClickListener {
            //Asignamos los datos de los editext a las variables user y password
            user = usuario.text.toString()
            password = contraseña.text.toString()
            if (user.isEmpty() || password.isEmpty()) { //Si un campo esta vacío, mostramos mensaje
                Toast.makeText(
                    this,
                    "Los campos usuario y/o contraseña no pueden estar vacios",
                    Toast.LENGTH_SHORT
                ).show()
                usuario.requestFocus() //Para que el cursor vuelva al campo usuario

            } else { //En caso contrario llamamos a la función validar Usuario
                validarUsuario(user, password)
            }
        }

    }

    /**
     * Este método se le pasan los parámetros de usuario y clave para que mediante una petición
     * HTTP a través de la librería Volley nos devuelva un resultado positivo si el usuario y clave
     * es satisfactorio.
     * @param user
     * @param pass
     * @return MenuActivity si el resultado es satisfactorio
     */
    fun validarUsuario(user: String, pass: String) {
        //Es la dirección ip donde se encuentran el archivo de validación al cual se le pasan los datos user y pass
        val URL = "http://192.168.16.103/login_empleados/iniciarSesion.php?user=$user&pass=$pass"
        //Función para hacer la petición a la base de datos e iniciar la actividad MenuActivity
        //Se le pasa el método, en este caso GET, la URl de consulta, jsonRequest que va a null sino se pasan parámetros
        //Response.Listener que es la respuesta a la solicitud, si hay datos de respuesta ejecuta el código que haya dentro
        //Response.ErrorListener en caso de que no haya respuesta salta un error declarado, se puede poner a null sino queremos tratar errores.
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            URL,
            null,
            Response.Listener { response ->
                val user = Usuario() //Se crea un objeto Usuario
                val jsonArray = response.optJSONArray("datos") //Obtenemos los datos del array en formato JSON
                try {
                    val jsonObject = jsonArray.getJSONObject(0); //Creamos el objeto de tipo jsonObjet desde el índice 0
                    user.nombre = jsonObject.optString("nombre") //Asignamos los datos del objeto jsonObjet a cada
                    user.NIF = jsonObject.optString("NIF")      //variable correspondiente del objeto Usuario.
                    user.usuario = jsonObject.optString("usuario")
                    user.email = jsonObject.optString("email")
                    user.movil = jsonObject.optString("movil")
                    user.apellidos = jsonObject.optString("apellidos")
                    //Lanzamos la nueva actividad si la respuesta es correcta, se devuelven datos de la petición HTTP
                    val menu = Intent(this@LoginActivity, MenuActivity::class.java)
                    menu.putExtra("nombre", user.nombre) //Pasamos a la nueva actividad datos que necesitaremos utilizar
                    menu.putExtra("apellidos", user.apellidos)
                    menu.putExtra("NIF", user.NIF)
                    startActivity(menu)
                    finish()

                } catch (e: Exception) { //Si la respuesta no contiene datos, salta la excepción con el mensaje de datos erroneos.
                    Toast.makeText(
                        this,
                        "Error usuario y/o contraseña erroneos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                usuario.setText("") //Se limpian los datos de usuario y contraseña
                contraseña.setText("")
                usuario.requestFocus() //Se pone el cursor en el campo usuario
            },
            Response.ErrorListener { error ->
                // Si da un error la respuesta, salta el mensaje de error
                Toast.makeText(
                    this@LoginActivity,
                    "Error en conexión: ${error.message}",
                    Toast.LENGTH_LONG
                )
                    .show()
                usuario.setText("")
                contraseña.setText("")
                usuario.requestFocus()
            }) {}

        requestQueue.add(jsonObjectRequest)
    }

}