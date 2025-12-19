// src/services/api.js
import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';
import router from '@/router';

const api = axios.create({
  // Em produção (Docker), usa /api que será proxy pelo Nginx
  // Em desenvolvimento, usa o proxy do Vite
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor de REQUEST - adiciona token JWT em todas as requisições
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('🔑 Token adicionado à requisição:', config.url);
    } else {
      console.log('⚠️ Nenhum token disponível para:', config.url);
    }

    return config;
  },
  (error) => {
    console.error('❌ Erro no interceptor de request:', error);
    return Promise.reject(error);
  }
);

// Interceptor de RESPONSE - trata respostas e erros
api.interceptors.response.use(
  (response) => {
    console.log('✅ Resposta bem-sucedida:', response.config.url);
    return response;
  },
  (error) => {
    console.error('❌ Erro na resposta da API:', {
      url: error.config?.url,
      status: error.response?.status,
      data: error.response?.data
    });

    // Se receber 401 ou 403, significa que o token é inválido/expirado
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      console.warn('🚫 Token inválido ou expirado. Fazendo logout...');

      // Limpa o store de autenticação
      const authStore = useAuthStore();
      authStore.logout();

      // Redireciona para login apenas se não estiver já na rota de login
      if (router.currentRoute.value.path !== '/login') {
        router.push('/login');
      }
    }

    return Promise.reject(error);
  }
);

export default api;