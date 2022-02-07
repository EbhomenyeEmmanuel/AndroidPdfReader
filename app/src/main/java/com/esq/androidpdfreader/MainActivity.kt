package com.esq.androidpdfreader

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.esq.androidpdfreader.databinding.ActivityMainBinding
import com.esq.androidpdfreader.utils.Constants.FILE_URI
import com.esq.androidpdfreader.utils.Constants.VIEW_TYPE
import com.esq.androidpdfreader.utils.longToast
import com.esq.androidpdfreader.utils.shortToast
import com.github.barteksc.pdfviewer.PDFView

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readPDFPermission
        setUpBottomAppBar()
        setUpFab()
    }

    //Set up bottom bar
    private fun setUpBottomAppBar() {
        //Set bottom bar to Action bar as it is similar like toolbar
        setSupportActionBar(binding.bottomAppBar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomAppBar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                }
                R.id.speechFragment -> {
                    shortToast("Speech")
                }
            }

//            binding.bottomAppBar.visibility =  when (destination.id) {
//                R.id.homeFragment, R.id.Fragment -> View.VISIBLE
//                else -> View.GONE
//            }
//
//            if ( destination.id == R.id.homeFragment) {
//                supportActionBar?.hide()
//            } else {
//                supportActionBar?.show()
//            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (readPDFPermissionsGranted) {
                Log.i(TAG, "onActivityResult: Permissions has been granted")
                val selectedPDF: Uri = data.data!!
                val intent = Intent().apply {
                    putExtra(VIEW_TYPE, "storage")
                    putExtra(FILE_URI, selectedPDF.toString())
                }
                //start activity silently
                startActivityForResult(intent, READ_PDF_CODE)
                //startActivity(intent);
                Log.i(TAG, "onActivityResult: startActivityForResult() called")
            } else {
                longToast(
                    "Allow all permissions to read PDF"
                )
                val permissions = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this@MainActivity, permissions, PICK_PDF_CODE)
            }
        } else if (requestCode == READ_PDF_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Log.i(TAG, "onActivityResult: result gotten from same Activity")
            navController.navigate(R.id.homeFragment, data.extras)
        }
    }

    //Set up fab
    private fun setUpFab() {
        binding.fab.setOnClickListener(View.OnClickListener { view: View? ->
            Log.i(TAG, "setUpFab: FAB is clicked")
            val PDFIntent = Intent(Intent.ACTION_GET_CONTENT)
            PDFIntent.type = "application/pdf" //Set type of data
            PDFIntent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(
                Intent.createChooser(PDFIntent, "Select Your PDF"),
                PICK_PDF_CODE
            )
            Log.i(TAG, "setUpFab: startActivityForResult method is called")
        })
    }

    //Get permission to read PDF in the user's device
    private val readPDFPermission: Unit
        private get() {
            Log.i(TAG, "getReadPDFPermission: getting PDF permissions")
            val permissions = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
            if (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        this.applicationContext,
                        WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    readPDFPermissionsGranted = true
                    Log.i(TAG, "getReadPDFPermission: PDF permissions has been granted")
                } else {
                    Log.i(TAG, "getReadPDFPermission: PDF permissions has not been granted")
                    ActivityCompat.requestPermissions(this@MainActivity, permissions, PICK_PDF_CODE)
                }
            } else {
                Log.i(TAG, "getReadPDFPermission: PDF permissions has not been granted")
                ActivityCompat.requestPermissions(this@MainActivity, permissions, PICK_PDF_CODE)
            }
        }

    //Inflate menu to bottom Bar: this adds the items to the action bar if present
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bottom_bar, menu)
        //return super.onCreateOptionsMenu(menu);
        return true
    }

    companion object {
        private const val PICK_PDF_CODE = 1000
        const val READ_PDF_CODE = 2000
        private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val TAG = "HomeActivity"
        private var readPDFPermissionsGranted = false
        const val PDF_FILE = "pdf"
    }
}