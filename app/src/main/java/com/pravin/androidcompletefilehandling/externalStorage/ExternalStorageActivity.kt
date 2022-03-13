package com.pravin.androidcompletefilehandling.externalStorage

import android.Manifest.permission
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.pravin.androidcompletefilehandling.FileHandler
import com.pravin.androidcompletefilehandling.databinding.ActivityExternalStorageBinding
import com.pravin.androidcompletefilehandling.internalStorage.InternalStorageActViewModel
import java.io.*
import java.lang.Exception

class ExternalStorageActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = "**"
    val STORAGE_PERMISSION_REQUEST = 123
    var IS_PERMISSION_GRANTED = false
    lateinit var binding: ActivityExternalStorageBinding
    lateinit var viewModel: InternalStorageActViewModel
    lateinit var absolutePath:String
    private val DIR_NAME = "MY_TEST_DIR"

    var PATH_ROOT_EXTERNAL:File? = null
    var PATH_DOC_EXTERNAL :File? = null
    var PATH_PIC_EXTERNAL :File? = null
    var PATH_CUSTOM_DIR_EXTERNAL :File? = null
    var MY_DIR_NAME = "MyDir"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExternalStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "External Storage Operations"
        viewModel = ViewModelProvider(this).get(InternalStorageActViewModel::class.java)
        binding.readEPubFileButton  .setOnClickListener(this)
        binding.writeEPubFileButton .setOnClickListener(this)
        binding.readEPriFileButton .setOnClickListener(this)
        binding.writeEPriFileButton.setOnClickListener(this)

        PATH_ROOT_EXTERNAL = getExternalFilesDir(null)
        PATH_DOC_EXTERNAL  = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        PATH_PIC_EXTERNAL  = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        PATH_CUSTOM_DIR_EXTERNAL = getExternalFilesDir(MY_DIR_NAME)

        //Absolute paths
        binding.pathTv.text = PATH_ROOT_EXTERNAL?.absolutePath
        Log.e(TAG, "Path absolute "+PATH_ROOT_EXTERNAL )
        Log.e(TAG, "Path doc "+PATH_DOC_EXTERNAL )
        Log.e(TAG, "Path pic "+PATH_PIC_EXTERNAL )
        Log.e(TAG, "Path custome dir "+PATH_CUSTOM_DIR_EXTERNAL?.absolutePath )

    }

    override fun onClick(v: View?) {
            when(v){
                binding.readEPubFileButton   ->{
                  val res = storagePermissionCheck()
                  if(res){
                          val filePath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                          val file = File(filePath, getFileName())
                          if (file.exists()){
                              val data = FileHandler.readFromFile(file)
                              setData(data)

                      }else{
                        storagePermissionCheck()
                      }
                  }else{
                      storagePermissionCheck()
                  }
                }
                binding.writeEPubFileButton  ->{
                    val res = storagePermissionCheck()
                   if(res){
                             val filePath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                             if (!filePath.exists()) filePath.mkdirs()
                             val file = File(filePath, getFileName())
                             FileHandler.writeToFile(file, getData())
                             Log.e(TAG, "onClick: $filePath" )
                   }else{
                       storagePermissionCheck()
                   }
                }
                binding.readEPriFileButton   ->{
                    val file:File = File(PATH_ROOT_EXTERNAL, getFileName())
                    val data = FileHandler.readFromFile(file)
                    setData( data )
                }
                binding.writeEPriFileButton  ->{
                    val file:File = File(PATH_ROOT_EXTERNAL, getFileName())
                    FileHandler.writeToFile(file, getData())
                }
            }
    }


    private fun storagePermissionCheck():Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if (
               (checkSelfPermission(permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
               &&
               (checkSelfPermission(permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
           ){
               IS_PERMISSION_GRANTED = true
           }else{
               requestPermissions(arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST)
           }
        }
            return IS_PERMISSION_GRANTED
    // && isExternalStorageRedable() && isExternalStorgaeWritable()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            STORAGE_PERMISSION_REQUEST -> {
                if (grantResults.size > 0) {
                    val WRITEACCEPTED = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (WRITEACCEPTED) {
                        IS_PERMISSION_GRANTED = true
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(
                                arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST
                            )
                        }
                    }
                }
            }
        }
    }

    fun getFileName() = binding.fileNameEt.text.toString()+".txt"
    fun setFileName(fileName:String) = binding.fileNameEt.setText(fileName)
    fun getData() = binding.dataEt.text.toString()
    fun setData(data:String) = binding.dataEt.setText(data)


}