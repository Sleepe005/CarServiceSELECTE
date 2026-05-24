<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2>Регистрация</h2>
      
      <Form @submit="handleRegister" :validation-schema="schema" v-slot="{ errors, isSubmitting }">
        <!-- Поле Email -->
        <div class="form-field">
          <Field name="email" v-slot="{ value, handleChange, handleBlur, errorMessage }">
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
          <Field name="password" v-slot="{ value, handleChange, handleBlur, errorMessage }">
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
        
        <!-- Поле Подтверждение пароля -->
        <div class="form-field">
          <Field name="confirmPassword" v-slot="{ value, handleChange, handleBlur, errorMessage }">
            <el-input 
              :model-value="value"
              @update:model-value="handleChange"
              @blur="handleBlur"
              type="password" 
              placeholder="Подтвердите пароль"
            />
            <div class="error-message">{{ errorMessage }}</div>
          </Field>
        </div>
        
        <!-- Поле Полное имя -->
        <div class="form-field">
          <Field name="fullName" v-slot="{ value, handleChange, handleBlur, errorMessage }">
            <el-input 
              :model-value="value"
              @update:model-value="handleChange"
              @blur="handleBlur"
              placeholder="Полное имя"
            />
            <div class="error-message">{{ errorMessage }}</div>
          </Field>
        </div>
        
        <el-button type="primary" native-type="submit" :loading="isSubmitting" class="submit-btn">
          Зарегистрироваться
        </el-button>
        
        <p class="login-link">
          Уже есть аккаунт? <router-link to="/login">Войти</router-link>
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
  password: yup.string().required('Пароль обязателен').min(6, 'Пароль должен быть не менее 6 символов'),
  confirmPassword: yup.string()
    .required('Подтвердите пароль')
    .oneOf([yup.ref('password')], 'Пароли не совпадают'),
  fullName: yup.string().required('Имя обязательно')
})

async function handleRegister(values) {
  try {
    await authService.register(values.email, values.password, values.fullName)
    
    ElMessage.success('Регистрация прошла успешно!')
    
    setTimeout(() => {
      router.push('/login')
    }, 1500)
    
  } catch (error) {
    console.error('Ошибка регистрации:', error)
    
    if (error.response?.status === 409) {
      ElMessage.error('Пользователь с таким email уже существует')
    } else if (error.response?.status === 400) {
      ElMessage.error('Некорректные данные')
    } else if (error.request) {
      ElMessage.error('Не удалось подключиться к серверу')
    } else {
      ElMessage.error('Ошибка: ' + error.message)
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f0f2f5;
}

.register-card {
  width: 450px;
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

.login-link {
  text-align: center;
  margin-top: 20px;
}
</style>