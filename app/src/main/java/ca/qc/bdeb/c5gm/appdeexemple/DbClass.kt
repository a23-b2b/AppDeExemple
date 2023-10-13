package ca.qc.bdeb.c5gm.appdeexemple

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class MeilleurScore(
    @PrimaryKey(autoGenerate = true) val uid:Int?,
    val nomJ1:String,
    val scoreJ1: Int,
    val nomJ2:String,
    val scoreJ2: Int) {
}

@Dao
interface ScoreDao {
    @Query("SELECT * FROM MeilleurScore")
    suspend fun getAll(): List<MeilleurScore>
    @Query("SELECT * FROM MeilleurScore WHERE uid IN (:uniqueIds)")
    suspend fun loadAllByIds(uniqueIds: IntArray): List<MeilleurScore>
    @Insert
    suspend fun insertAll(vararg meilleursScores: MeilleurScore)
    @Update
    suspend fun updateAll(vararg meilleursScores: MeilleurScore)
    @Delete
    suspend fun delete(meilleurScore: MeilleurScore)
}

@Database(entities = [MeilleurScore::class], version = 1)
abstract class ScoreDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
    companion object {
        // Singleton empêche plusieurs instances de la base
        // d'être ouvertes en même temps
        @Volatile
        private var INSTANCE: ScoreDatabase? = null
        fun getDatabase(context: Context): ScoreDatabase {
            // si INSTANCE n'est pas null, on le retourne,
            // sinon, on crée la base de données
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreDatabase::class.java,
                    "scores_database"
                ) .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
