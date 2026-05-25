<template>
  <div class="recommendations-container">
    <el-card class="recommendations-card">
      <h2>🚗 Рекомендованные автомобили</h2>
      <p class="subtitle">Подобрано специально для вас на основе метода ELECTRE</p>

      <!-- Фильтр по брендам -->
      <div class="filters">
        <el-select
          v-model="selectedBrand"
          placeholder="Фильтр по бренду"
          clearable
          filterable
          @change="filterCars"
          style="width: 250px"
        >
          <el-option v-for="brand in brands" :key="brand" :label="brand" :value="brand" />
        </el-select>

        <el-input
          v-model="searchQuery"
          placeholder="Поиск по модели..."
          clearable
          @input="filterCars"
          style="width: 200px; margin-left: 10px"
        />
      </div>

      <!-- Вкладки: список / графики -->
      <el-tabs v-model="activeTab" class="tabs">
        <!-- Вкладка "Список" -->
        <el-tab-pane label="📋 Список рекомендаций" name="list">
          <div v-if="loading" class="loading">
            <el-skeleton :rows="5" animated />
          </div>

          <div v-else-if="error" class="error">
            <el-result icon="error" title="Ошибка" :sub-title="error">
              <template #extra>
                <el-button type="primary" @click="loadData">Повторить</el-button>
              </template>
            </el-result>
          </div>

          <div v-else-if="filteredCars.length === 0" class="empty">
            <el-empty description="Нет автомобилей, соответствующих фильтру" />
          </div>

          <div v-else class="recommendations-list">
            <div
              v-for="(car, index) in filteredCars"
              :key="car.id"
              class="recommendation-item"
              :class="{ 'top-1': index === 0, 'top-2': index === 1, 'top-3': index === 2 }"
            >
              <div class="rank-badge">{{ index + 1 }}</div>

              <div class="car-info">
                <div class="car-title">
                  <span class="brand">{{ car.brand }}</span>
                  <span class="model">{{ car.model }}</span>
                  <el-tag size="small" type="info" class="rank-tag"
                    >Score: {{ car.rank?.toFixed(2) || '—' }}</el-tag
                  >
                </div>

                <div class="car-details">
                  <div class="detail">
                    <span class="label">Цена:</span>
                    <span class="value price">{{ formatPrice(car.price) }} ₽</span>
                  </div>
                  <div class="detail" v-if="car.generation">
                    <span class="label">Поколение:</span>
                    <span class="value">{{ car.generation }}</span>
                  </div>
                </div>
              </div>

              <div class="car-actions">
                <el-button type="primary" size="small" @click="showCarDetails(car.id)">
                  Подробнее
                </el-button>
                <el-button size="small" @click="showComparison(car.id)">
                  Сравнить с профилем
                </el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- Вкладка "Графики" -->
        <el-tab-pane label="📊 Аналитика" name="charts">
          <div v-if="recommendations.length === 0" class="empty">
            <el-empty description="Нет данных для визуализации" />
          </div>
          <div v-else>
            <!-- Радарная диаграмма -->
            <div class="chart-container">
              <h3>📈 Сравнение топ-5 автомобилей с вашими предпочтениями</h3>
              <div ref="radarChartRef" class="chart"></div>
              <p class="chart-note">
                * Веса критериев показывают важность для вас (1-10), значения автомобилей — их
                оценки (1-10)
              </p>
            </div>

            <!-- Столбчатая диаграмма -->
            <div class="chart-container">
              <h3>🏆 Итоговый рейтинг (топ-10)</h3>
              <div ref="barChartRef" class="chart"></div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- Диалог с деталями автомобиля -->
    <el-dialog
      v-model="detailsDialogVisible"
      :title="selectedCar?.brand + ' ' + selectedCar?.model"
      width="750px"
    >
      <div v-if="selectedCar" class="car-details-dialog">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Поколение">{{
            selectedCar.generation || '-'
          }}</el-descriptions-item>
          <el-descriptions-item label="Годы выпуска"
            >{{ selectedCar.yearFrom }} - {{ selectedCar.yearTo || 'н.в.' }}</el-descriptions-item
          >
          <el-descriptions-item label="Цена"
            >{{ formatPrice(selectedCar.price) }} ₽</el-descriptions-item
          >
          <el-descriptions-item label="Тип кузова">{{
            selectedCar.bodyType || '-'
          }}</el-descriptions-item>
          <el-descriptions-item label="Топливо">{{
            selectedCar.fuelType || '-'
          }}</el-descriptions-item>
          <el-descriptions-item label="КПП">{{
            selectedCar.transmission || '-'
          }}</el-descriptions-item>
          <el-descriptions-item label="Привод">{{
            selectedCar.drivetrain || '-'
          }}</el-descriptions-item>
          <el-descriptions-item label="Мощность"
            >{{ selectedCar.powerHp }} л.с.</el-descriptions-item
          >
        </el-descriptions>

        <el-divider>⭐ Рейтинги по критериям (1-10)</el-divider>

        <div class="ratings-grid">
          <div class="rating-item">
            <div class="rating-label">
              <span>Безопасность</span>
              <span class="rating-value">{{ selectedCar.safetyRating || 5 }}/10</span>
            </div>
            <el-progress
              :percentage="(selectedCar.safetyRating || 5) * 10"
              :color="'#67C23A'"
              :stroke-width="14"
            />
          </div>
          <div class="rating-item">
            <div class="rating-label">
              <span>Надёжность</span>
              <span class="rating-value">{{ selectedCar.reliabilityRating || 5 }}/10</span>
            </div>
            <el-progress
              :percentage="(selectedCar.reliabilityRating || 5) * 10"
              :color="'#409EFF'"
              :stroke-width="14"
            />
          </div>
          <div class="rating-item">
            <div class="rating-label">
              <span>Экономичность</span>
              <span class="rating-value">{{ selectedCar.economyRating || 5 }}/10</span>
            </div>
            <el-progress
              :percentage="(selectedCar.economyRating || 5) * 10"
              :color="'#E6A23C'"
              :stroke-width="14"
            />
          </div>
          <div class="rating-item">
            <div class="rating-label">
              <span>Комфорт</span>
              <span class="rating-value">{{ selectedCar.comfortRating || 5 }}/10</span>
            </div>
            <el-progress
              :percentage="(selectedCar.comfortRating || 5) * 10"
              :color="'#909399'"
              :stroke-width="14"
            />
          </div>
          <div class="rating-item">
            <div class="rating-label">
              <span>Вместительность</span>
              <span class="rating-value">{{ selectedCar.capacityRating || 5 }}/10</span>
            </div>
            <el-progress
              :percentage="(selectedCar.capacityRating || 5) * 10"
              :color="'#67C23A'"
              :stroke-width="14"
            />
          </div>
          <div class="rating-item">
            <div class="rating-label">
              <span>Динамика</span>
              <span class="rating-value">{{ selectedCar.dynamicsRating || 5 }}/10</span>
            </div>
            <el-progress
              :percentage="(selectedCar.dynamicsRating || 5) * 10"
              :color="'#F56C6C'"
              :stroke-width="14"
            />
          </div>
          <div class="rating-item">
            <div class="rating-label">
              <span>Внешний вид</span>
              <span class="rating-value">{{ selectedCar.appearanceRating || 5 }}/10</span>
            </div>
            <el-progress
              :percentage="(selectedCar.appearanceRating || 5) * 10"
              :color="'#409EFF'"
              :stroke-width="14"
            />
          </div>
          <div class="rating-item">
            <div class="rating-label">
              <span>Оснащение</span>
              <span class="rating-value">{{ selectedCar.featuresRating || 5 }}/10</span>
            </div>
            <el-progress
              :percentage="(selectedCar.featuresRating || 5) * 10"
              :color="'#909399'"
              :stroke-width="14"
            />
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- Диалог сравнения с профилем -->
    <el-dialog
      v-model="comparisonDialogVisible"
      :title="'Сравнение: ' + comparisonCar?.brand + ' ' + comparisonCar?.model"
      width="650px"
    >
      <div v-if="comparisonCar && userProfile" class="comparison-content">
        <p class="comparison-intro">
          Как {{ comparisonCar.brand }} {{ comparisonCar.model }} соответствует вашим предпочтениям?
        </p>

        <div class="comparison-item" v-for="criterion in criteriaList" :key="criterion.key">
          <div class="comparison-label">
            <span>{{ criterion.label }}</span>
            <span class="weight-badge">Ваш вес: {{ getUserWeight(criterion.key) }}/10</span>
          </div>
          <div class="comparison-bar">
            <div
              class="car-value"
              :style="{ width: getCarValuePercent(comparisonCar, criterion.key) + '%' }"
            >
              {{ getCarValue(comparisonCar, criterion.key) }}/10
            </div>
          </div>
          <div
            class="comparison-difference"
            :class="getDifferenceClass(comparisonCar, criterion.key)"
          >
            {{ getDifferenceText(comparisonCar, criterion.key) }}
          </div>
        </div>

        <el-divider />

        <div class="verdict">
          <el-alert :type="getVerdictType(comparisonCar)" :closable="false" show-icon>
            <template #title>{{ getVerdictText(comparisonCar) }}</template>
          </el-alert>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, nextTick } from 'vue'
import { recommendationService } from '@/services/recommendationService'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

// Данные
const recommendations = ref([])
const allCars = ref([])
const userProfile = ref(null)
const loading = ref(true)
const error = ref(null)
const activeTab = ref('list')

// Фильтры
const selectedBrand = ref('')
const searchQuery = ref('')
const brands = ref([])
const filteredCars = ref([])

// UI состояние
const detailsDialogVisible = ref(false)
const comparisonDialogVisible = ref(false)
const selectedCar = ref(null)
const comparisonCar = ref(null)

// Ссылки для графиков
const radarChartRef = ref(null)
const barChartRef = ref(null)

// Список критериев
const criteriaList = [
  { key: 'safetyRating', label: 'Безопасность' },
  { key: 'reliabilityRating', label: 'Надёжность' },
  { key: 'economyRating', label: 'Экономичность' },
  { key: 'comfortRating', label: 'Комфорт' },
  { key: 'capacityRating', label: 'Вместительность' },
  { key: 'dynamicsRating', label: 'Динамика' },
  { key: 'appearanceRating', label: 'Внешний вид' },
  { key: 'featuresRating', label: 'Оснащение' },
]

// Форматирование цены
const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ru-RU')
}

// Получение веса пользователя
const getUserWeight = (criterionKey) => {
  if (!userProfile.value) return 5
  const weightMap = {
    safetyRating: 'weightSafety',
    reliabilityRating: 'weightReliability',
    economyRating: 'weightEconomy',
    comfortRating: 'weightComfort',
    capacityRating: 'weightCapacity',
    dynamicsRating: 'weightDynamics',
    appearanceRating: 'weightAppearance',
    featuresRating: 'weightServiceCost',
  }
  const weightKey = weightMap[criterionKey]
  return userProfile.value[weightKey] || 5
}

// Получение значения автомобиля по критерию
const getCarValue = (car, criterionKey) => {
  return car[criterionKey] || 5
}

// Процент для шкалы
const getCarValuePercent = (car, criterionKey) => {
  return (getCarValue(car, criterionKey) / 10) * 100
}

// Разница между автомобилем и ожиданием пользователя
const getDifference = (car, criterionKey) => {
  const carValue = getCarValue(car, criterionKey)
  const userWeight = getUserWeight(criterionKey)
  const expectedValue = (userWeight / 10) * 10
  return carValue - expectedValue
}

// Класс для отображения разницы
const getDifferenceClass = (car, criterionKey) => {
  const diff = getDifference(car, criterionKey)
  if (diff >= 2) return 'good'
  if (diff >= 0) return 'neutral'
  return 'bad'
}

// Текст разницы
const getDifferenceText = (car, criterionKey) => {
  const diff = getDifference(car, criterionKey)
  if (diff >= 2) return `✓ На ${diff.toFixed(1)} выше ваших ожиданий`
  if (diff >= 0) return `✓ Соответствует ожиданиям`
  return `⚠ На ${Math.abs(diff).toFixed(1)} ниже ожиданий`
}

// Вердикт по автомобилю
const getVerdictType = (car) => {
  const totalDiff = criteriaList.reduce((sum, c) => sum + getDifference(car, c.key), 0)
  if (totalDiff >= 5) return 'success'
  if (totalDiff >= 0) return 'info'
  return 'warning'
}

const getVerdictText = (car) => {
  const totalDiff = criteriaList.reduce((sum, c) => sum + getDifference(car, c.key), 0)
  if (totalDiff >= 5) {
    return 'Этот автомобиль отлично соответствует вашим предпочтениям! 🎉'
  }
  if (totalDiff >= 0) {
    return 'Этот автомобиль неплохо соответствует вашим критериям.'
  }
  return 'Этот автомобиль может не полностью соответствовать вашим ожиданиям. Возможно, стоит рассмотреть другие варианты.'
}

// Загрузка данных
const loadData = async () => {
  loading.value = true
  error.value = null

  try {
    const [rankRes, profileRes] = await Promise.all([
      recommendationService.getRankedCars(),
      recommendationService.getProfile(),
    ])

    recommendations.value = rankRes.data
    userProfile.value = profileRes.data

    // Извлекаем уникальные бренды
    const brandSet = new Set()
    recommendations.value.forEach((car) => {
      if (car.brand) brandSet.add(car.brand)
    })
    brands.value = Array.from(brandSet).sort()

    filterCars()

    if (activeTab.value === 'charts') {
      await nextTick()
      initCharts()
    }
  } catch (err) {
    console.error('Failed to load data:', err)
    error.value = err.response?.data?.message || 'Не удалось загрузить данные'
    ElMessage.error(error.value)
  } finally {
    loading.value = false
  }
}

// Фильтрация
const filterCars = () => {
  let filtered = [...recommendations.value]

  if (selectedBrand.value) {
    filtered = filtered.filter((car) => car.brand === selectedBrand.value)
  }

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(
      (car) => car.model?.toLowerCase().includes(query) || car.brand?.toLowerCase().includes(query),
    )
  }

  filteredCars.value = filtered
}

// Показать детали автомобиля (через отдельный запрос)
const showCarDetails = async (carId) => {
  try {
    const response = await recommendationService.getCarById(carId)
    selectedCar.value = response.data
    detailsDialogVisible.value = true
  } catch (err) {
    console.error('Failed to get car details:', err)
    ElMessage.error('Ошибка загрузки деталей автомобиля')
  }
}

// Показать сравнение с профилем
const showComparison = async (carId) => {
  try {
    const response = await recommendationService.getCarById(carId)
    comparisonCar.value = response.data
    comparisonDialogVisible.value = true
  } catch (err) {
    console.error('Failed to get car for comparison:', err)
    ElMessage.error('Ошибка загрузки данных для сравнения')
  }
}

// Инициализация графиков
const initCharts = () => {
  if (!radarChartRef.value || !barChartRef.value) return

  const topCars = recommendations.value.slice(0, 5)

  const radarOption = {
    radar: {
      indicator: criteriaList.map((c) => ({ name: c.label, max: 10 })),
      shape: 'circle',
      center: ['50%', '50%'],
      radius: '60%',
    },
    legend: {
      data: [...topCars.map((c) => `${c.brand} ${c.model}`), 'Ваши предпочтения'],
      type: 'scroll',
      orient: 'vertical',
      right: 10,
      top: 20,
    },
    series: [
      {
        type: 'radar',
        data: [
          ...topCars.map((car) => ({
            value: criteriaList.map((c) => car[c.key] || 5),
            name: `${car.brand} ${car.model}`,
            areaStyle: { opacity: 0.1 },
          })),
          {
            value: criteriaList.map((c) => getUserWeight(c.key)),
            name: 'Ваши предпочтения',
            lineStyle: { color: '#f56c6c', width: 3, type: 'dashed' },
            areaStyle: { opacity: 0 },
          },
        ],
        symbolSize: 6,
      },
    ],
  }

  const top10Cars = recommendations.value.slice(0, 10)
  const barOption = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    xAxis: {
      type: 'category',
      data: top10Cars.map((c) => `${c.brand} ${c.model}`),
      axisLabel: { rotate: 45, interval: 0 },
    },
    yAxis: { type: 'value', name: 'Рейтинг' },
    series: [
      {
        name: 'Рейтинг ELECTRE',
        type: 'bar',
        data: top10Cars.map((c) => c.rank?.toFixed(2) || 0),
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#409EFF' },
            { offset: 1, color: '#67C23A' },
          ]),
        },
        label: { show: true, position: 'top', formatter: '{c}' },
      },
    ],
  }

  const radarChart = echarts.init(radarChartRef.value)
  const barChart = echarts.init(barChartRef.value)

  radarChart.setOption(radarOption)
  barChart.setOption(barOption)

  window.addEventListener('resize', () => {
    radarChart.resize()
    barChart.resize()
  })
}

watch(activeTab, (newVal) => {
  if (newVal === 'charts' && recommendations.value.length > 0) {
    nextTick(() => initCharts())
  }
})

onMounted(() => loadData())
</script>

<style scoped>
.recommendations-container {
  max-width: 1100px;
  margin: 40px auto;
  padding: 0 20px;
}

.recommendations-card {
  padding: 20px;
}

h2 {
  margin: 0 0 5px;
}
.subtitle {
  color: #909399;
  font-size: 14px;
  margin-bottom: 20px;
}

.filters {
  display: flex;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
.tabs {
  margin-top: 10px;
}

.recommendations-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  max-height: 600px;
  overflow-y: auto;
}

.recommendation-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 12px;
  transition: all 0.3s;
}

.recommendation-item.top-1 {
  background: linear-gradient(135deg, #fff9e6 0%, #fff5d9 100%);
  border: 1px solid #ffd700;
}
.recommendation-item.top-2 {
  background: linear-gradient(135deg, #f5f5f5 0%, #efefef 100%);
  border: 1px solid #c0c0c0;
}
.recommendation-item.top-3 {
  background: linear-gradient(135deg, #fdf5e6 0%, #fce6cf 100%);
  border: 1px solid #cd7f32;
}

.rank-badge {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  background: white;
  border-radius: 50%;
  margin-right: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.car-info {
  flex: 1;
}
.car-title {
  display: flex;
  align-items: baseline;
  gap: 10px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}
.brand {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
.model {
  font-size: 16px;
  color: #606266;
}
.car-details {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}
.detail {
  font-size: 14px;
}
.label {
  color: #909399;
  margin-right: 5px;
}
.value.price {
  color: #f56c6c;
  font-weight: bold;
}
.car-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.loading,
.empty,
.error {
  padding: 40px;
  text-align: center;
}

.chart-container {
  margin-top: 30px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #ebeef5;
}
.chart-container h3 {
  margin: 0 0 15px;
  font-size: 16px;
  color: #303133;
}
.chart {
  width: 100%;
  height: 400px;
}
.chart-note {
  font-size: 12px;
  color: #909399;
  margin-top: 10px;
  text-align: center;
}

.ratings-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}
.rating-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
.rating-label {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #606266;
}
.rating-value {
  font-weight: bold;
  color: #303133;
}

.comparison-content {
  padding: 10px;
}
.comparison-intro {
  margin-bottom: 20px;
  color: #606266;
}
.comparison-item {
  margin-bottom: 15px;
}
.comparison-label {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  font-size: 14px;
}
.weight-badge {
  color: #909399;
  font-size: 12px;
}
.comparison-bar {
  background: #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  height: 30px;
}
.car-value {
  background: linear-gradient(90deg, #409eff, #67c23a);
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 10px;
  color: white;
  font-size: 12px;
  font-weight: bold;
  border-radius: 8px;
}
.comparison-difference {
  font-size: 12px;
  margin-top: 5px;
}
.comparison-difference.good {
  color: #67c23a;
}
.comparison-difference.neutral {
  color: #909399;
}
.comparison-difference.bad {
  color: #f56c6c;
}
.verdict {
  margin-top: 20px;
}
</style>
