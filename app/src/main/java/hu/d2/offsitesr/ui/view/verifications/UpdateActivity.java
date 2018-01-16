package hu.d2.offsitesr.ui.view.verifications;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.SupportActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import hu.d2.offsitesr.BuildConfig;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.OffsiteSRApplication;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.ui.model.Version;
import hu.d2.offsitesr.ui.view.component.UpdateAppDialog;
import hu.d2.offsitesr.ui.view.login.LoginActivity;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListActivity;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;

/**
 * Created by szidonia.laszlo on 2017. 12. 08..
 */

public class UpdateActivity extends AppCompatActivity{


    ProgressDialog bar;
    private VerificationPresenter presenter;

    private String newAppVersion ;
    private  UpdateAppDialog updateDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new VerificationPresenterImpl();
        presenter.setUpdateView(this);
        updateDialog = new UpdateAppDialog();
        presenter.getUpdateVersion(EnvironmentTool.getAppName());


   }


    public void verificUpdateInformations(Version updateVersionObj) throws ParseException {

        if (updateVersionObj.getAppName() == null){ //ha nincs frissites

            Intent intent = new Intent(this, TicketListActivity.class);
            startActivity(intent);
        }
        else{
            //ha van aktualis frissites

            if (compareVersionNames(EnvironmentTool.getVersionApp(),updateVersionObj.getVersionNumber()) == -1){ //verziok hasonlitasa => act verzio < new verzio
                // ES HA FRISSITESI VERZIO > AKTUALIS VERZIO => JON A DIALOG

                //AKTUALIS DATUM
                Date today = new Date();
                //System.out.println("actualDate = "+today);
                System.out.println(" akt.verzio < GET verzio => van frissites");
                //KET DATUM HASONLITASA

                long difference = getDifferenceBetween2Date(SettingsSingleton.getInstance().getDate(),EnvironmentTool.dateInString(today));

                if (difference > 1 || difference == -1) {

                    newAppVersion = updateVersionObj.getVersionNumber();
                    android.app.FragmentManager fm = getFragmentManager();
                    UpdateAppDialog updateDialog = new UpdateAppDialog();

                    updateDialog.show(fm, "update");

                }else
                {

                    Intent intent = new Intent(this, TicketListActivity.class);
                    startActivity(intent);
                }

            }else
            {  //akt verzio >= new verzio

                System.out.println(" akt. verzio > GET verzio => nincs frissites");
                Intent intent = new Intent(this, TicketListActivity.class);
                startActivity(intent);
            }

        }
    }


    public long getDifferenceBetween2Date(String pressedDate, String  today) throws ParseException {
        System.out.println("pressedDate = "+pressedDate+"  today = "+today);
        long diffDays;
        DateFormat outFormat  = new SimpleDateFormat("yyyy.MM.d. hh:mm");
        Date before, today2;

        if (pressedDate == null){
            diffDays = -1;
        }else{

            before = outFormat.parse(pressedDate);
            today2 = outFormat.parse(today);

            DateTime beforeDateTime = new DateTime(before);
            DateTime todayDateTime = new DateTime(today2);

            diffDays = Days.daysBetween(beforeDateTime,todayDateTime).getDays();
            System.out.println("beforeDateTime = "+beforeDateTime+"   todayDateTime = "+todayDateTime+"   diffDays = "+diffDays);

        }
        return diffDays;
    }



    public int compareVersionNames(String actualVersionName, String newVersionName){
        int result = 0;

        String[] actualNr = actualVersionName.split("\\.");
        String[] newNr = newVersionName.split("\\.");

        int maxIndex = Math.min(actualNr.length,newNr.length);

        for (int i = 0; i < maxIndex; i++){
            int actualVersionPart = Integer.valueOf(actualNr[i]);
            int newVersionPart = Integer.valueOf(newNr[i]);

            if (actualVersionPart < newVersionPart){
                result = -1;
                break;
            }else
                if (actualVersionPart > newVersionPart){
                    result = 1;
                    break;
                }
        }

        if (result == 0 && actualNr.length != newNr.length){
            result = (actualNr.length > newNr.length)?1:-1;
        }
        return result;
    }


    public void downloadNewApp(){
        presenter.getNewApp(EnvironmentTool.getAppName(),newAppVersion);
    }

    public void downloadNewAppAttachment(Version versionAttachment) {
        new DownloadNewVersion().execute(versionAttachment.getAppName(),versionAttachment.getNewAppDetails().get(0).getDocumentData()); //appname + documentdata

    }


    class  DownloadNewVersion extends AsyncTask<String,Integer,Boolean>{




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = new ProgressDialog(UpdateActivity.this);
            bar.setCancelable(false);

            bar.setMessage(getApplicationContext().getString(R.string.download_msg));

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

                msg=getApplicationContext().getString(R.string.finish_msg);

            }else {

                msg=getApplicationContext().getString(R.string.download_msg)+values[0]+"%";
            }
            bar.setMessage(msg);

        }


        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);
            bar.dismiss();
            if(result){
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.download_resultMessageOk),
                        Toast.LENGTH_SHORT).show();



            }else{
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.download_resultMessageError),
                        Toast.LENGTH_SHORT).show();

            }
      }


        @Override
        protected Boolean doInBackground(String... params) {
            Boolean flag = false;
            String fileName = params[0];
            String codeBase64 = params[1];

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

        public void openNewVersion(String location, String fileName)  {
            Intent intentInstall;

            File newFile = new File(location,fileName+".apk");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                intentInstall = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                Uri apkUri = FileProvider.getUriForFile(UpdateActivity.this,getApplicationContext().getPackageName()+".provider",newFile);

                System.out.println("apkUri ="+apkUri);
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //telepito ablak
                intentInstall.setDataAndType(apkUri,"application/vnd.android.package-archive");

            }else{

                intentInstall = new Intent(Intent.ACTION_VIEW);
                intentInstall.setDataAndType(Uri.fromFile(new File(location , fileName+".apk")),
                        "application/vnd.android.package-archive");
                intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            }

            startActivity(intentInstall);
            //System.out.println("Intent.getAction = "+intentInstall.getAction()+"  data = "+intentInstall.getData());

        }

    }

}
