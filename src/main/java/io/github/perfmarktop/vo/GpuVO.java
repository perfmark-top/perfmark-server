package io.github.perfmarktop.vo;

import lombok.Data;

@Data
public class GpuVO {
    private long id;
    private String name;
    private String g3d;
    private String value;
    private Integer g2dRank;
    private Integer g3dRank;
    private Integer valueRank;
}
