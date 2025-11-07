// src/stores/ticketStore.js
import { defineStore } from 'pinia';
import ticketService from '@/services/ticketService';

export const useTicketStore = defineStore('ticket', {
  state: () => ({
    tickets: [],
    enums: {
      statusChamado: [],
      setores: [],
      tipoChamado: [],
      tipoFuncionario: []
    },
    loading: false,
    error: null,
    totalPages: 0,
    currentPage: 0,
    totalElements: 0,
    size: 20,
    // Filtros ativos
    filtrosAtivos: {
      setor: null,
      tipoChamado: null,
      ativo: null,
      funcId: null,
      date: null,
      sort: 'dataCriacao,desc'
    }
  }),

  actions: {
    async carregarEnums() {
      try {
        const response = await ticketService.buscarEnums();
        this.enums = {
          statusChamado: response.data.StatusChamado || [],
          setores: response.data.Setores || [],
          tipoChamado: response.data['Tipo Chamado'] || [],
          tipoFuncionario: response.data['Tipo Funcionario'] || []
        };
      } catch (error) {
        console.error('Erro ao carregar enums:', error);
        this.error = 'Erro ao carregar dados do sistema';
      }
    },

    async carregarTickets(params = {}) {
      this.loading = true;
      this.error = null;
      try {
        // Merge com filtros ativos
        const filtros = {
          page: params.page || this.currentPage,
          size: params.size || this.size,
          ...this.filtrosAtivos,
          ...params
        };

        // Remove valores null/undefined
        Object.keys(filtros).forEach(key => {
          if (filtros[key] === null || filtros[key] === undefined || filtros[key] === '') {
            delete filtros[key];
          }
        });

        console.log('🔍 Carregando tickets com filtros:', filtros);

        const response = await ticketService.buscarComFiltros(filtros);

        this.tickets = response.data.content || [];
        this.totalPages = response.data.totalPages || 0;
        this.currentPage = response.data.number || 0;
        this.totalElements = response.data.totalElements || 0;
        this.size = response.data.size || 20;

        console.log('✅ Tickets carregados:', this.tickets.length);
      } catch (error) {
        console.error('Erro ao carregar tickets:', error);
        this.error = 'Erro ao carregar tickets';
        this.tickets = [];
      } finally {
        this.loading = false;
      }
    },

    async criarTicket(ticketData) {
      this.loading = true;
      this.error = null;
      try {
        const response = await ticketService.criarTicket(ticketData);
        await this.carregarTickets();
        return response.data;
      } catch (error) {
        console.error('Erro ao criar ticket:', error);
        this.error = 'Erro ao criar ticket';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async marcarConcluido(ticketId) {
      this.loading = true;
      this.error = null;
      try {
        console.log('🔄 Marcando ticket como concluído:', ticketId);
        const response = await ticketService.marcarConcluido(ticketId);
        console.log('✅ Ticket concluído com sucesso:', response.data);

        // Recarrega os tickets para atualizar a lista
        await this.carregarTickets();

        return response.data;
      } catch (error) {
        console.error('❌ Erro ao marcar ticket como concluído:', error);
        this.error = 'Erro ao marcar ticket como concluído';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    // Aplicar filtros
    async aplicarFiltros(filtros) {
      this.filtrosAtivos = {
        ...this.filtrosAtivos,
        ...filtros
      };
      await this.carregarTickets({ page: 0 });
    },

    // Limpar filtros
    async limparFiltros() {
      this.filtrosAtivos = {
        setor: null,
        tipoChamado: null,
        ativo: null,
        funcId: null,
        date: null,
        sort: 'dataCriacao,desc'
      };
      await this.carregarTickets({ page: 0 });
    },

    // Mudar página
    async mudarPagina(page) {
      await this.carregarTickets({ page });
    },

    // Mudar ordenação
    async mudarOrdenacao(sort) {
      this.filtrosAtivos.sort = sort;
      await this.carregarTickets({ page: 0 });
    }
  },

  getters: {
    ticketsAbertos: (state) => {
      return state.tickets.filter(t => t.ativo === true);
    },

    ticketsConcluidos: (state) => {
      return state.tickets.filter(t => t.ativo === false);
    },

    ticketsPorSetor: (state) => {
      const contagem = {};
      state.tickets.forEach(ticket => {
        const setor = ticket.setor?.nome || 'Sem Setor';
        contagem[setor] = (contagem[setor] || 0) + 1;
      });
      return contagem;
    },

    temFiltrosAtivos: (state) => {
      return Object.values(state.filtrosAtivos).some(v => v !== null && v !== 'dataCriacao,desc');
    }
  }
});