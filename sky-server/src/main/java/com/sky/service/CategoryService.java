package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    int save(CategoryDTO categoryDTO);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     * @return
     */
    int startOrStop(Integer status, Long id);

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    int update(CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id
     * @return
     */
    int deleteById(Long id);
}
