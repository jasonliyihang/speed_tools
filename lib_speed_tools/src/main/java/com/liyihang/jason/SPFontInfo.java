package com.liyihang.jason;

import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class SPFontInfo {

    private static final String TAG = "SPFontInfo";

    public boolean isExist = false;
    public String attrName;
    public WeakReference<View> viewWeakReference;

    public void use() {
        View view = viewWeakReference == null ? null : viewWeakReference.get();
        if (view == null) {
            return;
        }
        if (!(view instanceof TextView)) {
            Log.e(TAG, "font attr " + attrName + " bound to non-TextView " + view.getClass().getName());
            return;
        }
        TextView textView = (TextView) view;
        Resources resources = textView.getContext().getResources();
        int dimenId = resources.getIdentifier(attrName, SPThemeManager.RES_DIMEN,
                textView.getContext().getPackageName());
        if (dimenId == 0) {
            Log.w(TAG, "font dimen not found: " + attrName);
            return;
        }
        float dimension = resources.getDimensionPixelSize(dimenId);
        dimension += SPFontManager.getInstance().getFontScale();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension);
    }
}
