package com.example.healthdiet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.healthdiet.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}