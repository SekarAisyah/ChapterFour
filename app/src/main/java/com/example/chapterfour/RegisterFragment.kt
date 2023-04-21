package com.example.chapterfour

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chapterfour.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    lateinit var sharedRegis: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedRegis = requireContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE)

        binding.btnRegister.setOnClickListener {
            var getUsername = binding.etUsernameRegist.text.toString()
            var getPass = binding.etPasswordRegist.text.toString()
            var getRepeatPass = binding.etConfirmPassword.text.toString()

            var addUser = sharedRegis.edit()
            addUser.putString("user", getUsername)
            addUser.putString("password", getPass)
            addUser.putString("repeadPassword", getRepeatPass)
            if (getPass == getRepeatPass) {
                addUser.apply()
                Toast.makeText(context, "Register Berhasil", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                Toast.makeText(context, "Ulangi password yang anda masukan tidak sama", Toast.LENGTH_SHORT).show()
            }

        }
    }
}