<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>论坛交流</h1>
        <p>分享健康饮食经验和日常搭配思路</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="card">
          <SectionTitle title="帖子列表" subtitle="用户之间的饮食经验交流" />
          <table class="table">
            <thead>
              <tr>
                <th>标题</th>
                <th>内容</th>
                <th>浏览量</th>
                <th>评论数</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in posts" :key="item.id">
                <td>{{ item.title }}</td>
                <td>{{ item.content }}</td>
                <td>{{ item.viewCount }}</td>
                <td>{{ item.commentCount }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import SectionTitle from '../../components/SectionTitle.vue'
import { getPosts } from '../../api/forum'

const posts = ref([])

onMounted(async () => {
  const res = await getPosts()
  posts.value = res.data || []
})
</script>
