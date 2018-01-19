package com.example.mory.puzzle_attacks;

/**
 * My own Application.
 */

public class Niveau {

//    private int id;
    private int niveau;
    private int terminer;
    private int temps;

    public Niveau() {

    }

    public Niveau( int nv, int ter, int tps) {
        this.niveau = nv;
        this.terminer = ter;
        this.temps = tps;
    }

//    public int getId() {
//        return this.id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

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

    public int getTerminer() {
        return this.terminer;
    }

    public void setTerminer(int terminer) {
        this.terminer = terminer;
    }
}
