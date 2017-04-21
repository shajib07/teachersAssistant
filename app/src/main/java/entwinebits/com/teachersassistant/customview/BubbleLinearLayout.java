package entwinebits.com.teachersassistant.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import entwinebits.com.teachersassistant.R;

/**
 * Created by Nargis Rahman on 4/21/2017.
 */
public class BubbleLinearLayout extends LinearLayout {
    private BubbleDrawable bubbleDrawable;
    public float mArrowWidth;
    public float mAngle;
    public float mArrowHeight;
    public float mArrowPosition;
    public BubbleDrawable.ArrowLocation mArrowLocation;
    public int bubbleColor;

    //public float mArrowPointX = 0.0f, mArrowPointY = 0.0f;

    /**
     *
     */
    Paint trianglePaint;
    /**
     *
     */
    Path trianglePath;
    public BubbleLinearLayout(Context context) {
        super(context);
        initView(null);
    }

    public BubbleLinearLayout(Context context, float mArrowWidth, float mAngle, float mArrowHeight, float mArrowPosition, int bubbleColor ) {
        super(context);
        initView(null);
    }

    public BubbleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }


    private void initView(AttributeSet attrs){
        if (attrs != null){
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BubbleView);
            mArrowWidth = array.getDimension(R.styleable.BubbleView_arrowWidth,
                    BubbleDrawable.Builder.DEFAULT_ARROW_WITH);
            mArrowHeight = array.getDimension(R.styleable.BubbleView_arrowHeight,
                    BubbleDrawable.Builder.DEFAULT_ARROW_HEIGHT);
            mAngle = array.getDimension(R.styleable.BubbleView_angles,
                    BubbleDrawable.Builder.DEFAULT_ANGLE);
            mArrowPosition = array.getDimension(R.styleable.BubbleView_arrowPosition,
                    BubbleDrawable.Builder.DEFAULT_ARROW_POSITION);
            bubbleColor = array.getColor(R.styleable.BubbleView_bubbleColor,
                    BubbleDrawable.Builder.DEFAULT_BUBBLE_COLOR);
            int location = array.getInt(R.styleable.BubbleView_arrowLocation, 0);
            mArrowLocation = BubbleDrawable.ArrowLocation.mapIntToValue(location);
            array.recycle();
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        // mArrowPosition = this.getWidth()/2-mArrowWidth/2;
        Log.d("Value2:", "nnnn");
        setUp(this.getWidth(), this.getHeight());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0){
            setUp(w, h);
        }
    }

    private void setUp(int left, int right, int top, int bottom){
        Log.d("Value:", left+","+top+","+right+","+bottom);
        if (right < left || bottom < top)
            return;

        RectF rectF = new RectF(left, top, right, bottom);
        bubbleDrawable = new BubbleDrawable.Builder()
                .rect(rectF)
                .arrowLocation(mArrowLocation)
                .angle(mAngle)
                .arrowHeight(mArrowHeight)
                .arrowWidth(mArrowWidth)
                .arrowPosition(mArrowPosition)
                .bubbleColor(bubbleColor)
                .build();
    }

    private void setUp(int width, int height) {

        Log.d("Value1:", "innn");
        setUp(getPaddingLeft(), width - getPaddingRight(),
                getPaddingTop(), height - getPaddingBottom());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(bubbleDrawable);
        } else {
            setBackgroundDrawable(bubbleDrawable);
        }
    }

    public void setUp1(int left, int right, int top, int bottom){
        if (right < left || bottom < top)
            return;
        RectF rectF = new RectF(left, top, right, bottom);
//        bubbleDrawable = new BubbleDrawable.Builder()
//                .rect(rectF)
//                .arrowLocation(mArrowLocation)
//                .bubbleType(BubbleDrawable.BubbleType.COLOR)
//                .angle(mAngle)
//                .arrowHeight(mArrowHeight)
//                .arrowWidth(mArrowWidth)
//                .arrowPosition(mArrowPosition)
//                .bubbleColor(bubbleColor)
//                .build();
    }

    public void setUp1(int width, int height){
        setUp1(getPaddingLeft(),  + width - getPaddingRight(),
                getPaddingTop(), height - getPaddingBottom());
        // setBackgroundDrawable(bubbleDrawable);
    }



}