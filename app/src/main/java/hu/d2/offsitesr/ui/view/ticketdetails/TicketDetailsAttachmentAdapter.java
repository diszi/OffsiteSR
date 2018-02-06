package hu.d2.offsitesr.ui.view.ticketdetails;



import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.Attachment;
import hu.d2.offsitesr.ui.model.DocLinks;
import hu.d2.offsitesr.util.FileUtils;
import hu.d2.offsitesr.util.UIConstans;


/**
 * Created by szidonia.laszlo on 2017. 11. 27..
 *
 */

public class TicketDetailsAttachmentAdapter extends RecyclerView.Adapter<TicketDetailsAttachmentAdapter.AttachmentViewHolder> {

    private List<Attachment> attachmentlist = new ArrayList<>();
    private List<DocLinks> attachmentDocLinksList = new ArrayList<>();
    private TicketDetailsAttachmentTab ticketDetailsAttachmentTab;

    ProgressDialog mprogressDialog;


    public TicketDetailsAttachmentAdapter(TicketDetailsAttachmentTab ticketDetailsAttachmentTab) {
        this.ticketDetailsAttachmentTab = ticketDetailsAttachmentTab;
    }

    /**
     * @param attachments - attachment list with refresh
     * Refresh the attachment page
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachmentlist.clear();
        this.attachmentlist.addAll(attachments);
        this.notifyDataSetChanged();
    }


    public void setAttachmentDetails(List<DocLinks> attachmentsDocLinksList){
        Log.d("------------------>","AttachmentDocLinks list");

        int count=0;
        this.attachmentDocLinksList = attachmentsDocLinksList;

        if (this.attachmentDocLinksList != null && this.attachmentDocLinksList.size()!=0) {
            while (this.attachmentDocLinksList.get(count).getDocumentData() == null) {
                count++;
            }
            DocLinks attachmentDocObj = this.attachmentDocLinksList.get(count);
//            System.out.println(" ---> ADAPTER = "+attachmentDocObj.getWebURL()+" -- count =  "+count);

            // Call file downloader
            DownloadTask downloadTask = new DownloadTask(ticketDetailsAttachmentTab.getContext());
            downloadTask.execute(attachmentDocObj.getDocumentData(), attachmentDocObj.getWebURL());
        }
        this.notifyDataSetChanged();
    }

    @Override
    public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_ticket_details_attachment_row,
                parent, false);
        return new TicketDetailsAttachmentAdapter.AttachmentViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(AttachmentViewHolder holder, int position) {

        Attachment attachment = attachmentlist.get(position);
        holder.bind(attachment);
        holder.btnDownloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ticketDetailsAttachmentTab.setOnClickDownloadButton(attachment.getDoclinksID());
         }
        });

    }


    /**
     * @return - the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        if (attachmentlist != null && attachmentlist.size() > 0) {
            return attachmentlist.size();
        } else {
            return 0;
        }
    }





    public class AttachmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.actDetailsAttachment_createby)
        TextView compCreateBy;
        @BindView(R.id.actDetailsAttachment_createDate)
        TextView compCreateDate;
        @BindView(R.id.actDetailsAttachment_description)
        TextView compDescription;
        @BindView(R.id.actDetails_downloadButton)
        ImageButton btnDownloadFile;


        public AttachmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Attachment attachment) {
            compDescription.setText(attachment.getDescription());
            compCreateBy.setText(attachment.getCreateBy());
            compCreateDate.setText(attachment.getCreateDate());
        }

    }



    /**
     * AsyncTask enables proper and easy use of the UI thread.
     * An asynchronous task is defined by 3 generic types,
     * called Params = String, Progress = Integer and Result = String.
     *
     * This class extends of AsyncTask and download attachments.
     */
    class DownloadTask extends AsyncTask<String,Integer,String> {


        String downloaded_file_name =null, externalDir=null;
        File downloaded_filePath=null;

        private Context context;

        public DownloadTask(Context acticityContext){
                this.context = acticityContext;

        }

        /**
         * Runs on the UI thread before doInBackground(Params...).
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mprogressDialog = new ProgressDialog(context);
            mprogressDialog.setTitle(R.string.actAttachment_downloadTitle);
            mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mprogressDialog.show();

        }




        /**
         * This method can call publishProgress(Progress...) to publish updates on the UI thread.
         * @param params - params[0] = base64 code for downloaded file, params[1] = webUrl (parameters of the task)
         * @return - a result, defined by the subclass of this task.
         * Downloaded file place : download folder
         */
        @Override
        protected String doInBackground(String... params) {
            String base64Code = params[0];
            String webUrl = params[1];
            downloaded_file_name = webUrl.substring(webUrl.lastIndexOf('/')+1, webUrl.length());
            externalDir = Environment.getExternalStorageDirectory().toString()+ UIConstans.FILE_SAVE_DIR;
            downloaded_filePath = new File(externalDir,downloaded_file_name);

            byte[] fileAsBytes = Base64.decode(base64Code,0);

            try {
                FileOutputStream fos = new FileOutputStream(downloaded_filePath,true);
                fos.write(fileAsBytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ticketDetailsAttachmentTab.getActivity().getString(R.string.actAttachment_downloadCompleted);

        }


        /**
         * Runs on the UI thread after publishProgress(Progress...) is invoked.
         * @param values - The values indicating progress.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mprogressDialog.setIndeterminate(false);
            mprogressDialog.setMax(100);
            mprogressDialog.setProgress(values[0]);

        }

        /*
        *   Runs on the UI thread after doInBackground(Params...).
        *   Download is over : display a Snackbar with message: SHOW FILE
        *   OnClick on message : will be opened the file in correct reader/format
        * */
        @Override
        protected void onPostExecute(String result) {
            mprogressDialog.dismiss();
            Snackbar snack;
            snack = Snackbar.make(ticketDetailsAttachmentTab.getView(), result, Snackbar.LENGTH_INDEFINITE).setAction(ticketDetailsAttachmentTab.getActivity().getString(R.string.actAttachment_showFile), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String result="*/*";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri apkURI = FileProvider.getUriForFile(ticketDetailsAttachmentTab.getContext(),ticketDetailsAttachmentTab.getContext().getApplicationContext().getPackageName()+".provider",downloaded_filePath);

                    switch(FileUtils.getExtension(downloaded_file_name)){
                        case ".pdf":
                            result = "application/pdf";
                            break;
                        case ".jpg":
                        case ".jpeg":
                            result = "image/*";
                            break;
                        case ".png":
                            result = "image/png";
                            break;
                        case ".xls":
                        case ".xlsx":
                            result = "application/vnd.ms-excel";
                            break;
                        case ".doc":
                        case ".docx":
                            result  = "application/msword";
                            break;
                        case ".ppt":
                        case "pptx":
                            result = "application/vnd.ms-powerpoint";
                            break;
                        case ".zip":
                        case ".rar":
                            result = "application/x-wav";
                            break;
                        case ".gif":
                            result = "image/gif";
                            break;
                        case ".txt":
                            result = "text/plain";
                            break;
                    }
                    intent.setDataAndType(apkURI,result);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ticketDetailsAttachmentTab.getContext().startActivity(intent);
                }
            });
            snack.show();
        }
    }

}
