package com.example.mory.puzzle_attacks;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * My own application .
 */

public class PuzzleView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    // Declaration des images
    private Bitmap chelsea;
    private Bitmap napoli;
    private Bitmap psg;

    //declaration de l'objet qui va nous servir à manipuler le son
    MediaPlayer player;

    //declaration du niveau
    int nv = PuzzleActivity.niveau;

    //declaration du compteur qui compte le nombre de coups effectué
    int compteur = 0;

    //declaration du nombre de déplacement restant
    int nombre_de_coup;

    //declaration du nombre de case touché
    int nb_toucher = 0;

    //variable qui concernent la taille des cases
    int largeur_case = 0;
    int longueur_case = 0;

    //variables qui recupèrent la case toucher
    int x_toucher;
    int y_toucher;

    //variables qui recupèrent la première case toucher
    int premier_x_toucher;
    int premier_y_toucher;

    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources mes_ressources;
    private Context mon_context;

    // tableau modelisant la carte du jeu
    int[][] carte;

    // tableau de sauvegarde de la carte du jeu
    int[][] carte_precedent1;
    int[][] carte_precedent2;
    int[][] carte_precedent3;
    int[][] carte_precedent4;

    // taille de la carte
    static final int carteWidth = 6;
    static final int carteHeight = 8;

    // constante modelisant les differentes types de cases
    static final int CST_vide = 0;

    // tableau de reference du terrain
    int[][] ref = {
        {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
        {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
        {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
        {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
        {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
        {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
        {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
        {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide}
    };


    // thread
    private boolean in = true;
    private Thread cv_thread;

//permet de controller lA taille de la surface pixel et modification
    SurfaceHolder holder;

    //variable paint associé au dessin de la carte
    Paint paint;
    //variable paint associé au text affiché sur la carte
    Paint texte;

    //déclaration de la variable qui stocke les temps qu'il reste
    int rest;

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder = getHolder();
        holder.addCallback(this);

        mon_context = context;
        mes_ressources = mon_context.getResources();

        // chargement des images
        psg = BitmapFactory.decodeResource(mes_ressources, R.drawable.psg);
        chelsea = BitmapFactory.decodeResource(mes_ressources, R.drawable.chelsea);
        napoli = BitmapFactory.decodeResource(mes_ressources, R.drawable.napoli);
        // initialisation des parmametres du jeu
        initparameters();
        // creation du thread
        cv_thread = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true);
    }

    //--------------------------------------------------------------------definition des cubes dans le niveau
    public void niveau(int nv) {
        switch (nv) {
            case 1:
                ref[7][0] = 1;
                ref[7][1] = 1;
                ref[6][3] = 1;
                ref[6][1] = 2;
                ref[6][2] = 2;
                ref[7][2] = 2;
                ref[5][3] = 3;
                ref[7][3] = 3;
                ref[7][5] = 3;
                break;
            case 2:
                reinitialiser_ref();
                ref[7][0] = 1;
                ref[6][2] = 1;
                ref[3][2] = 1;
                ref[4][2] = 2;
                ref[5][2] = 2;
                ref[7][2] = 2;
                ref[2][2] = 3;
                ref[7][3] = 3;
                ref[7][5] = 3;
                break;
            case 3:
                reinitialiser_ref();
                ref[7][1] = 1;
                ref[6][1] = 1;
                ref[7][4] = 1;
                ref[7][5] = 1;
                ref[6][3] = 1;
                ref[3][3] = 1;
                ref[7][2] = 2;
                ref[7][3] = 2;
                ref[5][3] = 2;
                ref[5][1] = 3;
                ref[4][3] = 3;
                ref[2][3] = 3;
                break;
        }
    }


    //--------------------------------------------------------------------chargement du niveau a partir du tableau de reference du niveau
    private void loadlevel() {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                carte[i][j] = ref[i][j];
            }
        }
    }


    //--------------------------------------------------------------------initialisation du jeu
    public void initparameters() {
        paint = new Paint();
        paint.setDither(true);
        paint.setAlpha(0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(1);
        paint.setTextAlign(Paint.Align.LEFT);

        texte = new Paint();
        texte.setColor(mes_ressources.getColor(R.color.rouge));
        texte.setTextSize(mes_ressources.getDimensionPixelSize(R.dimen.custom_text_size));

        carte = new int[carteHeight][carteWidth];
        carte_precedent1 = new int[carteHeight][carteWidth];
        carte_precedent2 = new int[carteHeight][carteWidth];
        carte_precedent3 = new int[carteHeight][carteWidth];
        carte_precedent4 = new int[carteHeight][carteWidth];

        niveau(nv);

        loadlevel();
        if ((cv_thread != null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
        player = MediaPlayer.create(mon_context, R.raw.son);

    }

    //--------------------------------------------------------------------dessin de la carte
    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas) {
        Bitmap chelsea_petit = chelsea.createScaledBitmap(chelsea, (int) (canvas.getWidth() / carteWidth), (int) (canvas.getHeight() / carteHeight), true);
        Bitmap psg_petit = psg.createScaledBitmap(psg, (int) (canvas.getWidth() / carteWidth), (int) (canvas.getHeight() / carteHeight), true);
        Bitmap napoli_petit = napoli.createScaledBitmap(napoli, (int) (canvas.getWidth() / carteWidth), (int) (canvas.getHeight() / carteHeight), true);
        longueur_case = (int) (canvas.getWidth() / carteWidth);
        largeur_case = (int) (canvas.getHeight() / carteHeight);
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                switch (carte[i][j]) {
                    case 0:
                        canvas.drawRect(j * longueur_case, i * largeur_case, (j + 1) * longueur_case, (i + 1) * largeur_case, paint);
                        break;
                    case 1:
                        canvas.drawBitmap(chelsea_petit, j * longueur_case, i * largeur_case, null);
                        break;
                    case 2:
                        canvas.drawBitmap(psg_petit, j * longueur_case, i * largeur_case, null);
                        break;
                    case 3:
                        canvas.drawBitmap(napoli_petit, j * longueur_case, i * largeur_case, null);
                        break;
                }
            }
        }
    }

    // texte afficher lors d'une défaite
    private void text_lose(Canvas canvas) {
        canvas.drawText("vous avez perdu ! ", 1 * longueur_case, 1 * largeur_case, texte);
    }

    // texte afficher lors de la fin du jeu
    private void text_fin(Canvas canvas) {
        canvas.drawText("Bravo, vous avez fini le jeu ! ", 1 * longueur_case, 4 * largeur_case, texte);
    }

    // texte qui affiche le nombre de coups restant
    private void text_nb_coups(Canvas canvas) {
        if (nv == 1 || nv == 2) {
            nombre_de_coup = 10;
        } else if (nv == 3) {
            nombre_de_coup = 10;
        }
        canvas.drawText("coups restant : " + (nombre_de_coup - compteur), 1 * longueur_case, 1 * largeur_case, texte);
    }

    // dessin de la ligne du temps
    private void ligne_du_temps(Canvas canvas) {
        int p = (int) (longueur_case / 20);
    //La classe Canvas détient les appels "draw". Pour dessiner quelque chose, vous avez besoin de 4 composants de base: un bitmap pour contenir les pixels, un Canvas pour héberger les appels de tirage (écriture dans le bitmap),
        canvas.drawLine((rest * (p * p) + longueur_case), 20, 1 * longueur_case, 20, texte);

    }

    //déclaration d'un compte à rebours
    CountDownTimer counterdown = new CountDownTimer(20000, 1000) {
        public void onTick(long millisUntilFinished) {
            rest = (int) (millisUntilFinished / 1000);
        }

        public void onFinish() {
            rest = 0;
        }
    }.start();

    //--------------------------------------------------------------------verifie si on a gagner
    private boolean isWon() {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                if (carte[i][j] != 0) {
                    return false;
                }
            }
        }
        counterdown.cancel();
        controle_de_musique(2);
        int changement = 0;

        Niveau le_niveau = PuzzleActivity.base_de_donnees.getNiveau(nv);
        if (le_niveau.getTerminer() == 0) {
            le_niveau.setTerminer(1);
            changement = 1;
        }
        if (le_niveau.getTemps() > (20 - rest)) {
            le_niveau.setTemps(20 - rest);
            changement = 1;
        }
        if (changement == 1) {
            PuzzleActivity.base_de_donnees.miseAJourNiveau(le_niveau, nv);
        }
        nv++;
        compteur = 0;
        counterdown.start();
        return true;
    }

    //--------------------------------------------------------------------verifie si on a perdu
    private boolean isLost() {
        if (rest == 0) {
            controle_de_musique(2);
            counterdown.cancel();
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------verifie si on a atteint le nombre de déplacement maximum
    private boolean compteurMax() {
        if (((nv == 1 || nv == 2) && compteur == 2) || (nv == 3 && compteur == 4)) {
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------dessin des éléments du jeu
    protected void onDraw(Canvas canvas) {
        if (nv == 4) {
            canvas.drawRGB(39, 10, 103);
            text_fin(canvas);
        } else if (isWon()) {
            canvas.drawRGB(39, 10, 103);
            initparameters();
            paintcarte(canvas);
        } else if (isLost()) {
            canvas.drawRGB(39, 10, 103);
            paintcarte(canvas);
            supprimer_ligne();
            text_lose(canvas);
        } else {
            canvas.drawRGB(39, 10, 103);
            paintcarte(canvas);
            supprimer_ligne();
            text_nb_coups(canvas);
            ligne_du_temps(canvas);
            controle_de_musique(1);
        }
    }

    public void run() {
        Canvas c = null;
        while (in) {
            try {
                cv_thread.sleep(40);
                try {
                    c = holder.lockCanvas(null);
                    onDraw(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch (Exception e) {
                Log.e("-> RUN <-", "PB DANS RUN");
            }
        }
    }

    //--------------------------------------------------------------------etat de la surface
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i("-> FCT <-", "la surface a été créé");
    }

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("-> FCT <-", "surfaceChanged ");
        initparameters();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i("-> FCT <-", "la surface a été détruite");
        if (isLost() == false) {
            PuzzleActivity.base_de_donnees.insertionSauvegarde(nv, rest);
        }
        controle_de_musique(3);
        in = false;
        while (!in) {
            try {
                cv_thread.join();
                in = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //----------------------------------met à jour la position d'un cube
    private void UpdateCube(int x, int y, int new_x, int new_y) {
        int inter;
        int inter2;

        sauvegarde_carte();

        if (carte[y][x] != 0) {
            inter = carte[y][x];
            inter2 = carte[new_y][new_x];
            carte[new_y][new_x] = inter;
            carte[y][x] = inter2;
        }
        descente_cube();
    }


    //-------------------------fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent(MotionEvent event) {
        x_toucher = (int) (event.getX() / longueur_case);
        y_toucher = (int) (event.getY() / largeur_case);
        if (compteurMax() || rest == 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (carte[y_toucher][x_toucher] == 0) {
                    return false;
                }
                premier_x_toucher = x_toucher;
                premier_y_toucher = y_toucher;
                return true;
            case MotionEvent.ACTION_UP:
                nb_toucher = 0;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (x_toucher != premier_x_toucher) {
                    if (nb_toucher == 0) {
                        nb_toucher = 1;
                        UpdateCube(premier_x_toucher, premier_y_toucher, x_toucher, y_toucher);
                        compteur++;
                    }
                    return false;
                } else if (y_toucher != premier_y_toucher) {
                    nb_toucher = 1;
                    return false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //--------------------------------methode permettant la suppression des cubes
    private void supprimer_ligne() {

        //5 cubes
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < (carteWidth - 4); j++) {
                if ((carte[i][j] == (carte[i][j + 1])) && ((carte[i][j + 1]) == (carte[i][j + 2])) && ((carte[i][j + 2]) == (carte[i][j + 3])) && ((carte[i][j + 3]) == (carte[i][j + 4]))) {
                    carte[i][j] = 0;
                    carte[i][j + 1] = 0;
                    carte[i][j + 2] = 0;
                    carte[i][j + 3] = 0;
                    carte[i][j + 4] = 0;
                }
            }
        }

        for (int i = 0; i < (carteHeight - 4); i++) {
            for (int j = 0; j < carteWidth; j++) {
                if ((carte[i][j] == (carte[i + 1][j])) && ((carte[i + 1][j]) == (carte[i + 2][j])) && ((carte[i + 2][j]) == (carte[i + 3][j])) && ((carte[i + 3][j]) == (carte[i + 4][j]))) {
                    carte[i][j] = 0;
                    carte[i + 1][j] = 0;
                    carte[i + 2][j] = 0;
                    carte[i + 3][j] = 0;
                    carte[i + 4][j] = 0;
                }
            }
        }

        //4 cubes
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < (carteWidth - 3); j++) {
                if ((carte[i][j] == (carte[i][j + 1])) && ((carte[i][j + 1]) == (carte[i][j + 2])) && ((carte[i][j + 2]) == (carte[i][j + 3]))) {
                    carte[i][j] = 0;
                    carte[i][j + 1] = 0;
                    carte[i][j + 2] = 0;
                    carte[i][j + 3] = 0;
                }
            }
        }

        for (int i = 0; i < (carteHeight - 3); i++) {
            for (int j = 0; j < carteWidth; j++) {
                if ((carte[i][j] == (carte[i + 1][j])) && ((carte[i + 1][j]) == (carte[i + 2][j])) && ((carte[i + 2][j]) == (carte[i + 3][j]))) {
                    carte[i][j] = 0;
                    carte[i + 1][j] = 0;
                    carte[i + 2][j] = 0;
                    carte[i + 3][j] = 0;
                }
            }
        }

        //3 cubes
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < (carteWidth - 2); j++) {
                if ((carte[i][j] == (carte[i][j + 1])) && ((carte[i][j + 1]) == (carte[i][j + 2]))) {
                    carte[i][j] = 0;
                    carte[i][j + 1] = 0;
                    carte[i][j + 2] = 0;
                }
            }
        }

        for (int i = 0; i < (carteHeight - 2); i++) {
            for (int j = 0; j < carteWidth; j++) {
                if ((carte[i][j] == (carte[i + 1][j])) && ((carte[i + 1][j]) == (carte[i + 2][j]))) {
                    carte[i][j] = 0;
                    carte[i + 1][j] = 0;
                    carte[i + 2][j] = 0;
                }
            }
        }
        descente_cube();
    }

    //-------------------------------methode pour faire descendre les cube
    public void descente_cube() {
        int inter;
        boolean bool;

        do {
            bool = false;
            for (int i = 0; i < carteHeight - 1; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    if (carte[i][j] != 0 && carte[i + 1][j] == 0) {
                        bool = true;
                        inter = carte[i][j];
                        carte[i][j] = 0;
                        carte[i + 1][j] = inter;
                        try {
                            cv_thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } while (bool != false);
    }

    //---------------------------------methode pour faire revenir en arrière
    public void onBackPressed() {
        if (compteur > 0) {

            if (compteur == 1) {
                for (int i = 0; i < carteHeight; i++) {
                    for (int j = 0; j < carteWidth; j++) {
                        carte[i][j] = carte_precedent1[i][j];
                    }
                }
            } else if (compteur == 2) {
                for (int i = 0; i < carteHeight; i++) {
                    for (int j = 0; j < carteWidth; j++) {
                        carte[i][j] = carte_precedent2[i][j];
                    }
                }
            } else if (compteur == 3) {
                for (int i = 0; i < carteHeight; i++) {
                    for (int j = 0; j < carteWidth; j++) {
                        carte[i][j] = carte_precedent3[i][j];
                    }
                }
            } else if (compteur == 4) {
                for (int i = 0; i < carteHeight; i++) {
                    for (int j = 0; j < carteWidth; j++) {
                        carte[i][j] = carte_precedent4[i][j];
                    }
                }
            }
        }

        if (compteur > 0) {
            compteur--;
        }
    }

    //--------------------------------------------------------------------sauvegarde de la position des cubes
    public void sauvegarde_carte() {

        if (compteur == 0) {
            for (int i = 0; i < carteHeight; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    carte_precedent1[i][j] = carte[i][j];
                }
            }
        } else if (compteur == 1) {
            for (int i = 0; i < carteHeight; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    carte_precedent2[i][j] = carte[i][j];
                }
            }
        } else if (compteur == 2) {
            for (int i = 0; i < carteHeight; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    carte_precedent3[i][j] = carte[i][j];
                }
            }
        } else if (compteur == 3) {
            for (int i = 0; i < carteHeight; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    carte_precedent4[i][j] = carte[i][j];
                }
            }
        }

    }

    //--------------------------------------------------------------------réinitialisation de la carte
    public void reinitialiser_ref() {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < (carteWidth); j++) {
                ref[i][j] = 0;
            }
        }

    }

    //--------------------------------------------------------------------methode pour controler le lecteur de musique
    public void controle_de_musique(int music) {
        switch (music) {
            case 1:
                player.start();
                break;
            case 2:
                player.pause();
                break;
            case 3:
                player.reset();
        }
    }


    public void Pause() {

    }

    public void Resume() {

    }

    public void Destroy() {
        psg.recycle();
        chelsea.recycle();
        napoli.recycle();
        player.release();
        player = null;
    }


}
