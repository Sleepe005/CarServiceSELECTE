// src/services/recommendationService.js
import api from './api'

export const recommendationService = {
  // Получить ранжированный список автомобилей
  getRankedCars() {
    return api.get('/car/rank')
  },

  // Получить все автомобили
  getAllCars() {
    return api.get('/car/allCars')
  },

  // Получить автомобиль по ID
  getCarById(carId) {
    return api.get('/car/getCar', { params: { carId } })
  },

  // Получить профиль пользователя (веса критериев)
  getProfile() {
    return api.get('/profile/me')
  },
}
