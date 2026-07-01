<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>智能膳食推荐</h1>
        <p>填写基础信息，生成适合你的饮食参考方案</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="card">
          <SectionTitle title="健康信息填写" subtitle="系统根据规则匹配推荐食谱，不进行医疗诊断" />

          <div class="form-grid">
            <div class="form-item">
              <label>年龄</label>
              <input v-model="form.age" type="number" placeholder="请输入年龄" />
            </div>
            <div class="form-item">
              <label>性别</label>
              <select v-model="form.gender">
                <option>男</option>
                <option>女</option>
              </select>
            </div>
            <div class="form-item">
              <label>身高 cm</label>
              <input v-model="form.height" type="number" placeholder="例如 170" />
            </div>
            <div class="form-item">
              <label>体重 kg</label>
              <input v-model="form.weight" type="number" placeholder="例如 60" />
            </div>
            <div class="form-item">
              <label>运动强度</label>
              <select v-model="form.activityLevel">
                <option>低</option>
                <option>中</option>
                <option>高</option>
              </select>
            </div>
            <div class="form-item">
              <label>饮食目标</label>
              <select v-model="form.dietGoal">
                <option>减脂餐</option>
                <option>增肌餐</option>
                <option>控糖餐</option>
                <option>均衡餐</option>
              </select>
            </div>
            <div class="form-item">
              <label>饮食偏好</label>
              <input v-model="form.tastePreference" placeholder="如清淡、少油、偏素食" />
            </div>
            <div class="form-item">
              <label>过敏食物</label>
              <input v-model="form.allergyFoods" placeholder="如花生、海鲜、牛奶等，没有可填无" />
            </div>
          </div>

          <div style="text-align:center;margin-top:30px;">
            <button class="btn-primary" @click="generate">生成推荐</button>
          </div>
        </div>

        <div v-if="result" class="card" style="margin-top:32px;">
          <SectionTitle title="推荐结果" :subtitle="`BMI：${result.bmi}，状态：${result.bmiLevel}`" />
          <p style="line-height:2;color:#666;">{{ result.reason }}</p>
          <div class="recipe-grid" style="grid-template-columns: repeat(3, 1fr); margin-top:24px;">
            <RecipeCard v-if="result.breakfast" :recipe="result.breakfast" />
            <RecipeCard v-if="result.lunch" :recipe="result.lunch" />
            <RecipeCard v-if="result.dinner" :recipe="result.dinner" />
          </div>
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import SectionTitle from '../../components/SectionTitle.vue'
import RecipeCard from '../../components/RecipeCard.vue'
import { generateRecommend } from '../../api/recommend'

const result = ref(null)
const form = reactive({
  userId: 2,
  age: 22,
  gender: '女',
  height: 165,
  weight: 55,
  activityLevel: '中',
  dietGoal: '减脂餐',
  tastePreference: '清淡',
  allergyFoods: '无'
})

async function generate() {
  const res = await generateRecommend(form)
  result.value = res.data
}
</script>
