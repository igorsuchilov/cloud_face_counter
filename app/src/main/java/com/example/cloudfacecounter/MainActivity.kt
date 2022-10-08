package com.example.cloudfacecounter

//import aws.sdk.kotlin.services.s3.*
//import aws.sdk.kotlin.services.s3.model.PutObjectRequest
//import aws.sdk.kotlin.services.s3.model.ListObjectsV2Request
//import aws.sdk.kotlin.runtime.auth.credentials.*
//import com.amazonaws.auth.BasicAWSCredentials
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.example.cloudfacecounter.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imgViewer.setImageBitmap(imageBitmap)

//            val session = runBlocking {
//                this@MainActivity.readBucket()
//            }

//            val session = runBlocking {
//                readBucketOld()
//            }

            Thread {

                    readBucketOld()

                runOnUiThread {
                    // Post the result to the main thread
                }
            }.start()





            val file_path = Environment.getExternalStorageDirectory().absolutePath
//                    "/imagestorage"

//            File(file_path, "map.png").writeBitmap(imageBitmap, Bitmap.CompressFormat.PNG, 85)
//            val session = runBlocking {
//                putS3Object("igor-cloud-face-counter", "facestest.jpg", file_path+"/map.png")
//            }


        }
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }

    fun readBucketOld(){
        var creds = BasicAWSCredentials("______", "______")
        var s3Client1 = AmazonS3Client(creds)
        val responseRead = s3Client1.listObjectsV2(/* bucketName = */ "igor-cloud-face-counter")

        println(responseRead.getObjectSummaries())


    }

//    suspend fun readBucket(){
//
//        val s3 = S3Client.fromEnvironment { region = "eu-west-1"}
//        val reqRead = ListObjectsV2Request {
//            bucket = "igor-cloud-face-counter"
//        }
//        val responseRead = s3.listObjectsV2(reqRead)
//        responseRead.contents?.forEach {println(it.key)}
//
//    }
//
//    suspend fun putS3Object(bucketName: String, objectKey: String, objectPath: String) {
//
//        val metadataVal = mutableMapOf<String, String>()
//        metadataVal["myVal"] = "test"
//
//        val request = PutObjectRequest {
//            bucket = bucketName
//            key = objectKey
//            metadata = metadataVal
//            body = File(objectPath).asByteStream()
//        }
//
//        val s3 = S3Client.fromEnvironment { region = "eu-west-1"}
//        s3.putObject(request)

//        S3Client.fromEnvironment { region = "eu-west-1" }.use { s3 ->
//            val response = s3.putObject(request)
//            println("Tag information is ${response.eTag}")
//        }

    }



