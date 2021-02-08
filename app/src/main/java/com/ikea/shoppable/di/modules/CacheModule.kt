package com.ikea.shoppable.di.modules

import android.content.Context
import com.ikea.shoppable.persistence.ProductRepository
import com.ikea.shoppable.persistence.ProductRepositoryImpl
import com.ikea.shoppable.persistence.db.CacheDatabase
import com.ikea.shoppable.persistence.db.ProductDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CacheModule {
    @Provides
    @Singleton
    fun providesProductCache(productDao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(productDao)
    }

    @Provides
    fun providesProductDao(db: CacheDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    @Singleton
    fun providesCacheDB(context: Context): CacheDatabase {
        return CacheDatabase.getInstance(context)
    }
}