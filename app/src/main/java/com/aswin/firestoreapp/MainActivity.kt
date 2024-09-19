package com.aswin.firestoreapp

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.aswin.firestoreapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        /**For getting SHA1 value of the project**/
        //getSHA1()

        // Initialize Firestore
        db = Firebase.firestore

        /** Write Collections of document to Firebase Firestore Database **/
        //writeCollectionData()

        /**Read Collections of document Data from Firebase Firestore Database**/
        //ReadDocumentsData()

        /**Read All Documents in Collection from Firebase Firestore Database**/
        ReadAllDocuments()

        /**Updating Data in the Document from Firebase Firestore Database**/
        //UpdatingData()

        /**Delete Data in the Document from Firebase Firestore Database**/
        //DeleteData()
    }

    /** Write Collections of document to Firebase Firestore Database **/
    fun writeCollectionData() {

        // Creating a collection: "Users"
        val users_collection = db.collection("Users")

        // Creating documents
        val user1 = mapOf(
            "name" to "Aswin",
            "lname" to "KE",
            "born" to "1989"
        )
        val user2 = mapOf(
            "name" to "Saroj",
            "lname" to "Kumar",
            "born" to "1980"
        )

        // Adding Document to Collection
        users_collection.document("user1").set(user1)
        users_collection.document("user2").set(user2)
    }

    /**Read Collections of document Data from Firebase Firestore Database**/
    fun ReadDocumentsData() {
        // Receive Document from Firestore
        val docRef = db.collection("Users").document("user1")
        // Getting specific data from document
        docRef.get().addOnSuccessListener { document->
            if(document != null){
                //binding.tvResult.text = "${document.data}"
                binding.tvResult.text = "name : ${document.data?.get("name")} \nLastName : ${document.data?.get("lname")} \nBorn : ${document.data?.get("born")} \n"
            }
        }
    }

    /**Read All Documents in Collection from Firebase Firestore Database**/
    fun ReadAllDocuments() {
        // Getting All documents from collection named "Users"
        val collectionRef = db.collection("Users")
        collectionRef.get().addOnSuccessListener { collection ->
            for (document in collection){
                binding.tvResult.append("DocumentName - ${document.id} \n    name : ${document.data?.get("name")} \n    LastName : ${document.data?.get("lname")} \n    Born : ${document.data?.get("born")} \n\n")
            }
        }
    }

    /**Updating Data in the Document from Firebase Firestore Database**/
    fun UpdatingData() {
        //Updating the Data in document
        db.collection("Users").document("user1").update("born","2000")
    }

    /**Delete Data in the Document from Firebase Firestore Database**/
    fun DeleteData() {
        //Delete Data in document
        db.collection("Users").document("user1").delete()
    }

    fun getSHA1() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA-1")
                md.update(signature.toByteArray())
                val sha1 = md.digest()

                // Convert byte array to hex string with colons between each byte
                val hexString = sha1.joinToString(separator = ":") { byte ->
                    "%02X".format(byte) // %02X formats to uppercase hex
                }

                Log.d("SHA1", hexString) // Print the SHA-1 hash in the desired format
            }
        } catch (e: Exception) {
            Log.e("SHA1", "Error: ${e.message}")
        }
    }
}