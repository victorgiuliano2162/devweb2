<template>
  <div id="app">
    <RouterView />
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router';
import { onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();

onMounted(() => {
  // Carrega usuário do localStorage ao iniciar
  authStore.loadUserFromStorage();
});
</script>

<style>
/* Importar fonte Inclusive Sans */
@import url('https://fonts.googleapis.com/css2?family=Inclusive+Sans:ital@0;1&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Inclusive Sans', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  min-height: 100vh;
  background: #f5f5f5;
}

/* Garante que todos os elementos usem a fonte */
body,
button,
input,
select,
textarea,
h1, h2, h3, h4, h5, h6,
p, span, div, a {
  font-family: 'Inclusive Sans', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}
</style>