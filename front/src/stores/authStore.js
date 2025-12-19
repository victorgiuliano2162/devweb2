import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: null,
    isAuthenticated: false,
  }),

  actions: {
    init() {
      const token = localStorage.getItem('token');
      const userStr = localStorage.getItem('user');

      if (token && userStr) {
        try {
          this.user = JSON.parse(userStr);
          this.token = token;
          this.isAuthenticated = true;
          console.log('✅ Usuário carregado do localStorage:');
          console.log('   Nome:', this.user.nome);
          console.log('   Cargo:', this.user.cargo);
          console.log('   ID:', this.user.id);
        } catch (error) {
          console.error('❌ Erro ao carregar usuário do localStorage:', error);
          this.logout();
        }
      }
    },

    login(userData, token) {
      this.user = userData;
      this.token = token;
      this.isAuthenticated = true;

      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(userData));

      console.log('✅ Login realizado com sucesso!');
      console.log('👤 Dados do usuário salvos:');
      console.log('   ID:', userData.id);
      console.log('   Nome:', userData.nome);
      console.log('   Email:', userData.email);
      console.log('   Cargo:', userData.cargo);
      console.log('   Registro:', userData.registro);
      console.log('   Telefone:', userData.telefone);
      console.log('🔑 Token:', token.substring(0, 30) + '...');
    },

    logout() {
      this.user = null;
      this.token = null;
      this.isAuthenticated = false;

      localStorage.removeItem('token');
      localStorage.removeItem('user');

      console.log('🚪 Logout realizado');
    },

    updateUser(userData) {
      this.user = { ...this.user, ...userData };
      localStorage.setItem('user', JSON.stringify(this.user));
      console.log('🔄 Dados do usuário atualizados');
    },

    loadUserFromStorage() {
      this.init();
    },
  },

  getters: {
    getUser: (state) => state.user,
    isLoggedIn: (state) => state.isAuthenticated,
    getUserName: (state) => state.user?.nome || 'Usuário',
    getUserId: (state) => state.user?.id || null,
    getUserCargo: (state) => state.user?.cargo || null,
    getToken: (state) => state.token,
  },
});