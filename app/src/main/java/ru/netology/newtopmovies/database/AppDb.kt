package ru.netology.newtopmovies.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase




@Database(
    entities = [MovieEntity::class, SearchQueryEntity::class],
    version = 1,
    /*autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]*/
)
@TypeConverters(GenreConverter::class, CountriesConverter::class, SequelsAndPrequelsConverter::class)
abstract class AppDb : RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val franchiseDao: FranchiseDao
    abstract val searchQueryDao: SearchQueryDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "data_base_movie.db")
                //.fallbackToDestructiveMigration()
                //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .allowMainThreadQueries()
                .setJournalMode(JournalMode.TRUNCATE)
                .build()

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `movies` ADD COLUMN `sequelsAndPrequels` TEXT DEFAULT NULL")
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `movies` ADD COLUMN `review` TEXT DEFAULT NULL")
                database.execSQL("DROP TABLE 'franchises'")
                database.execSQL("DROP TABLE 'wishMovies'")
               database.execSQL("CREATE TABLE searchQuery AS SELECT * FROM movies")
            }
        }
    }
}

