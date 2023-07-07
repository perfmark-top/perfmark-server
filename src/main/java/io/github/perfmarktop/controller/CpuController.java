package io.github.perfmarktop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.perfmarktop.common.R;
import io.github.perfmarktop.service.CpuService;
import io.github.perfmarktop.vo.CpuVO;
import io.github.perfmarktop.vo.param.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cpuData")
public class CpuController {
    @Autowired
    private CpuService service;

    /**
     * 传入排序参数，页码， 返回CPU数据
     * @param current 当前页
     * @param size 每页数量
     * @param field 排序字段
     * @param order 排序方式
     * @param key  关键字
     * @return CPU数据信息
     */

    @GetMapping("/sortCpuData")
    public R getSortCpuData(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String key)
    {
        Page<CpuVO> page = new Page<>(current, size);
        SortParam sortParam = new SortParam(field,order);
        IPage<CpuVO> sortedEntities = service.getSortedEntities(page, sortParam,key);
        return R.ok(sortedEntities);
    }

    /**
     * 获取一条或者多条CPU数据
     * @param ids id列
     * @return CPU数据
     */
    @GetMapping("/getRankCpu")
    public R getRankCpu (@RequestBody List<String> ids) {
        List<CpuVO> rankCpu = service.getRankCpu(ids);
        return R.ok(rankCpu);
    }


}
