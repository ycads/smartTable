package com.bin.david.form.data.format.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.Column;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;

/**
 * Created by huang on 2017/10/30.
 */

public abstract class BitmapDrawFormat<T> implements IDrawFormat<T> {

    private int imageWidth;
    private int imageHeight;
    private Rect imgRect;
    private Rect drawRect;
    private boolean isDrawBg =true;
     CellInfo<T> cellInfo = new CellInfo<>();





    public BitmapDrawFormat(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        imgRect = new Rect();
        drawRect = new Rect();
    }



    @Override
    public int measureWidth(Column<T>column, TableConfig config) {
        return  imageWidth;
    }

    @Override
    public int measureHeight(Column<T> column,int position, TableConfig config) {

        return imageHeight;
    }

    /**
     * 获取bitmap
     * @param t 数据
     * @param value 值
     * @param position 位置
     * @return Bitmap 占位图
     */
    protected abstract Bitmap getBitmap(T t,String value, int position);

    @Override
    public void draw(Canvas c,Column<T> column, T t, String value, Rect rect, int position, TableConfig config) {
        Paint paint = config.getPaint();
        cellInfo.set(column,t,value,position);
        drawBackground(c,cellInfo,rect,config);
        Bitmap bitmap = getBitmap(t,value,position);
        if(bitmap != null) {
            paint.setStyle(Paint.Style.FILL);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            imgRect.set(0,0,width,height);
            float scaleX = (float)width/imageWidth;
            float scaleY = (float)height/imageHeight;
            if(scaleX >1 || scaleY >1){
                if(scaleX > scaleY){
                    width = (int) (width/scaleX);
                    height = imageHeight;
                }else{
                    height = (int) (height/scaleY);
                    width = imageWidth;
                }
            }
            width= (int) (width*config.getZoom());
            height = (int) (height*config.getZoom());
            int disX= (rect.right-rect.left-width)/2;
            int disY= (rect.bottom-rect.top-height)/2;
            drawRect.left = rect.left+disX;
            drawRect.top = rect.top+ disY;
            drawRect.right = rect.right - disX;
            drawRect.bottom = rect.bottom - disY;
            c.drawBitmap(bitmap, imgRect, drawRect, paint);
        }
    }

    public boolean drawBackground(Canvas c, CellInfo<T> cellInfo, Rect rect,TableConfig config) {
        ICellBackgroundFormat<CellInfo> backgroundFormat = config.getContentBackgroundFormat();
        if(isDrawBg &&backgroundFormat != null){
            backgroundFormat.drawBackground(c,rect,cellInfo,config.getPaint());
            return true;
        }
        return false;
    }

    public boolean isDrawBg() {
        return isDrawBg;
    }

    public void setDrawBg(boolean drawBg) {
        isDrawBg = drawBg;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
}
