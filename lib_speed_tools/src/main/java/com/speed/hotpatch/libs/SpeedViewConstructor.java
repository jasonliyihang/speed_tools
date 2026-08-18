package com.speed.hotpatch.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public final class SpeedViewConstructor {

    private static final Class<?>[] CONSTRUCTOR_SIGNATURE = new Class[]{
            Context.class, AttributeSet.class};

    private final Map<String, Constructor<? extends View>> constructorMap = new HashMap<>();
    private final ClassLoader classLoader;

    public SpeedViewConstructor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String resolveName(String name, AttributeSet attrs) {
        if ("view".equals(name)) {
            return attrs.getAttributeValue(null, "class");
        }
        return name;
    }

    public View createView(Context context, String name, String prefix, AttributeSet attrs)
            throws ReflectiveOperationException {
        String resolvedName = resolveName(name, attrs);
        String className = prefix == null ? resolvedName : prefix + resolvedName;
        Constructor<? extends View> constructor = constructorMap.get(className);
        if (constructor == null) {
            ClassLoader loader = classLoader == null ? context.getClassLoader() : classLoader;
            Class<? extends View> clazz = loader.loadClass(className).asSubclass(View.class);
            constructor = clazz.getConstructor(CONSTRUCTOR_SIGNATURE);
            constructorMap.put(className, constructor);
        }
        constructor.setAccessible(true);
        Object[] constructorArgs = new Object[]{context, attrs};
        return constructor.newInstance(constructorArgs);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
