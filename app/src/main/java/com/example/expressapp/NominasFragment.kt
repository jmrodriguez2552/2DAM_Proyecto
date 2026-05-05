package com.example.expressapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.FileProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import java.io.FileOutputStream


/**
 * Esta clase gestiona todo el funcionamiento relativo a la consulta de las nóminas por parte
 * de los empleados.
 * @author Jose Manuel Rodríguez Madrid
 */
class NominasFragment : Fragment() {
    //Variable iniciales
    private lateinit var spinnerAnio: Spinner
    private lateinit var spinnerMes: Spinner
    private lateinit var botonNomina: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //Asignamos las variables a sus id correspondientes
        val view = inflater.inflate(R.layout.fragment_nominas, container, false)
        spinnerAnio = view.findViewById(R.id.spinneranio)
        spinnerMes = view.findViewById(R.id.spinnermes)
        botonNomina = view.findViewById(R.id.btnNomina)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Creamos una lista con los años
        val anios = listOf("2024", "2025")
        //Creamos el adaptador y añadimos los años al spinner
        val adapterAnios =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, anios)
        adapterAnios.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerAnio.adapter = adapterAnios

        //Creamos una lista con los meses
        val meses = listOf(
            "enero",
            "febrero",
            "marzo",
            "abril",
            "mayo",
            "junio",
            "julio",
            "agosto",
            "septiembre",
            "octubre",
            "noviembre",
            "diciembre",
            "certificado_retenciones"
        )
        // Creamos el adaptador y añadimos los meses al spinner
        val adaterMeses =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, meses)
        adaterMeses.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerMes.adapter = adaterMeses

        botonNomina.setOnClickListener {
            val anio = spinnerAnio.selectedItem.toString() //Obtenemos el año del spinner
            val mes = spinnerMes.selectedItem.toString() //Obtenemos el mes del spinner
            val NIF = arguments?.getString("NIF") //Obtenemos el NIF del Bundle
            if (NIF != null) {
                //Es la dirección ip donde se encuentran el archivo de validación al cual se le pasan
                // los datos de las variables NIF,anio y mes
                val url =
                    "http://192.168.16.103/login_empleados/consultaNomina.php?NIF=$NIF&anio=$anio&mes=$mes"
                //Variable para poder inicializar una solicitud HTTP
                val queue = Volley.newRequestQueue(this.requireContext())
                //Función para hacer la petición a la base de datos e iniciar todo el proceso de
                //descarga, almacenamiento y visualización
                //Se le pasa el método, en este caso GET, la URl de consulta, jsonRequest que va a
                // null sino se pasan parámetros
                //Response.Listener que es la respuesta a la solicitud, si hay datos de respuesta
                // ejecuta el código que haya dentro
                //Response.ErrorListener en caso de que no haya respuesta salta un error declarado,
                // se puede poner a null sino queremos tratar errores.
                val jsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener { response ->
                        try {
                            //Se obtiene la cadena de texto "archivoBase64" de la respuesta la cual
                            // almacena el archivo en formato Base64
                            val archivoBase64 = response.getString("archivoBase64")
                            //Se decodifica la cadena Base64 obtenida en la variable anterior y la
                            // convierte en un array de bytes con el modo de decodificación DEFAULT
                            val archivoBytes = Base64.decode(archivoBase64, Base64.DEFAULT)
                            //Llamada a la función para alamcenar y mostrar el archivo en pdf
                            guardarMostrarNomina(archivoBytes, "$mes$anio", "pdf")

                        } catch (e: Exception) {
                            //Si se salta la excepción, eso significa que no ha sido posible obtener
                            // el documento, muestra mensaje
                            Toast.makeText(
                                this.requireContext(),
                                "Error al obtener el documento: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        //Si la respuesta no es satisfactoria, salta el error
                        Toast.makeText(
                            this.requireContext(),
                            "Error de conexión ${error.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }) {}
                queue.add(jsonObjectRequest)

            } else {
                //Si el campo NIF es nulo salta el error
                Toast.makeText(
                    this.requireContext(),
                    "El campo NIF no puede ser nulo o vacio.",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    /**
     *Esta función guarda el archivo en el almacenamiento externo del dispositivo y posteriormente
     * lo visualiza al usuario.
     * @param archivoBytes
     * @param nombreArchivo
     * @param tipoArchivo
     * @return Intent el cual muestra el archivo en PDF
     */
    fun guardarMostrarNomina(archivoBytes: ByteArray, nombreArchivo: String, tipoArchivo: String) {
        try {
            //Creación del directorio
            val filesDirectory = File(requireContext().getExternalFilesDir(null), "Documents")
            if (!filesDirectory.exists()) {
                filesDirectory.mkdirs()
            }
            //Creación y almacenamiento del archivo con el nombre y tipo especificados en los
            // parámetros pasados a la función
            val archivo = File(filesDirectory, "$nombreArchivo.$tipoArchivo")
            val outputStream = FileOutputStream(archivo)
            outputStream.write(archivoBytes) //Se escriben los bytes del archivo
            outputStream.close() //Se cierra el flujo de salida
            //Mensaje indicando la ruta de guardado
            Toast.makeText(
                requireContext(),
                "Documento guardado en: ${archivo.absolutePath}",
                Toast.LENGTH_LONG
            ).show()

            //Crear URI utilizando FileProvider y abrir el archivo mediante un intent
            //Permite acceder al archivo de forma segura ya que Android restringe el acceso directo
            // a archivos
            val uri: Uri? = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                archivo
            )
            if (uri != null) {
                //Se crea intent para abril el archivo, indicando el tipo del mismo (pdf) y se
                //conceden permisos de acceso
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    //Si el dispositivo no tiene una aplicación para poder abrir el tipo de archivo
                    //salta el error.
                    Toast.makeText(
                        requireContext(),
                        "No se encontro aplicación para abrir el archivo: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                //Si la URI falla, salta el error
                Toast.makeText(
                    requireContext(),
                    "Error al crear la URI del archivo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            //Si el proceso de guardado del archivo falla, salta el error
            Toast.makeText(
                requireContext(),
                "Error al guardar el documento: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}