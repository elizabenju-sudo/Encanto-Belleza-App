package com.encanto_belleza_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "encanto_belleza.db";
    private static final int DATABASE_VERSION = 1;
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
        
        // Crear nuevas tablas
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insertar usuario administrador por defecto
        ContentValues adminValues = new ContentValues();
        adminValues.put(DatabaseContract.UsuarioEntry.COLUMN_EMAIL, "admin@encanto.com");
        adminValues.put(DatabaseContract.UsuarioEntry.COLUMN_PASSWORD, "admin123");
        adminValues.put(DatabaseContract.UsuarioEntry.COLUMN_NOMBRE, "Administrador");
        adminValues.put(DatabaseContract.UsuarioEntry.COLUMN_TIPO, "admin");
        db.insert(DatabaseContract.UsuarioEntry.TABLE_NAME, null, adminValues);

        // Insertar algunos servicios iniciales
        insertServicio(db, "Corte de Cabello", "Corte profesional para hombres y mujeres", 30, 25.00);
        insertServicio(db, "Manicure", "Manicure básico con esmaltado", 45, 15.00);
        insertServicio(db, "Pedicure", "Pedicure completo con cuidado de pies", 60, 20.00);
        insertServicio(db, "Maquillaje", "Maquillaje para eventos especiales", 90, 35.00);
        insertServicio(db, "Tinte de Cabello", "Aplicación de tinte profesional", 120, 50.00);
        insertServicio(db, "Tratamiento Facial", "Limpieza facial profunda", 60, 40.00);
        
        // Insertar empleados de prueba
        insertEmpleado(db, "empleado1@encanto.com", "emp123", "María González", "empleado");
        insertEmpleado(db, "empleado2@encanto.com", "emp123", "Carlos Rodríguez", "empleado");
    }

    private void insertServicio(SQLiteDatabase db, String nombre, String descripcion, 
                               int duracion, double precio) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ServicioEntry.COLUMN_NOMBRE, nombre);
        values.put(DatabaseContract.ServicioEntry.COLUMN_DESCRIPCION, descripcion);
        values.put(DatabaseContract.ServicioEntry.COLUMN_DURACION, duracion);
        values.put(DatabaseContract.ServicioEntry.COLUMN_PRECIO, precio);
        db.insert(DatabaseContract.ServicioEntry.TABLE_NAME, null, values);
    }

    private void insertEmpleado(SQLiteDatabase db, String email, String password, 
                               String nombre, String tipo) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UsuarioEntry.COLUMN_EMAIL, email);
        values.put(DatabaseContract.UsuarioEntry.COLUMN_PASSWORD, password);
        values.put(DatabaseContract.UsuarioEntry.COLUMN_NOMBRE, nombre);
        values.put(DatabaseContract.UsuarioEntry.COLUMN_TIPO, tipo);
        db.insert(DatabaseContract.UsuarioEntry.TABLE_NAME, null, values);
    }
}
