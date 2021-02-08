package com.ikea.shoppable.persistence.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ikea.shoppable.model.Product
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * The Room database that contains the Users table
 */
@Database(entities = [Product::class], version = 3)
@TypeConverters(value = [Converters::class])
abstract class CacheDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {

        @Volatile
        private var INSTANCE: CacheDatabase? = null

        fun getInstance(context: Context): CacheDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                return INSTANCE!!
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CacheDatabase::class.java, "product-cache.db")
                .fallbackToDestructiveMigrationOnDowngrade()
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        Schedulers.io().scheduleDirect({
                            val path = "products.json"
                            val products = DBInputParser.readProducts(context, path)
                            //we use blocking await for these completables since we run this on the io scheduler
                            getInstance(context).productDao().deleteAll().blockingAwait()
                            getInstance(context).productDao().insert(products.toList()).blockingAwait()
                        }, 300, TimeUnit.MILLISECONDS)
                    }
                })
                .build()

    }
}