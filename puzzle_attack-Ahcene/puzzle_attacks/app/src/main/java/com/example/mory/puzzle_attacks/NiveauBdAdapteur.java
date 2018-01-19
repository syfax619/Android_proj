package com.example.mory.puzzle_attacks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * My own application.
 */

public class NiveauBdAdapteur {

    private static final String NOM_TABLE = "niveau";

    private static final String COLOMNE_NIVEAU = "niveau";
    private static final int COLOMNE_NIVEAU_ID = 0;
    private static final String COLOMNE_TEMPS = "temps";
    private static final int COLOMNE_TEMPS_ID = 1;
    private static final String COLOMNE_TERMINER = "terminer";
    private static final int COLOMNE_TERMINER_ID = 2;

    private SQLiteDatabase ma_bdd;
    private MaBaseDeDonnees bdd_helper;

    public NiveauBdAdapteur(Context context) {
        bdd_helper = new MaBaseDeDonnees(context);
    }

    public SQLiteDatabase open() {
        ma_bdd = bdd_helper.getWritableDatabase();
        return ma_bdd;
    }

    public void close() {
        ma_bdd.close();
    }

    public SQLiteDatabase getBDD() {
        return ma_bdd;
    }

    //-----------------------------------------------------récupération de résultat
    public Niveau getNiveau(int numero) {
        Cursor cursor = ma_bdd.query(NOM_TABLE, new String[]{COLOMNE_NIVEAU, COLOMNE_TEMPS, COLOMNE_TERMINER}, COLOMNE_NIVEAU + " = " + numero, null, null, null, null);
        return CurseurNiveau(cursor);
    }

    public ArrayList<Niveau> getAllNiveaux() {
        Cursor cursor = ma_bdd.query(NOM_TABLE, new String[]{COLOMNE_NIVEAU, COLOMNE_TEMPS, COLOMNE_TERMINER}, null, null, null, null, null);
        return CurseurNiveaux(cursor);
    }

    //-----------------------------------------------------mise à jour des données
    public long insertionNiveau(Niveau niveau) {
        ContentValues content = new ContentValues();
        content.put(COLOMNE_NIVEAU, niveau.getNiveau());
        content.put(COLOMNE_TEMPS, niveau.getTemps());
        content.put(COLOMNE_TERMINER, niveau.getTerminer());
        return ma_bdd.insert(NOM_TABLE, null, content);
    }

    public int miseAJourNiveau(Niveau niveau, int id) {
        ContentValues content = new ContentValues();
        content.put(COLOMNE_NIVEAU, niveau.getNiveau());
        content.put(COLOMNE_TEMPS, niveau.getTemps());
        content.put(COLOMNE_TERMINER, niveau.getTerminer());
        return ma_bdd.update(NOM_TABLE, content, COLOMNE_NIVEAU + "=" + id, null);
    }

    public int supprimerNiveau(int id) {
        return ma_bdd.delete(NOM_TABLE, COLOMNE_NIVEAU + "=" + id, null);
    }


    //-----------------------------------------------------curseur
    private Niveau CurseurNiveau(Cursor cursor) {

        //verifie que le curseur contient des données
        if (cursor.getCount() == 0) {
            return null;
        }

        Niveau niveau = new Niveau();

        //on place les données de la base de donnée dans l'objet Niveau créé précédement
        niveau.setNiveau(cursor.getInt(COLOMNE_NIVEAU_ID));
        niveau.setTemps(cursor.getInt(COLOMNE_TEMPS_ID));
        niveau.setTerminer(cursor.getInt(COLOMNE_TERMINER_ID));

        //fermeture du curseur pour le rendre invalides
        cursor.close();

        return niveau;
    }

    private ArrayList<Niveau> CurseurNiveaux(Cursor cursor) {

        //verifie que le curseur contient des données
        if (cursor.getCount() == 0) {
            return new ArrayList<Niveau>(0);
        }

        ArrayList<Niveau> niveaux = new ArrayList<Niveau>();

        //on place le curseur sur la première ligne
        cursor.moveToFirst();
        //on place les données de la base de donnée dans l'objet Niveau créé précédement
        do {
            Niveau niveau = new Niveau();
            niveau.setNiveau(cursor.getInt(COLOMNE_NIVEAU_ID));
            niveau.setTemps(cursor.getInt(COLOMNE_TEMPS_ID));
            niveau.setTerminer(cursor.getInt(COLOMNE_TERMINER_ID));
            niveaux.add(niveau);
            //on boucle tant qu'il y a une ligne suivante
        } while (cursor.moveToNext());

        //fermeture du curseur pour le rendre invalides
        cursor.close();

        return niveaux;
    }


}
