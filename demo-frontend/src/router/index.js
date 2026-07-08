import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', component: () => import('../views/front/Home.vue') },
  { path: '/foods', component: () => import('../views/front/FoodList.vue') },
  { path: '/food/:id', component: () => import('../views/front/FoodDetail.vue') },
  { path: '/recipes', component: () => import('../views/front/RecipeList.vue') },
  { path: '/recipe/:id', component: () => import('../views/front/RecipeDetail.vue') },
  { path: '/recommend', component: () => import('../views/front/Recommend.vue') },
  { path: '/articles', component: () => import('../views/front/ArticleList.vue') },
  { path: '/article/:id', component: () => import('../views/front/ArticleDetail.vue') },
  { path: '/forum', component: () => import('../views/front/ForumList.vue') },
  { path: '/message', component: () => import('../views/front/Message.vue') },
  { path: '/ai', component: () => import('../views/front/AiChat.vue') },
  { path: '/login', component: () => import('../views/front/Login.vue') },
  { path: '/user', component: () => import('../views/front/UserCenter.vue') },
  {
    path: '/admin',
    component: () => import('../layouts/AdminLayout.vue'),
    meta: { requiresAdmin: true },
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/admin/Dashboard.vue'), meta: { title: '数据统计', requiresAdmin: true } },
      { path: 'foods', component: () => import('../views/admin/FoodManage.vue'), meta: { title: '食材管理', requiresAdmin: true } },
      { path: 'recipes', component: () => import('../views/admin/RecipeManage.vue'), meta: { title: '食谱管理', requiresAdmin: true } },
      { path: 'users', component: () => import('../views/admin/UserManage.vue'), meta: { title: '用户管理', requiresAdmin: true } },
      { path: 'articles', component: () => import('../views/admin/ArticleManage.vue'), meta: { title: '公告资讯', requiresAdmin: true } },
      { path: 'messages', component: () => import('../views/admin/MessageManage.vue'), meta: { title: '留言咨询', requiresAdmin: true } },
      { path: 'ai', component: () => import('../views/admin/AiManage.vue'), meta: { title: 'AI问答管理', requiresAdmin: true } }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    if (user && user.role === 'ADMIN') next()
    else {
      alert('请先使用管理员账号登录')
      next('/login')
    }
  } else next()
})

export default router
