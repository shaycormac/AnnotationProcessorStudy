package com.shay.appapi;

import android.app.Activity;
import android.view.View;

/**
 * @author 作者：Shay-Patrick-Cormac
 * @datetime 创建时间：2017-04-10 10:41 GMT+8
 * @email 邮箱： 574583006@qq.com
 * @content 说明：Activity UI查找提供者
 */
public class ActivityViewFinder implements ViewFinder {
    @Override
    public View findView(Object object, int id) 
    {
        return ((Activity)object).findViewById(id);
    }
}
