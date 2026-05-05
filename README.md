## ExpressApp 📱🚀

* ExpressApp es una solución integral nativa para Android diseñada para la gestión de recursos humanos y comunicación interna entre la empresa y sus empleados.
* La aplicación permite centralizar el acceso a documentos laborales, gestión de permisos y autenticación segura en una sola plataforma.

### 📝 Descripción

La aplicación funciona como un Portal del Empleado móvil. Tras un inicio de sesión validado contra un servidor remoto, el usuario accede a un panel principal 
desde el cual puede consultar sus nóminas, solicitar permisos oficiales mediante flujos de trabajo automatizados y gestionar otros documentos laborales.

### ✨ Características Principales

🔐 1. Sistema de Autenticación

* Acceso Seguro: Validación de credenciales mediante servicios web PHP y MySQL.
* Gestión de Perfil: Recuperación dinámica de datos del empleado (Nombre, NIF, Apellidos, Email) para personalizar la experiencia de usuario.

🏠 2. Panel de Control (Dashboard)

* Navegación Fluida: Uso de un MenuActivity centralizado que gestiona la navegación mediante Fragments y Activities.
* Interfaz Dinámica: Saludo personalizado y acceso visual mediante iconos a las diferentes secciones.
* Gestión de Salida: Sistema de cierre de sesión seguro mediante diálogos de confirmación (AlertDialog).

📄 3. Gestión de Documentación (Nóminas y Otros)

* Consulta de Nóminas: Filtrado por año y mes.
* Procesamiento de PDF:
* Descarga asíncrona de archivos en formato Base64 (Volley).
* Almacenamiento local seguro y visualización mediante FileProvider.
* Sección de Otros Documentos: Espacio dedicado para certificados de retenciones y otros documentos de interés.

📅 4. Módulo de Permisos Laborales

* Formulario Inteligente: Selección de fechas y horas mediante DatePicker y TimePicker nativos.
* Automatización de Solicitudes: Generación y envío automático de correos electrónicos estructurados al departamento de RRHH mediante Intents de sistema.

### 🛠️ Stack Tecnológico

+ ▶️ Lenguaje: Kotlin
+ 🌐 Networking: Volley (Peticiones JSON y manejo de colas).
+ 🏗️ Arquitectura: Basada en Componentes (Activities, Fragments, ViewContainers).
+ 🎨 UI/UX: Material Design, Floating Action Buttons (FAB), Dialogs personalizados.
+ 🚀 Seguridad y Archivos: FileProvider para intercambio seguro de URIs.
+ 💾 Backend: PHP / MySQL.

### 📂 Estructura del Proyecto

- LoginActivity.kt: Punto de entrada y validación de usuarios.
- MenuActivity.kt: Orquestador de la navegación y gestión de fragmentos.
- NominasFragment.kt: Lógica de descarga y visualización de documentos PDF.
- PermisosActivity.kt: Gestión de solicitudes de ausencia y permisos.
- OtrosDocumentosFragment.kt: Repositorio de documentación adicional.
- models/Usuario.kt: Modelo de datos del empleado.

### ⚙️ Configuración e Instalación

* Clonación del Repositorio:
    - git clone https://github.com/jmrodriguez2552/2DAM_Proyecto
* Configuración del Backend:
    - Aloja los scripts PHP en tu servidor.
    - Actualiza la dirección IP en las variables URL de LoginActivity.kt y NominasFragment.kt.
* Permisos: Asegúrate de que el AndroidManifest.xml incluya permisos de internet y la declaración del FileProvider.
* Compilación: Utiliza Android Studio (Ladybug o superior) con un SDK mínimo de nivel 24.

 ### 🤝 Contribuciones
 
Si quieres mejorar la app:
1. Haz un Fork del proyecto.
2. Crea una rama para tu mejora o nueva funcionalidad (git checkout -b feature/NuevaFuncionalidad).
3. Haz un Commit de tus cambios (git commit -m 'Añade nueva funcionalidad').
4. Envía un Pull Request.

### 📄 Licencia de Uso Educativo

- Este proyecto ha sido desarrollado con fines estrictamente educativos y académicos.
- Uso permitido: Se autoriza la consulta, descarga y modificación del código con fines de aprendizaje, estudio personal o demostración de competencias técnicas.
- Restricciones: No se permite el uso de este software, ni de su marca "ExpressApp", para fines comerciales o lucrativos sin autorización previa.
- Responsabilidad: El autor no se hace responsable del mal uso de la aplicación o de la gestión de datos sensibles en entornos de producción reales.


### ✒️ Autor

Jose Manuel Rodríguez- Desarrollo de Software - @jmrodriguez2552

💡 Notas del Desarrollador

* Navegación: Se ha implementado addToBackStack en la transacción de fragmentos para permitir una navegación natural hacia atrás dentro del menú principal.
* Seguridad de Archivos: La aplicación cumple con las restricciones de Android 11+ sobre el acceso a archivos mediante el uso de almacenamiento interno y proveedores de contenido.
* UX: Se han incluido validaciones de campos y manejo de errores de red para mejorar la resiliencia de la aplicación.
