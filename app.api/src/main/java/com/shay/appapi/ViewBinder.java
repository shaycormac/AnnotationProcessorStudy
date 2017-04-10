package com.shay.appapi;

/**
 * @author 作者：Shay-Patrick-Cormac
 * @datetime 创建时间：2017-04-10 10:38 GMT+8
 * @email 邮箱： 574583006@qq.com
 * @content UI绑定解绑接口。
 */
public interface ViewBinder<T>
{
    void bindView(T host,Object object,ViewFinder finder);

    void unBindView(T host);
}
