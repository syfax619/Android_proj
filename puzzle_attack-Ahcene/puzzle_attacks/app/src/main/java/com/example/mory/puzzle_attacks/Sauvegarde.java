package com.example.mory.puzzle_attacks;

/**
 *  My own application.
 */

public class Sauvegarde {

    private int id;
    private int niveau;
    private int temps;

    public Sauvegarde() {

    }

    public Sauvegarde(int id, int niveau, int temps) {
        this.niveau = niveau;
        this.temps = temps;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNiveau() {
        return this.niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public int getTemps() {
        return this.temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }
}
