package ramilgabdrakhmanov.imagedrawcompare;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;

/**
 * Created by ramil.gabdrakhmanov on 20.12.17.
 */

public class TintableImageView extends android.support.v7.widget.AppCompatImageView {

    public TintableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TintableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TintableImageView);
        ColorStateList colorStateList = a.getColorStateList(R.styleable.TintableImageView_supportTint);

        if (colorStateList != null) {
            ImageViewCompat.setImageTintList(this, colorStateList);
        }

        a.recycle();
    }
}
