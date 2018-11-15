package com.kingdee.blockchainquery.web;

import com.kingdee.blockchainquery.model.KeyValue;
import com.kingdee.blockchainquery.model.Paging;
import com.kingdee.blockchainquery.model.Search;
import com.kingdee.blockchainquery.service.BlockChainService;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping({"/BlockChainQuery"})
public class BlockChainController {

    private final BlockChainService blockChainService;

    @PostMapping(path = "/query/block", consumes = "application/json;charset=UTF-8")
    public Object findBlock(@RequestBody Paging page) throws IOException {

        return blockChainService.findBlock(page);
    }

    @PostMapping(path = "/query/blockinfo", consumes = "application/json;charset=UTF-8")
    public Object findBlockInfo(@RequestBody KeyValue kv) throws IOException {

        return blockChainService.findBlockInfo(kv);
    }

    @PostMapping(path = "/query/transaction", consumes = "application/json;charset=UTF-8")
    public Object findTransaction(@RequestBody Paging page) throws IOException {

        return blockChainService.findTransaction(page);
    }

    @PostMapping(path = "/query/transactioninfo", consumes = "application/json;charset=UTF-8")
    public Object findTransactionInfo(@RequestBody KeyValue kv) throws IOException {

        return blockChainService.findTransactionInfo(kv);
    }

    @PostMapping(path = "/query/allsearch", consumes = "application/json")
    public JSONObject allSearch(@RequestBody Search search) throws IOException {

        return blockChainService.findAllSearch(search);
    }

    @PostMapping("/query/basicinfo")
    public Object findBasicInfo() {

        return blockChainService.findBasicInfo();
    }

    @PostMapping("/query/peers")
    public Object findIndex() {

        return blockChainService.findIndex();
    }
}
