package ca.qc.bdeb.c5gm.appdeexemple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel

/**
 * Dé avec un nombre de faces fixe.
 */
class De(private val nombreFaces: Int) {

    /**
     * Faire un lancer et retourner le résultat
     */
    fun lancer(): Int {
        return (1..nombreFaces).random()
    }
}

/**
 * Joueur avec un nom et un score
 */
class Joueur(var nom: String = "Joueur", var score: Int = 0, var scoreTour: Int = 0) {
    fun garder() {
        score += scoreTour
        scoreTour = 0
    }
}

/**
 * ViewModel pour le jeu de dé
 */
class JeuDeViewModel : ViewModel() {
    fun changerJoueur() {
        joueurCourant = (joueurCourant + 1) % 2
    }

    var valeurDe: Int = 0

    // créer un tableau de joueurs
    val joueurs: Array<Joueur> = arrayOf(Joueur("Joueur 1"), Joueur("Joueur 2"))
    var joueurCourant: Int = 0

    fun getJoueurCourant(): Joueur {
        return joueurs[joueurCourant]
    }

    fun reset() {
        joueurs[0].score = 0
        joueurs[0].scoreTour = 0
        joueurs[1].score = 0
        joueurs[1].scoreTour = 0
    }
}

const val NOMBRE_FACES = 6

class MainActivity : AppCompatActivity() {
    private val de = De(NOMBRE_FACES)
    private lateinit var imageDe: ImageView
    private lateinit var texteScore: TextView
    private lateinit var texteScoreJ1: TextView
    private lateinit var texteScoreJ2: TextView
    private lateinit var texteJoueur: TextView

    // créer un ViewModel pour l'activité
    private val viewModel: JeuDeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        // Trouver l'image dans le layout
        imageDe = findViewById(R.id.imageView)

        // Trouver les vues texte dans le layout
        texteScore = findViewById(R.id.score)
        texteScoreJ1 = findViewById(R.id.scoreJ1)
        texteScoreJ2 = findViewById(R.id.scoreJ2)
        texteJoueur = findViewById(R.id.joueur)

        // Trouver le bouton dans le layout
        val boutonLancer: Button = findViewById(R.id.button)
        val boutonGarder: Button = findViewById(R.id.buttonGarder)

        // Ajouter un ClickListener sur le bouton pour lancer le dé lorsque l'utilisateur clique sur le bouton.
        boutonLancer.setOnClickListener { lancerDe() }
        boutonGarder.setOnClickListener { garder() }
        majUI()
    }

    // Charger le menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // Gérer les actions du menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nouvelle -> nouvellePartie()
            R.id.quitter -> quitter()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    // Lancer le dé
    private fun lancerDe() {
        viewModel.valeurDe = de.lancer()
        if (viewModel.valeurDe != 1) {
            // ajouter le score du tour au score du joueur
            viewModel.getJoueurCourant().scoreTour += viewModel.valeurDe
            majUI()
        } else {
            // le score du tour est perdu
            viewModel.getJoueurCourant().scoreTour = 0
            changerJoueur()
        }
    }

    // Garder le score du tour
    private fun garder() {
        viewModel.joueurs[viewModel.joueurCourant].garder()
        if (viewModel.joueurs[viewModel.joueurCourant].score >= 50) {
            // le joueur a gagné
            Toast.makeText(this, "${viewModel.getJoueurCourant().nom} a gagné!", Toast.LENGTH_LONG)
                .show()
            nouvellePartie()
        } else {
            changerJoueur()
        }
    }

    // changer de joueur
    private fun changerJoueur() {
        viewModel.changerJoueur()
        majUI()
    }

    // recommencer une partie
    private fun nouvellePartie() {
        viewModel.reset()
        majUI()
    }

    /**
     * Mettre à jour l'interface utilisateur
     */
    private fun majUI() {
        when (viewModel.valeurDe) {
            1 -> imageDe.setImageResource(R.drawable.dice_1)
            2 -> imageDe.setImageResource(R.drawable.dice_2)
            3 -> imageDe.setImageResource(R.drawable.dice_3)
            4 -> imageDe.setImageResource(R.drawable.dice_4)
            5 -> imageDe.setImageResource(R.drawable.dice_5)
            6 -> imageDe.setImageResource(R.drawable.dice_6)
        }
        texteJoueur.text = viewModel.getJoueurCourant().nom
        texteScoreJ1.text = viewModel.joueurs[0].score.toString()
        texteScoreJ2.text = viewModel.joueurs[1].score.toString()
        texteScore.text = viewModel.getJoueurCourant().scoreTour.toString()
    }

    /**
     * Quitter l'application
     */
    private fun quitter() {
        finish()
    }
}

