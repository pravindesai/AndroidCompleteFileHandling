package com.pravin.androidcompletefilehandling.internalStorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pravin.androidcompletefilehandling.databinding.ActivityInternalStorageBinding
import java.io.*
import java.lang.Exception

class InternalStorageActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivityInternalStorageBinding
    lateinit var viewModel:InternalStorageActViewModel
    lateinit var absolutePath:String
    private val DIR_NAME = "MY_TEST_DIR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInternalStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Internal Storage Operations"
        viewModel = ViewModelProvider(this).get(InternalStorageActViewModel::class.java)

        binding.readFileButton  .setOnClickListener(this)
        binding.writeFileButton .setOnClickListener(this)
        binding.ShowFilesButton .setOnClickListener(this)
        binding.deleteFileButton.setOnClickListener(this)
        binding.createDirButton.setOnClickListener(this)

        with(filesDir.absolutePath){
            absolutePath = this
            binding.pathTv.text = this
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.createDirButton  ->{
                val path:File = getDir(DIR_NAME, MODE_PRIVATE)  //creates if not exists
                val copyFileName = "copy_"+getFileName()
                val copy_file:File = File(path, copyFileName)

                var fis:FileInputStream? = null
                var fos:FileOutputStream? = null
                val data = ""
                try {
                    fis = openFileInput(getFileName())
                    fos = FileOutputStream(copy_file)
                    val isr = InputStreamReader(fis, "UTF-8")
                    val br = BufferedReader(isr)
                    var fileContent:String = ""
                    br.forEachLine {
                        fileContent=fileContent+it
                    }
                    fos.write(data.toByteArray())
                    fos.flush()
                    br.close()
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT ).show()
                }catch (e:Exception){
                    Log.e("**", "onClick: $e" )
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT ).show()
                }finally {
                    fis?.close()
                    binding.dataEt.setText(data)
                }
            }
            binding.readFileButton   ->{
                var fis:FileInputStream? = null
                var data = ""
                try {
                    fis = openFileInput(getFileName())
                    val isr = InputStreamReader(fis, "UTF-8")
                    val br = BufferedReader(isr)
                    br.forEachLine {
                        data+=it
                    }
                    br.close()

            }catch (e:Exception){
                    Log.e("**", "onClick: ${e}" )
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT ).show()
                }finally {
                    fis?.close()
                    binding.dataEt.setText(data)
                }
            }
            binding.writeFileButton  ->{
                var fos:FileOutputStream? = null
                try {
                    fos = openFileOutput(getFileName(), MODE_PRIVATE)
                    fos.write(getData().toByteArray())
                    fos.flush()
                    Toast.makeText(this, "File status: Success", Toast.LENGTH_SHORT).show()
                }catch (e:Exception){
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }finally {
                    fos?.close()
                    binding.dataEt.text.clear()
                    binding.fileNameEt.text.clear()
                }
            }
            binding.deleteFileButton ->{
                if(deleteFile(getFileName())){
                    Toast.makeText(this, "File Deleted", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
                binding.ShowFilesButton.performClick()
            }
            binding.ShowFilesButton  ->{
                val fileList = fileList()
                binding.fileNameEt.text.clear()
                binding.dataEt.text.apply {
                    clear()
                    append("****File List****\n")
                    fileList.forEach {
                        append("--> "+it+"\n")
                    }
                    append("****END****\n")
                }
            }
        }
    }

    fun getFileName() = binding.fileNameEt.text.toString()+".txt"
    fun setFileName(fileName:String) = binding.fileNameEt.setText(fileName)
    fun getData() = binding.dataEt.text.toString()
    fun setData(data:String) = binding.dataEt.setText(data)

}