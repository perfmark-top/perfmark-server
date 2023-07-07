package io.github.perfmarktop.vo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortParam {
    public String field;
    public String order;
}
