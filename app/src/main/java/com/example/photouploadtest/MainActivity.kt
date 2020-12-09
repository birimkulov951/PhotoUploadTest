package com.example.photouploadtest

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.app.models.addfield.AddFieldResponse

class MainActivity : AppCompatActivity() {

    private val TAG = "AddStadiumFragment"


    private var fileUris = ArrayList<Uri>()

    private var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwaG9uZV9udW1iZXIiOiIrOTk2NzA5MDY5MDQ5IiwiZXhwIjoxNjA3NTEzNjYxfQ.Txb7lrPHMvSgemBPsiqgPFxipaH0d1R17aBTN3xJGA4"



    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE
                )
            }
        }
        val selectPhotoBtn = findViewById<Button>(R.id.select_photo)
        val uploadBtn = findViewById<Button>(R.id.upload)

        selectPhotoBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent,"Select"), IMAGE_PICK_CODE)
        }

        uploadBtn.setOnClickListener{
            addField()
        }





    }



    private fun addField() {
        if (fileUris.isEmpty()) {
            Toast.makeText(this, "Select an Images", Toast.LENGTH_SHORT).show()
        } else {

            val typePart: RequestBody = RequestBody.create(MultipartBody.FORM, "1")
            val namePart: RequestBody = RequestBody.create(MultipartBody.FORM, "fieldName")
            val pricePart: RequestBody = RequestBody.create(MultipartBody.FORM, "500")
            val minimumSizePart: RequestBody = RequestBody.create(MultipartBody.FORM, "50")
            val maximumSizePart: RequestBody = RequestBody.create(MultipartBody.FORM, "50")
            val locationPart: RequestBody = RequestBody.create(MultipartBody.FORM, "CHUY")
            val descriptionPart: RequestBody = RequestBody.create(MultipartBody.FORM, "yooooooooooooooooo oooooooooooo oooooooooooo")

            val numberOfPlayersPart: RequestBody = RequestBody.create(MultipartBody.FORM, "22")
            val hasParkingPart: RequestBody = RequestBody.create(MultipartBody.FORM, "false")
            val isIndoorPart: RequestBody = RequestBody.create(MultipartBody.FORM, "false")
            val hasShowersPart: RequestBody = RequestBody.create(MultipartBody.FORM, "false")
            val hasLockerRoomsPart: RequestBody = RequestBody.create(MultipartBody.FORM, "false")
            val hasLightsPart: RequestBody = RequestBody.create(MultipartBody.FORM, "false")
            val hasRostrumPart: RequestBody = RequestBody.create(MultipartBody.FORM, "false")
            val hasEquipmentPart: RequestBody = RequestBody.create(MultipartBody.FORM, "false")




            val imageParts: ArrayList<MultipartBody.Part> = ArrayList()
            for (i in 0 until fileUris.size) {
                if (!i.equals(Uri.EMPTY)) {
                    imageParts.add(prepareFilePart( fileUris[i]))
                }
            }


            val retroInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            val call = retroInstance.createField("Bearer $token", typePart, namePart, pricePart, minimumSizePart, maximumSizePart, imageParts, locationPart,
                    descriptionPart, numberOfPlayersPart, hasParkingPart, isIndoorPart, hasShowersPart, hasLockerRoomsPart, hasLightsPart, hasRostrumPart, hasEquipmentPart
            )


            call.enqueue(object : Callback<AddFieldResponse> {

                override fun onResponse(call: Call<AddFieldResponse>, response: Response<AddFieldResponse>) {


                    val text = findViewById<TextView>(R.id.text)


                    if (response.isSuccessful) {

                        Log.e(TAG, "onResponse: ${response.body()}")

                        text.text = response.body().toString()

                        //addFieldHours(response.body()?.id)

                    } else {
                        Log.e(TAG, "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<AddFieldResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: $t")
                    Toast.makeText(this@MainActivity, "unknown error", Toast.LENGTH_SHORT).show()
                }

            })

        }
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {

        // method 1
        val uriPathHelper = URIPathHelper()
        val originalFile2 = uriPathHelper.getPath(this, fileUri)!!

        // method 2
        val originalFile = RealPathUtil.getRealPath(this, fileUri)!!


        // did not worked
        //val name = File(fileUri.path!!).name
        //val file = File(requireActivity().cacheDir, originalFile)

        val fieldImagePart: RequestBody = RequestBody.create(applicationContext.contentResolver?.getType(fileUri)!!.toMediaTypeOrNull(), originalFile);

        return  MultipartBody.Part.createFormData("images", originalFile, fieldImagePart)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fileUris = ArrayList()
        fileUris.clear()
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            try {
                val clipData: ClipData = data?.clipData!!
                for (i in 0 until clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    val uri: Uri = item.uri
                    fileUris.add(uri)

                }

            } catch (e: NullPointerException) {
                Log.e(TAG, "catch: $e")

                val photo = data?.data!!
                fileUris.add(photo)

            }
        }
    }



    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when(requestCode) {

            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //pickImageFromGallery()
                } else {
                    Toast.makeText(this, "permission denial", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

}