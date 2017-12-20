package ramilgabdrakhmanov.imagedrawcompare;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by ramil.gabdrakhmanov on 14.12.17.
 */

public class MeasurableImageView extends TintableImageView {

    private ViewRedrawnListener mViewRedrawnListener;

    public MeasurableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasurableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        long startTime = System.nanoTime();

        super.onDraw(canvas);

        long endTime = System.nanoTime();
        notifyDraw(startTime, endTime);
    }

    private void notifyDraw(final double startTimeNano, final double endTimeNano) {
        double durationMiliseconds = (endTimeNano - startTimeNano) / 1000000;

        if (mViewRedrawnListener != null) {
            mViewRedrawnListener.onDrawFinished(durationMiliseconds);
        }
    }


    public void setViewRedrawnListener(ViewRedrawnListener mViewRedrawnListener) {
        this.mViewRedrawnListener = mViewRedrawnListener;
    }

    public interface ViewRedrawnListener {
        void onDrawFinished(double miliseconds);
    }
}
