<template>
  <div>
    <FrontHeader />

    <section class="user-hero">
      <div class="page-container user-hero-inner">
        <div>
          <h1>个人中心</h1>
          <p>管理个人资料、健康档案、收藏食谱与互动记录</p>
        </div>
        <div class="hero-actions">
          <router-link to="/recommend"><button class="btn-primary">获取智能推荐</button></router-link>
          <router-link to="/recipes"><button class="btn-ghost">浏览食谱</button></router-link>
        </div>
      </div>
    </section>

    <section class="section user-center-section">
      <div class="page-container user-center-layout">
        <aside class="profile-card">
          <div class="avatar-wrap">
            <img :src="user.avatar || defaultAvatar" alt="avatar" />
          </div>
          <h2>{{ user.nickname || user.username || '健康用户' }}</h2>
          <p class="account-text">账号：{{ user.username || '未登录' }}</p>
          <span class="role-badge">{{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}</span>

          <div class="profile-stats">
            <div>
              <b>{{ bmiText }}</b>
              <span>BMI</span>
            </div>
            <div>
              <b>{{ favorites.length }}</b>
              <span>收藏</span>
            </div>
            <div>
              <b>{{ recommendHistory.length }}</b>
              <span>推荐</span>
            </div>
          </div>

          <div class="side-tip">
            <b>温馨提示</b>
            <p>系统推荐结果仅作为日常健康饮食参考，不作为医疗诊断或治疗依据。</p>
          </div>
        </aside>

        <main class="user-main-card">
          <div class="tab-bar">
            <button
              v-for="tab in tabs"
              :key="tab.key"
              :class="['tab-btn', activeTab === tab.key ? 'active' : '']"
              @click="activeTab = tab.key"
            >
              {{ tab.label }}
            </button>
          </div>

          <div v-if="activeTab === 'info'" class="tab-panel">
            <SectionTitle title="基本资料" subtitle="维护昵称、联系方式和头像信息" />
            <div class="form-grid warm-form">
              <div class="form-item">
                <label>用户名</label>
                <input v-model="user.username" disabled />
              </div>
              <div class="form-item">
                <label>昵称</label>
                <input v-model="user.nickname" placeholder="请输入昵称" />
              </div>
              <div class="form-item">
                <label>手机号</label>
                <input v-model="user.phone" placeholder="请输入手机号" />
              </div>
              <div class="form-item">
                <label>邮箱</label>
                <input v-model="user.email" placeholder="请输入邮箱" />
              </div>
              <div class="form-item">
                <label>性别</label>
                <select v-model="user.gender">
                  <option value="男">男</option>
                  <option value="女">女</option>
                </select>
              </div>
              <div class="form-item">
                <label>头像地址</label>
                <input v-model="user.avatar" placeholder="可填写图片 URL" />
              </div>
            </div>
            <div class="panel-actions">
              <button class="btn-primary" @click="saveUserInfo">保存基本资料</button>
            </div>
          </div>

          <div v-if="activeTab === 'health'" class="tab-panel">
            <SectionTitle title="健康档案" subtitle="完善身高体重、饮食目标和偏好信息，便于生成膳食推荐" />
            <div class="health-summary">
              <div>
                <span>当前 BMI</span>
                <b>{{ bmiText }}</b>
              </div>
              <div>
                <span>身体状态</span>
                <b>{{ bmiLevel }}</b>
              </div>
              <div>
                <span>饮食目标</span>
                <b>{{ healthProfile.dietGoal || '暂未设置' }}</b>
              </div>
            </div>

            <div class="form-grid warm-form">
              <div class="form-item">
                <label>年龄</label>
                <input v-model.number="healthProfile.age" type="number" placeholder="请输入年龄" />
              </div>
              <div class="form-item">
                <label>性别</label>
                <select v-model="healthProfile.gender">
                  <option value="男">男</option>
                  <option value="女">女</option>
                </select>
              </div>
              <div class="form-item">
                <label>身高 cm</label>
                <input v-model.number="healthProfile.height" type="number" placeholder="例如 170" />
              </div>
              <div class="form-item">
                <label>体重 kg</label>
                <input v-model.number="healthProfile.weight" type="number" placeholder="例如 60" />
              </div>
              <div class="form-item">
                <label>运动强度</label>
                <select v-model="healthProfile.activityLevel">
                  <option value="低">低</option>
                  <option value="中">中</option>
                  <option value="高">高</option>
                </select>
              </div>
              <div class="form-item">
                <label>饮食目标</label>
                <select v-model="healthProfile.dietGoal">
                  <option value="减脂餐">减脂餐</option>
                  <option value="增肌餐">增肌餐</option>
                  <option value="控糖餐">控糖餐</option>
                  <option value="均衡餐">均衡餐</option>
                </select>
              </div>
              <div class="form-item">
                <label>饮食偏好</label>
                <input v-model="healthProfile.tastePreference" placeholder="如清淡、少油、偏素食" />
              </div>
              <div class="form-item">
                <label>过敏食物</label>
                <input v-model="healthProfile.allergyFoods" placeholder="如花生、海鲜、牛奶；没有可填无" />
              </div>
              <div class="form-item form-wide">
                <label>不喜欢或尽量避免的食物</label>
                <input v-model="healthProfile.avoidFoods" placeholder="如肥肉、甜品、油炸食物等" />
              </div>
            </div>
            <div class="panel-actions">
              <button class="btn-primary" @click="saveProfile">保存健康档案</button>
              <router-link to="/recommend"><button class="btn-ghost">使用档案生成推荐</button></router-link>
            </div>
          </div>

          <div v-if="activeTab === 'favorite'" class="tab-panel">
            <SectionTitle title="我的收藏" subtitle="保存喜欢的健康食谱，方便下次查看" />
            <div v-if="favorites.length" class="mini-recipe-grid">
              <div class="mini-recipe-card" v-for="item in favorites" :key="item.id">
                <img :src="item.image || defaultRecipeImage" :alt="item.name" @error="handleRecipeImageError" />
                <div>
                  <h3>{{ item.name }}</h3>
                  <p>{{ item.calorie }} kcal ｜ 蛋白质 {{ item.protein }}g</p>
                  <router-link :to="`/recipe/${item.id}`">查看详情</router-link>
                </div>
              </div>
            </div>
            <div v-else class="empty-box">
              暂无收藏食谱，去食谱中心挑选一些喜欢的搭配吧。
              <router-link to="/recipes">去浏览食谱</router-link>
            </div>
          </div>

          <div v-if="activeTab === 'recommend'" class="tab-panel">
            <SectionTitle title="推荐记录" subtitle="查看最近生成的智能膳食推荐" />
            <div v-if="recommendHistory.length" class="timeline-list">
              <div class="timeline-item" v-for="item in recommendHistory" :key="item.id">
                <div class="timeline-dot"></div>
                <div class="timeline-content">
                  <h3>{{ formatTime(item.createTime) }}</h3>
                  <p>{{ item.recommendReason }}</p>
                  <div class="recommend-tags">
                    <span v-if="item.breakfast">早餐：{{ item.breakfast.name }}</span>
                    <span v-if="item.lunch">午餐：{{ item.lunch.name }}</span>
                    <span v-if="item.dinner">晚餐：{{ item.dinner.name }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="empty-box">
              暂无推荐记录。
              <router-link to="/recommend">立即生成推荐</router-link>
            </div>
          </div>

          <div v-if="activeTab === 'message'" class="tab-panel">
            <SectionTitle title="我的留言" subtitle="查看留言咨询和管理员回复" />
            <div v-if="myMessages.length" class="record-list">
              <div class="record-card" v-for="item in myMessages" :key="item.id">
                <div class="record-head">
                  <h3>{{ item.title }}</h3>
                  <span :class="item.status === '已回复' ? 'status-done' : 'status-wait'">{{ item.status }}</span>
                </div>
                <p>{{ item.content }}</p>
                <div v-if="item.reply" class="reply-box">管理员回复：{{ item.reply }}</div>
              </div>
            </div>
            <div v-else class="empty-box">
              暂无留言记录。
              <router-link to="/message">去留言咨询</router-link>
            </div>
          </div>

          <div v-if="activeTab === 'ai'" class="tab-panel">
            <SectionTitle title="AI问答记录" subtitle="回看曾经咨询过的饮食问题" />
            <div v-if="myAiRecords.length" class="record-list">
              <div class="record-card" v-for="item in myAiRecords" :key="item.id">
                <div class="qa-question">问：{{ item.question }}</div>
                <p class="qa-answer">答：{{ item.answer }}</p>
              </div>
            </div>
            <div v-else class="empty-box">
              暂无 AI 问答记录。
              <router-link to="/ai">去 AI 问答</router-link>
            </div>
          </div>
        </main>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import SectionTitle from '../../components/SectionTitle.vue'
import { updateUser } from '../../api/user'
import { getHealthProfile, saveHealthProfile } from '../../api/health'
import { getFavoriteRecipes } from '../../api/recipe'
import { getRecommendHistory } from '../../api/recommend'
import { getMessages } from '../../api/message'
import { getAiHistory } from '../../api/ai'

const router = useRouter()
const defaultAvatar = 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=160&q=80'
const defaultRecipeImage = '/images/recipe-default.svg'

function handleRecipeImageError(event) {
  event.target.src = defaultRecipeImage
}
const storedUser = JSON.parse(localStorage.getItem('user') || '{}')

const activeTab = ref('info')
const tabs = [
  { key: 'info', label: '基本资料' },
  { key: 'health', label: '健康档案' },
  { key: 'favorite', label: '我的收藏' },
  { key: 'recommend', label: '推荐记录' },
  { key: 'message', label: '我的留言' },
  { key: 'ai', label: 'AI记录' }
]

const user = reactive({
  id: storedUser.id,
  username: storedUser.username || '',
  nickname: storedUser.nickname || '健康用户',
  avatar: storedUser.avatar || '',
  gender: storedUser.gender || '女',
  phone: storedUser.phone || '',
  email: storedUser.email || '',
  role: storedUser.role || 'USER',
  status: storedUser.status || 1
})

const healthProfile = reactive({
  userId: storedUser.id,
  age: 22,
  gender: user.gender || '女',
  height: 165,
  weight: 55,
  activityLevel: '中',
  dietGoal: '均衡餐',
  tastePreference: '清淡',
  allergyFoods: '无',
  avoidFoods: ''
})

const favorites = ref([])
const recommendHistory = ref([])
const messages = ref([])
const aiRecords = ref([])

const bmi = computed(() => {
  const h = Number(healthProfile.height) / 100
  const w = Number(healthProfile.weight)
  if (!h || !w) return 0
  return (w / (h * h)).toFixed(2)
})

const bmiText = computed(() => bmi.value ? bmi.value : '--')
const bmiLevel = computed(() => {
  const value = Number(bmi.value)
  if (!value) return '暂未计算'
  if (value < 18.5) return '偏瘦'
  if (value < 24) return '正常'
  if (value < 28) return '超重'
  return '肥胖'
})

const myMessages = computed(() => messages.value.filter(item => Number(item.userId) === Number(user.id)))
const myAiRecords = computed(() => aiRecords.value.filter(item => Number(item.userId) === Number(user.id)))

function checkLogin() {
  if (!user.id) {
    alert('请先登录后再进入个人中心')
    router.push('/login')
    return false
  }
  return true
}

async function loadProfile() {
  const res = await getHealthProfile(user.id)
  if (res.data) {
    Object.assign(healthProfile, res.data)
  }
}

async function loadRecords() {
  const [favoriteRes, recommendRes, messageRes, aiRes] = await Promise.all([
    getFavoriteRecipes(user.id),
    getRecommendHistory(user.id),
    getMessages(),
    getAiHistory()
  ])
  favorites.value = favoriteRes.data || []
  recommendHistory.value = recommendRes.data || []
  messages.value = messageRes.data || []
  aiRecords.value = aiRes.data || []
}

async function saveUserInfo() {
  if (!user.nickname) {
    alert('昵称不能为空')
    return
  }
  const res = await updateUser(user)
  if (res.code === 200) {
    localStorage.setItem('user', JSON.stringify(user))
    alert('基本资料保存成功')
  } else {
    alert(res.message || '保存失败')
  }
}

async function saveProfile() {
  if (!healthProfile.height || !healthProfile.weight) {
    alert('请填写身高和体重')
    return
  }
  healthProfile.userId = user.id
  healthProfile.bmi = bmi.value
  const res = await saveHealthProfile(healthProfile)
  if (res.code === 200) {
    Object.assign(healthProfile, res.data)
    alert('健康档案保存成功')
  } else {
    alert(res.message || '保存失败')
  }
}

function formatTime(value) {
  if (!value) return '最近推荐'
  return String(value).replace('T', ' ').slice(0, 16)
}

onMounted(async () => {
  if (!checkLogin()) return
  await loadProfile()
  await loadRecords()
})
</script>

<style scoped>
.user-hero {
  background:
    linear-gradient(rgba(92, 52, 38, .18), rgba(92, 52, 38, .18)),
    url('https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=1800&q=80');
  background-size: cover;
  background-position: center;
  height: 260px;
  color: #fff;
  display: flex;
  align-items: center;
}

.user-hero-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-hero h1 {
  margin: 0 0 12px;
  font-size: 40px;
  text-shadow: 0 3px 12px rgba(0,0,0,.25);
}

.user-hero p {
  font-size: 18px;
  margin: 0;
  text-shadow: 0 2px 8px rgba(0,0,0,.22);
}

.hero-actions {
  display: flex;
  gap: 14px;
}

.user-center-section {
  background: #FFF8F0;
}

.user-center-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 28px;
  align-items: start;
}

.profile-card,
.user-main-card {
  background: rgba(255, 255, 255, .92);
  border: 1px solid #F5E6D3;
  border-radius: 18px;
  box-shadow: 0 12px 32px rgba(128, 82, 58, .10);
}

.profile-card {
  padding: 28px 22px;
  text-align: center;
  position: sticky;
  top: 18px;
}

.avatar-wrap {
  width: 106px;
  height: 106px;
  margin: 0 auto 18px;
  border-radius: 50%;
  padding: 5px;
  background: linear-gradient(135deg, #F5E6D3, #D4856B);
}

.avatar-wrap img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
  border: 4px solid #fff;
}

.profile-card h2 {
  margin: 0 0 8px;
  color: #5A382A;
}

.account-text {
  color: #9C7A6A;
  font-size: 13px;
  margin: 0 0 12px;
}

.role-badge {
  display: inline-block;
  background: #F5E6D3;
  color: #D4856B;
  border-radius: 14px;
  padding: 5px 14px;
  font-size: 13px;
}

.profile-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin: 24px 0;
}

.profile-stats div {
  background: #FFF8F0;
  border-radius: 12px;
  padding: 12px 4px;
}

.profile-stats b {
  display: block;
  color: #D4856B;
  font-size: 20px;
}

.profile-stats span {
  color: #9C7A6A;
  font-size: 12px;
}

.side-tip {
  background: #FFF8F0;
  border-left: 4px solid #D4856B;
  border-radius: 12px;
  text-align: left;
  padding: 14px;
  color: #7B5B4C;
}

.side-tip p {
  margin: 8px 0 0;
  font-size: 13px;
  line-height: 1.8;
}

.user-main-card {
  padding: 26px;
}

.tab-bar {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  padding-bottom: 18px;
  border-bottom: 1px solid #F5E6D3;
  margin-bottom: 26px;
}

.tab-btn {
  border: 1px solid #F5E6D3;
  background: #FFF8F0;
  color: #8B6250;
  border-radius: 22px;
  padding: 9px 18px;
  cursor: pointer;
}

.tab-btn.active,
.tab-btn:hover {
  background: #D4856B;
  border-color: #D4856B;
  color: #fff;
}

.tab-panel {
  min-height: 420px;
}

.warm-form :deep(input),
.warm-form :deep(select),
.warm-form :deep(textarea) {
  background: #FFF8F0;
  border-color: #F5E6D3;
}

.form-wide {
  grid-column: 1 / -1;
}

.panel-actions {
  display: flex;
  gap: 14px;
  margin-top: 26px;
}

.health-summary {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.health-summary div {
  background: linear-gradient(135deg, #FFF8F0, #F5E6D3);
  border-radius: 16px;
  padding: 20px;
  border: 1px solid #F0D8C7;
}

.health-summary span {
  color: #9C7A6A;
  font-size: 13px;
}

.health-summary b {
  display: block;
  margin-top: 8px;
  color: #D4856B;
  font-size: 24px;
}

.mini-recipe-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px;
}

.mini-recipe-card {
  display: grid;
  grid-template-columns: 126px 1fr;
  gap: 16px;
  background: #FFF8F0;
  border: 1px solid #F5E6D3;
  border-radius: 16px;
  padding: 14px;
}

.mini-recipe-card img {
  width: 126px;
  height: 96px;
  object-fit: cover;
  border-radius: 12px;
}

.mini-recipe-card h3 {
  margin: 2px 0 8px;
  color: #5A382A;
}

.mini-recipe-card p {
  color: #9C7A6A;
  margin: 0 0 10px;
}

.mini-recipe-card a,
.empty-box a {
  color: #D4856B;
  font-weight: 600;
}

.timeline-list,
.record-list {
  display: grid;
  gap: 16px;
}

.timeline-item {
  display: grid;
  grid-template-columns: 18px 1fr;
  gap: 14px;
}

.timeline-dot {
  width: 12px;
  height: 12px;
  margin-top: 8px;
  border-radius: 50%;
  background: #D4856B;
  box-shadow: 0 0 0 6px #F5E6D3;
}

.timeline-content,
.record-card {
  background: #FFF8F0;
  border: 1px solid #F5E6D3;
  border-radius: 16px;
  padding: 18px;
}

.timeline-content h3,
.record-card h3 {
  margin: 0 0 10px;
  color: #5A382A;
}

.timeline-content p,
.record-card p {
  color: #7B5B4C;
  line-height: 1.8;
}

.recommend-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.recommend-tags span,
.status-done,
.status-wait {
  border-radius: 14px;
  padding: 5px 11px;
  font-size: 13px;
}

.recommend-tags span {
  background: #F5E6D3;
  color: #B46D58;
}

.record-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
}

.status-done {
  background: #EDF8EE;
  color: #4C9A55;
}

.status-wait {
  background: #FFF0D9;
  color: #C98725;
}

.reply-box {
  margin-top: 12px;
  background: #fff;
  border-left: 4px solid #D4856B;
  padding: 12px;
  color: #7B5B4C;
  border-radius: 8px;
}

.qa-question {
  color: #5A382A;
  font-weight: 700;
  margin-bottom: 10px;
}

.qa-answer {
  margin: 0;
}

.empty-box {
  background: #FFF8F0;
  border: 1px dashed #D4856B;
  color: #8B6250;
  padding: 34px;
  border-radius: 16px;
  text-align: center;
}

.empty-box a {
  margin-left: 8px;
}

@media (max-width: 980px) {
  .user-center-layout {
    grid-template-columns: 1fr;
  }
  .profile-card {
    position: static;
  }
  .mini-recipe-grid,
  .health-summary {
    grid-template-columns: 1fr;
  }
}
</style>
