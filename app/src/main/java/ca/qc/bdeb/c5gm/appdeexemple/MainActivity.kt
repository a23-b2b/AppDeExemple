package ca.qc.bdeb.c5gm.appdeexemple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

/**
 * Dé avec un nombre de faces.
 */
class De(private val nombreFaces: Int) {

    /**
     * Do a random dice roll and return the result.
     */
    fun lancer(): Int {
        return (1..nombreFaces).random()
    }
}

const val NOMBRE_FACES = 6

class MainActivity : AppCompatActivity() {
    private val de = De(NOMBRE_FACES)
    private lateinit var imageDe: ImageView
    private lateinit var texteScore: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Trouver l'image dans le layout
        imageDe = findViewById(R.id.imageView)

        // Trouver le texte dans le layout
        texteScore = findViewById(R.id.score)


        // Trouver le bouton dans le layout
        val boutonLancer: Button = findViewById(R.id.button)

        // Ajouter un ClickListener sur le bouton pour lancer le dé lorsque l'utilisateur clique sur le bouton.
        boutonLancer.setOnClickListener { lancerDe() }

    }

    private fun lancerDe(){
        val score = de.lancer()
        when (score) {
            1 -> imageDe.setImageResource(R.drawable.dice_1)
            2 -> imageDe.setImageResource(R.drawable.dice_2)
            3 -> imageDe.setImageResource(R.drawable.dice_3)
            4 -> imageDe.setImageResource(R.drawable.dice_4)
            5 -> imageDe.setImageResource(R.drawable.dice_5)
            6 -> imageDe.setImageResource(R.drawable.dice_6)
        }
        texteScore.text = score.toString()

    }
}

