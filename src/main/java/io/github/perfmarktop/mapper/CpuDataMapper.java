package io.github.perfmarktop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.perfmarktop.pojo.CpuData;
import io.github.perfmarktop.vo.CpuVO;
import io.github.perfmarktop.vo.param.SortParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * CPU Mapper接口类
 * @author LukeZhang
 * @date 2023/7/5 10:20
 */
public interface CpuDataMapper extends BaseMapper<CpuData> {
    /**
     * 返回CPU信息
     * @return CPU列表信息
     */
    List <CpuVO> selectVOList();
    /**
     * 批量插入数据
     * @param data CpuData列表数据
     * @return 插入条数
     */
     Integer batchInsert(@Param("data") List<CpuData> data);
    /**
     * 根据排序字段 返回分页数据
     * @param page 分页参数
     * @param sortParam 排序字段 排序方式
     * @return 列表数据
     */
    IPage<CpuVO> getSortedCpuData(Page<CpuVO> page, @Param("sortParam") SortParam sortParam, @Param("key") String key);

    /**
     * 获取单个id或者多个id的CPu列表数据
     * @param ids id
     * @return Cpu数据
     */
    List<CpuVO> getCpuByIds(@Param("ids") List<String> ids);
}
