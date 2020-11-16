package com.zb.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestProxy {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        // 按约定获取proxy
        HelloService proxy = (HelloService) ProxyBean.getProxyBean(helloService, new MyInterceptor());
        proxy.sayHello("ZhangSan");
        System.out.println("################# name is null!!! ###############");
        proxy.sayHello(null);
    }
}

interface HelloService {
    void sayHello(String name);
}

class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String name) {
        if (name == null || name.trim().equals("")) {
            throw new RuntimeException("parameter is null!!!");
        }
        System.out.println("hello " + name);
    }
}
/**
 * 拦截器接口
 */
interface Interceptor {
    // 事前方法
    boolean before();

    // 事后方法
    void after();

    /**
     * 取代原有事件方法
     * @param invocation 回调参数，可以通过它的proceed方法，回调原有事件
     * @return 原有事件返回对象
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    Object around(Invocation invocation) throws InvocationTargetException, IllegalAccessException;

    // 是否返回方法，事件没有发生异常执行
    void afterReturning();

    // 事后异常方法，当事件发生异常后执行
    void afterThrowing();

    // 是否使用around方法取代原有方法
    boolean useAround();
}

class Invocation {
    private Object[] params;
    private Method method;
    private Object target;

    public Invocation(Object target, Method method, Object[] params) {
        this.params = params;
        this.method = method;
        this.target = target;
    }

    // 反射方法
    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, params);
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}

class MyInterceptor implements Interceptor {
    @Override
    public boolean before() {
        System.out.println("before ------");
        return true;
    }

    @Override
    public boolean useAround() {
        return true;
    }

    @Override
    public void after() {
        System.out.println("after ------");
    }

    @Override
    public Object around(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        System.out.println("around before ------");
        Object obj = invocation.proceed();
        System.out.println("around after ------");
        return obj;
    }

    @Override
    public void afterReturning() {
        System.out.println("afterReturning ------");
    }

    @Override
    public void afterThrowing() {
        System.out.println("afterThrowing ------");
    }
}

class ProxyBean implements InvocationHandler {

    private Object target = null;
    private Interceptor interceptor = null;

    /**
     * 绑定代理对象
     * @param target 被代理对象
     * @param interceptor 拦截器
     * @return 代理对象
     */
    public static Object getProxyBean(Object target, Interceptor interceptor) {
        ProxyBean proxyBean = new ProxyBean();
        // 保存被代理对象
        proxyBean.target = target;
        // 保存拦截器
        proxyBean.interceptor = interceptor;
        // 生成代理对象
        Object proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                proxyBean);
        // 返回代理对象
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 异常标识
        boolean exceptionFlag = false;
        Invocation invocation = new Invocation(target, method, args);
        Object retObj = null;
        try {
            if (this.interceptor.before()) {
                retObj = this.interceptor.around(invocation);
            } else {
                retObj = method.invoke(target, args);
            }
        } catch (Exception e) {
            // 产生异常
            exceptionFlag = true;
        }
        this.interceptor.after();
        if (exceptionFlag) {
            this.interceptor.afterThrowing();
        } else {
            this.interceptor.afterReturning();
            return retObj;
        }

        return null;
    }
}