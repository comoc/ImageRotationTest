package com.blogspot.comolog.imagerotationtest;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Akihiro Komori on 2017/12/17.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint;
    private Bitmap bitmap;

    public MySurfaceView(Context context) {
        super(context);
        init();
        getHolder().addCallback(this);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getHolder().addCallback(this);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getHolder().addCallback(this);
    }

    private void init() {

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),R.drawable.from_hill, options);
        int ew = options.outWidth - 2048;
        int eh = options.outHeight - 2048;
        options.inJustDecodeBounds = false;
        if (ew > 0 || eh > 0) {
            if (ew > eh) {
                options.inSampleSize = (int)(options.outWidth / 2048.0f) + 1;
            } else {
                options.inSampleSize = (int)(options.outHeight / 2048.0f) + 1;
            }
        }
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.from_hill, options);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        customDraw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Canvas canvas = holder.lockCanvas();
        customDraw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void customDraw(Canvas canvas) {

//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLUE);
        paint.setTextSize(40);
        canvas.drawText("This is a SurfaceView", 10, 50, paint);

        int hw = canvas.getWidth() / 2;
        int hh = canvas.getHeight() / 2;
        canvas.translate(hw, hh);
        canvas.rotate(15);
        canvas.translate(-hw, -hh);
        if (bitmap.getWidth() > canvas.getWidth() ||  bitmap.getHeight() > canvas.getHeight()) {
            float sw = (float)bitmap.getWidth() / (float)canvas.getWidth();
            float sh = (float)bitmap.getHeight() / (float)canvas.getHeight();
            float scale;
            if (sw > sh) {
                scale = 1.0f / sw;
            } else {
                scale = 1.0f / sh;
            }
            scale *= 0.8f;
            int dw = (int)(bitmap.getWidth() * scale);
            int dh = (int)(bitmap.getHeight() * scale);
            int cx = (canvas.getWidth() - dw) / 2;
            int cy = (canvas.getHeight() - dh) / 2;
            canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(cx, cy, cx + dw - 1, cy + dh - 1), paint);
        } else {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            invalidate();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            invalidate();
        }
    }
}
