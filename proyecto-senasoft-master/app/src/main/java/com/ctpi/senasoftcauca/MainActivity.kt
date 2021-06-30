package com.ctpi.senasoftcauca

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ctpi.senasoftcauca.auth.AuthActivity
import com.ctpi.senasoftcauca.face.LiveFaceActivityCamera
import com.ctpi.senasoftcauca.models.Usuario
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var imagen: ImageView
        private val DEFINED_CODE = 222
        private val REQUEST_CODE_SCAN = 0X01
        var usuarioCreado = Usuario()
        private const val PERMISSION_REQUEST = 1
        private fun isPermissionGranted(
            context: Context,
            permission: String?
        ): Boolean {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission!!
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!allPermissionsGranted()) {
            runtimePermission
        }

        btnmostPeople.setOnClickListener {
            imagen = imgVFoto
            val intent = Intent(this@MainActivity, LiveFaceActivityCamera::class.java)
            intent.putExtra("detect_mode", 1002)
            startActivity(intent)
        }

        txtBarCodeAM.setOnClickListener{
            clickScan()
        }

        btnGuardar.setOnClickListener {
            usuarioCreado.nikName = etxtNikNameAM.text.toString()
            if (!usuarioCreado.foto.isEmpty() && usuarioCreado.codigo != "" && usuarioCreado.nikName != "") {
                var mensaje = Usuario().insertar(usuarioCreado)
                Toast.makeText(this, ""+mensaje, Toast.LENGTH_SHORT).show()
                if (mensaje == "Usuario Creado") {
                    finish()
                }
            }
        }

    }
    private val requiredPermissions: Array<String?>
        get() = try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Exception) {
            arrayOfNulls(0)
        }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(this, permission)) {
                return false
            }
        }
        return true
    }

    private val runtimePermission: Unit
        get() {
            val allNeedPermissions: MutableList<String?> = ArrayList()
            for (permission in requiredPermissions) {
                if (!isPermissionGranted(this, permission)) {
                    allNeedPermissions.add(permission)
                }
            }
            if (allNeedPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    this,
                    allNeedPermissions.toTypedArray(),
                    PERMISSION_REQUEST
                )
            }
        }

    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) return
        else if (requestCode == DEFINED_CODE) {
            // Call the barcode scanning view in Default View mode.
            ScanUtil.startScan(this, REQUEST_CODE_SCAN, HmsScanAnalyzerOptions.Creator().setHmsScanTypes(
                HmsScan.ALL_SCAN_TYPE).create())
        }
        if (requestCode != PERMISSION_REQUEST) {
            return
        }
        var isNeedShowDialog = false
        for (i in permissions.indices) {
            if (permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[i]
                != PackageManager.PERMISSION_GRANTED
            ) {
                isNeedShowDialog = true
            }
        }
        if (isNeedShowDialog && !ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val dialog: AlertDialog = AlertDialog.Builder(this)
                .setMessage("Esta aplicacion requiere de acceso a tu carpeta de medios y tu camara para poder funcionar")
                .setPositiveButton("Configuracion") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    startActivityForResult(intent, 200)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel")
                { _, _ -> finish() }.create()
            dialog.show()
        }
    }


    private fun logoutWithHuaweiID() {
        val mAuthParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .createParams()
        val mAuthManager = HuaweiIdAuthManager.getService(this, mAuthParams)
        val logoutTask = mAuthManager.signOut()
        logoutTask.addOnSuccessListener {
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
            finish()
        }
        logoutTask.addOnFailureListener {
            Toast.makeText(this, "Logout Fallo!", Toast.LENGTH_LONG).show()
        }

    }

    fun clickScan() {
        // Initialize a list of required permissions to request runtime
        val list = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, list, DEFINED_CODE)
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
                        usuarioCreado.codigo = obj.getOriginalValue()
                        txtBarCodeAM.setText(obj.getOriginalValue())
                    }
                    return
                }
            }
        }
    }

}
