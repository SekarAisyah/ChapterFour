package com.example.chapterfour

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chapterfour.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {
    lateinit var binding: FragmentSplashScreenBinding
    lateinit var sharedSplash: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        val view = binding.root

        Handler(Looper.myLooper()!!).postDelayed({

            sharedSplash = requireContext().getSharedPreferences("dataUser", Context.MODE_PRIVATE)

            if (sharedSplash.getString("user", "").equals("")) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
            } else if (sharedSplash.getString("user", "")!!.isNotEmpty()) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
            }
        }, 2000)
        return view
    }

}