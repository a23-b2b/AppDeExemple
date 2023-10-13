package ca.qc.bdeb.c5gm.appdeexemple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nomJoueur1: TextView
    private val scoreJoueur1: TextView
    private val nomJoueur2: TextView
    private val scoreJoueur2: TextView
    private val imageWinJ1: ImageView
    private val imageWinJ2: ImageView

    init {
        nomJoueur1 = itemView.findViewById(R.id.nom_j1)
        scoreJoueur1 = itemView.findViewById(R.id.score_j1)
        imageWinJ1 = itemView.findViewById(R.id.win_j1)
        nomJoueur2 = itemView.findViewById(R.id.nom_j2)
        scoreJoueur2 = itemView.findViewById(R.id.score_j2)
        imageWinJ2 = itemView.findViewById(R.id.win_j2)
    }

    fun bind(score: MeilleurScore) {
        nomJoueur1.text = score.nomJ1
        scoreJoueur1.text = score.scoreJ1.toString()
        nomJoueur2.text = score.nomJ2
        scoreJoueur2.text = score.scoreJ2.toString()
        if (score.scoreJ1 > score.scoreJ2) {
            imageWinJ1.visibility = View.VISIBLE
            imageWinJ2.visibility = View.INVISIBLE
        } else {
            imageWinJ1.visibility = View.INVISIBLE
            imageWinJ2.visibility = View.VISIBLE
        }
    }
}

class ScoresAdapter(private val scores: MutableList<MeilleurScore>) :
    RecyclerView.Adapter<ScoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.score_item, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(scores[position])
    }

    override fun getItemCount(): Int {
        return scores.size
    }
//
//    fun addScore(score: MeilleurScore) {
//        scores.add(score)
//        notifyDataSetChanged()
//    }
}


class ScoreActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ScoresAdapter
    private val scores: MutableList<MeilleurScore> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)

        recyclerView = findViewById(R.id.recyclerview)

        // accéder à la base de données
        val scoreDao = ScoreDatabase.getDatabase(applicationContext).scoreDao()

        adapter = ScoresAdapter(scores)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btn_ok).setOnClickListener {
            finish()
        }
        lifecycleScope.launch {

            // récupérer les scores de la base de données
            scores.addAll(kotlinx.coroutines.withContext(Dispatchers.IO) { scoreDao.getAll() })
            // notifier l'adapter que les données ont changé
            adapter.notifyDataSetChanged()
        }
    }
}