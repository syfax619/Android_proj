package com.example.mory.puzzle_attacks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * My own application.
 */

public class Puzzle extends AppCompatActivity {
    private PuzzleView AttackView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jeu);
        AttackView = (PuzzleView)findViewById(R.id.PuzzleView);
        AttackView.setVisibility(View.VISIBLE);
    }
}
