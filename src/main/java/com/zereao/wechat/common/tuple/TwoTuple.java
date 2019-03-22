package com.zereao.wechat.common.tuple;

/**
 * 含有两个元素的元祖
 *
 * @author Darion Mograine H
 * @version 2019/03/21  12:37
 */
public class TwoTuple<N, C> {
    public final N name;
    public final C classObj;

    public TwoTuple(N name, C classObj) {
        this.name = name;
        this.classObj = classObj;
    }
}
