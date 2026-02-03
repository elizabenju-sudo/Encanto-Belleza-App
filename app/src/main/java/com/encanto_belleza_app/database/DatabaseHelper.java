package com.encanto_belleza_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "encanto_belleza.db";
    private static final int DATABASE_VERSION = 2; // Incrementado a versión 2
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear todas las tablas
        db.execSQL(DatabaseContract.UsuarioEntry.CREATE_TABLE);
        db.execSQL(DatabaseContract.ServicioEntry.CREATE_TABLE);
        db.execSQL(DatabaseContract.CitaEntry.CREATE_TABLE);
        db.execSQL(DatabaseContract.PromocionEntry.CREATE_TABLE);
        db.execSQL(DatabaseContract.NotificacionEntry.CREATE_TABLE);
        db.execSQL(DatabaseContract.EmpleadoEntry.CREATE_TABLE); // Nueva tabla

        // Insertar datos iniciales
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas antiguas
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.UsuarioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.ServicioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.CitaEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.PromocionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.NotificacionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.EmpleadoEntry.TABLE_NAME);

        // Crear nuevas tablas
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insertar usuario administrador por defecto
        insertUsuario(db, "admin@encanto.com", "admin123", "Administrador", "admin");

        // Insertar cliente de prueba
        insertUsuario(db, "cliente@ejemplo.com", "cliente123", "Juan Pérez", "cliente");

        // Insertar servicios iniciales
        insertServicio(db, "Corte de Cabello", "Corte profesional para hombres y mujeres", 30, 25.00);
        insertServicio(db, "Manicure", "Manicure básico con esmaltado", 45, 15.00);
        insertServicio(db, "Pedicure", "Pedicure completo con cuidado de pies", 60, 20.00);
        insertServicio(db, "Maquillaje", "Maquillaje para eventos especiales", 90, 35.00);
        insertServicio(db, "Tinte de Cabello", "Aplicación de tinte profesional", 120, 50.00);
        insertServicio(db, "Tratamiento Facial", "Limpieza facial profunda", 60, 40.00);
        insertServicio(db, "Depilación", "Depilación con cera", 45, 30.00);
        insertServicio(db, "Masaje Relajante", "Masaje terapéutico de 60 minutos", 60, 45.00);

        // Insertar empleados con datos completos
        insertEmpleadoCompleto(db, "María González", "Corte y Peinado", "5 años de experiencia",
                "maria.gonzalez@encanto.com", "555-1234", true);
        insertEmpleadoCompleto(db, "Carlos Rodríguez", "Barbería", "Especialista en cortes masculinos",
                "carlos.rodriguez@encanto.com", "555-5678", true);
        insertEmpleadoCompleto(db, "Ana López", "Maquillaje", "Maquillaje para bodas y eventos",
                "ana.lopez@encanto.com", "555-9012", true);
        insertEmpleadoCompleto(db, "Pedro Martínez", "Uñas y Estética", "Especialista en uñas acrílicas",
                "pedro.martinez@encanto.com", "555-3456", true);
        insertEmpleadoCompleto(db, "Laura Sánchez", "Tratamientos Faciales", "Dermatología estética",
                "laura.sanchez@encanto.com", "555-7890", true);

        // Insertar promociones de ejemplo
        insertPromocion(db, "Primera Visita", "20% de descuento en tu primera visita", 20.0,
                "2024-01-01", "2024-12-31");
        insertPromocion(db, "Combo Belleza", "Corte + Manicure con 15% de descuento", 15.0,
                "2024-03-01", "2024-03-31");

        // Insertar notificaciones de ejemplo
        insertNotificacion(db, "Bienvenida", "¡Bienvenido a Encanto y Belleza!", "sistema",
                "2024-01-15", false);
    }

    private void insertUsuario(SQLiteDatabase db, String email, String password,
                               String nombre, String tipo) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UsuarioEntry.COLUMN_EMAIL, email);
        values.put(DatabaseContract.UsuarioEntry.COLUMN_PASSWORD, password);
        values.put(DatabaseContract.UsuarioEntry.COLUMN_NOMBRE, nombre);
        values.put(DatabaseContract.UsuarioEntry.COLUMN_TIPO, tipo);
        db.insert(DatabaseContract.UsuarioEntry.TABLE_NAME, null, values);
    }

    private void insertServicio(SQLiteDatabase db, String nombre, String descripcion,
                                int duracion, double precio) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ServicioEntry.COLUMN_NOMBRE, nombre);
        values.put(DatabaseContract.ServicioEntry.COLUMN_DESCRIPCION, descripcion);
        values.put(DatabaseContract.ServicioEntry.COLUMN_DURACION, duracion);
        values.put(DatabaseContract.ServicioEntry.COLUMN_PRECIO, precio);
        // Asignar imágenes por defecto según el servicio
        String imagen = asignarImagenServicio(nombre);
        values.put(DatabaseContract.ServicioEntry.COLUMN_IMAGEN, imagen);
        db.insert(DatabaseContract.ServicioEntry.TABLE_NAME, null, values);
    }

    private String asignarImagenServicio(String nombreServicio) {
        // Mapeo simple de servicios a nombres de imagen
        switch (nombreServicio.toLowerCase()) {
            case "corte de cabello":
                return "ic_corte_cabello";
            case "manicure":
                return "ic_manicure";
            case "pedicure":
                return "ic_pedicure";
            case "maquillaje":
                return "ic_maquillaje";
            case "tinte de cabello":
                return "ic_tinte";
            case "tratamiento facial":
                return "ic_facial";
            case "depilación":
                return "ic_depilacion";
            case "masaje relajante":
                return "ic_masaje";
            default:
                return "ic_service";
        }
    }

    private void insertEmpleadoCompleto(SQLiteDatabase db, String nombre, String especialidad,
                                        String experiencia, String email, String telefono,
                                        boolean disponible) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.EmpleadoEntry.COLUMN_NOMBRE, nombre);
        values.put(DatabaseContract.EmpleadoEntry.COLUMN_ESPECIALIDAD, especialidad);
        values.put(DatabaseContract.EmpleadoEntry.COLUMN_EXPERIENCIA, experiencia);
        values.put(DatabaseContract.EmpleadoEntry.COLUMN_EMAIL, email);
        values.put(DatabaseContract.EmpleadoEntry.COLUMN_TELEFONO, telefono);
        values.put(DatabaseContract.EmpleadoEntry.COLUMN_DISPONIBLE, disponible ? 1 : 0);
        values.put(DatabaseContract.EmpleadoEntry.COLUMN_HORARIO, "Lunes a Viernes: 9:00 AM - 6:00 PM");
        db.insert(DatabaseContract.EmpleadoEntry.TABLE_NAME, null, values);

        // También crear un usuario para el empleado (para login)
        insertUsuario(db, email, "empleado123", nombre, "empleado");
    }

    private void insertPromocion(SQLiteDatabase db, String nombre, String descripcion,
                                 double descuento, String fechaInicio, String fechaFin) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.PromocionEntry.COLUMN_NOMBRE, nombre);
        values.put(DatabaseContract.PromocionEntry.COLUMN_DESCRIPCION, descripcion);
        values.put(DatabaseContract.PromocionEntry.COLUMN_DESCUENTO, descuento);
        values.put(DatabaseContract.PromocionEntry.COLUMN_FECHA_INICIO, fechaInicio);
        values.put(DatabaseContract.PromocionEntry.COLUMN_FECHA_FIN, fechaFin);
        db.insert(DatabaseContract.PromocionEntry.TABLE_NAME, null, values);
    }

    private void insertNotificacion(SQLiteDatabase db, String titulo, String mensaje,
                                    String tipo, String fecha, boolean leida) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NotificacionEntry.COLUMN_TITULO, titulo);
        values.put(DatabaseContract.NotificacionEntry.COLUMN_MENSAJE, mensaje);
        values.put(DatabaseContract.NotificacionEntry.COLUMN_TIPO, tipo);
        values.put(DatabaseContract.NotificacionEntry.COLUMN_FECHA, fecha);
        values.put(DatabaseContract.NotificacionEntry.COLUMN_LEIDA, leida ? 1 : 0);
        db.insert(DatabaseContract.NotificacionEntry.TABLE_NAME, null, values);
    }

    // Método para poblar la base de datos si está vacía
    public void populateIfEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar si ya hay datos
        int countEmpleados = 0;
        android.database.Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " +
                DatabaseContract.EmpleadoEntry.TABLE_NAME, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                countEmpleados = cursor.getInt(0);
            }
            cursor.close();
        }

        // Si no hay empleados, insertar datos
        if (countEmpleados == 0) {
            insertInitialData(db);
        }

        db.close();
    }
}