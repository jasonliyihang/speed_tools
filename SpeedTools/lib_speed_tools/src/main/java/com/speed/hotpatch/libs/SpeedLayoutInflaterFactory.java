package com.speed.hotpatch.libs;

import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class SpeedLayoutInflaterFactory implements LayoutInflaterFactory {

    private HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();
    private Class<?>[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};
    private Object[] mConstructorArgs = new Object[2];

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }
        //如果没有'.'，说明是Android系统控件，直接返回null，让系统自己createView
        if (-1 == name.indexOf('.')) {
            return null;
        }
        Context lastContext = (Context) mConstructorArgs[0];
        mConstructorArgs[0] = context;
        Class<? extends View> clazz = null;
        //先从本地缓存读取
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        try {
            if (constructor == null) {
                //没有缓存，根据类名创建Constructor对象存入缓存
                // Class not found in the cache, see if it's real, and try to add it
                clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            Object[] args = mConstructorArgs;
            args[1] = attrs;
            constructor.setAccessible(true);
            return constructor.newInstance(args);
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
                    + ": Error inflating class " + (clazz == null ? "<unknown>" : clazz.getName()));
            ie.initCause(e);
            throw ie;
        } finally {
            mConstructorArgs[0] = lastContext;
            mConstructorArgs[1] = null;
        }
    }
}
