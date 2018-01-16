package hu.d2.offsitesr.ui.view.component;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetails;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetailsActivity;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;

import static android.R.attr.data;

/**
 * Created by szidonia.laszlo on 2017. 11. 10..
<<<<<<< HEAD
 *
 *   - add picture name + upload
=======
>>>>>>> 4076969d3dd138ea01ac183824c5eb0fe2987670
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

    Bitmap imageBitmap ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageBitmap = (Bitmap) getArguments().get("data");

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.dialog_save_picture, container, false);
        ButterKnife.bind(this, contentView);


        String timeStamp = EnvironmentTool.convertDate(new Date());
        pictureView.setImageBitmap(imageBitmap);

        uploadButton.setOnClickListener((v -> {
            String pictureName = picName.getText().toString();
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ UIConstans.PHOTO_SAVE_DIR);
            String path="", encodedImage="";

            try {
                if (pictureName.equals("")){

                    pictureName = timeStamp;
                    path = createImageFile(timeStamp,dir).getAbsolutePath();
                }else
                {
                    path = createImageFile(pictureName,dir).getAbsolutePath();
                }

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(path);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] byteFormat = baos.toByteArray();
                encodedImage = Base64.encodeToString(byteFormat,Base64.DEFAULT);

                FileOutputStream fo = new FileOutputStream(file);
                fo.write(baos.toByteArray());
                fo.flush();
                fo.close();

                ((TicketDetails)getActivity()).addFile(file.getName(), pictureName,encodedImage,path);

            } catch (IOException e) {
                e.printStackTrace();
            }

            dismiss();
        }));

        cancelButton.setOnClickListener((view -> {
            dismiss();
        }));

        title.setText(getString(R.string.dialogSavePicture_title));
        return contentView;
    }

    private File createImageFile(String nameFile,File directory) throws IOException {
        File imageFile = new File(directory,nameFile+".jpg");
        return imageFile;
    }


}
