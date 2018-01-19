package com.example.mory.puzzle_attacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoresActivity extends Activity {

    TextView niveau_1;
    TextView terminer_1;
    TextView temps_1;
    TextView niveau_2;
    TextView terminer_2;
    TextView temps_2;
    TextView niveau_3;
    TextView terminer_3;
    TextView temps_3;
    Intent intent;

    public static BaseDeDonneesAdapteur base_de_donnees;

    private AudioManager manager_audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);

        base_de_donnees = new BaseDeDonneesAdapteur(this);
        base_de_donnees.open();

        ArrayList<Niveau> les_niveaux = base_de_donnees.getTousLesNiveaux();

        manager_audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        niveau_1 = (TextView) findViewById(R.id.niveau_1);
        niveau_2 = (TextView) findViewById(R.id.niveau_2);
        niveau_3 = (TextView) findViewById(R.id.niveau_3);
        terminer_1 = (TextView) findViewById(R.id.terminer_1);
        terminer_2 = (TextView) findViewById(R.id.terminer_2);
        terminer_3 = (TextView) findViewById(R.id.terminer_3);
        temps_1 = (TextView) findViewById(R.id.temps_1);
        temps_2 = (TextView) findViewById(R.id.temps_2);
        temps_3 = (TextView) findViewById(R.id.temps_3);

        niveau_1.setText("niveau : " + les_niveaux.get(0).getNiveau());
        niveau_2.setText("niveau : " + les_niveaux.get(1).getNiveau());
        niveau_3.setText("niveau : " + les_niveaux.get(2).getNiveau());

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                if (les_niveaux.get(i).getTerminer() == 0) {
                    terminer_1.setText("ce niveau n'a pas été terminé");
                    temps_1.setText("aucun temps n'a été sauvegardé");
                } else {
                    terminer_1.setText("bravo ce niveau est terminé");
                    temps_1.setText("votre meilleur temps : " + les_niveaux.get(i).getTemps() + " secondes");
                }
            }

            if (i == 1) {
                if (les_niveaux.get(i).getTerminer() == 0) {
                    terminer_2.setText("ce niveau n'a pas été terminé");
                    temps_2.setText("aucun temps n'a été sauvegardé");
                } else {
                    terminer_2.setText("bravo ce niveau est terminé");
                    temps_2.setText("votre meilleur temps : " + les_niveaux.get(i).getTemps() + " secondes");
                }
            }

            if (i == 2) {
                if (les_niveaux.get(i).getTerminer() == 0) {
                    terminer_3.setText("ce niveau n'a pas été terminé");
                    temps_3.setText("aucun temps n'a été sauvegardé");
                } else {
                    terminer_3.setText("bravo ce niveau est terminé");
                    temps_3.setText("votre meilleur temps : " + les_niveaux.get(i).getTemps() + " secondes");
                }
            }
        }


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
