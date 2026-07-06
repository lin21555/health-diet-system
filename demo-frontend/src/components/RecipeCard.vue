<template>
  <div class="recipe-card">
    <router-link :to="`/recipe/${recipe.id}`">
      <img :src="recipe.image" :alt="recipe.name" />
    </router-link>
    <div class="recipe-card-body">
      <router-link :to="`/recipe/${recipe.id}`" class="recipe-card-title">
        {{ recipe.name }}
      </router-link>
      <div class="recipe-card-desc">{{ recipe.recommendReason }}</div>
      <div style="margin-top: 10px;">
        <span class="tag" v-for="tag in tagList" :key="tag">{{ tag }}</span>
      </div>
      <div class="recipe-nutrition">
        {{ recipe.calorie }} kcal ｜ 蛋白质 {{ recipe.protein }}g
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  recipe: {
    type: Object,
    required: true
  }
})

const tagList = computed(() => {
  return props.recipe.tags ? props.recipe.tags.split(',').slice(0, 2) : []
})
</script>
