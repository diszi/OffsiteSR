package hu.d2.offsitesr.ui.view.verifications;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.util.UIConstans;

/**
 * Created by szidonia.laszlo on 2018. 01. 19..
 *
 *      - download new app (application update)
 */

public class DownloadUpdate  extends AsyncTask<String,Integer,Boolean> {

    ProgressDialog bar;
    public Activity activity ;

    public DownloadUpdate(Activity activity){
        super();
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        bar = new ProgressDialog(activity);
        bar.setCancelable(false);
        bar.setMessage(activity.getApplicationContext().getString(R.string.download_msg));
        bar.setIndeterminate(true);
        bar.setCanceledOnTouchOutside(false);
        bar.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        bar.setIndeterminate(false);
        bar.setMax(100);
        bar.setProgress(values[0]);
        String msg = "";
        if(values[0]>99){
            msg=activity.getApplicationContext().getString(R.string.finish_msg);
        }else {
            msg=activity.getApplicationContext().getString(R.string.download_msg)+values[0]+"%";
        }
        bar.setMessage(msg);

    }


    @Override
    protected void onPostExecute(Boolean result){
        super.onPostExecute(result);
        bar.dismiss();
        if(result){
            Toast.makeText(activity.getApplicationContext(),activity.getApplicationContext().getString(R.string.download_resultMessageOk),
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(activity.getApplicationContext(),activity.getApplicationContext().getString(R.string.download_resultMessageError),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected Boolean doInBackground(String... params) {
        Boolean flag = false;
        String fileName =  params[0];
        String codeBase64 =  params[1];

        String directory = Environment.getExternalStorageDirectory().toString()+ UIConstans.FILE_SAVE_DIR;
        File outputFile = new File(directory,fileName+".apk");

        if (outputFile.exists()){
            outputFile.delete();
        }

        byte[] fileAsBytes = Base64.decode(codeBase64,0);

        try {
            FileOutputStream fos = new FileOutputStream(outputFile,true);
            fos.write(fileAsBytes);
            fos.flush();
            fos.close();

            openNewVersion(directory,fileName);
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     *
     * @param location = directory where the app APK is located
     * @param fileName = apk name
     *
     *  - install APK
     */
    public void openNewVersion(String location, String fileName)  {
        Intent intentInstall;
        Uri apkUri = null;
        File newFile = new File(location,fileName+".apk");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            intentInstall = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            apkUri = FileProvider.getUriForFile(activity,activity.getApplicationContext().getPackageName()+".provider",newFile);
            intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentInstall.setDataAndType(apkUri,"application/vnd.android.package-archive");
        }else{
            intentInstall = new Intent(Intent.ACTION_VIEW);
            intentInstall.setDataAndType(Uri.fromFile(new File(location , fileName+".apk")),
                    "application/vnd.android.package-archive");
            intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intentInstall);
    }

}
