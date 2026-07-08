<template>
  <div class="admin-panel">
    <div class="admin-toolbar">
      <div><h3 style="margin:0;color:var(--primary-color);">食材管理</h3></div>
      <button class="admin-btn blue" @click="openAdd">+ 新增食材</button>
    </div>

    <div class="admin-filter">
      <input v-model="query.keyword" placeholder="食材名称" @keyup.enter="loadData" />
      <select v-model="query.category">
        <option value="">食材类别</option>
        <option>蔬菜</option><option>水果</option><option>肉类</option><option>主食</option><option>豆制品</option><option>蛋奶</option><option>坚果</option>
      </select>
      <button class="admin-btn light" @click="loadData">搜索</button>
      <button class="admin-btn gray" @click="resetQuery">重置</button>
    </div>

    <table class="admin-table" style="margin-top:18px;">
      <thead>
        <tr><th>ID</th><th>图片</th><th>食材名称</th><th>类别</th><th>热量(kcal)</th><th>蛋白质(g)</th><th>脂肪(g)</th><th>碳水(g)</th><th>纤维(g)</th><th>操作</th></tr>
      </thead>
      <tbody>
        <tr v-for="item in list" :key="item.id">
          <td>{{ item.id }}</td>
          <td><img :src="item.image || defaultFoodImage" :alt="item.name" @error="handleImageError" /></td>
          <td>{{ item.name }}</td><td>{{ item.category }}</td><td>{{ item.calorie }}</td><td>{{ item.protein }}</td><td>{{ item.fat }}</td><td>{{ item.carbohydrate }}</td><td>{{ item.fiber }}</td>
          <td>
            <div class="admin-actions">
              <button class="admin-btn gray" @click="preview(item)">详情</button>
              <button class="admin-btn yellow" @click="edit(item)">编辑</button>
              <button class="admin-btn red" @click="remove(item.id)">删除</button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="showModal" class="admin-modal-mask">
      <div class="admin-modal">
        <h3 style="margin-top:0;color:var(--primary-color);">{{ form.id ? '编辑食材' : '新增食材' }}</h3>

        <div class="admin-form">
          <div><label>食材名称</label><input v-model="form.name" placeholder="请输入食材名称" /></div>
          <div><label>食材类别</label><select v-model="form.category"><option>蔬菜</option><option>水果</option><option>肉类</option><option>主食</option><option>豆制品</option><option>蛋奶</option><option>坚果</option></select></div>
          <div class="full"><label>图片地址</label><input v-model="form.image" placeholder="请输入图片URL或 /images/food-default.svg" /></div>
          <div><label>热量 kcal</label><input v-model="form.calorie" type="number" /></div>
          <div><label>蛋白质 g</label><input v-model="form.protein" type="number" /></div>
          <div><label>脂肪 g</label><input v-model="form.fat" type="number" /></div>
          <div><label>碳水 g</label><input v-model="form.carbohydrate" type="number" /></div>
          <div><label>膳食纤维 g</label><input v-model="form.fiber" type="number" /></div>
          <div class="full"><label>食材说明</label><textarea v-model="form.description" placeholder="请输入食材说明"></textarea></div>
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
import { addFood, deleteFood, getFoods, updateFood } from '../../api/food'

const router = useRouter()
const defaultFoodImage = '/images/food-default.svg'
const list = ref([])
const showModal = ref(false)
const query = reactive({ keyword: '', category: '' })

const emptyForm = { id:null, name:'', image:'/images/food-default.svg', category:'蔬菜', calorie:0, protein:0, fat:0, carbohydrate:0, fiber:0, description:'' }
const form = reactive({ ...emptyForm })

function handleImageError(event){ event.target.src = defaultFoodImage }
function resetForm(){ Object.assign(form, emptyForm) }
async function loadData(){ const res = await getFoods(query); list.value = res.data || [] }
function resetQuery(){ query.keyword=''; query.category=''; loadData() }
function openAdd(){ resetForm(); showModal.value = true }
function edit(row){ Object.assign(form, row); showModal.value = true }
function preview(row){ router.push(`/food/${row.id}`) }
async function save(){
  if (!form.name) { alert('请输入食材名称'); return }
  if (form.id) { await updateFood(form); alert('修改成功') }
  else { const data={...form}; delete data.id; await addFood(data); alert('新增成功') }
  showModal.value = false
  loadData()
}
async function remove(id){ if(!confirm('确定删除该食材吗？')) return; await deleteFood(id); alert('删除成功'); loadData() }
onMounted(loadData)
</script>
