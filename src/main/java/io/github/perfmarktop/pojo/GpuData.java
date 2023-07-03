package io.github.perfmarktop.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GpuData {

  @TableId
  private long id;
  private String name;
  private String price;
  private String g3d;
  private String g2d;
  private String value;
  private String tdp;
  private String powerPerf;
  private String date;
  private String cat;
  private String bus;
  private String memSize;
  private String coreClk;
  private String memClk;
  private String rank;
  private String samples;
  private String href;
  private String output;

}
