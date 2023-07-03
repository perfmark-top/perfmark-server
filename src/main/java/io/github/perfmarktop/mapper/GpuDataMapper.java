package io.github.perfmarktop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.github.perfmarktop.pojo.GpuData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface GpuDataMapper extends BaseMapper<GpuData> {
    Integer batchInsert(@Param("data") List<GpuData> data);
}
