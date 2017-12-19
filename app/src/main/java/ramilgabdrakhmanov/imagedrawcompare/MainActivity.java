package ramilgabdrakhmanov.imagedrawcompare;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String[] GRID_ITEM_COLORS = {"#f7cc41", "#ffb55a", "#ff8869", "#f66190", "#aed488", "#78cac3", "#9aa9d7", "#ba69c4", "#9fa9d7", "#1269c4"};

    static final int N = 10;

    static final int[] sViewIdis = {R.id.iv_image1, R.id.iv_image2, R.id.iv_image3, R.id.iv_image4, R.id.iv_image5,
            R.id.iv_image6, R.id.iv_image7, R.id.iv_image8, R.id.iv_image9, R.id.iv_image10};

    static final int[] sPngIds = {R.drawable.ic_account_card_details_black_24dp, R.drawable.ic_account_multiple_outline_black_24dp, R.drawable.ic_account_network_black_24dp,
            R.drawable.ic_alarm_bell_black_24dp, R.drawable.ic_alert_decagram_black_24dp, R.drawable.ic_animation_black_24dp,
            R.drawable.ic_battery_80_black_24dp, R.drawable.ic_cake_black_24dp, R.drawable.ic_dice_5_black_24dp,
            R.drawable.ic_message_bulleted_black_24dp};

    static final int[] sVertorIds = {R.drawable.account_card_details, R.drawable.account_multiple_outline, R.drawable.account_network,
            R.drawable.alarm_bell, R.drawable.alert_decagram, R.drawable.animation,
            R.drawable.battery_80, R.drawable.cake, R.drawable.dice_5,
            R.drawable.message_bulleted
    };

    private TextView mDurationText;

    private MeasurableImageView[] mMeasurableImageViews;

    private int mDrawCompleteCount;
    private double mDrawCompleteSumInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDurationText = findViewById(R.id.tv_duration);
        mMeasurableImageViews = new MeasurableImageView[N];
        for (int i = 0; i < N; i++) {
            mMeasurableImageViews[i] = findViewById(sViewIdis[i]);
            mMeasurableImageViews[i].setViewRedrawnListener(new MeasurableImageView.ViewRedrawnListener() {
                @Override
                public void onDrawFinished(double miliseconds) {
                    mDrawCompleteSumInMillis += miliseconds;
                    mDrawCompleteCount++;
                    if (mDrawCompleteCount == N && mDurationText != null) {
                        mDurationText.setText(getString(R.string.duration, mDrawCompleteSumInMillis));
                    }
                }
            });
        }

        RadioGroup group = (RadioGroup) findViewById(R.id.rg_source);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mDrawCompleteCount = 0;
                mDrawCompleteSumInMillis = 0;
                switch (checkedId) {
                    case R.id.btn_vector:
                        vectorDrawableSelected();
                        break;
                    case R.id.btn_png:
                        pngSelected();
                        break;
                    case R.id.btn_simple_png:
                        singlePngSelected();
                        break;
                }
            }
        });

        View cleanBtn = findViewById(R.id.btn_clean);
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < N; i++) {
                    mMeasurableImageViews[i].setImageResource(0);
                    ImageViewCompat.setImageTintList(mMeasurableImageViews[i], null);
                }
            }
        });
    }

    private void vectorDrawableSelected() {
        for (int i = 0; i < N; i++) {
            mMeasurableImageViews[i].setImageResource(sVertorIds[i]);
        }
    }

    private void pngSelected() {
        for (int i = 0; i < N; i++) {
            mMeasurableImageViews[i].setImageResource(sPngIds[i]);
            ImageViewCompat.setImageTintList(mMeasurableImageViews[i], new ColorStateList(new int[][]{new int[]{}},
                    new int[]{
                            Color.parseColor(GRID_ITEM_COLORS[i])
                    }));

        }
    }

    private void singlePngSelected() {
        for (int i = 0; i < N; i++) {
            mMeasurableImageViews[i].setImageResource(sPngIds[i]);
        }
    }
}
