package ca.qc.bdeb.c5gm.appdeexemple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ModifierNom : AppCompatActivity() {

    lateinit var nomJ1 : EditText
    lateinit var nomJ2 : EditText
    lateinit var boutonOk : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifier_nom)

        nomJ1 = findViewById(R.id.nom_joueur1)
        nomJ2 = findViewById(R.id.nom_joueur2)
        boutonOk = findViewById(R.id.bouton_ok)

        boutonOk.setOnClickListener {
            val intentMsg = intent
            intentMsg.putExtra(EXTRA_JOUEUR_1, nomJ1.text.toString())
            intentMsg.putExtra(EXTRA_JOUEUR_2, nomJ2.text.toString())
            setResult(RESULT_OK, intentMsg)
            finish()
        }

        nomJ1.setText(intent.getStringExtra(EXTRA_JOUEUR_1))
        nomJ2.setText(intent.getStringExtra(EXTRA_JOUEUR_2))
    }
}