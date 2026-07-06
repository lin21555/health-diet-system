<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:240px;">
      <div class="banner-title">
        <h1>个人中心</h1>
        <p>维护个人信息、健康档案和收藏食谱</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="card">
          <SectionTitle title="我的信息" subtitle="基础个人中心页面，后续可继续接入数据库" />
          <div class="form-grid">
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
                <option>男</option>
                <option>女</option>
              </select>
            </div>
          </div>
          <button class="btn-primary" style="margin-top:24px;" @click="save">保存信息</button>
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import SectionTitle from '../../components/SectionTitle.vue'

const stored = JSON.parse(localStorage.getItem('user') || '{}')
const user = reactive({
  nickname: stored.nickname || '健康用户',
  phone: stored.phone || '',
  email: stored.email || '',
  gender: stored.gender || '女'
})

function save() {
  localStorage.setItem('user', JSON.stringify(user))
  alert('保存成功')
}
</script>
