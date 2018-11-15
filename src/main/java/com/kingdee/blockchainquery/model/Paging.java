package com.kingdee.blockchainquery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paging implements Serializable {

    private Integer start;
    private Integer pageSize;

}
