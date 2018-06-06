package com.thinkgem.jeesite.modules.wx.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.thinkgem.jeesite.common.entity.ActionBaseDto;
import com.thinkgem.jeesite.modules.wx.entity.Food;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.wx.entity.FoodCategory;
import com.thinkgem.jeesite.modules.wx.dao.FoodCategoryDao;
import org.springframework.util.CollectionUtils;

/**
 * 菜品分类Service
 * @author tgp
 * @version 2018-06-04
 */
@Service
@Transactional(readOnly = true)
public class FoodCategoryService extends CrudService<FoodCategoryDao, FoodCategory> {

    @Autowired
    private FoodCategoryDao foodCategoryDao;

    @Autowired
    private FoodService foodService;

    public FoodCategory get(String id) {
        return super.get(id);
    }

    public FoodCategory getByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        FoodCategory foodCategory = foodCategoryDao.getByName(name);
        return foodCategory;
    }

    public List<FoodCategory> findList(FoodCategory foodCategory) {
        return super.findList(foodCategory);
    }
    
    public Page<FoodCategory> findPage(Page<FoodCategory> page, FoodCategory foodCategory) {
        return super.findPage(page, foodCategory);
    }
    
    @Transactional(readOnly = false)
    public void save(FoodCategory foodCategory) {
        if (StringUtils.isEmpty(foodCategory.getId())) {
            foodCategory.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            foodCategoryDao.insert(foodCategory);
        }
        foodCategoryDao.update(foodCategory);
    }
    
    @Transactional(readOnly = false)
    public void delete(FoodCategory foodCategory) {
        super.delete(foodCategory);
    }

    @Transactional(readOnly = false)
    public ActionBaseDto deleteByCategoryId(String categoryId) {
        // 判断categoryId是否被菜品引用
        Food food = new Food();
        food.setCategoryId(categoryId);
        List<Food> list = foodService.findList(food);
        if (!CollectionUtils.isEmpty(list)) {
            return ActionBaseDto.getFailedInstance("该分类下已经有菜品，不能删除！");
        }

        // 删除分类
        FoodCategory foodCategory = new FoodCategory();
        foodCategory.setId(categoryId);
        super.delete(foodCategory);
        return ActionBaseDto.getSuccessInstance();
    }

    /**
     * 查询所有菜单分类
     * @return
     */
    public List<FoodCategory> listAllFoodCategory() {
        return foodCategoryDao.listAllFoodCategory();
    }
}