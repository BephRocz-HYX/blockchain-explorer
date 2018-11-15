package com.kingdee.blockchainquery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2018/3/15.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeerState {

    private String _id;

    private String name;

    private String url;

    private String state;

    private String _class;

}
