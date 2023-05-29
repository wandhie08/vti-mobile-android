package com.rowantech.vti.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg authorization: Authorization)

    @Query("SELECT * FROM Authorization ORDER BY ID DESC LIMIT 1")
    abstract fun loadAuth(): LiveData<Authorization>

}