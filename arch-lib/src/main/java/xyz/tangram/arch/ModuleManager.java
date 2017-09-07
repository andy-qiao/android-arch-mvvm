package xyz.tangram.arch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 17:01
 */

class ModuleManager {
    private static Map<Class<?>, BaseModule> mModuleCache = new ConcurrentHashMap<>();
    private static Map<Class<?>, BaseModuleImpl> mModuleImplCache = new ConcurrentHashMap<>();

    public static <T extends BaseModule> T get(Class<T> moduleClass) {
        BaseModule module = mModuleCache.get(moduleClass);
        if (module != null) {
            return (T) module;
        }
        synchronized (mModuleCache) {
            try {
                module = mModuleCache.get(moduleClass);
                if (module == null) {
                    module = create(moduleClass);
                    mModuleCache.put(moduleClass, module);
                }
                return (T) module;
            } catch (Throwable t) {
                throw new RuntimeException("获取module失败 " + moduleClass.getName() + "  " + t);
            }
        }
    }

    static <T extends BaseModuleImpl> T getImpl(Class<T> moduleClass) {
        BaseModuleImpl module = mModuleImplCache.get(moduleClass);
        if (module != null) {
            return (T) module;
        }
        synchronized (mModuleImplCache) {
            try {
                module = mModuleImplCache.get(moduleClass);
                if (module == null) {
                    module = moduleClass.newInstance();
                    mModuleImplCache.put(moduleClass, module);
                }
                return (T) module;
            } catch (Throwable t) {
                throw new RuntimeException("获取moduleImpl失败 " + moduleClass.getName() + "  " + t);
            }
        }
    }

    private static <T extends BaseModule> T create(Class<T> moduleClass) throws Exception {
        ProxyTarget proxyTarget = moduleClass.getAnnotation(ProxyTarget.class);
        Class<? extends BaseModuleImpl> targetClass = proxyTarget.value();
        Method[] methods = moduleClass.getDeclaredMethods();
        Method[] targetMethods = targetClass.getDeclaredMethods();

        HashMap<Method, Method> methodMap = new HashMap<>();
        // TODO 完善方法的映射
        for (Method method : methods) {
            for (Method targetMethod : targetMethods) {
                if (methodEquals(method, targetMethod)) {
                    methodMap.put(method, targetMethod);
                    break;
                }
            }
        }
        Object targetObject = getImpl(targetClass);
        return (T) Proxy.newProxyInstance(moduleClass.getClassLoader(), new Class[]{moduleClass}, new ModuleInvocationHandler(targetObject, methodMap));
    }

    private static class ModuleInvocationHandler implements InvocationHandler {
        private Object mTargetObject;
        private Map<Method, Method> mMethodMap;

        public ModuleInvocationHandler(Object targetObject, Map<Method, Method> methodMap) {
            mTargetObject = targetObject;
            mMethodMap = methodMap;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object res = mMethodMap.get(method).invoke(mTargetObject, args);
            if (ModuleCall.class.equals(method.getReturnType())) {
                ModuleCall call = new ModuleCall();
                call.setObservable((Observable) res);
                return call;
            } else {
                return res;
            }
        }
    }

    private static boolean methodEquals(Method method1, Method method2) {
        if (!method1.getName().equals(method2.getName())) {
            return false;
        }
        Class<?>[] params1 = method1.getParameterTypes();
        Class<?>[] params2 = method2.getParameterTypes();
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; i++) {
                if (params1[i] != params2[i])
                    return false;
            }
            return true;
        }
        return false;
    }


}
