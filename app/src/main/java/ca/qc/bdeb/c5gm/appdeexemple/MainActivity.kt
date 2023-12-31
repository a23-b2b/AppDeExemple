package ca.qc.bdeb.c5gm.appdeexemple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

const val EXTRA_JOUEUR_1 = "EXTRA_JOUEUR1"
const val EXTRA_JOUEUR_2 = "EXTRA_JOUEUR2"
const val TAG = "debugApp"
class MainActivity : AppCompatActivity() {
    private val de = De(NOMBRE_FACES)
    private lateinit var imageDe: ImageView
    private lateinit var texteScore: TextView
    private lateinit var texteScoreJ1: TextView
    private lateinit var texteScoreJ2: TextView
    private lateinit var texteJoueur: TextView


    // créer un ViewModel pour l'activité
    private val viewModel: JeuDeViewModel by viewModels()

    // créer un ActivityResultLauncher pour l'activité 2
    val activity2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val intent = result.data ?: Intent()
            if (intent.hasExtra(EXTRA_JOUEUR_1)) {
                viewModel.joueurs[0].nom = intent.getStringExtra(EXTRA_JOUEUR_1).let { it.toString() }
                viewModel.joueurs[1].nom = intent.getStringExtra(EXTRA_JOUEUR_2).let { it.toString() }
                Log.d(TAG, viewModel.joueurs[0].nom)
                Log.d(TAG, viewModel.joueurs[1].nom)
                majUI()
            }
        }
    }

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
            R.id.options -> {
                val intent = Intent(applicationContext, ModifierNom::class.java)
                intent.putExtra(EXTRA_JOUEUR_1, viewModel.joueurs.get(0).nom)
                intent.putExtra(EXTRA_JOUEUR_2, viewModel.joueurs.get(1).nom)
                activity2.launch(intent)
            }
            R.id.scores -> {
                val intent = Intent(applicationContext, ScoreActivity::class.java)
                startActivity(intent)
            }
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

            // ajouter le score de la partie aux meilleurs scores
            val meilleurScore = MeilleurScore(null, viewModel.joueurs[0].nom, viewModel.joueurs[0].score, viewModel.joueurs[1].nom, viewModel.joueurs[1].score)
            lifecycleScope.launch(Dispatchers.IO) {
                val dao = ScoreDatabase.getDatabase(applicationContext).scoreDao()
                dao.insertAll(meilleurScore)
            }
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

