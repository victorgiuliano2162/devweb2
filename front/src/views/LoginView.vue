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
          <div class="mock-title">📋 Credenciais de Teste (Mock):</div>
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

        <!-- Credenciais Reais -->
        <div v-else class="real-credentials">
          <div class="real-title">🔑 Credenciais para API Real:</div>
          <div class="real-item">
            <strong>Email:</strong> carlos.alberto@empresa.com
          </div>
          <div class="real-item">
            <strong>Senha:</strong> senha123
          </div>
          <div class="real-hint">
            💡 Use qualquer funcionário do banco de dados
          </div>
          <button
            type="button"
            @click="preencherReal"
            class="btn-fill-real"
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
const usarMock = ref(false); // Desativado por padrão para testar API real

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

// Usuário real do banco para testes
const REAL_USER = {
  email: 'carlos.alberto@empresa.com',
  senha: 'senha123'
};

const preencherMock = () => {
  console.log('🔄 Preenchendo credenciais mock...');
  credentials.value.email = MOCK_USER.email;
  credentials.value.senha = MOCK_USER.senha;
  console.log('✓ Credenciais mock preenchidas');
};

const preencherReal = () => {
  console.log('🔄 Preenchendo credenciais reais...');
  credentials.value.email = REAL_USER.email;
  credentials.value.senha = REAL_USER.senha;
  console.log('✓ Credenciais reais preenchidas');
};

const loginMock = () => {
  console.log('\n🎭 === INICIANDO LOGIN MOCK ===');

  const normalizedEmail = credentials.value.email.trim().toLowerCase();
  const expectedEmail = MOCK_USER.email.toLowerCase();

  console.log('📧 Validação de Email:');
  console.log('  • Email digitado:', credentials.value.email);
  console.log('  • Email normalizado:', normalizedEmail);
  console.log('  • Email esperado:', expectedEmail);
  console.log('  • Emails coincidem?', normalizedEmail === expectedEmail);

  console.log('\n🔒 Validação de Senha:');
  console.log('  • Senha digitada:', credentials.value.senha);
  console.log('  • Senha esperada:', MOCK_USER.senha);
  console.log('  • Senhas coincidem?', credentials.value.senha === MOCK_USER.senha);

  if (
    normalizedEmail === expectedEmail &&
    credentials.value.senha === MOCK_USER.senha
  ) {
    console.log('\n✅ VALIDAÇÃO APROVADA!');
    console.log('✓ Credenciais mock corretas');
    console.log('✓ Dados do usuário:', JSON.stringify(MOCK_USER.userData, null, 2));

    authStore.login(MOCK_USER.userData, MOCK_USER.token);

    console.log('✓ Store atualizada');
    console.log('✓ Redirecionando para dashboard...');
    console.log('🎭 === LOGIN MOCK BEM-SUCEDIDO ===\n');

    router.push('/dashboard');
    return true;
  }

  console.error('\n❌ VALIDAÇÃO REJEITADA!');
  error.value = 'Credenciais mock inválidas. Use: admin@test.com / 123456';
  return false;
};

const loginReal = async () => {
  console.log('\n🌐 === INICIANDO LOGIN REAL (API) ===');

  try {
    // Prepara o payload exatamente como o backend espera
    const payload = {
      email: credentials.value.email.trim(),
      senha: credentials.value.senha
    };

    console.log('🔗 Configuração da Requisição:');
    console.log('  • Endpoint: /api/login');
    console.log('  • Método: POST');
    console.log('  • Payload:', JSON.stringify(payload, null, 2));

    console.log('\n📡 Enviando requisição para o backend...');

    // Faz a requisição para o backend
    const response = await ticketService.login(
      payload.email,
      payload.senha
    );

    console.log('\n✅ RESPOSTA RECEBIDA DO BACKEND!');
    console.log('📦 Status:', response.status);
    console.log('📦 Response.data:', JSON.stringify(response.data, null, 2));

    // O backend retorna o objeto Funcionario diretamente
    const funcionario = response.data;

    // Cria userData do formato esperado pela store
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

    // Gera um token simples (o backend ainda não usa JWT)
    const token = 'token-' + Date.now() + '-' + funcionario.id;

    console.log('\n👤 Dados Processados:');
    console.log('  • ID:', userData.id);
    console.log('  • Nome:', userData.nome);
    console.log('  • Email:', userData.email);
    console.log('  • Cargo:', userData.cargo);
    console.log('  • Token gerado:', token);

    console.log('\n💾 Salvando na store...');
    authStore.login(userData, token);

    console.log('✓ Dados salvos na store');
    console.log('✓ Redirecionando para dashboard...');
    console.log('🌐 === LOGIN REAL BEM-SUCEDIDO ===\n');

    router.push('/dashboard');

  } catch (err) {
    console.error('\n❌ ERRO NO LOGIN REAL!');
    console.error('🔴 Erro completo:', err);

    if (err.response) {
      console.error('\n📛 RESPOSTA DE ERRO DO SERVIDOR:');
      console.error('  • Status:', err.response.status);
      console.error('  • Status Text:', err.response.statusText);
      console.error('  • Data:', err.response.data);
      console.error('  • Headers:', err.response.headers);

      if (err.response.status === 401) {
        console.error('\n❌ ERRO 401 - CREDENCIAIS INVÁLIDAS');
        console.error('Possíveis causas:');
        console.error('  1. Email não existe no banco de dados');
        console.error('  2. Senha incorreta');
        console.error('  3. Senhas no banco não estão com hash BCrypt');
        console.error('\n💡 SOLUÇÃO:');
        console.error('  • Verifique se o email existe no banco');
        console.error('  • Confirme que a senha está correta: "senha123"');
        console.error('  • Execute o FuncionarioSQL para popular o banco');

        error.value = 'Email ou senha inválidos. Verifique suas credenciais.';
      } else if (err.response.status === 404) {
        console.error('  ➜ Endpoint /api/login não encontrado');
        error.value = 'Endpoint de login não encontrado. Verifique o backend.';
      } else if (err.response.status === 500) {
        console.error('  ➜ Erro interno do servidor');
        error.value = 'Erro no servidor. Verifique os logs do backend.';
      } else {
        error.value = err.response.data || 'Erro ao fazer login.';
      }
    } else if (err.request) {
      console.error('\n🔌 ERRO DE REDE:');
      console.error('  • Requisição enviada mas sem resposta');
      console.error('  • O backend pode estar offline');
      console.error('  • Verifique se o Docker está rodando');
      console.error('  • Confirme a URL: http://localhost:8080/api/login');
      error.value = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando.';
    } else {
      console.error('\n⚠️ ERRO INESPERADO:');
      console.error('  • Mensagem:', err.message);
      console.error('  • Stack:', err.stack);
      error.value = 'Erro inesperado. Tente novamente.';
    }

    console.log('🌐 === LOGIN REAL FALHOU ===\n');
    throw err;
  }
};

const handleLogin = async () => {
  console.log('\n' + '='.repeat(60));
  console.log('🚀 === TENTATIVA DE LOGIN INICIADA ===');
  console.log('='.repeat(60));
  console.log('⏰ Timestamp:', new Date().toISOString());
  console.log('📍 Modo:', usarMock.value ? 'MOCK (Desenvolvimento)' : 'REAL (API)');
  console.log('\n📋 Credenciais Fornecidas:');
  console.log('  • Email:', credentials.value.email);
  console.log('  • Senha:', credentials.value.senha ? '***' + credentials.value.senha.slice(-2) : '(vazia)');

  loading.value = true;
  error.value = '';

  try {
    if (usarMock.value) {
      console.log('\n🎯 Rota selecionada: LOGIN MOCK');
      loginMock();
    } else {
      console.log('\n🎯 Rota selecionada: LOGIN REAL');
      await loginReal();
    }
  } catch (err) {
    console.error('\n💥 EXCEÇÃO CAPTURADA NO handleLogin');
  } finally {
    loading.value = false;
    console.log('\n⏹️ Loading finalizado');
    console.log('='.repeat(60));
    console.log('=== FIM DA TENTATIVA DE LOGIN ===');
    console.log('='.repeat(60) + '\n');
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

.mock-credentials,
.real-credentials {
  padding: 16px;
  border-radius: 8px;
  border: 2px solid;
  margin-top: 8px;
}

.mock-credentials {
  background: linear-gradient(135deg, #e0f2fe 0%, #bae6fd 100%);
  border-color: #0ea5e9;
}

.real-credentials {
  background: linear-gradient(135deg, #dcfce7 0%, #bbf7d0 100%);
  border-color: #22c55e;
}

.mock-title,
.real-title {
  font-weight: 700;
  margin-bottom: 12px;
  font-size: 14px;
}

.mock-title {
  color: #0c4a6e;
}

.real-title {
  color: #166534;
}

.mock-item,
.real-item {
  font-size: 13px;
  margin-bottom: 6px;
  font-family: monospace;
}

.mock-item {
  color: #0c4a6e;
}

.real-item {
  color: #166534;
}

.real-hint {
  font-size: 12px;
  color: #15803d;
  font-style: italic;
  margin-top: 8px;
  margin-bottom: 8px;
}

.btn-fill-mock,
.btn-fill-real {
  width: 100%;
  padding: 8px;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 10px;
  transition: all 0.2s;
}

.btn-fill-mock {
  background: #0ea5e9;
}

.btn-fill-mock:hover {
  background: #0284c7;
  transform: translateY(-1px);
}

.btn-fill-real {
  background: #22c55e;
}

.btn-fill-real:hover {
  background: #16a34a;
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