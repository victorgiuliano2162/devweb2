import { computed } from 'vue';
import { useAuthStore } from '@/stores/authStore';

export function usePermissions() {
  const authStore = useAuthStore();

  const CARGOS_EDITORES = [
    'DIRETOR',
    'ANALISTA_SENIOR',
    'DESENVOLVIMENTO',
    'SUPERVISOR',
    'GESTOR',
    'TECNICO_SENIOR'
  ];

  const podeEditarTickets = computed(() => {
    const user = authStore.user;
    const cargo = user?.cargo;

    console.log('🔍 Verificando permissões:');
    console.log('   Usuário:', user?.nome);
    console.log('   Cargo do usuário:', cargo);
    console.log('   Cargos permitidos:', CARGOS_EDITORES);
    console.log('   Tem permissão?', cargo && CARGOS_EDITORES.includes(cargo));

    return cargo && CARGOS_EDITORES.includes(cargo);
  });

  const eResponsavelAbertura = (ticket) => {
    const userId = authStore.user?.id;
    return ticket?.responsavelPelaAbertura?.id === userId;
  };

  const eResponsavelExecucao = (ticket) => {
    const userId = authStore.user?.id;
    return ticket?.responsavelPelaExecucao?.id === userId;
  };

  const podeAtribuirResponsavel = computed(() => {
    return podeEditarTickets.value;
  });

  return {
    podeEditarTickets,
    podeAtribuirResponsavel,
    eResponsavelAbertura,
    eResponsavelExecucao,
    CARGOS_EDITORES
  };
}