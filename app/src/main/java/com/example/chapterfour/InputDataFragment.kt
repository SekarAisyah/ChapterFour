package com.example.chapterfour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.example.chapterfour.databinding.FragmentInputDataBinding
import com.example.chapterfour.room.DataNote
import com.example.chapterfour.room.NoteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class InputDataFragment : DialogFragment() {

    lateinit var binding : FragmentInputDataBinding
    var dbNote : NoteDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentInputDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbNote = NoteDatabase.getInstance(requireContext())
        binding.btnTambah.setOnClickListener{
            addNote()

        }
    }

    fun addNote(){
        GlobalScope.async {
            var title = binding.etTitleNote.text.toString()
            var content = binding.etContentNote.text.toString()
            dbNote!!.noteDao().insertNote(DataNote(0, title, content))
            Toast.makeText(context, "Catatan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
        }
        dismiss()

    }
    override fun onDetach() {
        super.onDetach()
        activity?.recreate()
    }
}