package com.example.xyzhang.testapp.util;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Created by flower on 18-3-1.
 */

public abstract class UploadFileAsync extends AsyncTask<String, Double, String> {
    private String sourceFileUri;
    private String fontName;

    public UploadFileAsync(String sourceFileUri, String fontName) {
        this.sourceFileUri = sourceFileUri;
        this.fontName = fontName;
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            HttpURLConnection conn;
            DataOutputStream dos;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;


            try {
                String upLoadServerUri = "http://10.0.2.2:8080/get_file.php?";

                // open a URL connection to the Servlet
                URL url = new URL(upLoadServerUri);

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE",
                        "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("Cookie", SessionID.getInstance().getId());

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"font_name\"" + lineEnd + lineEnd);
                dos.writeBytes(fontName + lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"pic_name\"" + lineEnd + lineEnd);

                JSONArray array = new JSONArray(Arrays.asList(params));
                dos.writeBytes(array.toString() + lineEnd);
//                    dos.writeBytes(picName + lineEnd);

                for (int i = 0; i < params.length; i++) {
                    File sourceFile = new File(sourceFileUri + "/" + params[i]);
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes(
                            MessageFormat.format("Content-Disposition: form-data; name=\"upload{0}\";filename=\"{1}\"{2}",
                                    i, sourceFileUri, lineEnd));

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }
                    fileInputStream.close();

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    publishProgress((double) i / params.length);
                }
                dos.writeBytes(twoHyphens + boundary + twoHyphens
                        + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn
                        .getResponseMessage();

                if (serverResponseCode == 200) {

                    // messageText.setText(msg);
                    //Toast.makeText(ctx, "File Upload Complete.",
                    //      Toast.LENGTH_SHORT).show();

                    // recursiveDelete(mDirectory1);

                }

                // close the streams //
                dos.flush();
                dos.close();

            } catch (Exception e) {

                // dialog.dismiss();
                e.printStackTrace();

            }
            // dialog.dismiss();


        } catch (Exception ex) {
            // dialog.dismiss();

            ex.printStackTrace();
        }
        return "Executed";
    }

    @Override
    protected abstract void onPostExecute(String result);

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected abstract void onProgressUpdate(Double... values);
}