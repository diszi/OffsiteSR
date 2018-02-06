package hu.d2.offsitesr.ui.view.component;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetails;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;


/**
 * Created by szidonia.laszlo on 2017. 11. 10..
 *
 */

public class SavePictureDialog extends DialogFragment {

    @BindView(R.id.diagSavePicture_uploadButton)
    Button uploadButton;
    @BindView(R.id.diagSavePicture_cancelButton)
    Button cancelButton;
    @BindView(R.id.diagSavePicture_pictureName)
    EditText picName;
    @BindView(R.id.diagSavePicture_pictureView)
    ImageView pictureView;
    @BindView(R.id.diagSavePicture_title)
    TextView title;


    public String photoPath;
    public File file;
    public Uri imageUri;


    public static SavePictureDialog newInstance(String path){
       SavePictureDialog savePictureDialog = new SavePictureDialog();
        Bundle args = new Bundle();
        args.putString("currentPhotoPath",path);
        savePictureDialog.setArguments(args);
        return savePictureDialog;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoPath = getArguments().getString("currentPhotoPath");

    }


    /**
     * Gives the dialog content
     * @param inflater - The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container - this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState - his fragment is being re-constructed from a previous saved state as given here
     * @return - the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.dialog_save_picture, container, false);
        ButterKnife.bind(this, contentView);

        imageUri = Uri.parse(photoPath);
        file = new File(imageUri.getPath());
        pictureView.setImageURI(imageUri);

        MediaScannerConnection.scanFile(getActivity(),
                new String[]{imageUri.getPath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });


        uploadButton.setOnClickListener((v -> {
            String pictureName = picName.getText().toString();

            String path="";
            String encodedImage="";

                if (pictureName.equals("")) {
                    pictureName = file.getName();
                    path = photoPath.substring(0,photoPath.lastIndexOf('/') + 1)+pictureName;
                }else{
                    path = photoPath.substring(0,photoPath.lastIndexOf('/') + 1)+pictureName+UIConstans.IMAGE_EXTENSION;
                    pictureName = pictureName+UIConstans.IMAGE_EXTENSION;
                }

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(imageUri);

                Bitmap bm = BitmapFactory.decodeFile(photoPath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG,100,baos);

                byte[] byteArrayImage = baos.toByteArray();
                encodedImage = Base64.encodeToString(byteArrayImage,Base64.DEFAULT);

                try {
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(baos.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ((TicketDetails)getActivity()).addFile(file.getName(), pictureName,encodedImage,path);


            dismiss();
        }));

        cancelButton.setOnClickListener((view -> {
            dismiss();
        }));

        title.setText(getString(R.string.dialogSavePicture_title));
        return contentView;
    }



}
