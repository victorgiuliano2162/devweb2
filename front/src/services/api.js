// src/services/api.js
import axios from 'axios';

const api = axios.create({
  // Em produção (Docker), usa /api que será proxy pelo Nginx
  // Em desenvolvimento, usa o proxy do Vite
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para adicionar token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para debug
api.interceptors.response.use(
  (response) => {
    console.log('✅ Resposta da API:', response);
    return response;
  },
  (error) => {
    console.error('❌ Erro na API:', error.response || error);
    return Promise.reject(error);
  }
);

export default api;