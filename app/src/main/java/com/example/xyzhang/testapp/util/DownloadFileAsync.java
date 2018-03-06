package com.example.xyzhang.testapp.util;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFileAsync extends AsyncTask<String, Double, String> {

    public DownloadFileAsync(UpdateTask task) {
        this.task = task;
    }

    public interface UpdateTask {
        void onProgressUpdate(double progress);
        void onPostExecute(String message);
        void onPreExecute();
    }

    private UpdateTask task;

    @Override
    protected String doInBackground(String... params) {
        int count;
        try {
            URL url = new URL("http://10.0.2.2:8080/download_font.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Cookie", SessionID.getInstance().getId());

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes("font_name=" + params[0]);

            // this will be useful so that you can show a typical 0-100%
            // progress bar
            int lengthOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(params[1]);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress((double) total / lengthOfFile);

                // writing data to file
                output.write(data, 0, count);
            }

            dos.flush();
            dos.close();

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        task.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        task.onProgressUpdate(values[0]);
    }

    @Override
    protected void onPreExecute() {
        task.onPreExecute();
    }
}
