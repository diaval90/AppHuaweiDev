package com.ctpi.senasoftcauca

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.ctpi.senasoftcauca.models.SesionUsuario
import com.ctpi.senasoftcauca.models.Usuario
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.activity__loggin__huawei.*
import kotlin.math.log

class Activity_Loggin_Huawei : AppCompatActivity() {

    private val DEFINED_CODE = 222
    private val REQUEST_CODE_SCAN = 0X01
    var usuario = Usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__loggin__huawei)
        btnLoginAL.setOnClickListener {
        }

        etxtBarCodeAL.setOnClickListener {
            clickScan()
        }

        btnLoginAL.setOnClickListener {
            usuario.nikName = etxtNikNameAM.text.toString()
            var login = ""
            if (!usuario.codigo.isEmpty() && !usuario.nikName.isEmpty()) {
                login = Usuario().iniciarSesion(usuario)
            }
            if (login == "Login") {
                var sesionUsuario = SesionUsuario.getInstance()
                sesionUsuario.usuario = usuario
                var intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(this, login, Toast.LENGTH_SHORT).show()
            }
        }

        btnGuardar.setOnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun clickScan() {
        // Initialize a list of required permissions to request runtime
        val list = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, list, DEFINED_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) return
        else if (requestCode == DEFINED_CODE) {
            // Call the barcode scanning view in Default View mode.
            ScanUtil.startScan(this, REQUEST_CODE_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(
                HmsScan.ALL_SCAN_TYPE).create())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //receive result after your activity finished scanning
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) { return }
        // Obtain the return value of HmsScan from the value returned by the onActivityResult method by using ScanUtil.RESULT as the key value.
        else if (requestCode == REQUEST_CODE_SCAN) {
            when (val obj: Any = data.getParcelableExtra(ScanUtil.RESULT)!!) {
                is HmsScan -> {
                    if (!TextUtils.isEmpty(obj.getOriginalValue())) {
                        Toast.makeText(this, obj.getOriginalValue(), Toast.LENGTH_SHORT).show()
                        usuario.codigo = obj.getOriginalValue()
                        etxtBarCodeAL.setText(usuario.codigo)
                    }
                    return
                }
            }
        }
    }
}