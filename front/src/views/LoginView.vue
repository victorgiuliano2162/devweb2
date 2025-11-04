<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo">🎫</div>
        <h1>Sistema de Tickets</h1>
        <p>Gerenciamento de Reparos</p>
      </div>

      <!-- Badge de Modo Dev -->
      <div class="dev-mode-badge" v-if="usarMock">
        🔧 Modo Desenvolvimento (Mock Ativo)
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

        <!-- Toggle Mock -->
        <div class="mock-toggle">
          <label class="toggle-label">
            <input
              type="checkbox"
              v-model="usarMock"
              class="toggle-checkbox"
            />
            <span class="toggle-text">Usar Login Mock (Desenvolvimento)</span>
          </label>
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? 'Entrando...' : 'Entrar' }}
        </button>

        <div v-if="error" class="error-message">
          {{ error }}
        </div>

        <!-- Credenciais Mock -->
        <div v-if="usarMock" class="mock-credentials">
          <div class="mock-title">📋 Credenciais de Teste:</div>
          <div class="mock-item">
            <strong>Email:</strong> admin@test.com
          </div>
          <div class="mock-item">
            <strong>Senha:</strong> 123456
          </div>
          <button
            type="button"
            @click="preencherMock"
            class="btn-fill-mock"
          >
            Preencher Automaticamente
          </button>
        </div>

        <div class="register-link">
          Não tem uma conta?
          <router-link to="/cadastro">Criar conta</router-link>
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
const usarMock = ref(true); // Ativo por padrão em desenvolvimento

// Usuário mock para desenvolvimento
const MOCK_USER = {
  email: 'admin@test.com',
  senha: '123456',
  userData: {
    id: 'mock-user-123',
    nome: 'Administrador Mock',
    email: 'admin@test.com',
    cargo: 'GESTOR'
  },
  token: 'mock-token-' + Date.now()
};

const preencherMock = () => {
  credentials.value.email = MOCK_USER.email;
  credentials.value.senha = MOCK_USER.senha;
};

const loginMock = () => {
  // Verifica credenciais mock
  if (
    credentials.value.email === MOCK_USER.email &&
    credentials.value.senha === MOCK_USER.senha
  ) {
    console.log('✅ Login Mock bem-sucedido!');
    authStore.login(MOCK_USER.userData, MOCK_USER.token);
    router.push('/dashboard');
    return true;
  }

  error.value = 'Credenciais mock inválidas. Use: admin@test.com / 123456';
  return false;
};

const loginReal = async () => {
  try {
    const response = await ticketService.login(
      credentials.value.email,
      credentials.value.senha
    );

    console.log('✅ Login real bem-sucedido:', response.data);

    const userData = response.data.funcionario || response.data.user || {
      id: response.data.id,
      nome: response.data.nome || credentials.value.email.split('@')[0],
      email: credentials.value.email,
      cargo: response.data.cargo || 'TECNICO'
    };

    const token = response.data.token || 'token-' + Date.now();

    authStore.login(userData, token);
    router.push('/dashboard');
  } catch (err) {
    console.error('❌ Erro no login real:', err);

    if (err.response) {
      if (err.response.status === 401 || err.response.status === 403) {
        error.value = 'Email ou senha inválidos';
      } else if (err.response.status === 404) {
        error.value = 'Servidor não encontrado. Ative o modo Mock para testar.';
      } else if (err.response.data?.message) {
        error.value = err.response.data.message;
      } else {
        error.value = 'Erro ao fazer login. Tente o modo Mock.';
      }
    } else if (err.request) {
      error.value = 'Não foi possível conectar ao servidor. Use o modo Mock para continuar testando.';
    } else {
      error.value = 'Erro inesperado. Tente novamente.';
    }

    throw err;
  }
};

const handleLogin = async () => {
  loading.value = true;
  error.value = '';

  try {
    if (usarMock.value) {
      // Usa login mock
      loginMock();
    } else {
      // Tenta login real
      await loginReal();
    }
  } catch (err) {
    // Erro já tratado nas funções acima
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
  margin-bottom: 24px;
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

.dev-mode-badge {
  background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);
  color: white;
  padding: 12px;
  border-radius: 8px;
  text-align: center;
  font-weight: 700;
  font-size: 13px;
  margin-bottom: 20px;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.8; }
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

.mock-toggle {
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 2px solid #e0e0e0;
}

.toggle-label {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  user-select: none;
}

.toggle-checkbox {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.toggle-text {
  font-size: 14px;
  color: #333;
  font-weight: 600;
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

.mock-credentials {
  background: linear-gradient(135deg, #e0f2fe 0%, #bae6fd 100%);
  padding: 16px;
  border-radius: 8px;
  border: 2px solid #0ea5e9;
  margin-top: 8px;
}

.mock-title {
  font-weight: 700;
  color: #0c4a6e;
  margin-bottom: 12px;
  font-size: 14px;
}

.mock-item {
  font-size: 13px;
  color: #0c4a6e;
  margin-bottom: 6px;
  font-family: monospace;
}

.btn-fill-mock {
  width: 100%;
  padding: 8px;
  background: #0ea5e9;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 10px;
  transition: all 0.2s;
}

.btn-fill-mock:hover {
  background: #0284c7;
  transform: translateY(-1px);
}

.register-link {
  text-align: center;
  color: #666;
  font-size: 14px;
  margin-top: 8px;
  padding-top: 8px;
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
</style>