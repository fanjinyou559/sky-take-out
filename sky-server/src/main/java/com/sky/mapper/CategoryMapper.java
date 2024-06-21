package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     * @param category
     * @return
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into category (type,name,sort ,status ,create_time,update_time,create_user,update_user ) " +
            "values " +
            "(#{type},#{name},#{sort},#{status},#{createTime}, #{updateTime}, #{createUser}, #{updateUser});")
    int insert(Category category);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     * @return
     */
    @AutoFill(OperationType.UPDATE)
    int update(Category category);

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @Delete("delete from category where id = #{id}")
    int deleteById(Long id);
}
