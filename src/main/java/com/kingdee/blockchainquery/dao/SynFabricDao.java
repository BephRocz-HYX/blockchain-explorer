package com.kingdee.blockchainquery.dao;

import com.kingdee.kchain.fabric.sdk.channel.Block;
import com.kingdee.kchain.fabric.sdk.channel.ChannelAccessor;
import com.kingdee.kchain.fabric.sdk.channel.Envelope;
import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class SynFabricDao {

    private final FabricDao mongoService;
    private final ChannelAccessor channel;

    private static Logger log = LoggerFactory.getLogger(SynFabricDao.class);

    public  int getNumber() {
        return (int) channel.blockchainHeight();
    }

    public  synchronized boolean saveBlockToMongo()
            throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        int mongoNumber = mongoService.queryBlockNumberFromMongo();
        log.info("mongoNumber:{}", Integer.valueOf(mongoNumber));
        int blockHeight = getNumber();
        log.info("blockHeight:{}", Integer.valueOf(blockHeight));
        boolean flag = false;

        int number = blockHeight - mongoNumber;
        log.info("number:{}", Integer.valueOf(number));
        for (int j = 0; j < number; j++) {
            Block block = channel.queryBlockByNumber(mongoNumber + j);
            net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
            jsonObject.put("channelId", block.getChannelId());
            jsonObject.put("blockNumber", Long.valueOf(block.getBlockNumber()));
            jsonObject.put("dataHash", block.getDataHash());
            jsonObject.put("currentHash", block.getHash());
            jsonObject.put("previousHash", block.getPreviousHash());
            jsonObject.put("envelopeCount", Integer.valueOf(block.getEnvelopeCount()));
            JSONArray re = new JSONArray();
            List<Envelope> temp = block.getEnvelopes();
            for (Envelope t : temp) {
                net.sf.json.JSONObject tem = new net.sf.json.JSONObject();
                tem.put("transactionId", t.getId());
                tem.put("epoch", Long.valueOf(t.getEpoch()));
                tem.put("type", t.getType());
                try {
                    Date date = format.parse(t.getTimestamp());
                    tem.put("timeStampS", Long.valueOf(date.getTime()));
                } catch (Exception e) {
                    Date date = format.parse(t.getTimestamp().toString().split("Z")[0] + ".000Z");
                    tem.put("timeStampS", Long.valueOf(date.getTime()));
                }
                re.add(tem);
            }
            jsonObject.put("transactionInfos", re);


            mongoService.saveBlockObject(jsonObject);
            JSONArray jsonObjectArray = JSONArray.fromObject(jsonObject.get("transactionInfos"));
            int a = jsonObjectArray.size();
            for (int s = 0; s < a; s++) {
                String data = jsonObjectArray.get(s).toString();
                net.sf.json.JSONObject jsonObjectData = net.sf.json.JSONObject.fromObject(data);
                String transactionId = jsonObjectData.get("transactionId").toString().replace(" ", "").trim();
                if (!transactionId.equals("")) {
                    net.sf.json.JSONObject trans = jsonObject;
                    trans.remove("transactionInfos");
                    net.sf.json.JSONObject en = net.sf.json.JSONObject.fromObject(channel.queryTransactionById(transactionId));
                    net.sf.json.JSONObject transactionInfo = new net.sf.json.JSONObject();
                    transactionInfo.put("transactionId", en.getString("id"));
                    transactionInfo.put("epoch", en.get("epoch"));
                    transactionInfo.put("type", en.get("type"));
                    try {
                        Date date = format.parse(en.get("timestamp").toString());
                        trans.put("timeStampS", Long.valueOf(date.getTime()));
                    } catch (ParseException e) {
                        Date date = format.parse(en.get("timestamp").toString().split("Z")[0] + ".000Z");
                        trans.put("timeStampS", Long.valueOf(date.getTime()));
                    }
                    JSONArray transactionActions = JSONArray.fromObject(en.get("transactionActions"));

                    JSONArray payload = new JSONArray();
                    for (Object pay : transactionActions) {
                        net.sf.json.JSONObject tt = net.sf.json.JSONObject.fromObject(pay);
                        JSONArray rwset = tt.getJSONArray("rwSets");
                        for (Object rw : rwset) {
                            net.sf.json.JSONObject r = net.sf.json.JSONObject.fromObject(rw);
                            if (!r.getString("namespace").equals("lscc")) {
                                payload.add(r);
                            }
                        }
                    }
                    transactionInfo.put("transactionInfos", payload);
                    trans.put("transactionInfo", transactionInfo);
                    String transaction = trans.toString();
                    mongoService.saveTranactionObject(new org.json.JSONObject(transaction));
                } else {
                    org.json.JSONObject jsonObjectNull = new org.json.JSONObject(jsonObjectArray.get(s).toString());
                    jsonObjectNull.put("payload", "");
                    jsonObjectNull.put("blockNumber", Integer.valueOf(jsonObject.get("blockNumber").toString()));
                    jsonObjectNull.put("currentHash", jsonObject.getString("currentHash").toString());
                    mongoService.saveTranactionObject(jsonObjectNull);
                }
            }
        }
        if (number == 0) {
            return flag;
        }
        flag = true;
        return flag;
    }
}
