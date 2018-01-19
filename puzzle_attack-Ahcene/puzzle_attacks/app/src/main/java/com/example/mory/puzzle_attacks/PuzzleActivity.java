package com.example.mory.puzzle_attacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

/**
 *  My own application.
 */

public class PuzzleActivity extends Activity {
    private PuzzleView AttackView;
    Intent intent;
    String reponse;

    private AudioManager manager_audio;
    public static int niveau = 1;
    public static BaseDeDonneesAdapteur base_de_donnees;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent_extra = getIntent();
        reponse = intent_extra.getStringExtra("reponse");

        base_de_donnees = new BaseDeDonneesAdapteur(this);
        base_de_donnees.open();

        ArrayList<Niveau> les_niveaux = base_de_donnees.getTousLesNiveaux();
        if (reponse.equals("non")) {
            PuzzleActivity.niveau = 1;
        } else {
            for (Niveau nv : les_niveaux) {
                if (nv.getTerminer() == 0) {
                    PuzzleActivity.niveau = nv.getNiveau();
                    break;
                }
            }
        }

        setContentView(R.layout.jeu);

        AttackView = (PuzzleView) findViewById(R.id.PuzzleView);
        AttackView.setVisibility(View.VISIBLE);
        manager_audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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

    @Override
    public void onBackPressed() {
        AttackView.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        AttackView.Pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        AttackView.Resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AttackView.Destroy();
    }
}
