package com.pravin.androidcompletefilehandling

import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.*
import java.lang.Exception

object FileHandler {
    private val TAG:String = "**FileHandler"

    fun readFromFile(file: File): String {
        var aBuffer: String? = ""
        try {
            val fis = FileInputStream(file)
            val myReader = BufferedReader(InputStreamReader(fis))
            var aDataRow: String? = ""
            while (myReader.readLine().also { aDataRow = it } != null) {
                aBuffer += aDataRow
            }
            myReader.close()
            fis.close()
            Log.e(TAG, "readFromFile: "+file.absolutePath )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return aBuffer.toString()

    }
    fun writeToFile(file: File, data: String):Boolean {
        try {
            val fos  = FileOutputStream(file)
            fos.write(data.toByteArray())
            fos.flush()
            fos.close()
            return true
        }catch (e: Exception){
            Log.e(TAG, "writeToFile: $e" )
            return false
        }
    }

    fun isExternalStorageRedable():Boolean = Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED )
    fun isExternalStorgaeWritable():Boolean = Environment.getExternalStorageState().let {
        (equals(Environment.MEDIA_MOUNTED)||equals(Environment.MEDIA_MOUNTED_READ_ONLY))
    }

}