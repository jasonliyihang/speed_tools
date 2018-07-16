package com.chengxing.cxsdk;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

public class CXThemeView {
    private WeakReference<View> view;//需要改变皮肤的view
    private List<CXThemeAttr> attrs;//这个view下所有的资源实例

    //传入view和所有资源
    public CXThemeView(View view, List<CXThemeAttr> skinAttrs) {
        this.view = new WeakReference<>(view);
        this.attrs = skinAttrs;
    }

    //执行换肤，将该view下的所有资源都进行换肤
    public void use() {
        if (view.get() == null) return;
        for (CXThemeAttr attr : attrs) {
            //skinattr中有针对单个资源的换肤
            attr.use(view.get());
        }
    }
}
