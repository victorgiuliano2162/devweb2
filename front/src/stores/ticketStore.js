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
    // Filtros ativos - armazena os filtros aplicados
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
        // Combina os filtros ativos com os parâmetros adicionais
        const filtros = {
          page: params.page !== undefined ? params.page : this.currentPage,
          size: params.size || this.size,
          sort: this.filtrosAtivos.sort || 'dataCriacao,desc'
        };

        // Adiciona filtros ativos (apenas os que têm valor)
        if (this.filtrosAtivos.setor) {
          filtros.setor = this.filtrosAtivos.setor;
        }

        if (this.filtrosAtivos.tipoChamado) {
          filtros.tipoChamado = this.filtrosAtivos.tipoChamado;
        }

        if (this.filtrosAtivos.ativo !== null && this.filtrosAtivos.ativo !== undefined) {
          filtros.ativo = this.filtrosAtivos.ativo;
        }

        if (this.filtrosAtivos.funcId) {
          filtros.funcId = this.filtrosAtivos.funcId;
        }

        if (this.filtrosAtivos.date) {
          filtros.date = this.filtrosAtivos.date;
        }

        // Sobrescreve com params explícitos se fornecidos
        Object.keys(params).forEach(key => {
          if (params[key] !== undefined && params[key] !== null) {
            filtros[key] = params[key];
          }
        });

        const response = await ticketService.buscarComFiltros(filtros);

        this.tickets = response.data.content || [];
        this.totalPages = response.data.totalPages || 0;
        this.currentPage = response.data.number || 0;
        this.totalElements = response.data.totalElements || 0;
        this.size = response.data.size || 20;

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
        const response = await ticketService.marcarConcluido(ticketId);

        // Recarrega os tickets mantendo os filtros atuais
        await this.carregarTickets();

        return response.data;
      } catch (error) {
        console.error('Erro ao marcar ticket como concluído:', error);
        this.error = 'Erro ao marcar ticket como concluído';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    // Aplicar múltiplos filtros combinados
    async aplicarFiltros(filtros) {
      // Atualiza os filtros ativos mantendo os valores anteriores
      Object.keys(filtros).forEach(key => {
        if (filtros[key] !== undefined && filtros[key] !== null && filtros[key] !== '') {
          this.filtrosAtivos[key] = filtros[key];
        } else if (key in this.filtrosAtivos) {
          this.filtrosAtivos[key] = null;
        }
      });

      // Volta para a primeira página ao aplicar novos filtros
      await this.carregarTickets({ page: 0 });
    },

    // Limpar todos os filtros
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

    // Mudar página mantendo os filtros
    async mudarPagina(page) {
      await this.carregarTickets({ page });
    },

    // Mudar ordenação mantendo os filtros
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
      return (
        state.filtrosAtivos.setor !== null ||
        state.filtrosAtivos.tipoChamado !== null ||
        state.filtrosAtivos.ativo !== null ||
        state.filtrosAtivos.funcId !== null ||
        state.filtrosAtivos.date !== null ||
        (state.filtrosAtivos.sort && state.filtrosAtivos.sort !== 'dataCriacao,desc')
      );
    }
  }
});