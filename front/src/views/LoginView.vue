<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo">🎫</div>
        <h1>Sistema de Tickets</h1>
        <p>Gerenciamento de Reparos</p>
      </div>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="email">Email</label>
          <input
            id="email"
            v-model="credentials.email"
            type="email"
            placeholder="seu@email.com"
            required
            :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="password">Senha</label>
          <input
            id="password"
            v-model="credentials.senha"
            type="password"
            placeholder="••••••••"
            required
            :disabled="loading"
          />
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? 'Entrando...' : 'Entrar' }}
        </button>

        <div v-if="error" class="error-message">
          {{ error }}
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import ticketService from '@/services/ticketService';

const router = useRouter();
const authStore = useAuthStore();

const credentials = ref({
  email: '',
  senha: ''
});

const loading = ref(false);
const error = ref('');

const handleLogin = async () => {
  loading.value = true;
  error.value = '';

  try {
    const payload = {
      email: credentials.value.email.trim(),
      senha: credentials.value.senha
    };

    const response = await ticketService.login(
      payload.email,
      payload.senha
    );

    const funcionario = response.data;

    const userData = {
      id: funcionario.id,
      nome: funcionario.nome,
      email: funcionario.email,
      cargo: funcionario.cargo,
      cpf: funcionario.cpf,
      telefone: funcionario.telefone,
      dataNascimento: funcionario.dataNascimento,
      dataContratacao: funcionario.dataContratacao
    };

    const token = 'token-' + Date.now() + '-' + funcionario.id;

    authStore.login(userData, token);
    router.push('/dashboard');

  } catch (err) {
    if (err.response) {
      if (err.response.status === 401) {
        error.value = 'Email ou senha inválidos.';
      } else if (err.response.status === 404) {
        error.value = 'Serviço indisponível. Tente novamente mais tarde.';
      } else if (err.response.status === 500) {
        error.value = 'Erro no servidor. Tente novamente mais tarde.';
      } else {
        error.value = 'Erro ao fazer login. Tente novamente.';
      }
    } else if (err.request) {
      error.value = 'Não foi possível conectar ao servidor. Verifique sua conexão.';
    } else {
      error.value = 'Erro inesperado. Tente novamente.';
    }
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 40px;
  width: 100%;
  max-width: 420px;
  animation: slideUp 0.4s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  font-size: 64px;
  margin-bottom: 16px;
  animation: bounce 1s ease infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.login-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.login-header p {
  color: #666;
  font-size: 14px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.form-group input {
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-group input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.btn-login {
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  margin-top: 8px;
}

.btn-login:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.btn-login:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.error-message {
  padding: 12px;
  background: #fee;
  color: #c33;
  border-radius: 8px;
  font-size: 14px;
  text-align: center;
  border-left: 4px solid #c33;
  animation: shake 0.5s;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-10px); }
  75% { transform: translateX(10px); }
}

.register-link {
  text-align: center;
  color: #666;
  font-size: 14px;
  padding-top: 16px;
  border-top: 1px solid #e0e0e0;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}

.register-link a:hover {
  color: #764ba2;
  text-decoration: underline;
}

@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
  }

  .logo {
    font-size: 48px;
  }

  .login-header h1 {
    font-size: 24px;
  }
}
</style>