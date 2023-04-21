package com.example.chapterfour

import com.example.chapterfour.room.DataNote
import com.example.chapterfour.room.NoteDatabase
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewModelNote(app: Application) : AndroidViewModel(app) {

    var allNote: MutableLiveData<List<DataNote>> = MutableLiveData()

    init {
        getAllNote()
    }

    fun getAllNoteObservers(): MutableLiveData<List<DataNote>> {
        return allNote
    }

    fun getAllNote() {
        GlobalScope.launch {
            val userDao = NoteDatabase.getInstance(getApplication())!!.noteDao()
            val listNote = userDao.getNoteAsc()
            allNote.postValue(listNote)
        }
    }

    fun insertNote(entity: DataNote) {
        val noteDao = NoteDatabase.getInstance(getApplication())?.noteDao()
        noteDao!!.insertNote(entity)
        getAllNote()
    }

    fun deleteNote(entity: DataNote) {
        val userDao = NoteDatabase.getInstance(getApplication())!!.noteDao()
        userDao?.deleteNote(entity)
        getAllNote()
    }

    fun updateNote(entity: DataNote) {
        val userDao = NoteDatabase.getInstance(getApplication())!!.noteDao()
        userDao?.updateNote(entity)
        getAllNote()
    }

}