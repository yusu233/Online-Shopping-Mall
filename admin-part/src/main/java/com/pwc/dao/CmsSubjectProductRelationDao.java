package com.pwc.dao;

import com.pwc.model.CmsSubjectProductRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CmsSubjectProductRelationDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<CmsSubjectProductRelation> subjectProductRelationList);
}
