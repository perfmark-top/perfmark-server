package io.github.perfmarktop.vo;

import lombok.Data;

@Data
public class CpuVO {
    private long id;
    private String name;
    // 跑分
    private String cpumark;
    // 性价比
    private String value;
    private String cpuCount;
    // 跑分排名
    private Integer cpuMarkRank;
    // 性价比排名
    private Integer valueRank;
    // 单线程排名
    private Integer threadRank;

}
