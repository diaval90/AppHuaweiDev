package com.ctpi.senasoftcauca

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.ctpi.senasoftcauca.models.Jugador
import com.ctpi.senasoftcauca.models.Preguntas
import com.ctpi.senasoftcauca.ui.main.SectionsPagerAdapter
import com.google.firebase.database.*
import com.huawei.hms.ml.scan.HmsScan
import kotlinx.android.synthetic.main.acticity_juego.view.*
import kotlinx.android.synthetic.main.activity_lista_rank.view.*
import kotlinx.android.synthetic.main.activity_menu.*
import java.util.stream.Collectors

class MenuActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
        private val DEFINED_CODE = 222
        private val REQUEST_CODE_SCAN = 0X01
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        fab.setOnClickListener {

            val infView = layoutInflater.inflate(R.layout.activity_lista_rank, null)
            consultarRank(infView)
        }
    }

    fun BtnClick() {
        val list = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, list, DEFINED_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return
        }
        else if (requestCode == DEFINED_CODE) {
            this.startActivityForResult(
                Intent(this, DefinedActivity::class.java), REQUEST_CODE_SCAN)
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        else if (requestCode == REQUEST_CODE_SCAN) {
            val hmsScan: HmsScan? = data.getParcelableExtra(DefinedActivity.SCAN_RESULT)
            if (hmsScan != null) {
                if (!TextUtils.isEmpty(hmsScan.getOriginalValue()))
                    redirecion(hmsScan.getOriginalValue())
                    Toast.makeText(this, hmsScan.getOriginalValue(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun redirecion(qr: String) {
        val intent2 = Intent(Intent.ACTION_VIEW)
        intent2.setData( Uri.parse(qr))
        startActivity(intent2)
    }

    fun consultarRank (infView: View){
        var lista: MutableList<Jugador> = ArrayList()
        var fireBase = FirebaseDatabase.getInstance()
        var myRef = fireBase.getReference()
        var query: Query = myRef.child("Jugador").orderByChild("puntaje").limitToFirst(5)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (objSnaptshot in dataSnapshot.children) {
                    val pregunta: Jugador? = objSnaptshot.getValue(Jugador::class.java)
                    lista.add(pregunta!!)
                }
                if (!lista.isEmpty()) {
                    showDialog(infView, lista)
                }
            }
        })
    }
    private fun showDialog(infView: View, jugadore: MutableList<Jugador>) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
        dialog.setView(infView)
        var litsView = infView.lvRank
        var adapter = ArrayAdapter<Jugador>(this,android.R.layout.simple_expandable_list_item_1,jugadore)
        litsView.adapter = adapter
        var creardo = dialog.create()
        creardo.show()
    }
}