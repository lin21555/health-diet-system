package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.entity.Food;
import com.example.healthdiet.service.FoodService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/list")
    public Result<List<Food>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "category", required = false) String category) {
        return Result.success(foodService.listByCondition(keyword, category));
    }

    @GetMapping("/{id}")
    public Result<Food> detail(@PathVariable("id") Long id) {
        Food food = foodService.getById(id);
        if (food == null) {
            return Result.error("食材不存在或已被删除");
        }
        return Result.success(food);
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Food food) {
        return Result.success(foodService.save(food));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Food food) {
        return Result.success(foodService.updateById(food));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.success(foodService.removeById(id));
    }
}
