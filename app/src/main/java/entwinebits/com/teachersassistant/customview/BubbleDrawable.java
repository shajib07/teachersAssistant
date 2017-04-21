package entwinebits.com.teachersassistant.customview;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by Nargis Rahman on 4/21/2017.
 */
public class BubbleDrawable extends Drawable {
    private RectF mRect;
    private Path mPath = new Path();
    private BitmapShader mBitmapShader;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mArrowWidth;
    private float mAngle;
    private float mArrowHeight;
    private float mArrowPosition;
    private int bubbleColor;
    private Bitmap bubbleBitmap;
    private ArrowLocation mArrowLocation;
    private BubbleDrawable(Builder builder) {
        this.mRect = builder.mRect;
        this.mAngle = builder.mAngle;
        this.mArrowHeight = builder.mArrowHeight;
        this.mArrowWidth = builder.mArrowWidth;
        this.mArrowPosition = builder.mArrowPosition;
        this.bubbleColor = builder.bubbleColor;
        this.mArrowLocation = builder.mArrowLocation;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        setUp(canvas);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    private void setUpPath(ArrowLocation mArrowLocation, Path path){

        switch (mArrowLocation){
            case TOP:
                setUpBottomPath(mRect, path);
                //setUpTopPath(mRect, path);
                break;
            case BOTTOM:
                setUpLeftPath(mRect, path);
                break;
        }
    }

    private void setUp(Canvas canvas){
        mPaint.setColor(bubbleColor);
        setUpPath(mArrowLocation, mPath);
        canvas.drawPath(mPath, mPaint);
    }


    private void setUpTopPath(RectF rect, Path path){
        path.moveTo(rect.left + Math.min(mArrowPosition, mAngle), rect.top + mArrowHeight);
        path.lineTo(rect.left + mArrowPosition,  rect.top + mArrowHeight);
        path.lineTo(rect.left + mArrowWidth / 2 + mArrowPosition, rect.top);
        path.lineTo(rect.left + mArrowWidth + mArrowPosition, rect.top + mArrowHeight);
        path.lineTo(rect.right - mAngle, rect.top + mArrowHeight);

        path.arcTo(new RectF(rect.right - mAngle,
                rect.top + mArrowHeight , rect.right, mAngle + rect.top + mArrowHeight), 270, 90);
        path.lineTo(rect.right, rect.bottom - mAngle);

        path.arcTo(new RectF(rect.right - mAngle , rect.bottom - mAngle,
                rect.right , rect.bottom), 0, 90);
        path.lineTo(rect.left + mAngle, rect.bottom);

        path.arcTo(new RectF(rect.left, rect.bottom - mAngle,
                mAngle + rect.left , rect.bottom), 90, 90);
        path.lineTo(rect.left , rect.top + mArrowHeight + mAngle);
        path.arcTo(new RectF(rect.left, rect.top + mArrowHeight , mAngle
                + rect.left, mAngle + rect.top + mArrowHeight), 180, 90);
        path.close();
    }


    private void setUpBottomPath(RectF rect, Path path){

        path.moveTo(rect.left + mAngle, rect.top);
        path.lineTo(rect.right - (rect.width()/4)+ mAngle, rect.top);
        /*path.arcTo(new RectF(rect.right - mAngle,
                rect.top, rect.right, mAngle + rect.top), 270, 90);*/

        path.lineTo(rect.right, rect.height()/2+ mAngle/2);
      /* path.arcTo(new RectF(rect.right - mAngle, rect.bottom - mAngle - mArrowHeight,
                rect.right, rect.bottom - mArrowHeight), 0, 90);*/

        path.lineTo(rect.right - (rect.width()/4)+ mAngle , rect.bottom);
        path.lineTo(rect.left+mAngle, rect.bottom);
        /*path.lineTo(rect.left + mArrowPosition, rect.bottom - mArrowHeight);
        path.lineTo(rect.left + Math.min(mAngle, mArrowPosition), rect.bottom - mArrowHeight);*/

        /*path.arcTo(new RectF(rect.left, rect.bottom - mAngle - mArrowHeight,
                mAngle + rect.left , rect.bottom - mArrowHeight), 90, 90);
        path.lineTo(rect.left, rect.top + mAngle);
        path.arcTo(new RectF(rect.left, rect.top, mAngle
                + rect.left, mAngle + rect.top), 180, 90);*/
        path.close();
    }


    private void setUpLeftPath(RectF rect, Path path){

        path.moveTo(rect.left + mAngle, rect.top);
        path.lineTo(rect.width(), rect.top);
        /*path.arcTo(new RectF(rect.right - mAngle,
                rect.top, rect.right, mAngle + rect.top), 270, 90);*/
        path.lineTo(rect.width(), rect.bottom);

        path.lineTo(rect.left + mAngle, rect.bottom);
      /* path.arcTo(new RectF(rect.right - mAngle, rect.bottom - mAngle - mArrowHeight,
                rect.right, rect.bottom - mArrowHeight), 0, 90);*/

        path.lineTo(rect.left+(rect.width()/4) , rect.height()/2+ mAngle/2);
        path.lineTo(rect.left + mAngle, rect.top);
        /*path.lineTo(rect.left + mArrowPosition, rect.bottom - mArrowHeight);
        path.lineTo(rect.left + Math.min(mAngle, mArrowPosition), rect.bottom - mArrowHeight);*/

        /*path.arcTo(new RectF(rect.left, rect.bottom - mAngle - mArrowHeight,
                mAngle + rect.left , rect.bottom - mArrowHeight), 90, 90);
        path.lineTo(rect.left, rect.top + mAngle);
        path.arcTo(new RectF(rect.left, rect.top, mAngle
                + rect.left, mAngle + rect.top), 180, 90);*/
        path.close();
    }

    @Override
    public int getIntrinsicWidth() {
        return (int)mRect.width();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int)mRect.height();
    }

    public static class Builder{
        public static float DEFAULT_ARROW_WITH = 25;
        public static float DEFAULT_ARROW_HEIGHT = 25;
        public static float DEFAULT_ANGLE = 20;
        public static float DEFAULT_ARROW_POSITION = 50;
        public static int DEFAULT_BUBBLE_COLOR = Color.RED;
        private RectF mRect;
        private float mArrowWidth = DEFAULT_ARROW_WITH;
        private float mAngle = DEFAULT_ANGLE;
        private float mArrowHeight = DEFAULT_ARROW_HEIGHT;
        private float mArrowPosition = DEFAULT_ARROW_POSITION;
        private int bubbleColor = DEFAULT_BUBBLE_COLOR;
        private ArrowLocation mArrowLocation = ArrowLocation.BOTTOM;

        public Builder rect(RectF rect){
            this.mRect = rect;
            return this;
        }

        public Builder arrowWidth(float mArrowWidth){
            this.mArrowWidth = mArrowWidth;
            return this;
        }

        public Builder angle(float mAngle){
            this.mAngle = mAngle * 2;
            return this;
        }

        public Builder arrowHeight(float mArrowHeight){
            this.mArrowHeight = mArrowHeight;
            return this;
        }

        public Builder arrowPosition(float mArrowPosition){
            this.mArrowPosition = mArrowPosition;
            return this;
        }

        public Builder arrowLocation(ArrowLocation arrowLocation){
            this.mArrowLocation = arrowLocation;
            return this;
        }

        public Builder bubbleColor(int bubbleColor){
            this.bubbleColor = bubbleColor;
            return this;
        }

        public BubbleDrawable build(){
            if (mRect == null){
                throw new IllegalArgumentException("BubbleDrawable Rect can not be null");
            }
            return new BubbleDrawable(this);
        }
    }

    public enum ArrowLocation{
        TOP(0x02),
        BOTTOM(0x03);

        private int mValue;

        ArrowLocation(int value){
            this.mValue = value;
        }

        public static ArrowLocation mapIntToValue(int stateInt) {
            for (ArrowLocation value : ArrowLocation.values()) {
                if (stateInt == value.getIntValue()) {
                    return value;
                }
            }
            return getDefault();
        }

        public static ArrowLocation getDefault(){
            return BOTTOM;
        }

        public int getIntValue() {
            return mValue;
        }
    }
}
