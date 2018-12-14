package com.cmit.testing.utils;

import java.util.function.Supplier;

/**
 * Created by Suviky on 2018/7/24 20:06
 */
public interface ConstructUtil {
    /**
     * 通过Supplier函数接口创建对象
     *
     * @param <T>
     * @param supplier
     * @return
     */
    static <T> T create(final Supplier<T> supplier) {
        return supplier.get();

    }
}
