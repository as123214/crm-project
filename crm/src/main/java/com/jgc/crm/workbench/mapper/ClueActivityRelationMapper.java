package com.jgc.crm.workbench.mapper;

import com.jgc.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat Nov 19 20:22:23 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat Nov 19 20:22:23 CST 2022
     */
    int insert(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat Nov 19 20:22:23 CST 2022
     */
    int insertSelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat Nov 19 20:22:23 CST 2022
     */
    ClueActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat Nov 19 20:22:23 CST 2022
     */
    int updateByPrimaryKeySelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat Nov 19 20:22:23 CST 2022
     */
    int updateByPrimaryKey(ClueActivityRelation record);

    /**
     * 批量插入市场关联
     * @param list
     * @return
     */
    int insertClueRelation(List<ClueActivityRelation> list);

    /**
     * 删除显示市场关联
     * @param clueActivityRelation
     * @return
     */
    int deleteClueRelation(ClueActivityRelation clueActivityRelation);
}