# ImageRotationTest

これはAndroidの*Viewを使ったBitmapを回転した際の描画のされ方のテストです。
画面をタップすることでViewとSurfaceViewのジャギーの差が比較できるようになっています。

+ アプリを起動すると、Canvasのrotate()とtranslate()を使って水平から15度回転した画像が表示されます。
+ 画面をタップするとViewとSurfaceViewが切り替わります。
+ CanvasにBitmap描画時に使うPaintに対して、それぞれ同じように下記の設定がしてあります。
```
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
```
+ SurfaceViewの方は、加えてPixelFormatを下記のように変更してあります。
```
getHolder().setFormat(PixelFormat.RGBA_8888);
```
+ SurfaceViewの方は回転アニメーションします。

