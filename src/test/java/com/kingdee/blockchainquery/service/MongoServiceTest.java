package com.kingdee.blockchainquery.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by Administrator on 2018/3/14.
 */
public class MongoServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void getConnect() throws Exception {

        mongoTemplate.getCollection("111");

    }

    @Test
    public void getJsonDatas() throws Exception {

    }

    @Test
    public void getFirstData() throws Exception {

    }

    @Test
    public void getMostBlockNum() throws Exception {

    }

    @Test
    public void getJsonDatasByRange() throws Exception {

    }

    @Test
    public void findJsonDataByKV() throws Exception {

    }

    @Test
    public void setJsonArrayData() throws Exception {

    }

    @Test
    public void saveBlockObject() throws Exception {

    }

    @Test
    public void saveTranactionObject() throws Exception {

    }

    @Test
    public void queryBlockNumberFromMongo() throws Exception {

    }

    @Test
    public void dropDataBase() throws Exception {

    }

}