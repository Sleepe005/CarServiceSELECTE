<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>Вход в аккаунт</h2>
      
      <Form @submit="handleLogin" :validation-schema="schema" v-slot="{ errors, isSubmitting }">
        <!-- Поле Email -->
        <div class="form-field">
          <Field name="email" v-slot="{ field, errorMessage, value, handleChange, handleBlur }">
            <el-input 
              :model-value="value"
              @update:model-value="handleChange"
              @blur="handleBlur"
              placeholder="Email" 
            />
            <div class="error-message">{{ errorMessage }}</div>
          </Field>
        </div>
        
        <!-- Поле Пароль -->
        <div class="form-field">
          <Field name="password" v-slot="{ field, errorMessage, value, handleChange, handleBlur }">
            <el-input 
              :model-value="value"
              @update:model-value="handleChange"
              @blur="handleBlur"
              type="password" 
              placeholder="Пароль"
            />
            <div class="error-message">{{ errorMessage }}</div>
          </Field>
        </div>
        
        <el-button type="primary" native-type="submit" :loading="isSubmitting" class="submit-btn">
          Войти
        </el-button>
        
        <p class="register-link">
          Нет аккаунта? <router-link to="/register">Зарегистрироваться</router-link>
        </p>
      </Form>
    </el-card>
  </div>
</template>

<script setup>
import { Form, Field } from 'vee-validate'
import * as yup from 'yup'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authService } from '@/services/authService'

const router = useRouter()

const schema = yup.object({
  email: yup.string().required('Email обязателен').email('Введите корректный email'),
  password: yup.string().required('Пароль обязателен')
})

async function handleLogin(values) {
  try {
    const response = await authService.login(values.email, values.password)
    const token = response.data
    
    localStorage.setItem('token', token)
    
    ElMessage.success('Вход выполнен успешно!')
    
    setTimeout(() => {
      router.push('/')
    }, 1000)
    
  } catch (error) {
    console.error('Ошибка входа:', error)
    
    if (error.response?.status === 401) {
      ElMessage.error('Неверный email или пароль')
    } else if (error.request) {
      ElMessage.error('Не удалось подключиться к серверу')
    } else {
      ElMessage.error('Ошибка: ' + error.message)
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f0f2f5;
}

.login-card {
  width: 400px;
}

.form-field {
  margin-bottom: 20px;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
  height: 20px;
}

.submit-btn {
  width: 100%;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}
</style>