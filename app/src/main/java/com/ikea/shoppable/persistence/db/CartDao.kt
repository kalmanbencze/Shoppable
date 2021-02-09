package com.ikea.shoppable.persistence.db

import androidx.room.*
import com.ikea.shoppable.model.CartItem
import com.ikea.shoppable.model.CartItemProduct
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CartDao {

    @Query("SELECT * FROM Products WHERE id = :id")
    fun getByProductId(id: String): Single<CartItemProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: CartItem): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categories: List<CartItem>): Completable

    @Query("DELETE FROM Cart WHERE id IN (SELECT id FROM Cart WHERE productId = :id LIMIT 1)")
    fun remove(id: String): Completable

    @Transaction
    @Query("SELECT * FROM Products")
    fun getAll(): Observable<List<CartItemProduct>>

    @Query("SELECT SUM(count) FROM Cart")
    fun getSize(): Observable<Int>

    @Query("DELETE FROM Cart")
    fun deleteAll(): Completable

}
