package com.example.chapterfour

import com.example.chapterfour.databinding.ItemNoteBinding
import com.example.chapterfour.room.DataNote
import com.example.chapterfour.room.NoteDatabase
import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.ArrayList

class NoteAdapter(var listNote: List<DataNote>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    var dbNote: NoteDatabase? = null
    lateinit var vmNote: ViewModelNote

    class ViewHolder(var binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun dataBinding(itemData: DataNote) {
            binding.note = itemData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding(listNote[position])
        holder.binding.ivHapusNote.setOnClickListener {
            dbNote = NoteDatabase.getInstance(it.context)

            //Membuat custom dialog untuk proses hapus data
            val customDialogDelete = LayoutInflater.from(it.context)
                .inflate(R.layout.dialog_hapus_data, null, false)
            val hapusDataDialog = AlertDialog.Builder(it.context)
                .setView(customDialogDelete)
                .create()

            val buttonhapus =
                customDialogDelete.findViewById<Button>(R.id.hapus_dialog_button_hapus)
            val buttoncancel =
                customDialogDelete.findViewById<Button>(R.id.hapus_dialog_button_cancel)

            //action cancel delete
            buttoncancel.setOnClickListener {
                hapusDataDialog.dismiss()
            }

            //delete action button
            buttonhapus.setOnClickListener {
                Toast.makeText(it.context, "Note Telah Dihapus", Toast.LENGTH_SHORT).show()
                GlobalScope.async {

                    //command for room database
                    val command = dbNote?.noteDao()?.deleteNote(listNote[position])

                    //check if delete process worked or not
                    (customDialogDelete.context as MainActivity).runOnUiThread {
                        if (command != 0) {
                            Toast.makeText(
                                customDialogDelete.context,
                                "Catatan berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                            (customDialogDelete.context as MainActivity).recreate()
                        } else {
                            Toast.makeText(
                                customDialogDelete.context,
                                "Catatan gagal dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            hapusDataDialog.show()
        }

        holder.binding.ivEditNote.setOnClickListener {
            dbNote = NoteDatabase.getInstance(it.context)

            //create custom dialog for delete process
            val customDialogEdit = LayoutInflater.from(it.context)
                .inflate(R.layout.dialog_edit_data, null, false)
            val inputeditJudul = customDialogEdit.findViewById<EditText>(R.id.etTitleEdit)
            val inputeditContent = customDialogEdit.findViewById<EditText>(R.id.etContentEdit)
            val buttonEdit = customDialogEdit.findViewById<Button>(R.id.btnEdit)

            inputeditJudul.setText(listNote[position].title)
            inputeditContent.setText(listNote[position].content)

            AlertDialog.Builder(it.context)
                .setView(customDialogEdit)
                .create()
                .show()
            buttonEdit.setOnClickListener {

                val hasilEditJudul = inputeditJudul.text.toString()
                val hasilEditContent = inputeditContent.text.toString()

                listNote[position].title = hasilEditJudul
                listNote[position].content = hasilEditContent
                GlobalScope.async {
                    val command = dbNote?.noteDao()?.updateNote(listNote[position])

                    //check if edit process worked or not
                    (customDialogEdit.context as MainActivity).runOnUiThread {
                        if (command != 0) {
                            Toast.makeText(
                                it.context,
                                "Catatan berhasil diupdate",
                                Toast.LENGTH_SHORT
                            ).show()
                            //recreate activity
                            (customDialogEdit.context as MainActivity).recreate()
                        } else {
                            Toast.makeText(it.context, "Catatan gagal diupdate", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    fun setData(listNote: ArrayList<DataNote>) {
        this.listNote = listNote
    }
}