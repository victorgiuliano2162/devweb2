<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();

onMounted(() => {
  // Garante que o store está inicializado
  authStore.init();
});
</script>

<style>
#app {
  width: 100%;
  min-height: 100vh;
}
</style>