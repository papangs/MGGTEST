package com.mgg.service;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.mgg.model.master.Mparam;

@Mapper
public interface MparamMapper {

	public List<Mparam> getAllParam();

    public Mparam findById(String ID) throws Exception;

    public String findParamPassword(HashMap<String, Object> params) throws Exception;

    public Integer updateParam(HashMap<String, Object> params) throws Exception;

    public String getParam(@Param("grp_param") String grpParam, @Param("kd_param") String kdParam) throws Exception;
}
