<template>
  <div>
    <FrontHeader />

    <section class="section" style="min-height:560px;display:flex;align-items:center;">
      <div class="page-container">
        <div class="card" style="width:430px;margin:0 auto;text-align:center;">
          <div style="display:flex;justify-content:center;gap:70px;margin-bottom:26px;color:var(--primary-hover);font-weight:bold;">
            <span :style="mode === 'login' ? 'border-bottom:2px solid var(--primary-color);padding-bottom:10px;' : ''" @click="mode='login'">登录</span>
            <span :style="mode === 'register' ? 'border-bottom:2px solid var(--primary-color);padding-bottom:10px;' : ''" @click="mode='register'">注册</span>
          </div>

          <h2>{{ mode === 'login' ? '欢迎回来' : '创建账号' }}</h2>
          <p style="color:#999;">登录后可使用智能膳食推荐功能</p>

          <div class="form-item" style="text-align:left;margin-top:24px;">
            <label>用户名</label>
            <input v-model="form.username" placeholder="请输入用户名" />
          </div>
          <div class="form-item" style="text-align:left;margin-top:16px;">
            <label>密码</label>
            <input v-model="form.password" type="password" placeholder="请输入密码" />
          </div>
          <div v-if="mode === 'register'" class="form-item" style="text-align:left;margin-top:16px;">
            <label>昵称</label>
            <input v-model="form.nickname" placeholder="请输入昵称" />
          </div>

          <button class="btn-primary" style="width:100%;margin-top:28px;" @click="submit">
            {{ mode === 'login' ? '登录' : '立即注册' }}
          </button>

          <p style="margin-top:22px;color:#999;">
            测试账号：admin / 123456，user / 123456
          </p>
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import { login, register } from '../../api/user'

const mode = ref('login')
const router = useRouter()
const form = reactive({
  username: '',
  password: '',
  nickname: ''
})

async function submit() {
  if (!form.username || !form.password) {
    alert('请输入用户名和密码')
    return
  }
  if (mode.value === 'login') {
    const res = await login(form)
    if (res.code === 200) {
      localStorage.setItem('user', JSON.stringify(res.data))
      alert('登录成功')
      if (res.data.role === 'ADMIN') {
        router.push('/admin/dashboard')
      } else {
        router.push('/')
      }
    } else {
      alert(res.message)
    }
  } else {
    await register(form)
    alert('注册成功，请登录')
    mode.value = 'login'
  }
}
</script>
