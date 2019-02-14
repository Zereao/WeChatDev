package com.zereao.wechat.pojo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Darion Mograine H
 * @version 2019/02/14  14:17
 */
@Data
@Builder
public class AlmanacDTO {
    /**
     * 当前日期
     */
    private String date;
    /**
     * 当日忌讳
     */
    private List<String> tabooList;
    /**
     * 当日适宜
     */
    private List<String> suitableList;
    /**
     * 时辰运势
     */
    private List<String> timeLuck;

}
