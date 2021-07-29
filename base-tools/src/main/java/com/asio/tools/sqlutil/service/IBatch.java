package com.asio.tools.sqlutil.service;

/**
 * @author: asio
 * @time: 2021-07-28 15:14
 * 描述:  操作接口
 */
public interface IBatch<T, U> {

    /**
     * Description: 处理sql方法
     * @author asio
     * @time 2021-07-28 15:15
     * @param arg1 参数一
     * @param arg2 参数二
     */
    Integer accept(T arg1, U arg2) throws Exception;

}
