# ImageRotationTest

これはAndroidの*Viewを使った画像回転表示のテストで、ViewとSurfaceViewでのジャギーの差が比較できるようになっています。

+ アプリを起動すると、Canvasのrotate()とtranslate()を使って水平から15度回転した画像が表示されます。
+ 画面をタップするとViewとSurfaceViewが切り替わります。
+ CanvasにBitmap描画時に使うPaintに対して、それぞれ同じように下記の設定がしてあります。
```
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
```
