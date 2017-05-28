package com.witlife.timesheet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.witlife.timesheet.R;
import com.witlife.timesheet.model.UserProfileModel;
import com.witlife.timesheet.util.SPUtil;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity {

    EditText editLastName;
    EditText editFirstName;
    EditText editEmail;
    EditText editEmployeeNo;
    EditText editRole;
    UserProfileModel userModel;
    MenuItem btnSave;
    MenuItem btnDelete;
    LinearLayout layoutProfileImage;
    CircleImageView ivProfilePhoto;
    private int PICK_IMAGE_REQUEST = 68;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        bindView();
        initialView();
        setTextChange();
    }

    private void bindView(){
        editLastName = (EditText) findViewById(R.id.editLastName);
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editEmployeeNo = (EditText) findViewById(R.id.editEmployeeNo);
        editRole = (EditText) findViewById(R.id.editRole);
        layoutProfileImage = (LinearLayout) findViewById(R.id.layoutProfileImage);
        ivProfilePhoto = (CircleImageView) findViewById(R.id.ivProfilePhoto);
    }

    private void initialView(){
        userModel = SPUtil.readUserModel(this);
        if (userModel != null) {
            editLastName.setText(userModel.getLastName());
            editFirstName.setText(userModel.getFirstName());
            editEmail.setText(userModel.getEmail());
            editEmployeeNo.setText(userModel.getEmployeeNo());
            editRole.setText(userModel.getRole());
            if (userModel.getImageString() != null){

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            Uri.parse(userModel.getImageString()));

                    ivProfilePhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        initialAppBar("Edit Profile");

        layoutProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(true);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Photo"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void initialAppBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        btnSave = menu.findItem(R.id.action_save);
        btnSave.setEnabled(false);
        btnDelete = menu.findItem(R.id.action_delete);
        btnDelete.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.action_save:
                saveToModel();
                SPUtil.writeUserModel(this, userModel);
                finish();
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                CircleImageView imageView = (CircleImageView) findViewById(R.id.ivProfilePhoto);
                imageView.setImageBitmap(bitmap);
                userModel.setImageString(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToModel(){
        if(!TextUtils.isEmpty(editFirstName.getText()))
        {
            userModel.setFirstName(editFirstName.getText().toString());
        }

        if(!TextUtils.isEmpty(editLastName.getText()))
        {
            userModel.setLastName(editLastName.getText().toString());
        }
        if(!TextUtils.isEmpty(editEmail.getText()))
        {
            userModel.setEmail(editEmail.getText().toString());
        }
        if(!TextUtils.isEmpty(editEmployeeNo.getText()))
        {
            userModel.setEmployeeNo(editEmployeeNo.getText().toString());
        }
        if(!TextUtils.isEmpty(editRole.getText()))
        {
            userModel.setRole(editRole.getText().toString());
        }
    }

    private void setTextChange() {
        editFirstName.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        editLastName.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        editEmail.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        editRole.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        editEmployeeNo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }
}
