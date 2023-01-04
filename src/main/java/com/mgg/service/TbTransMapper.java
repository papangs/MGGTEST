package com.mgg.service;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.mgg.model.master.TbTrans;

@Mapper
public interface TbTransMapper {

    public TbTrans find0100Commit(HashMap<String, Object> params) throws Exception;
    public List<TbTrans> findTxBeforePayment(HashMap<String, Object> params) throws Exception;
    public Integer deleteByTxIdBeforePayment(HashMap<String, Object> params) throws Exception;
    public TbTrans findTxVacctNo(HashMap<String, Object> params) throws Exception;
    public Integer insertTxId(HashMap<String, Object> params) throws Exception;
    public Integer updateTxId0203(HashMap<String, Object> params) throws Exception;
    public Integer updateTxId0403(HashMap<String, Object> params) throws Exception;
    public Integer updateTxIdd(HashMap<String, Object> params) throws Exception;

}
