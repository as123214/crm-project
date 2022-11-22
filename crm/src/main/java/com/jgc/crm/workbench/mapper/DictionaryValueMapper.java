package com.jgc.crm.workbench.mapper;

import com.jgc.crm.workbench.domain.DictionaryValue;

import java.util.List;

public interface DictionaryValueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Wed Nov 16 12:27:40 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Wed Nov 16 12:27:40 CST 2022
     */
    int insert(DictionaryValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Wed Nov 16 12:27:40 CST 2022
     */
    int insertSelective(DictionaryValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Wed Nov 16 12:27:40 CST 2022
     */
    DictionaryValue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Wed Nov 16 12:27:40 CST 2022
     */
    int updateByPrimaryKeySelective(DictionaryValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Wed Nov 16 12:27:40 CST 2022
     */
    int updateByPrimaryKey(DictionaryValue record);

    List<DictionaryValue> selectDictionaryValueById(String id);

}