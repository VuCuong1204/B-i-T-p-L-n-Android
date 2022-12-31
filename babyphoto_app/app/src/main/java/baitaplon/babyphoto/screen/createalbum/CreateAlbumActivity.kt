package baitaplon.babyphoto.screen.createalbum

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.Constant
import baitaplon.babyphoto.data.model.DataResult
import baitaplon.babyphoto.screen.createalbum.preview.PhotoFolderActivity
import baitaplon.babyphoto.screen.createalbum.relation.DialogRelationFragment
import baitaplon.babyphoto.screen.home.HomeActivity
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class CreateAlbumActivity : AppCompatActivity(), DialogRelationFragment.ICreateName,
    ICreateContract.IView {
    private lateinit var ivBackHome: ImageView
    private lateinit var btnCreate: Button
    private lateinit var ivAvatar: CircleImageView
    private lateinit var edtName: EditText
    private lateinit var ivBoy: ImageView
    private lateinit var ivGirl: ImageView
    private lateinit var tvBirthday: TextView
    private lateinit var flBirthday: FrameLayout
    private lateinit var tvRelation: TextView
    private lateinit var flRelation: FrameLayout
    private lateinit var flCamera: FrameLayout
    private lateinit var createAlbumPresenter: CreateAlbumPresenter
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var file_path: String
    private lateinit var strGender: String
    private lateinit var requestBody: RequestBody
    private lateinit var singleFile: MultipartBody.Part
    private lateinit var rqIdaccount: RequestBody
    private lateinit var rqName: RequestBody
    private lateinit var rqGender: RequestBody
    private lateinit var rqBirthday: RequestBody
    private lateinit var rqRelation: RequestBody
    private var dialog: Dialog? = null
    var bitmapAvatar: Boolean = false
    var ID_ACCOUNT: Int = 0
    private var select: Int = 1
    lateinit var file: File
    lateinit var path: String
    val RESULT_CODE_CAMERA = 123
    val RESULT_CODE_IMAGE = 234


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_album_activity)

        ID_ACCOUNT = Constant.account.idaccount
        initView()
        getUriBaby()
        getGenderAlbum()
        getRelationAlbum()
        changeButton()
        onClick()
        getAvatar()
    }

    private fun onClick() {
        flBirthday.setOnClickListener {
            createAlbumPresenter.getBirthdayAlbum(this)
        }
    }

    private fun initView() {
        ivBackHome = findViewById(R.id.ivCreateAlbumBackHome)
        btnCreate = findViewById(R.id.btnCreateAlbumCreate)
        ivAvatar = findViewById(R.id.ivCreateAlbumAvatar)
        edtName = findViewById(R.id.edtCreateAlbumName)
        ivBoy = findViewById(R.id.ivCreateAlbumBoy)
        ivGirl = findViewById(R.id.ivCreateAlbumGirl)
        tvBirthday = findViewById(R.id.tvCreateAlbumBirthday)
        flBirthday = findViewById(R.id.flCreateAlbumBirthday)
        tvRelation = findViewById(R.id.tvCreateAlbumRelation)
        flRelation = findViewById(R.id.flCreateAlbumRelation)
        flCamera = findViewById(R.id.flCreateAlbumCamera)
        dialog = Dialog(this)
        createAlbumPresenter = CreateAlbumPresenter(this)
    }

    // nhận ảnh từ library và camera
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAvatar() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result?.resultCode == RESULT_CODE_CAMERA) {
                val intent = result.data
                if (intent != null) {
                    val bitmap: Bitmap = intent.extras?.get("uriImage") as Bitmap
                    bitmapAvatar = true
                    //convert bitmap to uri
                    val uri = convertUri(bitmap)
                    val path_image = getRealPathFromUri(uri)
                    ivAvatar.setImageBitmap(bitmap)
                    setBackgroundButton()
                    btnCreate.setOnClickListener {
                        if (edtName.text.toString() != "" && tvBirthday.text.toString() != "" && tvRelation.text.toString() != "") {
                            sendSingleImage(path_image)
                            createAlbumPresenter.createAlbum(
                                singleFile,
                                rqIdaccount, rqName, rqGender, rqBirthday, rqRelation
                            )
                        }
                    }
                    flCamera.visibility = View.GONE
                }
            }

            if (result?.resultCode == RESULT_CODE_IMAGE) {
                val intent = result.data
                if (intent != null) {
                    val dataImage: String = intent.extras?.get("uri") as String
                    ivAvatar.setImageBitmap(BitmapFactory.decodeFile(dataImage))
                    bitmapAvatar = true
                    setBackgroundButton()
                    btnCreate.setOnClickListener {
                        if (edtName.text.toString() != "" && tvBirthday.text.toString() != "" && tvRelation.text.toString() != "") {
                            sendSingleImage(dataImage)
                            createAlbumPresenter.createAlbum(
                                singleFile,
                                rqIdaccount, rqName, rqGender, rqBirthday, rqRelation
                            )
                        }
                    }
                    flCamera.visibility = View.GONE
                }
            }
        }
    }

    private fun changeButton() {
        edtName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeybroad(v)
            }
        }

        edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (count > 0) {
                    setBackgroundButton()
                } else {
                    btnCreate.setBackgroundResource(R.drawable.shape_gray_bg_corner_20)
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    setBackgroundButton()
                } else {
                    btnCreate.setBackgroundResource(R.drawable.shape_gray_bg_corner_20)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                setBackgroundButton()

            }
        })
    }

    private fun getUriBaby() {
        ivAvatar.setOnClickListener {
            val intent = Intent(this, PhotoFolderActivity::class.java)
            activityResultLauncher.launch(intent)
        }
        ivBackHome.setOnClickListener {
            createAlbumPresenter.openBackDialog(this)
        }
    }


    private fun getGenderAlbum(): Int {
        ivBoy.setOnClickListener {
            ivBoy.setBackgroundResource(R.drawable.shape_cir_yellow_bg_corner_large)
            ivGirl.setBackgroundResource(R.drawable.shape_cir_grey_bg_corner_90)
            select = 1
        }
        ivGirl.setOnClickListener {
            ivGirl.setBackgroundResource(R.drawable.shape_cir_yellow_bg_corner_large)
            ivBoy.setBackgroundResource(R.drawable.shape_cir_grey_bg_corner_90)
            select = 0
        }
        return select
    }

    private fun getRelationAlbum() {
        flRelation.setOnClickListener {
            val dialogRelationFragment = DialogRelationFragment()
            dialogRelationFragment.show(this.supportFragmentManager, dialogRelationFragment.tag)
        }
    }


    private fun sendSingleImage(pathImage: String) {
        file = File(pathImage)
        file_path = file.absolutePath + System.currentTimeMillis() + ".png"
        requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        singleFile = MultipartBody.Part.createFormData("file", file_path, requestBody)


        val name = edtName.text.toString()
        val gender = getGenderAlbum()
        strGender = if (gender == 1) {
            "1"
        } else {
            "0"
        }
        val birthday: String = tvBirthday.text.toString()
        val relation: String = tvRelation.text.toString()

        rqIdaccount =
            RequestBody.create(MediaType.parse("multipart/form-data"), ID_ACCOUNT.toString())
        rqName = RequestBody.create(MediaType.parse("multipart/form-data"), name)
        rqGender = RequestBody.create(MediaType.parse("multipart/form-data"), strGender)
        rqBirthday = RequestBody.create(MediaType.parse("multipart/form-data"), birthday)
        rqRelation = RequestBody.create(MediaType.parse("multipart/form-data"), relation)
    }


    private fun getRealPathFromUri(contentUri: Uri): String {
        var projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
        )

        val cursor = this.contentResolver.query(
            contentUri, projection, null, null, null
        )
        if (cursor!!.moveToFirst()) {
            path = cursor.getString(0)
        }
        cursor.close()
        return path
    }

    //convert bitmap to uri
    private fun convertUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            this.getContentResolver(),
            bitmap,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun hideKeybroad(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun getName(name: String) {
        tvRelation.text = name
        setBackgroundButton()
    }

    fun setBackgroundButton() {
        if (bitmapAvatar && edtName.text.toString() != "" && tvBirthday.text != "" && tvRelation.text != "")
            btnCreate.setBackgroundResource(R.drawable.shape_orange_bg_corner_20)
    }

    override fun onResult(data: DataResult<String>) {
        when (data.state) {
            DataResult.State.SUCCESS -> {
                Toast.makeText(applicationContext, data.data, Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, HomeActivity::class.java)
                intent.putExtra("idaccount", ID_ACCOUNT)
                startActivity(intent)
            }
            DataResult.State.FAIL -> {
                Toast.makeText(applicationContext, data.data, Toast.LENGTH_SHORT).show()
            }
            DataResult.State.ERROR -> {
                Toast.makeText(applicationContext, data.data, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getData(time: String) {
        tvBirthday.text = time
        if (tvBirthday.text != "") {
            setBackgroundButton()
        }
    }


    override fun showLoading() {
        dialog?.setContentView(R.layout.dialog_loading_album_layout)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.show()
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }

    override fun onBackPressed() {
        createAlbumPresenter.openBackDialog(this)
    }
}
