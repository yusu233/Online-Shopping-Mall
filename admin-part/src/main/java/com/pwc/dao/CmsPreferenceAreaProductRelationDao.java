package com.pwc.dao;

import com.pwc.model.CmsPreferenceAreaProductRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CmsPreferenceAreaProductRelationDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<CmsPreferenceAreaProductRelation> prefrenceAreaProductRelationList);
}
