<template>
  <footer class="footer">
    <div class="page-container">
      <div class="footer-grid">
        <div>
          <h3>关于我们</h3>
          <p>本系统面向普通用户提供健康食谱展示、智能膳食推荐、AI问答与留言咨询功能。</p>
          <p>推荐结果仅作为日常饮食参考。</p>
        </div>
        <div>
          <h3>膳食分类</h3>
          <router-link to="/recipes?category=早餐">健康早餐</router-link>
          <router-link to="/recipes?category=减脂餐">减脂轻食</router-link>
          <router-link to="/recipes?category=增肌餐">增肌餐</router-link>
          <router-link to="/recipes?category=均衡餐">均衡饮食</router-link>
        </div>
        <div>
          <h3>帮助中心</h3>
          <router-link to="/recommend">智能推荐</router-link>
          <router-link to="/ai">AI问答</router-link>
          <router-link to="/message">留言咨询</router-link>
          <router-link to="/admin/dashboard">后台管理</router-link>
        </div>
      </div>
      <div class="footer-copy">© 2026 智能健康膳食推荐系统 All Rights Reserved.</div>
    </div>
  </footer>
</template>
