package hu.d2.offsitesr.ui.view.ticketdetails;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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


    public TicketDetailsAttachmentAdapter(TicketDetailsAttachmentTab ticketDetailsAttachmentTab) {
        this.ticketDetailsAttachmentTab = ticketDetailsAttachmentTab;
    }


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

            // Call file downloader
            DownloadTask downloadTask = new DownloadTask();
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
            compDescription.setText(attachment.getFileName());
            compCreateBy.setText(attachment.getCreateBy());
            compCreateDate.setText(attachment.getCreateDate());
        }

    }


    /**
     *  Download attachment
     */
    class DownloadTask extends AsyncTask<String,Integer,String> {

        ProgressDialog mprogressDialog;
        String downloaded_file_name =null, externalDir=null;
        File downloaded_filePath=null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mprogressDialog = new ProgressDialog(ticketDetailsAttachmentTab.getActivity());
            mprogressDialog.setTitle(R.string.actAttachment_downloadTitle);
            mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mprogressDialog.show();

        }

        /*
        *   Downloaded file place: Download folder
        * */
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


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mprogressDialog.setIndeterminate(false);
            mprogressDialog.setMax(100);
            mprogressDialog.setProgress(values[0]);

        }

        /*
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
