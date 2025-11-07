// src/services/ticketService.js
import api from './api';

export default {
  // ===== AUTENTICAÇÃO =====

  // Login
  login(email, senha) {
    return api.post('/login', { email, senha });
  },

  // Cadastro de novo funcionário/usuário
  cadastrar(dados) {
    return api.post('/funcionarios', dados);
  },

  // ===== TICKETS =====

  // Listar tickets com filtros e paginação
  listarTickets(params = {}) {
    return api.get('/tickets', { params });
  },

  // Criar novo ticket
  criarTicket(ticket) {
    return api.post('/tickets', ticket);
  },

  // Buscar tickets por setor
  buscarPorSetor(setor, page = 0, size = 20, sort = null) {
    const params = { setor, page, size };
    if (sort) params.sort = sort;
    return api.get('/tickets', { params });
  },

  // Buscar tickets por tipo
  buscarPorTipo(tipoChamado, page = 0, size = 20, sort = null) {
    const params = { tipoChamado, page, size };
    if (sort) params.sort = sort;
    return api.get('/tickets', { params });
  },

  // Buscar tickets ativos
  buscarAtivos(page = 0, size = 20) {
    return api.get('/tickets', {
      params: { ativo: true, page, size }
    });
  },

  // Buscar tickets inativos/finalizados
  buscarInativos(page = 0, size = 20) {
    return api.get('/tickets', {
      params: { ativo: false, page, size }
    });
  },

  // Buscar tickets por responsável (quem abriu)
  buscarPorResponsavel(funcId, page = 0, size = 20) {
    return api.get('/tickets', {
      params: { funcId, page, size }
    });
  },

  // Buscar tickets por funcionário e data
  buscarPorFuncionarioEData(funcId, date, page = 0, size = 20) {
    return api.get('/tickets', {
      params: { funcId, date, page, size }
    });
  },

  // Buscar com filtros combinados
  buscarComFiltros(filtros = {}) {
    const params = {};

    if (filtros.setor) params.setor = filtros.setor;
    if (filtros.tipoChamado) params.tipoChamado = filtros.tipoChamado;
    if (filtros.funcId) params.funcId = filtros.funcId;
    if (filtros.ativo !== undefined) params.ativo = filtros.ativo;
    if (filtros.date) params.date = filtros.date;
    if (filtros.page !== undefined) params.page = filtros.page;
    if (filtros.size) params.size = filtros.size;
    if (filtros.sort) params.sort = filtros.sort;

    return api.get('/tickets', { params });
  },

// Marcar ticket como concluído
  marcarConcluido(ticketId) {
    return api.post(`/tickets/end`, null, {
      params: { id: ticketId }
    });
  },

  // ===== ENUMS E FUNCIONÁRIOS =====

  // Buscar todos os enums disponíveis
  buscarEnums() {
    return api.get('/enums');
  },

  // Listar todos os funcionários
  listarFuncionarios() {
    return api.get('/funcionarios');
  }
};