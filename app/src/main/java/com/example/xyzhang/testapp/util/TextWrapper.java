package com.example.xyzhang.testapp.util;

/**
 * Created by flower on 03/03/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;

import java.io.IOException;


public class TextWrapper extends AsyncTask<Context, Double, Integer> {
    //    private static char[] END_CHARS = {'s'};
    private static final String END_CHARS = "，。》、？；：’”】｝、！％）" + ",.>?;:]}!%)";
    private UpdateTask updateTask;
    private final int screenWidth;
    private final int screenHeight;
    private final String fontPath;
    private String time;
    private String text;

    public interface UpdateTask {
        void onProgressUpdate(double progress);
        void onFinish(int x);
    }

    public TextWrapper(UpdateTask updateTask, int screenWidth, int screenHeight, String fontPath, String time, String text) {
        this.updateTask = updateTask;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.fontPath = fontPath;
        this.time = time;
        this.text = text;
    }

    private int draw(int screenWidth, int screenHeight, String fontPath, String text, Context context) throws IOException {

        char[] textChars = text.toCharArray();

//        String[] paras = str.split("\n"); //breaking the lines into an array


        Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Typeface typeface = Typeface.createFromFile(fontPath);

        float xPos = screenWidth * 0.05f;
        float yPos = screenHeight * 0.1f;
        float lineWidth = screenWidth * 0.9f;

        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(lineWidth / 20);
        paint.setColor(Color.BLACK);

        Paint.FontMetrics metrics = paint.getFontMetrics();

        float lineHeight = metrics.bottom - metrics.top + metrics.leading;//计算行高


        int linesEveryPage = (int) (screenHeight * 0.8 / lineHeight);

        int lineIndex = 1;//表示要绘制的行号
        int total = text.length();
        int offset = 0;//已经绘制的字符个数

//        float lineWidth = 20 * paint.measureText("我");//每一行的标准宽度
//        float xPos = (screenWidth - lineWidth) / 2;//每一行的左边距


        int nextCharIndex = 0;//当前要绘制的行中有多少个字

        while (nextCharIndex < total) {

            if (textChars[nextCharIndex] == '\n') {
                String line = text.substring(offset, nextCharIndex);
                if (lineIndex % linesEveryPage == 1 && lineIndex > 1) {

                    Saver.savePng(context, bitmap, (lineIndex / linesEveryPage) + ".png", time);
                    bitmap.recycle();
                    bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
                    canvas.setBitmap(bitmap);
                }

                canvas.drawText(line, xPos, yPos + ((lineIndex - 1) % linesEveryPage + 1) * lineHeight, paint);

                nextCharIndex++;
                offset = nextCharIndex;
                lineIndex++;
            } else if (paint.measureText(textChars, offset, nextCharIndex - offset) >= lineWidth) {
                if (END_CHARS.indexOf(textChars[nextCharIndex]) >= 0) {
                    nextCharIndex++;
                }
                String line = text.substring(offset, nextCharIndex);
                if (lineIndex % linesEveryPage == 1 && lineIndex > 1) {
                    Saver.savePng(context, bitmap, (lineIndex / linesEveryPage) + ".png", time);
                    bitmap.recycle();
                    bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
                    canvas.setBitmap(bitmap);
                }

                canvas.drawText(line, xPos, yPos + ((lineIndex - 1) % linesEveryPage + 1) * lineHeight, paint);

                if (textChars[nextCharIndex] == '\n') {
                    nextCharIndex++;
                }
                offset = nextCharIndex;
                lineIndex++;
            } else {
                nextCharIndex++;
            }

            publishProgress((double) nextCharIndex / total);
        }

        String line = text.substring(offset, nextCharIndex);
        if (!"".equals(line) && lineIndex % linesEveryPage == 1 && lineIndex > 1) {

            Saver.savePng(context, bitmap, (lineIndex / linesEveryPage) + ".png", time);
            bitmap.recycle();
            bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
        }
        canvas.drawText(line, xPos, yPos + ((lineIndex - 1) % linesEveryPage + 1) * lineHeight, paint);

        Saver.savePng(context, bitmap, (lineIndex / linesEveryPage + 1) + ".png", time);
        bitmap.recycle();

        return (lineIndex / linesEveryPage + 1);
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
        updateTask.onProgressUpdate(values[0]);
    }

    @Override
    protected Integer doInBackground(Context... contexts) {
        try {
            return draw(screenWidth, screenHeight, fontPath, text, contexts[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);
        updateTask.onFinish(s);
    }
}
