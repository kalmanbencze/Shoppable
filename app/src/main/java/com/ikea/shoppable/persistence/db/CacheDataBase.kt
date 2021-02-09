package com.ikea.shoppable.persistence.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ikea.shoppable.model.CartItem
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.db.util.Converters
import com.ikea.shoppable.persistence.db.util.DBInputParser
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * The Room database that contains the Users table
 */
@Database(entities = [Product::class, CartItem::class], version = 6)
@TypeConverters(value = [Converters::class])
abstract class CacheDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {

        @Volatile
        private var INSTANCE: CacheDatabase? = null

        fun getInstance(context: Context): CacheDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                return INSTANCE!!
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CacheDatabase::class.java, "product-cache.db"
            )
                .fallbackToDestructiveMigrationOnDowngrade()
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {

                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        insertDataAsync(context)
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        insertDataAsync(context)
                    }
                })
                .build()

        @VisibleForTesting
        fun insertDataAsync(context: Context) {
            Schedulers.io().scheduleDirect({
                insertData(context)
            }, 300, TimeUnit.MILLISECONDS)
        }

        @VisibleForTesting
        fun insertData(context: Context) {
            val path = "products.json"
            val products = DBInputParser.readProducts(context, path)
            //we use blocking await for these completables since we run this on the io scheduler
            getInstance(context).productDao().deleteAll().blockingAwait()
            getInstance(context).cartDao().deleteAll().blockingAwait()
            getInstance(context).productDao().insert(products.toList()).blockingAwait()
        }

    }
}