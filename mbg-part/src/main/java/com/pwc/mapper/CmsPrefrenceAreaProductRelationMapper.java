package com.pwc.mapper;

import com.pwc.model.CmsPreferenceAreaProductRelation;
import com.pwc.model.CmsPreferenceAreaProductRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsPrefrenceAreaProductRelationMapper {
    long countByExample(CmsPreferenceAreaProductRelationExample example);

    int deleteByExample(CmsPreferenceAreaProductRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsPreferenceAreaProductRelation row);

    int insertSelective(CmsPreferenceAreaProductRelation row);

    List<CmsPreferenceAreaProductRelation> selectByExample(CmsPreferenceAreaProductRelationExample example);

    CmsPreferenceAreaProductRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") CmsPreferenceAreaProductRelation row, @Param("example") CmsPreferenceAreaProductRelationExample example);

    int updateByExample(@Param("row") CmsPreferenceAreaProductRelation row, @Param("example") CmsPreferenceAreaProductRelationExample example);

    int updateByPrimaryKeySelective(CmsPreferenceAreaProductRelation row);

    int updateByPrimaryKey(CmsPreferenceAreaProductRelation row);
}