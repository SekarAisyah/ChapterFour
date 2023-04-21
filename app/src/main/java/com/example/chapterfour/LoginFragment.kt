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
import com.example.chapterfour.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var sharedLogin: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedLogin = requireContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE)

        binding.txtBelumPunyaAkun.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            var getUserName = sharedLogin.getString("user", "")
            var getDataPass = sharedLogin.getString("password", "")
            var user = binding.edtTextUsername.text.toString()
            var pass = binding.edtTextPassword.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if (user == getUserName.toString() && pass == getDataPass.toString()) {

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                Toast.makeText(context, "Login Berhasil", Toast.LENGTH_SHORT).show()
            } else if (user != getUserName.toString() || pass != getDataPass.toString()) {
                Toast.makeText(context, "Email dan Pasword anda salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

}