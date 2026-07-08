<template>
  <div class="admin-panel">
    <div class="admin-toolbar">
      <div>
        <h3 style="margin:0;color:var(--primary-color);">食谱管理</h3>
      </div>
      <button class="admin-btn blue" @click="openAdd">+ 新增食谱</button>
    </div>

    <div class="admin-filter">
      <input v-model="query.keyword" placeholder="搜索食谱名称" @keyup.enter="loadData" />
      <select v-model="query.category">
        <option value="">选择分类</option>
        <option>早餐</option>
        <option>午餐</option>
        <option>晚餐</option>
        <option>减脂餐</option>
        <option>增肌餐</option>
        <option>控糖餐</option>
        <option>均衡餐</option>
      </select>
      <button class="admin-btn light" @click="loadData">搜索</button>
      <button class="admin-btn gray" @click="resetQuery">重置</button>
    </div>

    <table class="admin-table" style="margin-top:18px;">
      <thead>
        <tr>
          <th>ID</th>
          <th>图片</th>
          <th>食谱名称</th>
          <th>分类</th>
          <th>热量</th>
          <th>蛋白质</th>
          <th>状态</th>
          <th>收藏</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in list" :key="item.id">
          <td>{{ item.id }}</td>
          <td><img :src="item.image || defaultRecipeImage" :alt="item.name" @error="handleImageError" /></td>
          <td>{{ item.name }}</td>
          <td>{{ item.category }}</td>
          <td>{{ item.calorie }} kcal</td>
          <td>{{ item.protein }} g</td>
          <td><span class="status-online">在线</span></td>
          <td>{{ item.collectCount || 0 }}</td>
          <td>
            <div class="admin-actions">
              <button class="admin-btn gray" @click="edit(item)">编辑</button>
              <button class="admin-btn yellow" @click="preview(item)">预览</button>
              <button class="admin-btn red" @click="remove(item.id)">删除</button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="showModal" class="admin-modal-mask">
      <div class="admin-modal">
        <h3 style="margin-top:0;color:var(--primary-color);">{{ form.id ? '编辑食谱' : '新增食谱' }}</h3>

        <div class="admin-form">
          <div>
            <label>食谱名称</label>
            <input v-model="form.name" placeholder="请输入食谱名称" />
          </div>
          <div>
            <label>分类</label>
            <select v-model="form.category">
              <option>早餐</option>
              <option>午餐</option>
              <option>晚餐</option>
              <option>减脂餐</option>
              <option>增肌餐</option>
              <option>控糖餐</option>
              <option>均衡餐</option>
            </select>
          </div>
          <div>
            <label>图片地址</label>
            <input v-model="form.image" placeholder="请输入图片URL" />
          </div>
          <div>
            <label>适用人群</label>
            <input v-model="form.suitablePeople" placeholder="如减脂、健身、均衡饮食" />
          </div>
          <div>
            <label>热量 kcal</label>
            <input v-model="form.calorie" type="number" />
          </div>
          <div>
            <label>蛋白质 g</label>
            <input v-model="form.protein" type="number" />
          </div>
          <div>
            <label>脂肪 g</label>
            <input v-model="form.fat" type="number" />
          </div>
          <div>
            <label>碳水 g</label>
            <input v-model="form.carbohydrate" type="number" />
          </div>
          <div class="full">
            <label>标签</label>
            <input v-model="form.tags" placeholder="例如 高蛋白,低脂,早餐" />
          </div>
          <div class="full">
            <label>食材</label>
            <textarea v-model="form.ingredients" placeholder="请输入食材清单"></textarea>
          </div>
          <div class="full">
            <label>制作步骤</label>
            <textarea v-model="form.steps" placeholder="请输入制作步骤"></textarea>
          </div>
          <div class="full">
            <label>推荐理由</label>
            <textarea v-model="form.recommendReason" placeholder="请输入推荐理由"></textarea>
          </div>
        </div>

        <div style="text-align:right;margin-top:22px;">
          <button class="admin-btn light" @click="showModal=false">取消</button>
          <button class="admin-btn" style="margin-left:10px;" @click="save">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { addRecipe, deleteRecipe, getRecipes, updateRecipe } from '../../api/recipe'

const defaultRecipeImage = '/images/recipe-default.svg'

function handleImageError(event) {
  event.target.src = defaultRecipeImage
}

const router = useRouter()
const list = ref([])
const showModal = ref(false)
const query = reactive({ keyword: '', category: '' })
const emptyForm = {
  id: null,
  name: '',
  image: '/images/recipe-default.svg',
  category: '减脂餐',
  calorie: 380,
  protein: 25,
  fat: 10,
  carbohydrate: 45,
  suitablePeople: '均衡饮食',
  tags: '高蛋白,低脂',
  ingredients: '',
  steps: '',
  recommendReason: '',
  viewCount: 0,
  collectCount: 0
}
const form = reactive({ ...emptyForm })

function resetForm() {
  Object.assign(form, emptyForm)
}

async function loadData() {
  const res = await getRecipes(query)
  list.value = res.data || []
}

function resetQuery() {
  query.keyword = ''
  query.category = ''
  loadData()
}

function openAdd() {
  resetForm()
  showModal.value = true
}

function edit(row) {
  Object.assign(form, row)
  showModal.value = true
}

function preview(row) {
  router.push(`/recipe/${row.id}`)
}

async function save() {
  if (!form.name) {
    alert('请输入食谱名称')
    return
  }
  if (form.id) {
    await updateRecipe(form)
    alert('修改成功')
  } else {
    const data = { ...form }
    delete data.id
    await addRecipe(data)
    alert('添加成功')
  }
  showModal.value = false
  loadData()
}

async function remove(id) {
  if (!confirm('确定删除该食谱吗？')) return
  await deleteRecipe(id)
  alert('删除成功')
  loadData()
}

onMounted(loadData)
</script>
