package com.elbbbird.android.socialsdk.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zhanghailong-ms on 2015/12/11.
 */
public class OverlayImageView extends ImageView {
    public OverlayImageView(Context context) {
        super(context);
    }

    public OverlayImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (this.getDrawable() != null) {
            if (pressed) {
                this.getDrawable().setColorFilter(1140850688, PorterDuff.Mode.SRC_ATOP);
                this.invalidate();
            } else {
                this.getDrawable().clearColorFilter();
                this.invalidate();
            }
        }
    }
}
