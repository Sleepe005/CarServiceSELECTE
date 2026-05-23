// src/services/profileService.js
import api from './api'

export const profileService = {
  // Получить профиль текущего пользователя
  getMyProfile() {
    return api.get('/profile/me')
  },
  
  // Обновить профиль
  updateProfile(profileData) {
    return api.put('/profile/me', profileData)
  }
}