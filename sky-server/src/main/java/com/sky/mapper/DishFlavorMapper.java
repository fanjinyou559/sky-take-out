package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量新增口味
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据dish_id批量删除口味
     * @param ids
     */
    void delectBatch(List<Long> ids);

    /**
     * 根据菜品id查询口味
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id =#{id}")
    List<DishFlavor> getByDishId(Long id);

    /**
     * 根据菜品id删除口味
     * @param id
     */
    @Delete("DELETE FROM dish_flavor WHERE dish_id =#{id}")
    void delectByDishId(Long id);
}
