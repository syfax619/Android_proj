package com.example.mory.puzzle_attacks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *  My own application.
 */

public class MaBaseDeDonnees extends SQLiteOpenHelper {

    //version de la base de données
    private static final int VERSION_BDD = 1;

    //nom de la base de données
    private static final String NOM_BDD = "PuzzleAttacks.db";

    //déclaration du nom des champs de la base de données
    private static final String COLOMNE_ID = "id";
    private static final String COLOMNE_NIVEAU = "niveau";
    private static final String COLOMNE_TERMINER = "terminer";
    private static final String COLOMNE_TEMPS = "temps";

    //déclaration du nom de la première table
    private static final String NOM_TABLE = "niveau";

    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + NOM_TABLE + " (" +
            COLOMNE_NIVEAU + " INTEGER not null, " +
            COLOMNE_TERMINER + " INTEGER," +
            COLOMNE_TEMPS + " INTEGER )";

    //déclaration du nom de la deuxième table
    private static final String NOM_TABLE_2 = "sauvegarde";

    private static final String SQL_CREATE_ENTRIES_2 =
        "CREATE TABLE " + NOM_TABLE_2 + " (" +
            COLOMNE_ID + " INTEGER primary key autoincrement," +
            COLOMNE_NIVEAU + " INTEGER not null, " +
            COLOMNE_TEMPS + " INTEGER )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + NOM_TABLE;

    private static final String SQL_DELETE_ENTRIES_2 =
        "DROP TABLE IF EXISTS " + NOM_TABLE_2;

    public MaBaseDeDonnees(Context context) {
        super(context, NOM_BDD, null, VERSION_BDD);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //création des tables dans la base de données
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //suppression des tables dans la base de données
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES_2);
        //recréation de la base de données
        onCreate(sqLiteDatabase);
    }
}
