// src/services/authService.js
import api from './api'

export const authService = {
  /**
   * Регистрация нового пользователя
   * @param {string} email - Email пользователя
   * @param {string} rawPass - Пароль
   * @param {string} fullName - Полное имя
   * @returns {Promise} - Ответ от сервера
   */
  register(email, rawPass, fullName) {
    return api.post('/auth/register', {
      email,
      rawPass,
      fullName
    })
  },

  /**
   * Вход пользователя
   * @param {string} email - Email
   * @param {string} password - Пароль
   * @returns {Promise<string>} - JWT токен
   */
  login(email, password) {
    return api.post('/auth/login', {
      email,
      password
    })
  }
}