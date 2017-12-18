package com.blogspot.comolog.imagerotationtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Akihiro Komori on 2017/12/17.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint;
    private Bitmap bitmap;

    private boolean isRunning;
    private float angle = 0.0f;
    private Thread thread;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long lastTime = System.currentTimeMillis();
            while(isRunning) {
                long now = System.currentTimeMillis();
                long dt = now - lastTime;
                lastTime = now;

                SurfaceHolder holder = getHolder();
                Canvas canvas = holder.lockCanvas();
                customDraw(canvas, angle);
                angle += dt / 300.0f;
                angle = angle % 360.0f;
                holder.unlockCanvasAndPost(canvas);

                Log.v(MySurfaceView.class.getSimpleName(), "dt: " + dt);

                Thread.yield();
            }
        }
    };

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

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) {
            pause();
        }
    }

    private void init() {
        Log.v(MySurfaceView.class.getSimpleName(), "PixelFormat: " + getHolder());

        getHolder().setFormat(PixelFormat.RGBA_8888);

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

        thread = new Thread(runnable);
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (!isRunning) {
            isRunning = true;
            thread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void customDraw(Canvas canvas, float angle) {

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLUE);
        paint.setTextSize(40);
        canvas.drawText("This is a SurfaceView", 10, 50, paint);

        int hw = canvas.getWidth() / 2;
        int hh = canvas.getHeight() / 2;
        canvas.translate(hw, hh);
        canvas.rotate(angle);
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

    public void pause() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
