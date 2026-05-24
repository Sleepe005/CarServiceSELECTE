// src/stores/authStore.js
import { defineStore } from 'pinia'
import { authService } from '@/services/authService'
import { profileService } from '@/services/profileService'
import api from '@/services/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    profile: null,
    token: localStorage.getItem('token') || null,
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
  },

  actions: {
    async login(email, password) {
      try {
        const response = await authService.login(email, password)
        this.token = response.data
        localStorage.setItem('token', this.token)
        api.defaults.headers.common['Authorization'] = `Bearer ${this.token}`

        await this.fetchProfile()
        return true
      } catch (error) {
        console.error('Login failed:', error)
        return false
      }
    },

    async fetchProfile() {
      try {
        const response = await profileService.getMyProfile()
        this.profile = response.data
        console.log('Profile loaded:', this.profile)
      } catch (error) {
        if (error.response?.status === 404) {
          console.log('Profile not found, will be created on save')
        } else {
          console.error('Failed to fetch profile:', error)
        }
      }
    },

    async updateProfile(profileData) {
      try {
        const response = await profileService.updateProfile(profileData)
        this.profile = response.data
        console.log('Profile updated:', this.profile)
        return true
      } catch (error) {
        console.error('Failed to update profile:', error)
        return false
      }
    },

    logout() {
      this.profile = null
      this.token = null
      localStorage.removeItem('token')
      delete api.defaults.headers.common['Authorization']
      window.location.href = '/login'
    },
  },
})
