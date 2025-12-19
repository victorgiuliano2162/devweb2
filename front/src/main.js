// src/main.js
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import { useAuthStore } from './stores/authStore';
import './style.css';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);

// Inicializa o authStore ANTES de usar o router
const authStore = useAuthStore();
authStore.init(); // Carrega token e usuário do localStorage

app.use(router);
app.mount('#app');

console.log('🚀 Aplicação iniciada');
console.log('🔐 Autenticação:', authStore.isAuthenticated ? 'Autenticado' : 'Não autenticado');