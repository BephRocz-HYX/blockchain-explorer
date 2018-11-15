/**
 ** ********************************************************
 ** @file block-pagination.js
 ** @author zhongxian_liang <zhongxian_liang@kingdee.com>
 ** @date 2018-02-07 16:19:39
 ** @last_modified_by zhongxian_liang <zhongxian_liang@kingdee.com>
 ** @last_modified_date 2018-02-07 19:02:43
 ** @copyright (c) 2018 @yfe/new-supply-chain-frontend
 ** ********************************************************
 */

const getRandomHash = () => (Math.random() * 1e17).toString(17);
const getBlockId = () => Math.floor(Math.random() * 1000);
const getTransactionNum = () => Math.floor(Math.random() * 10);

export const getPaginationData = (len = 5) =>
  Array(len).fill(0).map(
    () => ({
      blockId: getBlockId(),
      blockHash: getRandomHash(),
      dataHash: getRandomHash(),
      transactionNum: getTransactionNum()
    })
  );
