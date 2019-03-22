package com.santos.dev.UI.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.santos.dev.R;
import com.santos.firebasecomponents.FirebaseMethods;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

import static com.santos.dev.MainActivity.KEY_NOTAS;
import static com.santos.firebasecomponents.Nodos.IMAGENES_FOLDER;
import static com.santos.firebasecomponents.Nodos.NODO_CURSOS;
import static com.santos.firebasecomponents.Nodos.NODO_NOTAS;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FOTO1 = "https://firebasestorage.googleapis.com/v0/b/trigonometria-1c5cb.appspot.com/o/Imagenes%2Fnoimage.png?alt=media&token=1e1df63e-25ef-42b0-8520-3cd0992799c3";
    private static final int GalleriaPick = 1;
    public static final int CAMERA_COD = 502;

    //FirebaseMethods
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private StorageReference mStorageReference;

    private String url_imagen;
    public static String id_curso;
    private Uri mImageUri;
    private Bitmap thumb_bitmap = null;

    //Widgets
    private EditText mEditTextTitulo;
    private EditText mEditTextDescripcion;
    private Button mButtonSubir;
    private ImageButton mImageButtonImagen;
    private Dialog mChoosingDialog;
    private ImageView ivbChooseClose;
    private LinearLayout llCamera, llGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference(IMAGENES_FOLDER);
        getInitComponentes();

        Intent i = getIntent();
        id_curso = i.getStringExtra(KEY_NOTAS);
        firebaseMethods = new FirebaseMethods(this, NODO_CURSOS, id_curso, NODO_NOTAS);

        Toolbar toolbar = findViewById(R.id.toolbar_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nueva Nota");

        toolbar.setNavigationOnClickListener(v -> finish());

    }

    private void getInitComponentes() {
        mImageButtonImagen = findViewById(R.id.select_post_image);
        mEditTextTitulo = findViewById(R.id.click_post_tittle);
        mEditTextDescripcion = findViewById(R.id.click_post_description);
        mButtonSubir = findViewById(R.id.update_post_button);
        mChoosingDialog = new Dialog(OptionsActivity.this, R.style.AppTheme);
        mChoosingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        mChoosingDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        mChoosingDialog.getWindow().setGravity(Gravity.BOTTOM);
        mChoosingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mChoosingDialog.setCancelable(true);
        mChoosingDialog.setContentView(R.layout.dialog_choose_cameragallery);

        ivbChooseClose = (ImageView) mChoosingDialog.findViewById(R.id.ivbChooseClose);
        llCamera = (LinearLayout) mChoosingDialog.findViewById(R.id.llCamera);
        llGallery = (LinearLayout) mChoosingDialog.findViewById(R.id.llGallery);

        llCamera.setOnClickListener(this);
        llGallery.setOnClickListener(this);
        ivbChooseClose.setOnClickListener(this);
        mImageButtonImagen.setOnClickListener(this);
        mButtonSubir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_post_image:
                mChoosingDialog.show();
                break;
            case R.id.update_post_button:
                String titulo = mEditTextTitulo.getText().toString();
                String descripcion = mEditTextDescripcion.getText().toString();
                String otra = "hola";

                if (checkInputs(titulo, descripcion, otra)) {

                    crearNuevoAlumno(titulo, descripcion, otra);

                }
                break;
            case R.id.ivbChooseClose:
                mChoosingDialog.dismiss();
                break;
            case R.id.llCamera:
                mChoosingDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {
                        openCamera();
                        mChoosingDialog.dismiss();
                    }
                } else {
                    openCamera();
                    mChoosingDialog.dismiss();
                }
                break;
            case R.id.llGallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(OptionsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                        mChoosingDialog.dismiss();
                    } else {
                        ActivityCompat.requestPermissions(OptionsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 401);
                        mChoosingDialog.dismiss();
                    }
                } else {
                    openGallery();
                    mChoosingDialog.dismiss();
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 402);
            return false;
        }
        return true;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GalleriaPick);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_COD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GalleriaPick && resultCode == RESULT_OK && data != null) {
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


//            CropImage.activity(selectedURI).start(this);

        } else if (requestCode == CAMERA_COD && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");

            Uri selectedURI = getImageUri(mphoto);

            CropImage.activity(selectedURI)
                    .start(this);

            /*if(data.getData()==null){
                bitmap = (Bitmap)data.getExtras().get("data");
            }else{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            }
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);*/

//            ImageLoad.onBitmapLoadCirlce(DrawerActivity.this, mphoto, civProfilePic);
        } /*else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri selectedURI = result.getUri();

                try {
                    Bitmap bitmap = convert_UriToBitmap(selectedURI);
                    ivImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_close_black_24dp)
                        .error(R.drawable.ic_close_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH);

                Glide.with(this)
                        .load(mImageUri)
                        .apply(options)
                        .into(mImageButtonImagen);


                File thumb_file_path = new File(mImageUri.getPath());
                thumb_file_path.getAbsolutePath();

                try {
                    thumb_bitmap = new Compressor(this)
                            .setQuality(75)
                            .compressToBitmap(thumb_file_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                Task<Uri> uriTask = fileReference.putBytes(thumb_byte).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        url_imagen = downloadUri.toString();
                    } else {
                        // Handle failures
                        // ...
                    }
                });

                if (uriTask != null) {
                    Toast.makeText(this, "Imagen Subida", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }*/
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 401) {
            if (grantResults.length == 0 || grantResults == null) {
//                Logger.e(TAG, "Null Every thing");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }
        } else if (requestCode == 402) {
            if (grantResults.length == 0 || grantResults == null) {
//                Logger.e(TAG, "Null Every thing");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (bitmap != null) {
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            return Uri.parse(path);
        } else {
            return Uri.parse("");
        }
    }

    @Override
    public void onBackPressed() {
        if (mChoosingDialog.isShowing()) {
            mChoosingDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mChoosingDialog.isShowing()) {
            mChoosingDialog.dismiss();
        }
        super.onDestroy();
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy  HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private boolean checkInputs(String nombres, String apellidos, String edad) {
        if (nombres.equals("") || apellidos.equals("") || edad.equals("")) {
            Toast.makeText(this, "Todos los compos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void crearNuevoAlumno(String nombre, String apellidos, String edad) {
        //firebaseMethods.registrarNuevoEmail(correo,"123456789");

        final StorageReference fileReference = mStorageReference.child(firebaseUser.getUid() + getDate() + ".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);

        final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

        Task<Uri> uriTask = fileReference.putBytes(thumb_byte).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return fileReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                url_imagen = downloadUri.toString();

                if (url_imagen == null)
                    url_imagen = FOTO1;

                firebaseMethods.nuevaNota(
                        nombre,
                        apellidos,
                        edad,
                        firebaseUser.getDisplayName(),
                        firebaseUser.getPhotoUrl().toString(),
                        firebaseUser.getEmail(),
                        url_imagen,
                        firebaseUser.getUid(),
                        id_curso);

                finish();
            }
        });


    }
}
