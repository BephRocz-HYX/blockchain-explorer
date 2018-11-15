package com.kingdee.blockchainquery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable{

    private String transactionId;

    private String type;

    private long timeStampS;

    private int epoch;

}
