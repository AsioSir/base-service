package com.asio.tools.sqlutil;

import com.asio.tools.sqlutil.service.IBatch;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

/**
 * @author: asio
 * @time: 2021-07-28 15:07
 * 描述:  批量处理sql工具类
 */
public class BatchUtils {

    //批量操作数据库 最大操作数
    private static final int MAX_COUNT = 50;

    public static <T, E> Integer commitBatch(int maxCount, List<T> paraList, Class<E> clazz, IBatch<T, E> commitSql) throws Exception {
        SqlSessionTemplate sqlSessionTemplate;
        return 0;
    }

}
