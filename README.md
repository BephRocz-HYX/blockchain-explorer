# 区块链查询后端接口文档



- [区块链查询后端接口文档](#区块链查询后端接口文档)
	- [说明](#说明)
		- [API前缀](#api前缀)
		- [数据格式](#数据格式)
		- [通用错误码](#通用错误码)
	- [API详细描述](#api详细描述)
		- [查询相关](#查询相关)
			- [查询基础信息](#查询基础信息)
			- [查询所有区块信息](#查询所有区块信息)
			- [查询所有交易信息](#查询所有交易信息)
			- [获取单个区块详细信息](#获取单个区块详细信息)
			- [获取单个交易详细信息](#获取单个交易详细信息)
			- [统一搜索入口](#统一搜索入口)
			- [获取节点列表](#获取节点列表)
		- [websocket获取是否有数据更新](#websocket获取是否有数据更新)


## 说明

### API前缀
测试PI接口前缀为： http://172.20.166.144:8080/BlockChainQuery 

正式API接口前缀为： http://123.207.51.234:50090/BlockChainQuery （在服务器上）

若前端上传到相同服务器，用内网ip调用为: http://10.244.4.153:50090/BlockChainQuery

### 数据格式
接口正常情况下返回json格式的数据，格式如下：

```json
{
    "errcode": 0,
    "description": "查询成功",
    "data": [{
        //some data...JSONArray格式
    }]
}
```

### 通用错误码
通用错误码为int型

错误码 | 说明
---- | ----
  0  | 操作成功
1001 | 操作失败


## API详细描述

### 查询相关
#### 查询基础信息

**创建版本号：** 1.0

**更新版本号：** 1.0

**URL：** /query/basicinfo

**请求方式：** POST

**参数：** 暂无


**返回：**
```json
{
    "errcode": 0,
    "description": "获取基础信息成功！",
    "data": 
        {
           "transactionNum":100,
           "blockHeight":100,
           "runDays":100,
        }
}
```
**返回说明：**

data: JSONObject,为空时是个空对象


#### 查询所有区块信息

**创建版本号：** 1.0

**更新版本号：** 1.0

**URL：** /query/block

**请求方式：** POST

**参数**

参数名 | 是否必须 | 类型 | 说明
----| ---- | ---- | ----
start | 是 | int | 查询开始位置
pageSize | 是 | int | 查询一页的数量


**返回：**
```json
{
    "errcode": 0,
    "description": "查找区块信息成功！",
    "data":{
      "data": [
       {
	"previousHash": "d09828447543e310b161a566634b040db699c1e840bf4acb352ecd779f83fc6c",
	"dataHash": "261025ce950fd7b50e98e3134c0f87a88a2410806fbee9f278cb8b1a462488d7",
	"blockNumber": 192,
	"currentHash": "034f0afdadb221b635fff65298afa0f7e5e54b76928a064406bdcab606297aee",
	"transactionInfos": [{
		"epoch": 0,
		"type": "TRANSACTION_ENVELOPE",
		"timeStampS": 1515485380918,
		"timeStampNYR": "2018-1-9 16:09:40",
		"transactionId": "5e94ab3c344b717dd8e821a282ebd93e56e4b4c3b383fe6fc4421a5ed7b5093f",
		"channelId": "composerchannel"
	}],
	"envelopeCount": 1,
	"channelId": "composerchannel"
      }, 
      {
	"previousHash": "d9163bf59da28617e35950a5a757556e775d8b08b2c8ff580b0b9772a6ad3572",
	"dataHash": "9de701a77183850ab74d1a9c8554dfe70203ff599fc93fe24bdf49bd6e9187c2",
	"blockNumber": 191,
	"currentHash": "d09828447543e310b161a566634b040db699c1e840bf4acb352ecd779f83fc6c",
	"transactionInfos": [{
		"epoch": 0,
		"type": "TRANSACTION_ENVELOPE",
		"timeStampS": 1515374914788,
		"timeStampNYR": "2018-1-8 9:28:34",
		"transactionId": "b52b8588ef86829451f397d4f00f95bf52eb9a24c02645f8e5cbe99a4a00f294",
		"channelId": "composerchannel"
	}],
	"envelopeCount": 1,
	"channelId": "composerchannel"
      }
    ],
    "blockHeight":192
    }
}
```

**返回说明：**

data: JSONArray类型,为空时是个空数组


#### 查询所有交易信息

**创建版本号：** 1.0

**更新版本号：** 1.0

**URL：** /query/transaction

**请求方式：** POST

**参数：**

参数名 | 是否必须 | 类型 | 说明
----| ---- | ---- | ----
start | 是 | int | 查询开始位置
pageSize | 是 | int | 查询一页的数量


**返回：**
```json
{
    "errcode": 0,
    "description": "查找某个用户所有资产成功！",
    "data":{
      "data": [
        {
	"payload": "payload: ..."
	"blockNumber": 192,
	"currentHash": "034f0afdadb221b635fff65298afa0f7e5e54b76928a064406bdcab606297aee",
	"epoch": 0,
	"type": "TRANSACTION_ENVELOPE",
	"timeStampS": 1515485380918,
	"timeStampNYR": "2018-1-9 16:09:40",
	"transactionId": "5e94ab3c344b717dd8e821a282ebd93e56e4b4c3b383fe6fc4421a5ed7b5093f",
	"channelId": "composerchannel"
}, {
	"payload": "payload: ..."
	"blockNumber": 191,
	"currentHash": "d09828447543e310b161a566634b040db699c1e840bf4acb352ecd779f83fc6c",
	"epoch": 0,
	"type": "TRANSACTION_ENVELOPE",
	"timeStampS": 1515374914788,
	"timeStampNYR": "2018-1-8 9:28:34",
	"transactionId": "b52b8588ef86829451f397d4f00f95bf52eb9a24c02645f8e5cbe99a4a00f294",
	"channelId": "composerchannel"
}
    ],
    "transactionNum":192
    }
}
```
**返回说明：**

data: JSONArray类型,为空时是个空数组

#### 获取单个区块详细信息

**创建版本号：** 1.0

**更新版本号：** 1.0

**URL：** /query/blockinfo

**请求方式：** POST

**参数：** 

参数名 | 是否必须 | 类型 | 说明
----| ---- | ---- | ----
key | 是 | String | 字段的名称（如：blockNumber）
value | 是 | String | 字段的值(统一转成String传过来)



**返回：**
```json
{
    "errcode": 0,
    "description": "查找区块信息成功！",
    "data": {
	"previousHash": "20f5ca6d43986464b1bbc421f6e0e306f5061ac06e25f0a22a7b4d97b6cc1d83",
	"dataHash": "ac232058bfe84cd393e540c89b6a83e0c1c85e3265c715a150f2e7feb0b5c532",
	"blockNumber": 10,
	"currentHash": "9dc1c94251028bafb0ac501ff99c356e75b1cf0949e19ad56c94fc5b2f1544b9",
	"transactionInfos": [{
		"epoch": 0,
		"type": "TRANSACTION_ENVELOPE",
		"timeStampS": 1513913148362,
		"timeStampNYR": "2017-12-22 11:25:48",
		"transactionId": "5dd8678593d787849f8d002d4d09781070296e7ca07e4c4eb3a1f385a490cf44",
		"channelId": "composerchannel"
	}],
	"envelopeCount": 1,
	"channelId": "composerchannel"
    }
}
```



#### 获取单个交易详细信息

**创建版本号：** 1.0

**更新版本号：** 1.0

**URL：** /query/transactioninfo

**请求方式：** POST

**参数：** 

参数名 | 是否必须 | 类型 | 说明
----| ---- | ---- | ----
key | 是 | String | 字段的名称（如：transactionId）
value | 是 | String | 字段的值(统一转成String传过来)


**返回：**
```json

{
    "errcode": 0,
    "description": "查找交易信息成功！",
    "data": {
	"payload": "payload: ..."
	"blockNumber": 3,
	"currentHash": "645b659037abf48edd6c02a9b7207c6c781a6479153ae669c7948a30573a87e8",
	"epoch": 0,
	"type": "TRANSACTION_ENVELOPE",
	"timeStampS": 1513767567471,
	"timeStampNYR": "2017-12-20 18:59:27",
	"transactionId": "62d9ebcec68509fe44c1d5a3ac91c5e3506f03b6457b1e50634c45b5976bd46b",
	"channelId": "composerchannel"
     }
}
```

#### 统一搜索入口

**创建版本号：** 1.0

**更新版本号：** 1.0

**URL：** /query/allsearch

**请求方式：** POST

**参数：** 

参数名 | 是否必须 | 类型 | 说明
----| ---- | ---- | ----
search | 是 | String | 搜索的内容



**返回：**
参考
获取单个区块详细信息
获取单个交易详细信息
的返回结果

#### 获取节点列表

**创建版本号：** 1.0

**更新版本号：** 1.0

**URL：** /query/peers

**请求方式：** POST

**参数：** 暂无


**返回：**
```json
{
    "errcode": 0,
    "description": "获取基础信息成功！",
    "data": [
        {
           "name":"xxx",
           "url":"xxx",
           "state":"运行",
        },
        ...
    ]
}
```
**返回说明：**

data: JSONArray,为空时是个空数组

#### websocket获取是否有数据更新

**创建版本号：** 1.0

**更新版本号：** 1.0

**URL：** /

**请求方式：** websocket

**参数：** 暂无


**返回：**
String:
"chainUpdate"/
"peerUpdate"

**说明：**

Json格式的字符串，这两个boolean值为true时就分别调一次对应的节点、链码区块相关的接口刷新（peer对应节点状态刷新，chain对应首页其余所有接口的刷新）
