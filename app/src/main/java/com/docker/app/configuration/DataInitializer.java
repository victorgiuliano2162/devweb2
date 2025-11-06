package com.docker.app.configuration;

import com.docker.app.configuration.populateSQL.ChamadoTecnicoSQL;
import com.docker.app.configuration.populateSQL.FuncionarioSQL;
import com.docker.app.configuration.populateSQL.SetorSQL;
import com.docker.app.entities.enums.Setores;
import com.docker.app.repositories.ChamadoTecnicoRepository;
import com.docker.app.repositories.SetorRepository;
import com.docker.app.repositories.FuncionarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final SetorRepository setorRepository;
    private final ChamadoTecnicoRepository chamadoTecnicoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioSQL funcionarioSQL;
    private final SetorSQL setorSQL;
    private final ChamadoTecnicoSQL chamadoTecnicoSQL;

    // Injeção via construtor
    public DataInitializer(SetorRepository setorRepository,
                           ChamadoTecnicoRepository chamadoTecnicoRepository,
                           FuncionarioRepository funcionarioRepository,
                           FuncionarioSQL funcionarioSQL,
                           SetorSQL setorSQL,
                           ChamadoTecnicoSQL chamadoTecnicoSQL) {
        this.setorRepository = setorRepository;
        this.chamadoTecnicoRepository = chamadoTecnicoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioSQL = funcionarioSQL;
        this.setorSQL = setorSQL;
        this.chamadoTecnicoSQL = chamadoTecnicoSQL;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try {
            logger.info("🚀 Iniciando inicialização dos dados...");

            inicializarSetores();
            inicializarFuncionarioPadrao();
            inicializarChamadoTecnicoPadrao();

            logger.info("✅ Inicialização concluída com sucesso!");
        } catch (Exception e) {
            logger.error("❌ Erro durante a inicialização dos dados", e);
        }
    }

    private void inicializarSetores() {
        if (setorRepository.count() == 0) {
            logger.info("📝 Criando setores padrão...");
            setorSQL.createSetor(); // agora é injetado pelo Spring
            logger.info("✅ {} setores criados!", Setores.values().length);
        } else {
            logger.info("ℹ️  Setores já existem ({} registros)", setorRepository.count());
        }
    }

    private void inicializarFuncionarioPadrao() {
        if (funcionarioRepository.count() == 0) {
            logger.info("📝 Criando usuário administrador padrão...");
            funcionarioSQL.createFuncionarios(); // também injetado
            logger.info("✅ Usuário administrador criado!");
        } else {
            logger.info("ℹ️  Usuários já existem ({} registros)", funcionarioRepository.count());
        }
    }

    private void inicializarChamadoTecnicoPadrao() {
        if (chamadoTecnicoRepository.count() == 0) {
            logger.info("📝 Criando chamados técnicos padrão...");
            chamadoTecnicoSQL.createChamados(); // idem
            logger.info("✅ Chamados técnicos criados!");
        } else {
            logger.info("ℹ️  Chamados já existem ({} registros)", chamadoTecnicoRepository.count());
        }
    }
}
