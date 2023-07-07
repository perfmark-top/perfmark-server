package io.github.perfmarktop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.perfmarktop.vo.CpuVO;
import io.github.perfmarktop.vo.param.SortParam;

import java.util.List;
/**
 * CPU 接口方法类
 * @author LukeZhang
 * @date 2023/7/4 10:04
 */
public interface CpuService {
    /**
     * 指定字段排序
     * @param page 分页字段
     * @param sortParam 排序字段
     * @return 分页信息
     */
    public IPage<CpuVO> getSortedEntities(Page<CpuVO> page, SortParam sortParam, String key);

    /**
     * 获取多个id的CPU详情信息
     * @param ids id列表
     * @return CPU详情列表
     */
    public List<CpuVO> getRankCpu(List<String> ids);
}
