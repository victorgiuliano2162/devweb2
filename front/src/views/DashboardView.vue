<template>
  <div class="dashboard">
    <header class="dashboard-header">
      <div>
        <h1>Dashboard de Tickets</h1>
        <p class="user-info">Bem-vindo, {{ authStore.getUserName }}</p>
      </div>
      <button @click="logout" class="btn-logout">Sair</button>
    </header>

    <!-- Cards de estatísticas -->
    <div class="stats-grid">
      <div class="stat-card abertos">
        <div class="stat-icon">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
          </svg>
        </div>
        <div class="stat-info">
          <h3>Abertos</h3>
          <p class="stat-number">{{ ticketsAbertos.length }}</p>
        </div>
      </div>

      <div class="stat-card concluidos">
        <div class="stat-icon">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
        </div>
        <div class="stat-info">
          <h3>Concluídos</h3>
          <p class="stat-number">{{ ticketsConcluidos.length }}</p>
        </div>
      </div>

      <div class="stat-card total">
        <div class="stat-icon">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
          </svg>
        </div>
        <div class="stat-info">
          <h3>Total</h3>
          <p class="stat-number">{{ totalElements }}</p>
        </div>
      </div>

      <div class="stat-card setores">
        <div class="stat-icon">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/>
          </svg>
        </div>
        <div class="stat-info">
          <h3>Setores Ativos</h3>
          <p class="stat-number">{{ Object.keys(ticketsPorSetor).length }}</p>
        </div>
      </div>
    </div>

    <!-- Filtros Avançados -->
    <div class="filters-section">
      <h3>Filtros</h3>
      <div class="filters">
        <!-- Tipo de Chamado (trocado com Setor) -->
        <select v-model="filtros.tipoChamado" @change="aplicarFiltros" class="filter-select">
          <option value="">Todos os Tipos</option>
          <option v-for="tipo in enums.tipoChamado" :key="tipo" :value="tipo">
            {{ formatarEnum(tipo) }}
          </option>
        </select>

        <!-- Setor (trocado com Tipo) -->
        <select v-model="filtros.setor" @change="aplicarFiltros" class="filter-select">
          <option value="">Todos os Setores</option>
          <option v-for="setor in enums.setores" :key="setor" :value="setor">
            {{ formatarEnum(setor) }}
          </option>
        </select>

        <!-- Status (Ativo/Inativo) -->
        <select v-model="filtros.ativo" @change="aplicarFiltros" class="filter-select">
          <option value="">Todos os Status</option>
          <option value="true">Abertos</option>
          <option value="false">Concluídos</option>
        </select>

        <!-- Data -->
        <input
          v-model="filtros.date"
          @change="aplicarFiltros"
          type="date"
          class="filter-select"
          placeholder="Filtrar por data"
        />

        <!-- Ordenação -->
        <select v-model="filtros.sort" @change="aplicarFiltros" class="filter-select">
          <option value="dataCriacao,desc">Mais Recentes</option>
          <option value="dataCriacao,asc">Mais Antigos</option>
          <option value="tipoChamado,asc">Tipo (A-Z)</option>
          <option value="setor.nome,asc">Setor (A-Z)</option>
        </select>

        <button
          @click="limparFiltros"
          class="btn-clear"
          :disabled="!temFiltrosAtivos"
        >
          Limpar Filtros
        </button>

        <button @click="abrirModalNovo" class="btn-novo">
          + Novo Ticket
        </button>
      </div>

      <!-- Indicador de filtros ativos -->
      <div v-if="temFiltrosAtivos" class="filtros-ativos">
        <span class="badge-filtro" v-if="filtros.tipoChamado">
          Tipo: {{ formatarEnum(filtros.tipoChamado) }}
          <button @click="removerFiltro('tipoChamado')">×</button>
        </span>
        <span class="badge-filtro" v-if="filtros.setor">
          Setor: {{ formatarEnum(filtros.setor) }}
          <button @click="removerFiltro('setor')">×</button>
        </span>
        <span class="badge-filtro" v-if="filtros.ativo !== ''">
          Status: {{ filtros.ativo === 'true' ? 'Abertos' : 'Concluídos' }}
          <button @click="removerFiltro('ativo')">×</button>
        </span>
        <span class="badge-filtro" v-if="filtros.date">
          Data: {{ formatarData(filtros.date) }}
          <button @click="removerFiltro('date')">×</button>
        </span>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>Carregando tickets...</p>
    </div>

    <!-- Tabela de tickets -->
    <div v-else class="tickets-table-container">
      <table class="tickets-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Setor</th>
            <th>Tipo</th>
            <th>Responsável Abertura</th>
            <th>Responsável Execução</th>
            <th>Data Criação</th>
            <th>Status</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ticket in tickets" :key="ticket.id">
            <td><span class="ticket-id">{{ ticket.id.substring(0, 8) }}</span></td>
            <td>
              <span class="badge-setor">{{ formatarEnum(ticket.setor?.nome) }}</span>
            </td>
            <td>{{ formatarEnum(ticket.tipoChamado) }}</td>
            <td>{{ ticket.responsavelPelaAbertura?.nome || '-' }}</td>
            <td>{{ ticket.responsavelPelaExecucao?.nome || '-' }}</td>
            <td>{{ formatarData(ticket.dataCriacao) }}</td>
            <td>
              <span :class="['badge', ticket.ativo ? 'badge-aberto' : 'badge-concluido']">
                {{ ticket.ativo ? 'Aberto' : 'Concluído' }}
              </span>
            </td>
            <td>
              <button @click="verDetalhes(ticket)" class="btn-action">Ver</button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-if="tickets.length === 0" class="no-data">
        <p>🔭 Nenhum ticket encontrado</p>
        <p class="no-data-subtitle">Tente ajustar os filtros ou criar um novo ticket</p>
      </div>
    </div>

    <!-- Paginação -->
    <div v-if="totalPages > 1" class="pagination">
      <button
        @click="mudarPagina(currentPage - 1)"
        :disabled="currentPage === 0"
        class="btn-page"
      >
        ← Anterior
      </button>

      <div class="page-numbers">
        <button
          v-for="page in paginasVisiveis"
          :key="page"
          @click="mudarPagina(page)"
          :class="['btn-page-num', { active: page === currentPage }]"
        >
          {{ page + 1 }}
        </button>
      </div>

      <span class="page-info">
        Página {{ currentPage + 1 }} de {{ totalPages }}
        ({{ totalElements }} tickets)
      </span>

      <button
        @click="mudarPagina(currentPage + 1)"
        :disabled="currentPage >= totalPages - 1"
        class="btn-page"
      >
        Próxima →
      </button>
    </div>

    <!-- MODAL DE DETALHES DO TICKET -->
    <div v-if="modalAberto" class="modal-overlay" @click="fecharModal">
      <div class="modal-container" @click.stop>
        <div class="modal-header">
          <div class="modal-header-content">
            <h2>Detalhes do Ticket</h2>
            <span class="ticket-id-modal">ID: {{ ticketSelecionado.id }}</span>
          </div>
          <button @click="fecharModal" class="btn-close-modal">×</button>
        </div>

        <div class="modal-body">
          <!-- Status Badge Grande -->
          <div class="status-badge-large">
            <span :class="['badge-status-large', ticketSelecionado.ativo ? 'status-aberto' : 'status-concluido']">
              <svg v-if="ticketSelecionado.ativo" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 8px;">
                <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
              </svg>
              <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 8px;">
                <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              {{ ticketSelecionado.ativo ? 'Aberto' : 'Concluído' }}
            </span>
          </div>

          <!-- Informações Principais -->
          <div class="info-grid">
            <div class="info-card">
              <div class="info-label">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 6px;">
                  <path d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"/>
                </svg>
                Tipo de Chamado
              </div>
              <div class="info-value">{{ formatarEnum(ticketSelecionado.tipoChamado) }}</div>
            </div>

            <div class="info-card">
              <div class="info-label">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 6px;">
                  <path d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/>
                </svg>
                Setor
              </div>
              <div class="info-value">{{ formatarEnum(ticketSelecionado.setor?.nome) }}</div>
            </div>

            <div class="info-card">
              <div class="info-label">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 6px;">
                  <path d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                </svg>
                Data de Criação
              </div>
              <div class="info-value">{{ formatarDataCompleta(ticketSelecionado.dataCriacao) }}</div>
            </div>

            <div class="info-card">
              <div class="info-label">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 6px;">
                  <path d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
                </svg>
                Última Atualização
              </div>
              <div class="info-value">{{ formatarDataCompleta(ticketSelecionado.dataAtualizacao) }}</div>
            </div>
          </div>

          <!-- Responsáveis -->
          <div class="responsaveis-section">
            <h3>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 8px;">
                <path d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
              </svg>
              Responsáveis
            </h3>
            <div class="responsaveis-grid">
              <div class="responsavel-card abertura">
                <div class="responsavel-header">
                  <span class="responsavel-icon">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M8 11V7a4 4 0 118 0m-4 8v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2z"/>
                    </svg>
                  </span>
                  <span class="responsavel-tipo">Responsável pela Abertura</span>
                </div>
                <div class="responsavel-info">
                  <div class="responsavel-nome">
                    {{ ticketSelecionado.responsavelPelaAbertura?.nome || 'Não atribuído' }}
                  </div>
                  <div class="responsavel-contato" v-if="ticketSelecionado.contatoDoResponsavelPelaAbertura">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 6px;">
                      <path d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
                    </svg>
                    {{ ticketSelecionado.contatoDoResponsavelPelaAbertura }}
                  </div>
                </div>
              </div>

              <div class="responsavel-card execucao">
                <div class="responsavel-header">
                  <span class="responsavel-icon">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/>
                      <path d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                    </svg>
                  </span>
                  <span class="responsavel-tipo">Responsável pela Execução</span>
                </div>
                <div class="responsavel-info">
                  <div class="responsavel-nome">
                    {{ ticketSelecionado.responsavelPelaExecucao?.nome || 'Não atribuído' }}
                  </div>
                  <div class="responsavel-contato" v-if="ticketSelecionado.responsavelPelaExecucao?.email">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 6px;">
                      <path d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                    </svg>
                    {{ ticketSelecionado.responsavelPelaExecucao.email }}
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Descrição/Notas -->
          <div class="notas-section">
            <h3>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="display: inline-block; vertical-align: middle; margin-right: 8px;">
                <path d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
              </svg>
              Descrição do Problema
            </h3>
            <div class="notas-content">
              {{ ticketSelecionado.notas || 'Sem descrição' }}
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button @click="fecharModal" class="btn-modal btn-secondary">
            Fechar
          </button>
          <button
            v-if="ticketSelecionado.ativo"
            @click="atribuirResponsavel"
            class="btn-modal btn-primary"
          >
            Atribuir Responsável
          </button>
          <button
            v-if="ticketSelecionado.ativo"
            @click="marcarConcluido"
            class="btn-modal btn-success"
          >
            Marcar como Concluído
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useTicketStore } from '@/stores/ticketStore';
import { useAuthStore } from '@/stores/authStore';

const router = useRouter();
const ticketStore = useTicketStore();
const authStore = useAuthStore();

const filtros = ref({
  setor: '',
  tipoChamado: '',
  ativo: '',
  date: '',
  sort: 'dataCriacao,desc'
});

const modalAberto = ref(false);
const ticketSelecionado = ref({});

const tickets = computed(() => ticketStore.tickets);
const ticketsAbertos = computed(() => ticketStore.ticketsAbertos);
const ticketsConcluidos = computed(() => ticketStore.ticketsConcluidos);
const ticketsPorSetor = computed(() => ticketStore.ticketsPorSetor);
const enums = computed(() => ticketStore.enums);
const loading = computed(() => ticketStore.loading);
const totalPages = computed(() => ticketStore.totalPages);
const currentPage = computed(() => ticketStore.currentPage);
const totalElements = computed(() => ticketStore.totalElements);
const temFiltrosAtivos = computed(() => ticketStore.temFiltrosAtivos);

// Páginas visíveis na paginação
const paginasVisiveis = computed(() => {
  const pages = [];
  const max = Math.min(totalPages.value, 5);
  let start = Math.max(0, currentPage.value - 2);
  let end = Math.min(totalPages.value, start + max);

  if (end - start < max) {
    start = Math.max(0, end - max);
  }

  for (let i = start; i < end; i++) {
    pages.push(i);
  }
  return pages;
});

onMounted(async () => {
  await ticketStore.carregarEnums();
  await ticketStore.carregarTickets();
});

const formatarEnum = (valor) => {
  if (!valor) return '-';
  return valor.replace(/_/g, ' ').toLowerCase()
    .split(' ')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
};

const formatarData = (data) => {
  if (!data) return '-';
  return new Date(data).toLocaleDateString('pt-BR');
};

const formatarDataCompleta = (data) => {
  if (!data) return '-';
  return new Date(data).toLocaleString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};


const aplicarFiltros = async () => {
  const filtrosLimpos = {};

  // Adiciona cada filtro apenas se tiver valor
  if (filtros.value.setor) {
    filtrosLimpos.setor = filtros.value.setor;
  }

  if (filtros.value.tipoChamado) {
    filtrosLimpos.tipoChamado = filtros.value.tipoChamado;
  }

  if (filtros.value.ativo !== '') {
    filtrosLimpos.ativo = filtros.value.ativo === 'true';
  }

  if (filtros.value.date) {
    filtrosLimpos.date = filtros.value.date;
  }

  if (filtros.value.sort) {
    filtrosLimpos.sort = filtros.value.sort;
  }

  // Aplica os filtros combinados
  await ticketStore.aplicarFiltros(filtrosLimpos);
};

const limparFiltros = async () => {
  filtros.value = {
    setor: '',
    tipoChamado: '',
    ativo: '',
    date: '',
    sort: 'dataCriacao,desc'
  };
  await ticketStore.limparFiltros();
};

const removerFiltro = async (filtro) => {
  filtros.value[filtro] = '';
  await aplicarFiltros();
};

const mudarPagina = async (page) => {
  await ticketStore.mudarPagina(page);
};

const verDetalhes = (ticket) => {
  ticketSelecionado.value = ticket;
  modalAberto.value = true;
};

const fecharModal = () => {
  modalAberto.value = false;
  ticketSelecionado.value = {};
};

const atribuirResponsavel = () => {
  alert('Funcionalidade de atribuir responsável em desenvolvimento!');
};

const marcarConcluido = async () => {
  if (confirm('Deseja realmente marcar este ticket como concluído?')) {
    try {
      await ticketStore.marcarConcluido(ticketSelecionado.value.id);
      fecharModal();
    } catch (error) {
      console.error('Erro ao marcar ticket como concluído:', error);
      alert('❌ Erro ao marcar ticket como concluído. Tente novamente.');
    }
  }
};

const abrirModalNovo = () => {
  router.push('/novo-ticket');
};

const logout = () => {
  authStore.logout();
  router.push('/login');
};
</script>

<style scoped>
.dashboard {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.dashboard-header h1 {
  font-size: 32px;
  color: #333;
  margin-bottom: 4px;
}

.user-info {
  color: #666;
  font-size: 14px;
}

.btn-logout {
  padding: 10px 20px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}

.btn-logout:hover {
  background: #c82333;
  transform: translateY(-2px);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
}

.stat-card.abertos { border-left: 4px solid #3b82f6; }
.stat-card.concluidos { border-left: 4px solid #10b981; }
.stat-card.total { border-left: 4px solid #8b5cf6; }
.stat-card.setores { border-left: 4px solid #f59e0b; }

.stat-icon {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
}

.stat-card.abertos .stat-icon {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.stat-card.concluidos .stat-icon {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
}

.stat-card.total .stat-icon {
  background: rgba(139, 92, 246, 0.1);
  color: #8b5cf6;
}

.stat-card.setores .stat-icon {
  background: rgba(245, 158, 11, 0.1);
  color: #f59e0b;
}

.stat-info h3 {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
  color: #333;
}

.filters-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.filters-section h3 {
  margin-bottom: 16px;
  color: #333;
}

.filters {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-select {
  padding: 10px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  min-width: 180px;
  background: white;
  transition: all 0.2s;
}

.filter-select:focus {
  outline: none;
  border-color: #3b82f6;
}

.btn-clear, .btn-novo {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
}

.btn-clear {
  background: #6c757d;
  color: white;
}

.btn-clear:hover:not(:disabled) {
  background: #5a6268;
}

.btn-clear:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-novo {
  background: #10b981;
  color: white;
  margin-left: auto;
}

.btn-novo:hover {
  background: #059669;
  transform: translateY(-2px);
}

.filtros-ativos {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #e0e0e0;
}

.badge-filtro {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background: #e3f2fd;
  color: #1565c0;
  border-radius: 16px;
  font-size: 13px;
  font-weight: 600;
}

.badge-filtro button {
  background: none;
  border: none;
  color: #1565c0;
  font-size: 18px;
  cursor: pointer;
  line-height: 1;
  padding: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background 0.2s;
}

.badge-filtro button:hover {
  background: rgba(0,0,0,0.1);
}

.loading {
  text-align: center;
  padding: 60px 20px;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3b82f6;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.tickets-table-container {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.tickets-table {
  width: 100%;
  border-collapse: collapse;
}

.tickets-table thead {
  background: #f8f9fa;
}

.tickets-table th {
  padding: 16px;
  text-align: left;
  font-weight: 600;
  color: #333;
  border-bottom: 2px solid #e0e0e0;
  font-size: 14px;
}

.tickets-table td {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}

.tickets-table tbody tr:hover {
  background: #f8f9fa;
}

.ticket-id {
  font-family: monospace;
  background: #f0f0f0;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.badge-setor {
  display: inline-block;
  padding: 4px 10px;
  background: #fff3cd;
  color: #856404;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.badge-aberto {
  background: #dbeafe;
  color: #1e40af;
}

.badge-concluido {
  background: #d1fae5;
  color: #065f46;
}

.btn-action {
  padding: 6px 16px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}

.btn-action:hover {
  background: #2563eb;
}

.no-data {
  padding: 60px;
  text-align: center;
  color: #666;
}

.no-data p:first-child {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
}

.no-data-subtitle {
  font-size: 14px;
  color: #999;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 24px;
  flex-wrap: wrap;
}

.btn-page {
  padding: 8px 16px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.2s;
}

.btn-page:hover:not(:disabled) {
  background: #2563eb;
}

.btn-page:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 4px;
}

.btn-page-num {
  padding: 8px 12px;
  background: white;
  color: #333;
  border: 2px solid #e0e0e0;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.2s;
}

.btn-page-num:hover {
  border-color: #3b82f6;
  color: #3b82f6;
}

.btn-page-num.active {
  background: #3b82f6;
  color: white;
  border-color: #3b82f6;
}

.page-info {
  color: #666;
  font-size: 14px;
  font-weight: 600;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-container {
  background: white;
  border-radius: 16px;
  max-width: 800px;
  width: 100%;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
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
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.modal-header-content h2 {
  font-size: 24px;
  margin-bottom: 8px;
}

.ticket-id-modal {
  font-family: monospace;
  font-size: 13px;
  opacity: 0.9;
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 10px;
  border-radius: 6px;
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
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  line-height: 1;
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

.status-badge-large {
  text-align: center;
  margin-bottom: 24px;
}

.badge-status-large {
  display: inline-block;
  padding: 12px 32px;
  border-radius: 24px;
  font-size: 18px;
  font-weight: 700;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.badge-status-large.status-aberto {
  background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
  color: #1e40af;
}

.badge-status-large.status-concluido {
  background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
  color: #065f46;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.info-card {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 12px;
  border-left: 4px solid #667eea;
}

.info-label {
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
  font-weight: 600;
}

.info-value {
  font-size: 16px;
  color: #333;
  font-weight: 700;
}

.responsaveis-section {
  margin-bottom: 24px;
}

.responsaveis-section h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 16px;
  font-weight: 700;
}

.responsaveis-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
}

.responsavel-card {
  background: white;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  padding: 16px;
  transition: all 0.3s;
}

.responsavel-card:hover {
  border-color: #667eea;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.responsavel-card.abertura {
  border-left: 4px solid #3b82f6;
}

.responsavel-card.execucao {
  border-left: 4px solid #10b981;
}

.responsavel-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e0e0e0;
}

.responsavel-icon {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.responsavel-tipo {
  font-size: 13px;
  font-weight: 700;
  color: #666;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.responsavel-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.responsavel-nome {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.responsavel-contato {
  font-size: 14px;
  color: #666;
  background: #f8f9fa;
  padding: 8px 12px;
  border-radius: 8px;
}

.notas-section {
  margin-bottom: 24px;
}

.notas-section h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 12px;
  font-weight: 700;
}

.notas-content {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  border-left: 4px solid #f59e0b;
  font-size: 15px;
  line-height: 1.6;
  color: #333;
  white-space: pre-wrap;
  word-wrap: break-word;
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

.btn-modal.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-modal.btn-secondary:hover {
  background: #5a6268;
}

.btn-modal.btn-primary {
  background: #3b82f6;
  color: white;
}

.btn-modal.btn-primary:hover {
  background: #2563eb;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.btn-modal.btn-success {
  background: #10b981;
  color: white;
}

.btn-modal.btn-success:hover {
  background: #059669;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
}

@media (max-width: 768px) {
  .filters {
    flex-direction: column;
  }

  .filter-select {
    width: 100%;
  }

  .btn-novo {
    margin-left: 0;
    width: 100%;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .responsaveis-grid {
    grid-template-columns: 1fr;
  }

  .modal-footer {
    flex-direction: column;
  }

  .btn-modal {
    width: 100%;
  }

  .modal-container {
    max-height: 95vh;
  }
}
</style>