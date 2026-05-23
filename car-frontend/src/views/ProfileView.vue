<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <h2>Настройки подбора автомобилей</h2>

      <el-divider content-position="left">Основные параметры</el-divider>
      <el-form label-width="160px">
        <el-form-item label="Название профиля">
          <el-input v-model="profileForm.name" placeholder="Мой профиль" />
        </el-form-item>

        <el-form-item label="Бюджет (тыс. ₽)">
          <el-slider v-model="profileForm.budgetMax" :min="0" :max="10000" :step="50" show-input />
        </el-form-item>

        <el-form-item label="Цель использования">
          <el-select v-model="profileForm.usagePurpose" placeholder="Выберите цель">
            <el-option :value="1" label="Поездки по городу" />
            <el-option :value="2" label="Семейные путешествия" />
            <el-option :value="3" label="Перевозка грузов" />
            <el-option :value="4" label="Спортивная езда" />
            <el-option :value="5" label="Комфортный круиз" />
          </el-select>
        </el-form-item>
      </el-form>

      <el-divider content-position="left">Важность критериев (1-10)</el-divider>
      <el-form label-width="180px">
        <el-form-item label="Цена">
          <el-slider v-model="profileForm.weightPrice" :min="1" :max="10" show-input />
        </el-form-item>

        <el-form-item label="Безопасность">
          <el-slider v-model="profileForm.weightSafety" :min="1" :max="10" show-input />
        </el-form-item>

        <el-form-item label="Надёжность">
          <el-slider v-model="profileForm.weightReliability" :min="1" :max="10" show-input />
        </el-form-item>

        <el-form-item label="Экономичность">
          <el-slider v-model="profileForm.weightEconomy" :min="1" :max="10" show-input />
        </el-form-item>

        <el-form-item label="Комфорт">
          <el-slider v-model="profileForm.weightComfort" :min="1" :max="10" show-input />
        </el-form-item>

        <el-form-item label="Вместительность">
          <el-slider v-model="profileForm.weightCapacity" :min="1" :max="10" show-input />
        </el-form-item>

        <el-form-item label="Динамика">
          <el-slider v-model="profileForm.weightDynamics" :min="1" :max="10" show-input />
        </el-form-item>

        <el-form-item label="Внешний вид">
          <el-slider v-model="profileForm.weightAppearance" :min="1" :max="10" show-input />
        </el-form-item>

        <el-form-item label="Стоимость обслуживания">
          <el-slider v-model="profileForm.weightServiceCost" :min="1" :max="10" show-input />
        </el-form-item>
      </el-form>

      <el-divider />

      <div class="buttons">
        <el-button @click="resetToDefault">Сбросить веса</el-button>
        <el-button type="primary" @click="saveProfile" :loading="saving">
          Сохранить изменения
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const saving = ref(false)

// Форма профиля (только поля из ProfileDTO)
const profileForm = reactive({
  name: '',
  budgetMax: 0,
  usagePurpose: 1,
  weightPrice: 5,
  weightSafety: 5,
  weightReliability: 5,
  weightEconomy: 5,
  weightComfort: 5,
  weightCapacity: 5,
  weightDynamics: 5,
  weightAppearance: 5,
  weightServiceCost: 5,
})

// Загружаем данные профиля
onMounted(async () => {
  if (authStore.isAuthenticated) {
    // Ждём загрузки профиля
    if (!authStore.profile) {
      await authStore.fetchProfile()
    }

    // Заполняем форму из store
    if (authStore.profile) {
      profileForm.name = authStore.profile.name || ''
      profileForm.budgetMax = authStore.profile.budgetMax ?? 0
      profileForm.usagePurpose = authStore.profile.usagePurpose ?? 1
      profileForm.weightPrice = authStore.profile.weightPrice ?? 5
      profileForm.weightSafety = authStore.profile.weightSafety ?? 5
      profileForm.weightReliability = authStore.profile.weightReliability ?? 5
      profileForm.weightEconomy = authStore.profile.weightEconomy ?? 5
      profileForm.weightComfort = authStore.profile.weightComfort ?? 5
      profileForm.weightCapacity = authStore.profile.weightCapacity ?? 5
      profileForm.weightDynamics = authStore.profile.weightDynamics ?? 5
      profileForm.weightAppearance = authStore.profile.weightAppearance ?? 5
      profileForm.weightServiceCost = authStore.profile.weightServiceCost ?? 5
    }
  }
})

const resetToDefault = () => {
  profileForm.weightPrice = 5
  profileForm.weightSafety = 5
  profileForm.weightReliability = 5
  profileForm.weightEconomy = 5
  profileForm.weightComfort = 5
  profileForm.weightCapacity = 5
  profileForm.weightDynamics = 5
  profileForm.weightAppearance = 5
  profileForm.weightServiceCost = 5

  ElMessage.info('Веса сброшены к значениям по умолчанию')
}

const saveProfile = async () => {
  saving.value = true

  const profileData = {
    name: profileForm.name,
    budgetMax: profileForm.budgetMax,
    usagePurpose: profileForm.usagePurpose,
    weightPrice: profileForm.weightPrice,
    weightSafety: profileForm.weightSafety,
    weightReliability: profileForm.weightReliability,
    weightEconomy: profileForm.weightEconomy,
    weightComfort: profileForm.weightComfort,
    weightCapacity: profileForm.weightCapacity,
    weightDynamics: profileForm.weightDynamics,
    weightAppearance: profileForm.weightAppearance,
    weightServiceCost: profileForm.weightServiceCost,
  }

  const success = await authStore.updateProfile(profileData)
  saving.value = false

  if (success) {
    ElMessage.success('Профиль успешно обновлён!')
  } else {
    ElMessage.error('Ошибка при сохранении профиля')
  }
}
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 0 20px;
}

.profile-card {
  padding: 20px;
}

.buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
