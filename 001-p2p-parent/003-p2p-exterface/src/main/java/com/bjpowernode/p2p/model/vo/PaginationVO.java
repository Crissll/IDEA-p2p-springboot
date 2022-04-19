package com.bjpowernode.p2p.model.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationVO<T> implements Serializable {

    //总页数
    private Integer totalSize;

    //分页数据
    private List<T> datas;
}
