import api from './api';

export default {
  login(email, password) {
    return api.post('/login', { email, password });
  },

  cadastrar(dados) {
    return api.post('/funcionario', dados);
  },

  listarTickets(params = {}) {
    return api.get('/tickets', { params });
  },

  criarTicket(ticket) {
    return api.post('/tickets', ticket);
  },

  atualizarTicket(ticket) {
    return api.patch('/tickets', ticket);
  },

  buscarPorSetor(setor, page = 0, size = 20, sort = null) {
    const params = { setor, page, size };
    if (sort) params.sort = sort;
    return api.get('/tickets', { params });
  },

  buscarPorTipo(tipoChamado, page = 0, size = 20, sort = null) {
    const params = { tipoChamado, page, size };
    if (sort) params.sort = sort;
    return api.get('/tickets', { params });
  },

  buscarAtivos(page = 0, size = 20) {
    return api.get('/tickets', {
      params: { ativo: true, page, size }
    });
  },

  buscarInativos(page = 0, size = 20) {
    return api.get('/tickets', {
      params: { ativo: false, page, size }
    });
  },

  buscarPorResponsavel(funcId, page = 0, size = 20) {
    return api.get('/tickets', {
      params: { funcId, page, size }
    });
  },

  buscarPorFuncionarioEData(funcId, date, page = 0, size = 20) {
    return api.get('/tickets', {
      params: { funcId, date, page, size }
    });
  },

  buscarComFiltros(filtros = {}) {
    const params = {
      page: filtros.page !== undefined ? filtros.page : 0,
      size: filtros.size || 20
    };

    if (filtros.setor) {
      params.setor = filtros.setor;
    }

    if (filtros.tipoChamado) {
      params.tipoChamado = filtros.tipoChamado;
    }

    if (filtros.ativo !== undefined && filtros.ativo !== null && filtros.ativo !== '') {
      params.ativo = filtros.ativo;
    }

    if (filtros.funcId) {
      params.funcId = filtros.funcId;
    }

    if (filtros.date) {
      params.date = filtros.date;
    }

    if (filtros.sort) {
      params.sort = filtros.sort;
    }

    return api.get('/tickets', { params });
  },

  marcarConcluido(ticketId) {
    return api.post(`/tickets/end`, null, {
      params: { id: ticketId }
    });
  },

  buscarEnums() {
    return api.get('/enums');
  },

  buscarSetores() {
    return api.get('/enums/setores');
  },

  listarFuncionarios() {
    return api.get('/funcionario/all');
  }
};