package com.shay.appapi;

import android.view.View;

/**
 * @author 作者：Shay-Patrick-Cormac
 * @datetime 创建时间：2017-04-10 10:40 GMT+8
 * @email 邮箱： 574583006@qq.com
 * @content 说明：定义一个被绑定者查找view的接口。
 */
public interface ViewFinder
{
    View findView(Object object, int id);
}
