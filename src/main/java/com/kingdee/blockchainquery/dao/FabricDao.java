package com.kingdee.blockchainquery.dao;

import com.google.gson.Gson;
import com.kingdee.blockchainquery.config.MongoConfig;
import com.kingdee.blockchainquery.util.FabricUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@AllArgsConstructor
public class FabricDao {

    private static Logger log = LoggerFactory.getLogger(FabricDao.class);
    private final MongoTemplate mongoTemplate;
    private final MongoConfig mongo;

    public <T> List<net.sf.json.JSONObject> getJsonDatas(String collectionName, Class<T> foo) {

        List<net.sf.json.JSONObject> result = new ArrayList();
        MongoCollection docu = mongoTemplate.getCollection(collectionName);
        FindIterable iter = docu.find();
        MongoCursor cur = iter.iterator();

        while (cur.hasNext()) {

            JSONObject jsonObject = null;
            String json = cur.next().toString().replace("Document{", "").replace("}}", "}");
            Gson gson = new Gson();

            if (collectionName.equals("peerinfo")) {
                json = json.replace("grpc://", "").replace(":", "a");
                jsonObject = JSONObject.fromObject(gson.fromJson(json, foo));
                jsonObject.remove("_id");
                jsonObject.remove("_class");
                jsonObject.replace("url", jsonObject.getString("url").replace("a", ":"));
                result.add(net.sf.json.JSONObject.fromObject(jsonObject));
                continue;
            }

            jsonObject = JSONObject.fromObject(gson.fromJson(json, foo));
            jsonObject.remove("_id");
            jsonObject.remove("_class");
            result.add(net.sf.json.JSONObject.fromObject(jsonObject));
        }

        cur.close();
        return result;
    }

    public <T> net.sf.json.JSONObject getFirstData(String collectionName, Class<T> foo) throws IllegalAccessException {

        net.sf.json.JSONObject result = new net.sf.json.JSONObject();
        MongoCollection docu = mongoTemplate.getCollection(collectionName);
        FindIterable iter = docu.find();
        MongoCursor cur = iter.iterator();

        while (cur.hasNext()) {

            Object object = cur.next();
            JSONObject jsonObject = new JSONObject();

            Class<?> s = object.getClass();
            for (Field f : object.getClass().getDeclaredFields()) {   //遍历通过反射获取object的类中的属性名
                f.setAccessible(true);    //设置改变属性为可访问
                jsonObject.put(f.getName(), f.get(object));
            }

            jsonObject.remove("serialVersionUID");
            result = net.sf.json.JSONObject.fromObject(jsonObject.getJSONObject("documentAsMap"));
            break;
        }
        return result;
    }

    /**
     * 获取最大区块数目
     *
     * @param collectionName 对应的集合名称
     * @return
     */
    public int getMostBlockNum(String collectionName) {

        return (int) mongoTemplate.getCollection(collectionName).count();
    }

    public <T> List<net.sf.json.JSONObject> getJsonDatasByRange(String collectionName, int start, int pageSize, Class<T> cla) throws IOException {

        List<net.sf.json.JSONObject> result = new ArrayList();

        Query query = new Query();
        query.skip(start).limit(pageSize).with(new Sort(new Sort.Order(Sort.Direction.DESC, "blockNumber")));
        List<T> foos = mongoTemplate.find(query, cla, collectionName);

        for (T f : foos) {

            JSONObject json = null;
            JSONObject j = JSONObject.fromObject(FabricUtil.objectToJson(f));
            j.remove("_id");
            j.remove("_class");

            if (collectionName.equals("transactioninfo")) {
                if (j.containsKey("transactionInfo")) {
                    net.sf.json.JSONObject info = j.getJSONObject("transactionInfo");
                    Iterator it = info.keys();
                    while (it.hasNext()) {
                        String key = it.next().toString();
                        json = key.equals("transactionInfo") ? j.accumulate("payload", parsePayload(info.get(key).toString())) : j.accumulate(key, info.get(key));
                    }
                    j.remove("transactionInfo");
                }
            }
            result.add(j);
        }
        return result;
    }

    public <T> net.sf.json.JSONObject findJsonDataByKV(String collectionName, String key, String value, Class<T> cla) throws IOException {

        net.sf.json.JSONObject result = new net.sf.json.JSONObject();
        Query query = new Query();
        query = key.equals("blockNumber") ? query.addCriteria(new Criteria().where(key).is(Integer.valueOf(value))) : query.addCriteria(new Criteria().where(key).is(value));

        List<T> foos = mongoTemplate.find(query, cla, collectionName);

        for (T f : foos) {

            JSONObject json = new JSONObject();
            JSONObject j = JSONObject.fromObject(FabricUtil.objectToJson(f));
            j.remove("_id");
            j.remove("_class");

            if ((collectionName.equals("transactioninfo")) && (j.containsKey("transactionInfo"))) {
                net.sf.json.JSONObject info = j.getJSONObject("transactionInfo");
                Iterator it = info.keys();
                while (it.hasNext()) {

                    String k = it.next().toString();
                    json = k.equals("transactionInfos") ? j.accumulate("payload", parsePayload(info.get(k).toString())) : j.accumulate(k, info.get(k));
                }
                json.remove("transactionInfo");
            } else {
                return j;
            }
            result = json;
        }
        return result;
    }

    public void setJsonArrayData(String collectionName, JSONArray datas) {
        mongoTemplate.getCollection(collectionName).drop();

        for (Object data : datas) {
            net.sf.json.JSONObject temp = net.sf.json.JSONObject.fromObject(data);
            if (!temp.getString("url").isEmpty()) {
                BasicDBObject object = new BasicDBObject();
                BasicDBObject oldobject = new BasicDBObject();
                oldobject.put("name", temp.getString("name"));
                oldobject.put("url", temp.getString("url"));
                object.put("name", temp.getString("name"));
                object.put("url", temp.getString("url"));
                if (temp.getString("status").equals("up")) {
                    object.put("state", "运行中");
                }
                if (temp.getString("status").equals("down")) {
                    object.put("state", "未连通");
                }
                mongoTemplate.save(object, collectionName);
            }
        }
    }

    /**
     * 保存区块数据到mongo
     *
     * @param jsonObject 数据对象
     */
    public void saveBlockObject(net.sf.json.JSONObject jsonObject) {
        try {
            mongoTemplate.save(jsonObject.toString(), mongo.getBlock());
        } catch (Exception e) {
            log.error("保存blockinfo错误：{}", e);
        }
    }

    /**
     * 保存交易信息到mongo
     *
     * @param jsonObject
     */
    public void saveTranactionObject(org.json.JSONObject jsonObject) {
        try {
            mongoTemplate.save(jsonObject.toString(), mongo.getTransaction());
        } catch (Exception e) {
            log.error("保存交易信息错误：{}", e);
        }
    }

    /**
     * 从blockinfo集合里获取区块高度
     *
     * @return
     */
    public int queryBlockNumberFromMongo() {
        try {
            return (int) mongoTemplate.getCollection(mongo.getBlock()).count();
        } catch (Exception e) {
            log.error("从mongo获取区块高度：{}", e);
        }
        return 0;
    }

    /**
     * 删除对应的collection
     */
    public void dropDataBase() {

        mongoTemplate.dropCollection(mongo.getBlock());

        mongoTemplate.dropCollection(mongo.getPeer());

        mongoTemplate.dropCollection(mongo.getTransaction());
    }

    public List<JSONObject> setPayload(List<JSONObject> datas) {

        List<JSONObject> newDatas = new ArrayList();

        for (JSONObject temp : datas) {
            try {
                JSONArray transactionInfos = temp.getJSONArray("transactionInfos");

                JSONArray tinfos = new JSONArray();
                for (Object trans : transactionInfos) {

                    JSONObject info = JSONObject.fromObject(trans);
                    String id = info.getString("transactionId");
                    JSONObject transData = findJsonDataByKV("transactioninfo", "transactionInfo.transactionId", id, DBObject.class);
                    try {
                        info.put("payload", parsePayload(transData.get("payload").toString()));
                    } catch (Exception e) {
                        log.error("添加payload值错误：{}", e);
                    }
                    tinfos.add(info);
                }
                temp.put("transactionInfos", tinfos);
                newDatas.add(temp);
            } catch (Exception e) {
                log.error("获取transactionInfos信息错误：{}", e);
            }
        }
        return newDatas;
    }

    public JSONObject setPayload(JSONObject temp) {
        JSONObject newDatas = new JSONObject();
        try {
            JSONArray transactionInfos = temp.getJSONArray("transactionInfos");
            JSONArray tinfos = new JSONArray();
            for (Object trans : transactionInfos) {
                JSONObject info = JSONObject.fromObject(trans);
                String id = info.getString("transactionId");
                JSONObject transData = findJsonDataByKV("transactioninfo", "transactionInfo.transactionId", id, DBObject.class);
                try {
                    info.put("payload", parsePayload(transData.get("payload").toString()));
                } catch (Exception e) {
                    log.error("添加payload值错误：{}", e);
                }
                tinfos.add(info);
            }
            temp.put("transactionInfos", tinfos);
            newDatas = temp;
        } catch (Exception e) {
            log.error("获取transactionInfos信息错误：{}", e);
        }
        return newDatas;
    }

    public String parsePayload(String payload) {
        JSONArray jsonArray = JSONArray.fromObject(payload);
        Iterator<JSONObject> json = jsonArray.iterator();
        JSONArray jsonArray1 = new JSONArray();
        while (json.hasNext()) {

            JSONObject jsonObject = json.next();
            String reads = jsonObject.get("reads").toString();

            JSONArray jsonArrayReads = parseReads(reads);
            jsonObject.remove("reads");
            jsonObject.put("reads", jsonArrayReads);

            jsonArray1.add(jsonObject);
        }
        return jsonArray1.toString();
    }

    public JSONArray parseReads(String reads) {
        JSONArray jsonArray = JSONArray.fromObject(reads);
        for (int i = 0; i < jsonArray.size(); i++) {
            Object object = jsonArray.get(i);
            if (object.toString().contains("\"version\":{\"txNum\":0,\"blockNum\":0}")) {
                jsonArray.remove(object);
            }
        }
        return jsonArray;
    }
}
