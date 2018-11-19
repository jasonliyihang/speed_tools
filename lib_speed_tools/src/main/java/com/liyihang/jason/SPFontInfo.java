package com.liyihang.jason;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class SPFontInfo {

    public boolean isExist = false;
    public String attrName;
    public WeakReference<View> viewWeakReference;

    public void use() {
        if (viewWeakReference.get() != null) {
            TextView textView = (TextView) viewWeakReference.get();
            Resources resources = textView.getContext().getResources();
            float dimension = resources.getDimensionPixelSize(resources.getIdentifier(attrName, SPThemeManager.RES_DIMEN, textView.getContext().getPackageName()));
            dimension += SPFontManager.getInstance().getFontScale();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension);
        }
    }
}
