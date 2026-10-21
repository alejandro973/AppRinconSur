// Requisito: persistencia local en SQLite de los registros manuales de ingreso y salida
package com.example.apprinconsur.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.apprinconsur.model.RegistroItem

class RegistroDbHelper(context: Context) : SQLiteOpenHelper(context, "rinconsur.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE registros (" +
                "id TEXT PRIMARY KEY, " +
                "nombre_acceso TEXT NOT NULL, " +
                "tipo_flujo TEXT NOT NULL, " +
                "rango_etario TEXT NOT NULL, " +
                "movimiento TEXT NOT NULL CHECK(movimiento IN ('Ingreso','Salida')), " +
                "cantidad_personas INTEGER NOT NULL CHECK(cantidad_personas > 0), " +
                "observaciones TEXT NOT NULL, " +
                "fecha_hora INTEGER NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS registros")
        onCreate(db)
    }

    fun insertar(item: RegistroItem) {
        val valores = ContentValues().apply {
            put("id", item.id)
            put("nombre_acceso", item.nombreAcceso)
            put("tipo_flujo", item.tipoFlujo)
            put("rango_etario", item.rangoEtario)
            put("movimiento", item.movimiento)
            put("cantidad_personas", item.cantidadPersonas)
            put("observaciones", item.observaciones)
            put("fecha_hora", item.fechaHora)
        }
        writableDatabase.insertOrThrow("registros", null, valores)
    }

    fun listar(): List<RegistroItem> {
        val lista = mutableListOf<RegistroItem>()
        readableDatabase.rawQuery(
            "SELECT id, nombre_acceso, tipo_flujo, rango_etario, movimiento, cantidad_personas, observaciones, fecha_hora " +
                "FROM registros ORDER BY fecha_hora DESC, rowid DESC",
            null
        ).use { c ->
            while (c.moveToNext()) {
                lista.add(
                    RegistroItem(
                        id = c.getString(0),
                        nombreAcceso = c.getString(1),
                        tipoFlujo = c.getString(2),
                        rangoEtario = c.getString(3),
                        movimiento = c.getString(4),
                        cantidadPersonas = c.getInt(5),
                        observaciones = c.getString(6),
                        fechaHora = c.getLong(7)
                    )
                )
            }
        }
        return lista
    }
}
