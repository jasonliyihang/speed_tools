package com.speed.hotpatch.libs;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @deprecated Use direct *InterfaceImp instances instead of dynamic proxies.
 *             Kept for binary compatibility with older integrations.
 */
@Deprecated
public class SpeedInvocationHandler implements InvocationHandler {

    private static final String TAG = "SpeedInvocationHandler";
    private Object delegate;

    public Object bind(Class<?> interfaceClass) {
        delegate = SpeedUtils.getObj(interfaceClass.getName(), getClass().getClassLoader());
        if (delegate == null) {
            throw new IllegalStateException("Cannot bind proxy for " + interfaceClass.getName());
        }
        return Proxy.newProxyInstance(
                delegate.getClass().getClassLoader(),
                delegate.getClass().getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(delegate, args);
        } catch (InvocationTargetException e) {
            // Rethrow what the delegate actually threw instead of the reflection wrapper.
            Throwable cause = e.getCause();
            Log.e(TAG, "invoke failed: " + method.getName(), cause != null ? cause : e);
            throw cause != null ? cause : e;
        }
    }
}
