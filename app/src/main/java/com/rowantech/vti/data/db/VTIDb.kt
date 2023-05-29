package com.rowantech.vti.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Authorization::class],
    version = 3,
    exportSchema = false
)
abstract class VTIDb : RoomDatabase() {

    abstract fun authDao(): AuthDao

}