package com.ikea.shoppable.persistence.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikea.shoppable.model.Product
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ProductDao {

    @Query("SELECT * FROM Products WHERE id = :id")
    fun getById(id: String): Observable<Product>

    @Query("SELECT * FROM Products WHERE name = :name")
    fun getByName(name: String): Single<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Product): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categories: List<Product>): Completable

    @Query("SELECT * FROM Products")
    fun getAll(): Observable<List<Product>>

    @Query("SELECT COUNT(name) FROM Products")
    fun getSize(): Observable<Int>

    @Query("DELETE FROM Products")
    fun deleteAll(): Completable

}
