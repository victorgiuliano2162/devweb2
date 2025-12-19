<template>
  <div class="modal-overlay" @click="$emit('close')">
    <div class="modal-container edit-modal" @click.stop>
      <div class="modal-header">
        <div class="modal-header-content">
          <h2> Editar Ticket</h2>
          <span class="ticket-id-modal">ID: {{ ticket.id?.substring(0, 8) }}</span>
        </div>
        <button @click="$emit('close')" class="btn-close-modal">×</button>
      </div>

      <div class="modal-body">
        <form @submit.prevent="salvarEdicao" class="edit-form">

          <!-- CAMPOS SOMENTE LEITURA -->
          <div class="readonly-section">
            <h4> Informações do Ticket</h4>

            <div class="readonly-grid">
              <div class="readonly-item">
                <span class="readonly-label">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"/>
                  </svg>
                  Tipo de Chamado
                </span>
                <span class="readonly-value">{{ formatarEnum(ticket.tipoChamado) }}</span>
              </div>

              <div class="readonly-item">
                <span class="readonly-label">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/>
                  </svg>
                  Setor
                </span>
                <span class="readonly-value">{{ formatarEnum(ticket.setor?.nome) }}</span>
              </div>

              <div class="readonly-item">
                <span class="readonly-label">Aberto por:</span>
                <span class="readonly-value">{{ ticket.responsavelPelaAbertura?.nome || '-' }}</span>
              </div>

              <div class="readonly-item">
                <span class="readonly-label">Data de Criação:</span>
                <span class="readonly-value">{{ formatarData(ticket.dataCriacao) }}</span>
              </div>

              <div class="readonly-item">
                <span class="readonly-label">Última Atualização:</span>
                <span class="readonly-value">{{ formatarData(ticket.dataAtualizacao) }}</span>
              </div>

              <div class="readonly-item">
                <span class="readonly-label">Status:</span>
                <span :class="['readonly-badge', ticket.ativo ? 'badge-ativo' : 'badge-inativo']">
                  {{ ticket.ativo ? '🟢 Aberto' : '🔴 Concluído' }}
                </span>
              </div>
            </div>
          </div>

          <!-- CAMPOS EDITÁVEIS -->
          <div class="editable-section">
            <h4>✍Campos Editáveis</h4>

            <div class="form-group">
              <label for="responsavelExecucao">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                </svg>
                Responsável pela Execução
              </label>
              <select
                id="responsavelExecucao"
                v-model="formData.responsavelExecucaoId"
                class="form-select"
              >
                <option value="">Nenhum responsável</option>
                <option v-for="func in funcionarios" :key="func.id" :value="func.id">
                  {{ func.nome }} - {{ formatarEnum(func.cargo) }}
                </option>
              </select>
              <span class="form-hint">Deixe vazio para não atribuir a ninguém</span>
            </div>

            <div class="form-group">
              <label for="notas">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                </svg>
                Notas / Observações
              </label>
              <textarea
                id="notas"
                v-model="formData.notas"
                rows="6"
                class="form-textarea"
                placeholder="Adicione observações sobre o ticket..."
              ></textarea>
            </div>
          </div>

          <div v-if="loading" class="loading-overlay">
            <div class="spinner"></div>
            <p>Salvando alterações...</p>
          </div>

          <div v-if="erro" class="error-message">
            ⚠️ {{ erro }}
          </div>
        </form>
      </div>

      <div class="modal-footer">
        <button @click="$emit('close')" class="btn-modal btn-secondary" :disabled="loading">
          Cancelar
        </button>
        <button @click="salvarEdicao" class="btn-modal btn-primary" :disabled="loading">
           Salvar Alterações
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import ticketService from '@/services/ticketService';

const props = defineProps({
  ticket: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['close', 'saved']);

const loading = ref(false);
const erro = ref('');
const funcionarios = ref([]);

// Agora apenas responsavelExecucaoId e notas são editáveis
const formData = reactive({
  responsavelExecucaoId: props.ticket.responsavelPelaExecucao?.id || '',
  notas: props.ticket.notas || ''
});

onMounted(async () => {
  await carregarFuncionarios();
});

const carregarFuncionarios = async () => {
  try {
    const response = await ticketService.listarFuncionarios();
    funcionarios.value = response.data;
  } catch (error) {
    console.error('Erro ao carregar funcionários:', error);
    erro.value = 'Erro ao carregar lista de funcionários';
  }
};

const salvarEdicao = async () => {
  loading.value = true;
  erro.value = '';

  try {
    const responsavelExecucao = formData.responsavelExecucaoId
      ? funcionarios.value.find(f => f.id === formData.responsavelExecucaoId)
      : null;

    // Mantém todos os dados originais, apenas atualiza responsável e notas
    const ticketAtualizado = {
      id: props.ticket.id,
      tipoChamado: props.ticket.tipoChamado, // Mantém o original
      setor: props.ticket.setor, // Mantém o original
      responsavelPelaAbertura: props.ticket.responsavelPelaAbertura, // Mantém o original
      responsavelPelaExecucao: responsavelExecucao, // EDITÁVEL
      notas: formData.notas, // EDITÁVEL
      ativo: props.ticket.ativo,
      dataCriacao: props.ticket.dataCriacao,
      dataAtualizacao: new Date().toISOString(),
      contatoDoResponsavelPelaAbertura: props.ticket.contatoDoResponsavelPelaAbertura
    };

    await ticketService.atualizarTicket(ticketAtualizado);

    emit('saved', ticketAtualizado);
    emit('close');

  } catch (error) {
    console.error('Erro ao salvar ticket:', error);
    erro.value = error.response?.data?.message || 'Erro ao salvar alterações';
  } finally {
    loading.value = false;
  }
};

const formatarEnum = (valor) => {
  if (!valor) return '-';
  return valor
    .replace(/_/g, ' ')
    .toLowerCase()
    .replace(/\b\w/g, l => l.toUpperCase());
};

const formatarData = (data) => {
  if (!data) return '-';
  const date = new Date(data);
  return date.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-container.edit-modal {
  background: white;
  border-radius: 20px;
  max-width: 800px;
  width: 95%;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 24px;
  border-radius: 20px 20px 0 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header-content h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}

.ticket-id-modal {
  display: inline-block;
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 13px;
  margin-top: 8px;
}

.btn-close-modal {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  font-size: 32px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close-modal:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: rotate(90deg);
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  flex: 1;
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* SEÇÃO SOMENTE LEITURA */
.readonly-section {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  border-left: 4px solid #6c757d;
}

.readonly-section h4 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 16px;
}

.readonly-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.readonly-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.readonly-label {
  font-size: 12px;
  color: #666;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4px;
}

.readonly-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  background: white;
  padding: 8px 12px;
  border-radius: 6px;
}

.readonly-badge {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  width: fit-content;
}

.readonly-badge.badge-ativo {
  background: #d1fae5;
  color: #065f46;
}

.readonly-badge.badge-inativo {
  background: #fee2e2;
  color: #991b1b;
}

/* SEÇÃO EDITÁVEL */
.editable-section {
  background: #e8f4ff;
  padding: 20px;
  border-radius: 12px;
  border-left: 4px solid #667eea;
}

.editable-section h4 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 16px;
}

.form-group:last-child {
  margin-bottom: 0;
}

.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.form-select,
.form-textarea {
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s;
  background: white;
}

.form-select:focus,
.form-textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 120px;
}

.form-hint {
  font-size: 12px;
  color: #666;
  font-style: italic;
}

.loading-overlay {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e0e0e0;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-message {
  padding: 12px;
  background: #fee;
  color: #c33;
  border-radius: 8px;
  border-left: 4px solid #c33;
  font-size: 14px;
}

.modal-footer {
  padding: 20px 24px;
  background: #f8f9fa;
  border-top: 1px solid #e0e0e0;
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.btn-modal {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-modal:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-modal.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-modal.btn-secondary:hover:not(:disabled) {
  background: #5a6268;
}

.btn-modal.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-modal.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

@media (max-width: 768px) {
  .modal-container.edit-modal {
    max-width: 100%;
    height: 100vh;
    max-height: 100vh;
    border-radius: 0;
  }

  .modal-header {
    border-radius: 0;
  }

  .readonly-grid {
    grid-template-columns: 1fr;
  }

  .modal-footer {
    flex-direction: column;
  }

  .btn-modal {
    width: 100%;
  }
}
</style>