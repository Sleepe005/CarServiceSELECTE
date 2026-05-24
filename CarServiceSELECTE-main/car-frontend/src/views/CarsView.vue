<template>
  <div class="cars-container">
    <el-card class="cars-card">
      <h2>Каталог автомобилей</h2>

      <!-- Список автомобилей -->
      <div v-if="loading" class="loading">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="cars.length === 0" class="empty">
        <el-empty description="Автомобили не найдены" />
      </div>

      <div v-else class="cars-grid">
        <el-card v-for="car in cars" :key="car.id" class="car-card" shadow="hover">
          <h3>{{ car.brand }} {{ car.model }}</h3>
          <p class="generation">{{ car.generation }}</p>

          <el-divider />

          <div class="car-details">
            <div class="detail">
              <span class="label">Годы выпуска:</span>
              <span class="value">{{ car.yearFrom }} - {{ car.yearTo }}</span>
            </div>
            <div class="detail">
              <span class="label">Цена:</span>
              <span class="value price">{{ formatPrice(car.price) }} ₽</span>
            </div>
            <div class="detail">
              <span class="label">Кузов:</span>
              <span class="value">{{ car.bodyType }}</span>
            </div>
            <div class="detail">
              <span class="label">Топливо:</span>
              <span class="value">{{ car.fuelType }}</span>
            </div>
            <div class="detail">
              <span class="label">КПП:</span>
              <span class="value">{{ car.transmission }}</span>
            </div>
            <div class="detail">
              <span class="label">Привод:</span>
              <span class="value">{{ car.drivetrain }}</span>
            </div>
            <div class="detail">
              <span class="label">Мощность:</span>
              <span class="value">{{ car.powerHp }} л.с.</span>
            </div>
          </div>

          <el-divider />

          <div class="ratings">
            <el-tag size="small" type="success">Безопасность: {{ car.safetyRating }}/10</el-tag>
            <el-tag size="small" type="warning">Надёжность: {{ car.reliabilityRating }}/10</el-tag>
            <el-tag size="small" type="info">Комфорт: {{ car.comfortRating }}/10</el-tag>
            <el-tag size="small" type="danger">Динамика: {{ car.dynamicsRating }}/10</el-tag>
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { carService } from '@/services/carService'
import { ElMessage } from 'element-plus'

const cars = ref([])
const loading = ref(true)

// Форматирование цены
const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ru-RU')
}

// Загрузка автомобилей
const loadCars = async () => {
  loading.value = true
  try {
    const response = await carService.getAllCars()
    cars.value = response.data
    console.log('Cars loaded:', cars.value)
  } catch (error) {
    console.error('Failed to load cars:', error)
    ElMessage.error('Ошибка загрузки автомобилей')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCars()
})
</script>

<style scoped>
.cars-container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 0 20px;
}

.cars-card {
  padding: 20px;
}

.cars-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.car-card {
  transition: transform 0.2s;
}

.car-card:hover {
  transform: translateY(-5px);
}

.car-card h3 {
  margin: 0 0 5px;
  font-size: 18px;
  color: #303133;
}

.generation {
  color: #909399;
  font-size: 14px;
  margin-bottom: 10px;
}

.car-details {
  margin: 10px 0;
}

.detail {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
}

.label {
  color: #909399;
}

.value {
  color: #303133;
  font-weight: 500;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.ratings {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.loading,
.empty {
  padding: 40px;
  text-align: center;
}
</style>
