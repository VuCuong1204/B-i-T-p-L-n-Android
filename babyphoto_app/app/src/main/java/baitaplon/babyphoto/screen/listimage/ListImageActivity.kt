package baitaplon.babyphoto.screen.listimage

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.model.DataResult
import baitaplon.babyphoto.screen.createalbum.preview.DialogPreviewFragment
import baitaplon.babyphoto.screen.timeline.TimelineActivity
import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ListImageActivity : AppCompatActivity(), IListContract.IView {
    private lateinit var rvImageView: RecyclerView
    private lateinit var ivCancel: ImageView
    private lateinit var btnAdd: Button
    private lateinit var tvTitle: TextView
    private var arrayImage: MutableList<String> = ArrayList()
    private var arrayCb: MutableList<Boolean> = ArrayList()
    private var count: Int = 0
    lateinit var idalbum: RequestBody
    private lateinit var description: RequestBody
    private lateinit var timeline: RequestBody
    private lateinit var multiFile: MultipartBody.Part
    private lateinit var singFile: MultipartBody.Part
    lateinit var path: String
    private val REQUEST_CODE_CAMERA = 998
    private val REQUEST_CODE_IMAGE = 999
    var dialog: Dialog? = null

    var ID_ALBUM: String? = "1"
    private lateinit var listImagePresent: ListImagePresenter
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_image_activity)

        val bundle: Bundle? = intent.extras
        ID_ALBUM = bundle?.getString("idalbum")
        initView()
        default()
        checkPermission()
        cancelCreate()
    }


    private fun initView() {
        rvImageView = findViewById(R.id.rvListImage)
        ivCancel = findViewById(R.id.ivListImageCancel)
        btnAdd = findViewById(R.id.btnListImageAdd)
        tvTitle = findViewById(R.id.tvListImageTitle)
        listImagePresent = ListImagePresenter(this)
        dialog = Dialog(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun default() {
        // nhận kết quả trả về từ máy ảnh
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result?.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val bitmap: Bitmap = intent.extras?.get("data") as Bitmap
                    val uri: Uri = convertUri(bitmap)
                    readFileSingle(uri)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                listImagePresent.getImage(this)
            }
            REQUEST_CODE_CAMERA -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    activityResultLauncher.launch(intent)
                }
                return
            }

            else -> {

            }
        }
    }

    // xin quyền truy cập thư viện
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 998
            )
        }
        listImagePresent.getImage(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getData(listImage: MutableList<String>, listCb: MutableList<Boolean>) {

        arrayImage = listImage
        arrayCb = listCb
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvImageView.layoutManager = gridLayoutManager
        val adapter = ListImageAdapter(arrayImage, arrayCb, object : IListImage {
            override fun setCamera() {
                checkPermissionCamera()
            }

            override fun setImage(position: Int, select: Boolean) {
                arrayCb[position] = select
                if (select) {
                    count++
                    tvTitle.text = "Select (${count})"
                    btnAdd.setBackgroundResource(R.drawable.shape_orange_bg_corner_20)
                } else {
                    count--
                    if (count > 0) {
                        tvTitle.text = "Select (${count})"
                        btnAdd.setBackgroundResource(R.drawable.shape_orange_bg_corner_20)
                    } else {
                        tvTitle.text = "Add Moment"
                        btnAdd.setBackgroundResource(R.drawable.shape_gray_bg_corner_20)
                    }
                }
            }

            override fun showPreview(linkFolder: String) {
                val dialogPreviewFragment = DialogPreviewFragment()
                val bundle = Bundle()
                bundle.putString("urlImage", linkFolder)
                bundle.putBoolean("status", false)
                dialogPreviewFragment.arguments = bundle
                dialogPreviewFragment.show(supportFragmentManager, dialogPreviewFragment.tag)
            }


        })
        rvImageView.setHasFixedSize(true)
        rvImageView.adapter = adapter
        addImage()
    }

    //check quyền camera
    fun checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA
            )
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activityResultLauncher.launch(intent)
    }

    //Đẩy dữ liệu ảnh lên server
    @RequiresApi(Build.VERSION_CODES.O)
    fun addImage() {
        btnAdd.setOnClickListener {
            val files: MutableList<MultipartBody.Part> = ArrayList()
            for (position in 0 until arrayImage.size) {
                if (arrayCb[position]) {
                    convertToFile(arrayImage[position])
                    files.add(multiFile)
                }
            }
            if (files.size > 0) {
                getInfoImage()
                listImagePresent.addMultiImageToServer(
                    files,
                    idalbum,
                    description,
                    timeline
                )
            }

        }
    }


    private fun convertToFile(linkImage: String) {
        val file = File(linkImage)
        val file_path: String = file.absolutePath + System.currentTimeMillis() + ".png"

        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        multiFile = MultipartBody.Part.createFormData("file[]", file_path, requestBody)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readFileSingle(uri: Uri) {
        val image_path = getRealPathFromUri(uri)
        val file = File(image_path)
        val file_path: String = file.absolutePath + System.currentTimeMillis() + ".png"

        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        singFile = MultipartBody.Part.createFormData("file", file_path, requestBody)
        getInfoImage()
        listImagePresent.addImageSingleToServer(singFile, idalbum, description, timeline)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getInfoImage() {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatted: String = current.format(formatter)

        idalbum = RequestBody.create(MediaType.parse("multipart/form-data"), ID_ALBUM)
        description = RequestBody.create(MediaType.parse("multipart/form-data"), "Hello")
        timeline = RequestBody.create(MediaType.parse("multipart/form-data"), formatted)
    }


    private fun convertUri(bitmap: Bitmap): Uri {
        //convert bitmap to uri
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun getRealPathFromUri(contentUri: Uri): String {
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
        )

        val cursor = contentResolver.query(
            contentUri, projection, null, null, null
        )
        if (cursor!!.moveToFirst()) {
            path = cursor.getString(0)
        }
        cursor.close()
        return path
    }

    private fun cancelCreate() {
        ivCancel.setOnClickListener {
            listImagePresent.openBackDialog(this)
        }
    }


    override fun onResult(data: DataResult<String>) {
        when (data.state) {
            DataResult.State.SUCCESS -> {
                Toast.makeText(applicationContext, data.data, Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(applicationContext, TimelineActivity::class.java)
                intent.putExtra("idalbum", ID_ALBUM)
                setResult(200, intent)
                finish()
            }
            DataResult.State.FAIL -> {
                Toast.makeText(applicationContext, data.data, Toast.LENGTH_SHORT).show()
            }
            DataResult.State.ERROR -> {
                Toast.makeText(applicationContext, data.data, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun showLoading() {
        dialog?.setContentView(R.layout.dialog_loading_image_layout)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.show()
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }

    override fun onBackPressed() {
        listImagePresent.openBackDialog(this)
    }


}
