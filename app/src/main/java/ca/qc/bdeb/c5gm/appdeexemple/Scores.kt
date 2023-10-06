package ca.qc.bdeb.c5gm.appdeexemple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nomJoueur1: TextView = itemView.findViewById(R.id.nom_joueur_1)
    val scoreJoueur1: TextView = itemView.findViewById(R.id.score_joueur_1)
}

class Scores : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)
    }
}