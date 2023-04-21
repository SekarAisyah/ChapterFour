package com.example.chapterfour.room

import androidx.room.*

@Dao
interface NoteDAO {

    @Insert
    fun insertNote(note: DataNote)

    @Query("SELECT * FROM DataNote ORDER BY title ASC ")
    fun getNoteAsc(): List<DataNote>

    @Query("SELECT * FROM DataNote ORDER BY title DESC ")
    fun getNoteDesc(): List<DataNote>

    @Delete
    fun deleteNote(note: DataNote): Int

    @Update
    fun updateNote(note: DataNote): Int

}