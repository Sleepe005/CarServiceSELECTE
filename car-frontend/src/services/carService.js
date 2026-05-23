// src/services/carService.js
import api from './api'

export const carService = {
  // Получить все автомобили
  getAllCars() {
    return api.get('/car/allCars')
  },
}
