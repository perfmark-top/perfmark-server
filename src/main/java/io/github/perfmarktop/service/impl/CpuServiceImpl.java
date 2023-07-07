package io.github.perfmarktop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.perfmarktop.mapper.CpuDataMapper;
import io.github.perfmarktop.pojo.CpuData;
import io.github.perfmarktop.service.CpuService;
import io.github.perfmarktop.vo.CpuVO;
import io.github.perfmarktop.vo.param.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CpuServiceImpl implements CpuService {
    @Autowired
    private CpuDataMapper cpuDataMapper;

    @Override
    public IPage<CpuVO> getSortedEntities(Page<CpuVO> page, SortParam sortParam, String key) {
        IPage<CpuVO> iPage = cpuDataMapper.getSortedCpuData(page, sortParam, key);
        return iPage;
    }

    @Override
    public List<CpuVO> getRankCpu(List<String> ids) {
        return cpuDataMapper.getCpuByIds(ids);
    }

}
