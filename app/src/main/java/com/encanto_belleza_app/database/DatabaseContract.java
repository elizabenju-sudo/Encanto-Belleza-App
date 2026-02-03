package com.encanto_belleza_app.database;

public class DatabaseContract {
    private DatabaseContract() {}

    public static class UsuarioEntry {
        public static final String TABLE_NAME = "usuarios";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_TIPO = "tipo"; // cliente, empleado, admin

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL," +
                COLUMN_PASSWORD + " TEXT NOT NULL," +
                COLUMN_NOMBRE + " TEXT," +
                COLUMN_TIPO + " TEXT DEFAULT 'cliente')";
    }

    public static class ServicioEntry {
        public static final String TABLE_NAME = "servicios";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_DESCRIPCION = "descripcion";
        public static final String COLUMN_DURACION = "duracion";
        public static final String COLUMN_PRECIO = "precio";
        public static final String COLUMN_IMAGEN = "imagen";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOMBRE + " TEXT NOT NULL," +
                COLUMN_DESCRIPCION + " TEXT," +
                COLUMN_DURACION + " INTEGER," +
                COLUMN_PRECIO + " REAL," +
                COLUMN_IMAGEN + " TEXT)";
    }

    public static class CitaEntry {
        public static final String TABLE_NAME = "citas";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ID_CLIENTE = "id_cliente";
        public static final String COLUMN_ID_SERVICIO = "id_servicio";
        public static final String COLUMN_ID_EMPLEADO = "id_empleado";
        public static final String COLUMN_FECHA = "fecha";
        public static final String COLUMN_HORA = "hora";
        public static final String COLUMN_ESTADO = "estado";
        public static final String COLUMN_PAGADA = "pagada";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID_CLIENTE + " INTEGER NOT NULL," +
                COLUMN_ID_SERVICIO + " INTEGER NOT NULL," +
                COLUMN_ID_EMPLEADO + " INTEGER," +
                COLUMN_FECHA + " TEXT NOT NULL," +
                COLUMN_HORA + " TEXT NOT NULL," +
                COLUMN_ESTADO + " TEXT DEFAULT 'pendiente'," +
                COLUMN_PAGADA + " INTEGER DEFAULT 0," +
                "FOREIGN KEY(" + COLUMN_ID_CLIENTE + ") REFERENCES " +
                UsuarioEntry.TABLE_NAME + "(" + UsuarioEntry.COLUMN_ID + ")," +
                "FOREIGN KEY(" + COLUMN_ID_SERVICIO + ") REFERENCES " +
                ServicioEntry.TABLE_NAME + "(" + ServicioEntry.COLUMN_ID + ")," +
                "FOREIGN KEY(" + COLUMN_ID_EMPLEADO + ") REFERENCES " +
                EmpleadoEntry.TABLE_NAME + "(" + EmpleadoEntry.COLUMN_ID + "))";
    }

    public static class EmpleadoEntry {
        public static final String TABLE_NAME = "empleados";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_ESPECIALIDAD = "especialidad";
        public static final String COLUMN_EXPERIENCIA = "experiencia";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_TELEFONO = "telefono";
        public static final String COLUMN_DISPONIBLE = "disponible";
        public static final String COLUMN_HORARIO = "horario";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOMBRE + " TEXT NOT NULL," +
                COLUMN_ESPECIALIDAD + " TEXT," +
                COLUMN_EXPERIENCIA + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_TELEFONO + " TEXT," +
                COLUMN_DISPONIBLE + " INTEGER DEFAULT 1," +
                COLUMN_HORARIO + " TEXT)";
    }

    public static class PromocionEntry {
        public static final String TABLE_NAME = "promociones";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_DESCRIPCION = "descripcion";
        public static final String COLUMN_DESCUENTO = "descuento";
        public static final String COLUMN_FECHA_INICIO = "fecha_inicio";
        public static final String COLUMN_FECHA_FIN = "fecha_fin";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOMBRE + " TEXT NOT NULL," +
                COLUMN_DESCRIPCION + " TEXT," +
                COLUMN_DESCUENTO + " REAL," +
                COLUMN_FECHA_INICIO + " TEXT," +
                COLUMN_FECHA_FIN + " TEXT)";
    }

    public static class NotificacionEntry {
        public static final String TABLE_NAME = "notificaciones";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_MENSAJE = "mensaje";
        public static final String COLUMN_TIPO = "tipo"; // recordatorio, promocion
        public static final String COLUMN_FECHA = "fecha";
        public static final String COLUMN_LEIDA = "leida";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITULO + " TEXT NOT NULL," +
                COLUMN_MENSAJE + " TEXT," +
                COLUMN_TIPO + " TEXT," +
                COLUMN_FECHA + " TEXT," +
                COLUMN_LEIDA + " INTEGER DEFAULT 0)";
    }
}