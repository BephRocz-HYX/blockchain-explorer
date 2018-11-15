package com.kingdee.blockchainquery.service;

import com.kingdee.blockchainquery.dao.FabricDao;
import com.kingdee.blockchainquery.model.*;
import com.kingdee.blockchainquery.web.util.ResponseUtil;
import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */
@Service
@Slf4j
@AllArgsConstructor
public class BlockChainService {

    private final FabricDao fabricDao;

    public Object findBlock(Paging page) throws IOException {

        String pattern = "\\{\"key.+?version.+?blockNum\":0.+?txNum\":0}}";
        JSONObject resultData = new JSONObject();
        int start = page.getStart().intValue();
        int pageSize = page.getPageSize().intValue();

        List<JSONObject> datas = fabricDao.getJsonDatasByRange("blockinfo", start, pageSize, DBObject.class);
        List<JSONObject> newDatas = fabricDao.setPayload(datas);
        int bNUM = fabricDao.getMostBlockNum("blockinfo");
        JSONObject data = new JSONObject();
        data.put("data", newDatas.toString().replaceAll(pattern, ""));
        data.put("blockHeight", Integer.valueOf(bNUM - 1));
        resultData = datas.isEmpty() ? ResponseUtil.jsonResponse(resultData, 1001, data) : ResponseUtil.jsonResponse(resultData, 0, data);
        return resultData;
    }

    public Object findBlockInfo(KeyValue kv) throws IOException {

        JSONObject resultData = new JSONObject();
        String key = kv.getKey();
        String value = kv.getValue();
        JSONObject data = fabricDao.findJsonDataByKV("blockinfo", key, value, DBObject.class);
        resultData = data.isEmpty() ? ResponseUtil.jsonResponse(resultData, 1001, new JSONArray()) : ResponseUtil.jsonResponse(resultData, 0, data);
        return resultData;
    }

    public Object findTransaction(Paging page) throws IOException {

        String pattern = "\\{\"key.+?version.+?blockNum\":0.+?txNum\":0}}";
        JSONObject resultData = new JSONObject();
        int start = page.getStart().intValue();
        int pageSize = page.getPageSize().intValue();
        List<JSONObject> datas = fabricDao.getJsonDatasByRange("transactioninfo", start, pageSize, DBObject.class);

        String dataMacter = datas.toString().replaceAll(pattern, "");
        int tNUM = fabricDao.getMostBlockNum("transactioninfo");
        JSONObject data = new JSONObject();
        data.put("data", dataMacter);
        data.put("transactionNum", Integer.valueOf(tNUM));

        resultData = datas.isEmpty() ? ResponseUtil.jsonResponse(resultData, 1001, data) : ResponseUtil.jsonResponse(resultData, 0, data);
        return resultData;
    }

    public Object findTransactionInfo(KeyValue kv) throws IOException {

        JSONObject resultData = new JSONObject();
        String key = kv.getKey();
        String value = kv.getValue();

        JSONObject data = fabricDao.findJsonDataByKV("transactioninfo", "transactionInfo." + key, value, DBObject.class);
        JSONObject data2 = fabricDao.findJsonDataByKV("blockinfo", "blockNumber", String.valueOf(data.get("blockNumber")), DBObject.class);

        data.put("previousHash", data2.getString("previousHash"));
        data.put("envelopeCount", data2.getString("envelopeCount"));
        data.put("dataHash", data2.getString("dataHash"));

        resultData = data.isEmpty() ? ResponseUtil.jsonResponse(resultData, 1101, new JSONArray()) : ResponseUtil.jsonResponse(resultData, 0, data);
        return resultData;
    }

    public Object findBasicInfo() {

        JSONObject resultData = new JSONObject();
        try {
            int tNum = fabricDao.getMostBlockNum("transactioninfo");
            int bNUM = fabricDao.getMostBlockNum("blockinfo");
            JSONObject data = new JSONObject();
            data.put("transactionNum", Integer.valueOf(tNum));
            data.put("blockHeight", Integer.valueOf(bNUM - 1));
            long start = 0L;
            start = Long.valueOf(fabricDao.getFirstData("transactioninfo", DBObject.class).getString("timeStampS"));
            long now = System.currentTimeMillis();
            int days = (int) ((now - start) / 86400000L);
            data.put("runDays", Integer.valueOf(days));
            data.put("startTimeStamp", Long.valueOf(start));
            resultData =ResponseUtil.jsonResponse(resultData,0,data);
        } catch (Exception e) {
            log.error("获取基础信息失败", e.toString());
        }
        return resultData;
    }


    public JSONObject findAllSearch(Search search) throws IOException {

        JSONObject resultData = new JSONObject();
        String searchWord = search.getSearch();
        JSONObject data = new JSONObject();
        String pattern = "\\{\"key.+?version.+?blockNum\":0.+?txNum\":0}}";
        boolean isNumber = StringUtils.isNumeric(searchWord);
        try {

            int blockNum = Integer.parseInt(searchWord);
            data = fabricDao.findJsonDataByKV("blockinfo", "blockNumber", String.valueOf(blockNum), DBObject.class);
            if (!data.isEmpty()) {
                data = fabricDao.setPayload(data);
                data.put("type", Integer.valueOf(1));
            }
        } catch (Exception e) {
            data = fabricDao.findJsonDataByKV("transactioninfo", "transactionInfo.transactionId", searchWord, DBObject.class);

            if (data.isEmpty()) {
                data = fabricDao.findJsonDataByKV("blockinfo", "currentHash", searchWord, DBObject.class);
                log.info("data1:{}", data);
                if (!data.isEmpty()) {
                    data = fabricDao.setPayload(data);
                    data.put("type", Integer.valueOf(2));
                }
            } else {

                JSONObject data2 = fabricDao.findJsonDataByKV("blockinfo", "blockNumber", data.getString("blockNumber"), DBObject.class);
                data.put("previousHash", data2.getString("previousHash"));
                data.put("envelopeCount", data2.getString("envelopeCount"));
                data.put("dataHash", data2.getString("dataHash"));
                if (!data.isEmpty()) {
                    data.put("type", Integer.valueOf(3));
                }
            }
        }

        resultData = data.isEmpty() ? ResponseUtil.jsonResponse(resultData, 1101, new JSONArray()) : ResponseUtil.jsonResponse(resultData, 0, data.toString().replaceAll(pattern, ""));
        return resultData;
    }

    public Object findIndex() {

        JSONObject resultData = new JSONObject();
        JSONArray datas = new JSONArray();
        datas = JSONArray.fromObject(fabricDao.getJsonDatas("peerinfo", PeerState.class));
        resultData = datas.isEmpty() ? ResponseUtil.jsonResponse(resultData, 1101, datas) : ResponseUtil.jsonResponse(resultData, 0, datas);
        return resultData;
    }
}
