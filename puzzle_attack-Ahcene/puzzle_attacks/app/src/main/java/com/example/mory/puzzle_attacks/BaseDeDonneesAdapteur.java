package com.example.mory.puzzle_attacks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * My own application
 */

public class BaseDeDonneesAdapteur {

    private static final String NOM_TABLE = "niveau";

    private static final String COLOMNE_ID = "id";
    private static final int COLOMNE_ID_ID = 0;
    private static final String COLOMNE_NIVEAU = "niveau";
    private static final int COLOMNE_NIVEAU_ID = 0;
    private static final int COLOMNE_NIVEAU_SAUVEGARDE_ID = 1;
    private static final String COLOMNE_TERMINER = "terminer";
    private static final int COLOMNE_TERMINER_ID = 1;
    private static final String COLOMNE_TEMPS = "temps";
    private static final int COLOMNE_TEMPS_ID = 2;

    private static final String NOM_TABLE_2 = "sauvegarde";


    private SQLiteDatabase ma_base;
    private MaBaseDeDonnees mon_helper;
    String champs[] = new String[]{COLOMNE_NIVEAU, COLOMNE_TERMINER, COLOMNE_TEMPS};
    String champs_2[] = new String[]{COLOMNE_ID, COLOMNE_NIVEAU, COLOMNE_TEMPS};

    public BaseDeDonneesAdapteur(Context context) {
        mon_helper = new MaBaseDeDonnees(context);
    }

    public void open() {
        ma_base = mon_helper.getWritableDatabase();
    }

    public void close() {
        ma_base.close();
    }

    public SQLiteDatabase getBDD() {
        return ma_base;
    }

    //-----------------------------------------------------récupération de résultat
    public Niveau getNiveau(int nv) {
        Cursor cursor = ma_base.query(NOM_TABLE, champs, COLOMNE_NIVEAU + " = " + nv, null, null, null, null);
        return CurseurNiveau(cursor);
    }

    public ArrayList<Niveau> getTousLesNiveaux() {
        Cursor cursor = ma_base.query(NOM_TABLE, champs, null, null, null, null, null);
        return CurseurNiveaux(cursor);
    }

    public Sauvegarde getSauvegarde(int id) {
        Cursor cursor = ma_base.query(NOM_TABLE_2, champs_2, COLOMNE_ID + " = " + id, null, null, null, null);

        if (cursor.getCount() == 0) {
            return null;
        }
        Sauvegarde save = new Sauvegarde();
        cursor.moveToFirst();

        //on place les données de la base de données dans l'objet Sauvegarde créé précédement
        save.setId(cursor.getInt(COLOMNE_ID_ID));
        save.setNiveau(cursor.getInt(COLOMNE_NIVEAU_SAUVEGARDE_ID));
        save.setTemps(cursor.getInt(COLOMNE_TEMPS_ID));

        //fermeture du curseur pour le rendre invalides
        cursor.close();

        return save;
    }

    public ArrayList<Sauvegarde> getLesSauvegardes() {
        Cursor cursor = ma_base.query(NOM_TABLE_2, champs_2, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            return new ArrayList<Sauvegarde>(0);
        }
        ArrayList<Sauvegarde> sauvegardes= new ArrayList<Sauvegarde>();

        //on place le curseur sur la première ligne
        cursor.moveToFirst();
        do {
            Sauvegarde save = new Sauvegarde();
            save.setId(cursor.getInt(COLOMNE_ID_ID));
            save.setNiveau(cursor.getInt(COLOMNE_NIVEAU_SAUVEGARDE_ID));
            save.setTemps(cursor.getInt(COLOMNE_TEMPS_ID));
            sauvegardes.add(save);
            //on boucle tant qu'il y a une ligne suivante
        } while (cursor.moveToNext());
        cursor.close();

        return sauvegardes;
    }

    //-----------------------------------------------------mise à jour des données
    //---------------niveau

    //fonction qui sert à inserer un niveau dans la base de données
    public long insertionNiveau(Niveau niveau) {
        ContentValues content = new ContentValues();
        content.put(COLOMNE_NIVEAU, niveau.getNiveau());
        content.put(COLOMNE_TERMINER, niveau.getTerminer());
        content.put(COLOMNE_TEMPS, niveau.getTemps());
        return ma_base.insert(NOM_TABLE, null, content);
    }
    //fonction qui sert à mettre à jour un niveau dans la base de données
    public int miseAJourNiveau(Niveau niveau, int nv) {
        ContentValues content = new ContentValues();
        content.put(COLOMNE_NIVEAU, niveau.getNiveau());
        content.put(COLOMNE_TERMINER, niveau.getTerminer());
        content.put(COLOMNE_TEMPS, niveau.getTemps());
        return ma_base.update(NOM_TABLE, content, COLOMNE_NIVEAU + "=" + nv, null);
    }

    //fonction qui sert à supprimer un niveau dans la base de données
    public int supprimerNiveau(int nv) {
        return ma_base.delete(NOM_TABLE, COLOMNE_NIVEAU + "=" + nv, null);
    }

    //---------------sauvegarde
    //fonction qui sert à inserer une sauvegarde dans la base de données
    public long insertionSauvegarde(int nv, int tps) {
        ContentValues content = new ContentValues();
        content.put(COLOMNE_NIVEAU, nv);
        content.put(COLOMNE_TEMPS, tps);
        return ma_base.insert(NOM_TABLE_2, null, content);
    }

    //fonction qui sert à supprimer une sauvegarde dans la base de données
    public int supprimerSauvegarde(int id) {
        return ma_base.delete(NOM_TABLE_2, COLOMNE_ID + "=" + id, null);
    }

    //-----------------------------------------------------curseur
    private Niveau CurseurNiveau(Cursor cursor) {

        //verifie que le curseur contient des données
        if (cursor.getCount() == 0) {
            return null;
        }

        Niveau niveau = new Niveau();
        cursor.moveToFirst();

        //on place les données de la base de donnée dans l'objet Niveau créé précédement
        niveau.setNiveau(cursor.getInt(COLOMNE_NIVEAU_ID));
        niveau.setTerminer(cursor.getInt(COLOMNE_TERMINER_ID));
        niveau.setTemps(cursor.getInt(COLOMNE_TEMPS_ID));

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
        do {
            Niveau niveau = new Niveau();
            niveau.setNiveau(cursor.getInt(COLOMNE_NIVEAU_ID));
            niveau.setTerminer(cursor.getInt(COLOMNE_TERMINER_ID));
            niveau.setTemps(cursor.getInt(COLOMNE_TEMPS_ID));
            niveaux.add(niveau);
            //on boucle tant qu'il y a une ligne suivante
        } while (cursor.moveToNext());

        //fermeture du curseur pour le rendre invalides
        cursor.close();

        return niveaux;
    }


}
