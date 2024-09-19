# 1. Firebase Adding
> **Reference URL -** https://console.firebase.google.com/  
login to firebase and open firebase console page.
```
Create a Project -> "kotlin-FireStoreApp" -> Continue ->
    Choose or create a Google Analytics account : Default Account for Firebase 
    -> Create porject -> Continue
Open the project "kotlin-FireStoreApp" in firebase console
Get started by adding Firebase to your app - select 'android' for Add an app to get started 
```
- 1 Register app
```
->  Android package Name : com.aswin.firestoreapp
    App nickname : Firestore App
    Debug signing certificate SHA-1 (Optional) :
    -> Register App
```
- 2 Download and then add config file
```
Download google.services.json file and Move your downloaded google-services.json file
into your module (app-level) root directory.
-> Click Next
```
- 3 Add Firebase SDK

1. Add the plugin as a dependency to your project-level build.gradle.kts file:
- Root-level (project-level) Gradle file (<project>/build.gradle.kts):
```
plugins {
  // Add the dependency for the Google services Gradle plugin
  id("com.google.gms.google-services") version "4.4.2" apply false
}
```
2 .Then, in your module (app-level) build.gradle.kts file, add both the google-services plugin and any Firebase SDKs that you want to use in your app:
- Module (app-level) Gradle file (<project>/<app-module>/build.gradle.kts):
```
plugins {
  id("com.android.application")

  // Add the Google services Gradle plugin
  id("com.google.gms.google-services")

  ...
}

dependencies {
  // Import the Firebase BoM
  implementation(platform("com.google.firebase:firebase-bom:33.3.0"))


  // TODO: Add the dependencies for Firebase products you want to use
  // When using the BoM, don't specify versions in Firebase dependencies
  implementation("com.google.firebase:firebase-analytics")


  // Add the dependencies for any other desired Firebase products
  // https://firebase.google.com/docs/android/setup#available-libraries
}
```
Click Next -> Click Continue to the console.


- **Note**  How to Generate the SHA-1 is listed below
 ```
Method 1 :
    Go to Android Studio for -> File -> settings -> Experimental -> 
    UnTick - Do not build Gradle task list during Gradle sync -> Apply -> OK

    ->Build -> Clean and rebuild the project
    -> Sync Project with Gradle Files -> Open 'Gradle' in the right side -> FirebaseApp -> Tasks -> android -> signinReport (RtClick ) 
    -> Run 'FirebaseApp[Sigining..]' -> This will generate SHA1
     
Method 2 : 
    if the above case is note working use programatic Approch:
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
```
3. Add Internet and Access Network State permission in AndroidManifest.xml
```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```


# 2. Firestore Database (Cloud Firestore)
> **Reference URL -** https://console.firebase.google.com/project/kotlin-firestoreapp/overview
```
Open the appliction in Firebase console -> Build (Left side) -> Firestore Database
Create Database ->  
                   location : asia-south1 (Mumbai) -> Next
                   Security rules : Start in test mode - Create  (Your data is open by default to enable quick setup. However, you must update your security rules within 30 days to enable long-term client read/write access. )
 
 This will open a Console for Cloud Firestore
```
 - Check the firestore Rules
```
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.time < timestamp.date(2024, 10, 19);
    }
  }
}
```
OR
```
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

   - Add the Firestore Database SDK to your app
First go to Documentation for more details
> **Reference URL -**  https://firebase.google.com/docs?authuser=0&hl=en
Build -> Cloud Firestore/Firestore(From left side) -> Get started

**build.gradle.kts -**  Module (app-level) Gradle file (<project>/<app-module>/build.gradle.kts):
```
dependencies {
    // Declare the dependency for the Cloud Firestore library
    implementation("com.google.firebase:firebase-firestore")
}
```


# 3. Kotlin codding
### 1. Initialize Cloud Firestore.
> **Reference URL -**  https://firebase.google.com/docs/firestore/quickstart?hl=en&authuser=0#kotlin+ktx
- Goto 'Initialize Cloud Firestore'
```
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        
        // Access a Cloud Firestore instance from your Activity
        db = Firebase.firestore
    }
}
```
### 2. Write Collections of document to Firebase Firestore Database
> **Reference URL -** https://firebase.google.com/docs/firestore/data-model?hl=en&authuser=0#kotlin+ktx
```
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
```
### 3. Read Document data From Firebase Firestore Database
```
    /**Read Collections of document Data from Firebase Firestore Database**/
    fun ReadDocumentsData() {
        // Receive Document from Firestore
        val docRef = db.collection("Users").document("user1")
        // Getting specific data from document
        docRef.get().addOnSuccessListener { document->
            if(document != null){
                //binding.tvResult.text = "${document.data}"
                binding.tvResult.text = "name : ${document.data?.get("name")} \nLastName : ${document.data?.get("lname")} \nBorn : ${document.data?.get("born")} "
            }
        }
    }
```
### 4. Read All Documents in Collection from Firebase Firestore Database
```
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
```
### 5. Updating Data in the Document from Firebase Firestore Database
```
    /**Updating Data in the Document from Firebase Firestore Database**/
    fun UpdatingData() {
        //Updating the Data in document
        db.collection("Users").document("user1").update("born","2000")
    }
```
### 6. Delete Data in the Document from Firebase Firestore Database
```
    /**Delete Data in the Document from Firebase Firestore Database**/
    fun DeleteData() {
        //Delete Data in document
        db.collection("Users").document("user1").delete()
    }
```




































