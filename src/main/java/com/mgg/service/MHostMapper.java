package com.mgg.service;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.mgg.model.master.MHost;

@Mapper
public interface MHostMapper {

	public List<MHost> getAllHost();

    public MHost findByBankCode(HashMap<String, Object> KD_BANK) throws Exception;
}
