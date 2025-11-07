<template>
  <div class="novo-ticket-container">
    <div class="page-header">
      <button @click="voltar" class="btn-back">← Voltar</button>
      <h1>Novo Ticket de Reparo</h1>
    </div>

    <div class="form-card">
      <form @submit.prevent="criarTicket">
        <div class="form-grid">
          <!-- Tipo de Chamado -->
          <div class="form-group">
            <label for="tipoChamado">Tipo de Chamado *</label>
            <select id="tipoChamado" v-model="novoTicket.tipoChamado" required>
              <option value="">Selecione o tipo</option>
              <option v-for="tipo in enums.tipoChamado" :key="tipo" :value="tipo">
                {{ formatarEnum(tipo) }}
              </option>
            </select>
          </div>

          <!-- Setor -->
          <div class="form-group">
            <label for="setor">Setor *</label>
            <select id="setor" v-model="setorSelecionado" required>
              <option value="">Selecione o setor</option>
              <option v-for="setor in enums.setores" :key="setor" :value="setor">
                {{ formatarEnum(setor) }}
              </option>
            </select>
          </div>

          <!-- Notas -->
          <div class="form-group full-width">
            <label for="notas">Descrição do Problema *</label>
            <textarea
              id="notas"
              v-model="novoTicket.notas"
              rows="6"
              placeholder="Descreva detalhadamente o problema ou solicitação..."
              required
            ></textarea>
          </div>
        </div>

        <div class="form-actions">
          <button type="button" @click="voltar" class="btn-cancel">
            Cancelar
          </button>
          <button type="submit" class="btn-submit" :disabled="loading">
            {{ loading ? 'Criando...' : 'Criar Ticket' }}
          </button>
        </div>

        <div v-if="error" class="error-message">
          {{ error }}
        </div>

        <div v-if="success" class="success-message">
          ✅ Ticket criado com sucesso! Redirecionando...
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useTicketStore } from '@/stores/ticketStore';
import { useAuthStore } from '@/stores/authStore';

const router = useRouter();
const ticketStore = useTicketStore();
const authStore = useAuthStore();

const setorSelecionado = ref('');
const loading = ref(false);
const error = ref('');
const success = ref(false);

const novoTicket = ref({
  tipoChamado: '',
  notas: '',
});

const enums = computed(() => ticketStore.enums);

onMounted(async () => {
  if (ticketStore.enums.setores.length === 0) {
    await ticketStore.carregarEnums();
  }

  // Verifica se o usuário está logado
  if (!authStore.user?.id) {
    error.value = 'Erro: Usuário não está logado. Faça login novamente.';
  }
});

const formatarEnum = (valor) => {
  if (!valor) return '';
  return valor.replace(/_/g, ' ').toLowerCase()
    .split(' ')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
};

const criarTicket = async () => {
  // Validação: verifica se o usuário está logado
  if (!authStore.user?.id) {
    error.value = 'Erro: Usuário não está logado. Faça login novamente.';
    return;
  }

  loading.value = true;
  error.value = '';
  success.value = false;

  try {
    const ticketData = {
      responsavelPelaAbertura: {
        id: authStore.user.id
      },
      tipoChamado: novoTicket.value.tipoChamado,
      setor: {
        nome: setorSelecionado.value
      },
      notas: novoTicket.value.notas
    };

    await ticketStore.criarTicket(ticketData);
    success.value = true;

    setTimeout(() => {
      router.push('/dashboard');
    }, 1500);
  } catch (err) {
    error.value = 'Erro ao criar ticket. Verifique os dados e tente novamente.';
  } finally {
    loading.value = false;
  }
};

const voltar = () => {
  router.push('/dashboard');
};
</script>

<style scoped>
.novo-ticket-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.btn-back {
  padding: 10px 20px;
  background: #6c757d;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}

.btn-back:hover {
  background: #5a6268;
}

.page-header h1 {
  font-size: 32px;
  color: #333;
}

.form-card {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  margin-bottom: 32px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group.full-width {
  grid-column: 1 / -1;
}

.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 120px;
}

.form-actions {
  display: flex;
  gap: 16px;
  justify-content: flex-end;
}

.btn-cancel,
.btn-submit {
  padding: 12px 32px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel {
  background: #e0e0e0;
  color: #333;
}

.btn-cancel:hover {
  background: #d0d0d0;
}

.btn-submit {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-message {
  margin-top: 20px;
  padding: 16px;
  background: #fee;
  color: #c33;
  border-radius: 8px;
  font-size: 14px;
  border-left: 4px solid #c33;
  font-weight: 600;
}

.success-message {
  margin-top: 20px;
  padding: 16px;
  background: #d1fae5;
  color: #065f46;
  border-radius: 8px;
  font-size: 14px;
  text-align: center;
  border-left: 4px solid #10b981;
  font-weight: 600;
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }

  .btn-cancel,
  .btn-submit {
    width: 100%;
  }
}
</style>