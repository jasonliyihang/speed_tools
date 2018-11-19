package com.liyihang.jason;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

public class SPThemeView {
    private WeakReference<View> view;
    private List<SPThemeAttr> attrs;

    public SPThemeView(View view, List<SPThemeAttr> skinAttrs) {
        this.view = new WeakReference<>(view);
        this.attrs = skinAttrs;
    }

    public void use() {
        if (view.get() == null) return;
        for (SPThemeAttr attr : attrs) {
            attr.use(view.get());
        }
    }
}
