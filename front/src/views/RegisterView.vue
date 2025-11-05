<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <div class="logo">🎫</div>
        <h1>Criar Conta</h1>
        <p>Sistema de Tickets</p>
      </div>

      <form @submit.prevent="handleRegister" class="register-form">
        <div class="form-group">
          <label for="nome">Nome Completo</label>
          <input
            id="nome"
            v-model="formData.nome"
            type="text"
            placeholder="Seu nome completo"
            required
            :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input
            id="email"
            v-model="formData.email"
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
            v-model="formData.password"
            type="password"
            placeholder="Mínimo 6 caracteres"
            required
            minlength="6"
            :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="confirmPassword">Confirmar Senha</label>
          <input
            id="confirmPassword"
            v-model="formData.confirmPassword"
            type="password"
            placeholder="Digite a senha novamente"
            required
            :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="cargo">Cargo</label>
          <select
            id="cargo"
            v-model="formData.cargo"
            required
            :disabled="loading"
          >
            <option value="">Selecione um cargo</option>
            <option value="TECNICO">Técnico</option>
            <option value="GESTOR">Gestor</option>
            <option value="USUARIO">Usuário</option>
          </select>
        </div>

        <button type="submit" class="btn-register" :disabled="loading">
          {{ loading ? 'Cadastrando...' : 'Criar Conta' }}
        </button>

        <div v-if="error" class="error-message">
          {{ error }}
        </div>

        <div v-if="success" class="success-message">
          {{ success }}
        </div>

        <div class="login-link">
          Já tem uma conta?
          <router-link to="/login">Fazer login</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ticketService from '@/services/ticketService';

const router = useRouter();

const formData = ref({
  nome: '',
  email: '',
  password: '',
  confirmPassword: '',
  cargo: ''
});

const loading = ref(false);
const error = ref('');
const success = ref('');

const handleRegister = async () => {
  error.value = '';
  success.value = '';

  // Validações
  if (formData.value.password !== formData.value.confirmPassword) {
    error.value = 'As senhas não coincidem';
    return;
  }

  if (formData.value.password.length < 6) {
    error.value = 'A senha deve ter pelo menos 6 caracteres';
    return;
  }

  loading.value = true;

  try {
    const userData = {
      nome: formData.value.nome,
      email: formData.value.email,
      senha: formData.value.password,
      cargo: formData.value.cargo
    };

    const response = await ticketService.cadastrar(userData);

    console.log('✅ Cadastro bem-sucedido:', response.data);

    success.value = 'Conta criada com sucesso! Redirecionando...';

    // Redirecionar para login após 2 segundos
    setTimeout(() => {
      router.push('/login');
    }, 2000);

  } catch (err) {
    console.error('❌ Erro no cadastro:', err);

    if (err.response) {
      if (err.response.status === 409) {
        error.value = 'Este email já está cadastrado';
      } else if (err.response.status === 400) {
        error.value = err.response.data.message || 'Dados inválidos';
      } else if (err.response.status === 404) {
        error.value = 'Endpoint de cadastro não encontrado no servidor';
      } else if (err.response.data?.message) {
        error.value = err.response.data.message;
      } else {
        error.value = 'Erro ao criar conta. Tente novamente.';
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
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 40px;
  width: 100%;
  max-width: 480px;
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

.register-header {
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

.register-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.register-header p {
  color: #666;
  font-size: 14px;
}

.register-form {
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

.form-group input,
.form-group select {
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-group input:disabled,
.form-group select:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.btn-register {
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

.btn-register:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.btn-register:disabled {
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

.success-message {
  padding: 12px;
  background: #e8f5e9;
  color: #2e7d32;
  border-radius: 8px;
  font-size: 14px;
  text-align: center;
  border-left: 4px solid #4caf50;
  animation: slideUp 0.4s;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-10px); }
  75% { transform: translateX(10px); }
}

.login-link {
  text-align: center;
  color: #666;
  font-size: 14px;
  margin-top: 8px;
}

.login-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>