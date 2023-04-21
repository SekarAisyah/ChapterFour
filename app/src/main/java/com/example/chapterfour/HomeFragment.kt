package com.example.chapterfour

import com.example.chapterfour.databinding.FragmentHomeBinding
import com.example.chapterfour.room.DataNote
import com.example.chapterfour.room.NoteDatabase
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var sharedPreferences: SharedPreferences
    var dbNote: NoteDatabase? = null
    lateinit var adapterNote: NoteAdapter
    lateinit var vmNote: ViewModelNote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        (activity as AppCompatActivity).setSupportActionBar(binding.tbHome)

        sharedPreferences = requireContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE)

        var getUser = sharedPreferences.getString("user", "")
        binding.tbHome.title = "Welcome, $getUser"

        dbNote = NoteDatabase.getInstance(requireContext())

        noteVM()
        vmNote = ViewModelProvider(this).get(ViewModelNote::class.java)
        vmNote.getAllNoteObservers().observe(viewLifecycleOwner) {
            adapterNote.setData(it as ArrayList<DataNote>)
        }

        binding.btnAddNote.setOnClickListener {
            InputDataFragment().show(childFragmentManager, "InputDialogFragment")
        }

        binding.ivLogout.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("LOGOUT")
                .setMessage("Yakin ingin logout?")
                .setNegativeButton("Tidak"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }.setPositiveButton("Ya") { dialogInterface: DialogInterface, i: Int ->
                    val pref = sharedPreferences.edit()
                    pref.clear()
                    Toast.makeText(context, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter_data, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_ascending -> {
                getAllNoteAsc()
                Toast.makeText(context, "Sorted by Ascending", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.option_descending -> {
                getAllNoteDesc()
                Toast.makeText(context, "Sorted by Descending", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun noteVM() {
        adapterNote = NoteAdapter(ArrayList())
        binding.rvHome.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvHome.adapter = adapterNote
    }

    fun getAllNoteAsc() {
        GlobalScope.launch {
            var data = dbNote?.noteDao()?.getNoteAsc()
            activity?.runOnUiThread {
                adapterNote = NoteAdapter(data!!)
                binding.rvHome.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvHome.adapter = adapterNote

                // Simpan preferensi pengguna pada shared preference
                with(sharedPreferences.edit()) {
                    putBoolean("ascending", true)
                    apply()
                }
            }
        }

    }

    fun getAllNoteDesc() {
        GlobalScope.launch {
            var data = dbNote?.noteDao()?.getNoteDesc()
            activity?.runOnUiThread {
                adapterNote = NoteAdapter(data!!)
                binding.rvHome.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvHome.adapter = adapterNote

                // Simpan preferensi pengguna pada shared preference
                with(sharedPreferences.edit()) {
                    putBoolean("ascending", false)
                    apply()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val isAscending = sharedPreferences.getBoolean("ascending", true)
        if (isAscending) {
            getAllNoteAsc()
        } else {
            getAllNoteDesc()
        }
        GlobalScope.launch {
            var data = dbNote?.noteDao()?.getNoteAsc()
            activity?.runOnUiThread {
                adapterNote = NoteAdapter(data!!)
                binding.rvHome.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvHome.adapter = adapterNote
            }
        }
    }
}