package com.speed.hotpatch.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

/**
 *  by liyihang
 */
public class SpeedLayoutInflaterFactory implements LayoutInflater.Factory2 {

    private SpeedHostActivityHelper hostActivityHelper;
    private SpeedViewConstructor viewConstructor;

    public void setHostActivityHelper(SpeedHostActivityHelper hostActivityHelper) {
        this.hostActivityHelper = hostActivityHelper;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return createPluginView(name, context, attrs);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return createPluginView(name, context, attrs);
    }

    private View createPluginView(String name, Context context, AttributeSet attrs) {
        ClassLoader loader = context.getClassLoader();
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            loader = hostActivityHelper.getClassLoader();
        }
        if (viewConstructor == null || viewConstructor.getClassLoader() != loader) {
            viewConstructor = new SpeedViewConstructor(loader);
        }
        name = viewConstructor.resolveName(name, attrs);
        //如果没有'.'，说明是Android系统控件，直接返回null，让系统自己createView
        if (-1 == name.indexOf('.')) {
            return null;
        }
        try {
            return viewConstructor.createView(context, name, null, attrs);
        } catch (NoSuchMethodException e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name);
            ie.initCause(e);
            throw ie;
        } catch (ClassCastException e) {
            // If loaded class is not a View subclass
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Class is not a View " + name);
            ie.initCause(e);
            throw ie;
        } catch (ClassNotFoundException e) {
            // If loaded class is not a View subclass
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Class not found " + name);
            ie.initCause(e);
            throw ie;
        } catch (Exception e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name);
            ie.initCause(e);
            throw ie;
        }
    }
}
