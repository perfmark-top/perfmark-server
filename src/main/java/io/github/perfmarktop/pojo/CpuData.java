package io.github.perfmarktop.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CpuData {
  private long id;
  private String name;
  private String price;
  private String cpumark;
  private String thread;
  private String value;
  private String threadValue;
  private String tdp;
  private String powerPerf;
  private String date;
  private String socket;
  private String cat;
  private String speed;
  private String turbo;
  private String cpuCount;
  private String cores;
  private String logicals;
  private String secondaryCores;
  private String secondaryLogicals;
  private long rank;
  private String samples;
  private String href;
  private String output;
  private Integer cpumarkRank;
  private Integer threadRank;
  private Integer valueRank;
}
