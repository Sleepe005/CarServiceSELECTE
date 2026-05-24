<template>
  <div id="app">
    <el-menu mode="horizontal" router class="nav-menu">
      <el-menu-item index="/profile">Мой профиль</el-menu-item>
      <el-menu-item index="/cars">Каталог авто</el-menu-item>
      <el-menu-item @click="handleLogout" style="float: right">Выйти</el-menu-item>
    </el-menu>

    <router-view />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.nav-menu {
  margin-bottom: 20px;
}
</style>
