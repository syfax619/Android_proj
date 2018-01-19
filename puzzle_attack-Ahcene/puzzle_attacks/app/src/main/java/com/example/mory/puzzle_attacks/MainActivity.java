package com.example.mory.puzzle_attacks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends Activity {

    Button btn_jouer;
    Button btn_comment_jouer;
    Button btn_a_propos;
    Button btn_scores;
    Button btn_son;
    Intent intent;

    //déclaration d'un objet pour controler le volume
    private AudioManager manager_audio;

    //declaration d'un objet permettant d'agir sur la base de donnée
    public static BaseDeDonneesAdapteur base_de_donnees;

    ArrayList<Niveau> les_niveaux;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        base_de_donnees = new BaseDeDonneesAdapteur(this);
        base_de_donnees.open();

        les_niveaux = base_de_donnees.getTousLesNiveaux();

        if (les_niveaux.size() == 0) {
            Niveau niveau1 = new Niveau(1, 0, 21);
            Niveau niveau2 = new Niveau(2, 0, 21);
            Niveau niveau3 = new Niveau(3, 0, 21);
            base_de_donnees.insertionNiveau(niveau1);
            base_de_donnees.insertionNiveau(niveau2);
            base_de_donnees.insertionNiveau(niveau3);
        }

        ArrayList<Sauvegarde> les_sauvegardes = base_de_donnees.getLesSauvegardes();

        if (les_sauvegardes.size() > 0) {
            Sauvegarde sauve = les_sauvegardes.get(les_sauvegardes.size() - 1);
        }

        manager_audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        btn_jouer = (Button) findViewById(R.id.jouer);
        btn_comment_jouer = (Button) findViewById(R.id.comment_jouer);
        btn_a_propos = (Button) findViewById(R.id.a_propos);
        btn_scores = (Button) findViewById(R.id.score);
        btn_son = (Button) findViewById(R.id.son);

        btn_jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (les_niveaux.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("voulez-vous reprendre ou vous en étiez ?")
                        .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                                intent.putExtra("reponse", "oui");
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                                intent.putExtra("reponse", "non");
                                startActivity(intent);
                            }
                        }).show();
                } else {
                    intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                    intent.putExtra("reponse", "non");
                    startActivity(intent);
                }
            }
        });

        btn_comment_jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Puzzle Attack");
                alert.setMessage("Déplace (horizontalement) et inverse les blocks pour former des lignes ou des colonnes de 3 ou plus.\n\n" +
                    "Tu as un nombre donné de déplacement.\n\n" +
                    "Supprime tous les blocks pour finir un niveau.\n");
                alert.setPositiveButton("OK", null);
                alert.show();

            }
        });

        btn_a_propos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("A propos");
                alert.setMessage("BELMERABET Ahcene\n\n" +
                    "ARKOUB Syfax");
                alert.setPositiveButton("OK", null);
                alert.show();

            }
        });

        btn_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ScoresActivity.class);
                startActivity(intent);
            }
        });

        btn_son.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manager_audio.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
                    manager_audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                } else {
                    manager_audio.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_accueil:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_son:
                if (manager_audio.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
                    manager_audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                } else {
                    manager_audio.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
