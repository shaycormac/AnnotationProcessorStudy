package com.shay.appapi;

import android.app.Activity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 作者：Shay-Patrick-Cormac
 * @datetime 创建时间：2017-04-10 10:44 GMT+8
 * @email 邮箱： 574583006@qq.com
 * @content 说明：注解框架向外提供绑定方法，这里使用静态类来管理。
 */
public class ShayViewBinder 
{
    //默认声明一个Activity View查找器
    private static final ActivityViewFinder activityFinder = new ActivityViewFinder();
    //管理保持管理者Map集合
    private static final Map<String, ViewBinder> bindMap = new LinkedHashMap<>();
    /**
     * Activity注解绑定 ActivityViewFinder
     *
     * @param activity
     */
    public static void bind(Activity activity)
    {
        bind(activity, activity, activityFinder);
    }
    /**
     * '注解绑定
     *
     * @param host   表示注解 View 变量所在的类，也就是注解类
     * @param object 表示查找 View 的地方，Activity & View 自身就可以查找，Fragment 需要在自己的 itemView 中查找
     * @param finder ui绑定提供者接口
     */
    private static void bind(Object host, Object object, ViewFinder finder) 
    {
        String className = host.getClass().getName();
        try {
            ViewBinder binder = bindMap.get(className);
            if (binder==null)
            {
                Class<?> aClass = Class.forName(className + "$$ViewBinder");
                binder = (ViewBinder) aClass.newInstance();
                bindMap.put(className, binder);
            }else 
                binder.bindView(host, object, finder);
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
       
    }
    /**
     * 解除注解绑定 ActivityViewFinder
     *
     * @param host
     */
    public static void unBind(Object host)
    {
        String className = host.getClass().getName();
        ViewBinder binder = bindMap.get(className);
        if (binder!=null)
            binder.unBindView(host);
        bindMap.remove(className);
    }
}
