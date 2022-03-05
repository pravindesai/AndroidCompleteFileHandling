package com.pravin.androidcompletefilehandling

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pravin.androidcompletefilehandling.databinding.ActivityMainBinding
import com.pravin.androidcompletefilehandling.externalStorage.ExternalStorageActivity
import com.pravin.androidcompletefilehandling.internalStorage.InternalStorageActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.internalStorageButton.setOnClickListener(this)
        binding.externalStorageButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.internalStorageButton ->{
                startActivity(Intent(this, InternalStorageActivity::class.java))
            }
            binding.externalStorageButton->{
                startActivity(Intent(this, ExternalStorageActivity::class.java))
            }
        }
    }

}