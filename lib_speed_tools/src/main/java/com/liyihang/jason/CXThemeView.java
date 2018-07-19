package com.liyihang.jason;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

public class CXThemeView {
    private WeakReference<View> view;
    private List<CXThemeAttr> attrs;

    public CXThemeView(View view, List<CXThemeAttr> skinAttrs) {
        this.view = new WeakReference<>(view);
        this.attrs = skinAttrs;
    }

    public void use() {
        if (view.get() == null) return;
        for (CXThemeAttr attr : attrs) {
            attr.use(view.get());
        }
    }
}
