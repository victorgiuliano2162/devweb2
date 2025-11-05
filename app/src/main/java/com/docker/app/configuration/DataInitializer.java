package com.docker.app.configuration;

import com.docker.app.entities.Setor;
import com.docker.app.entities.Funcionario;
import com.docker.app.entities.enums.Setores;
import com.docker.app.repositories.SetorRepository;
import com.docker.app.repositories.FuncionarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private SetorRepository setorRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try {
            logger.info("🚀 Iniciando inicialização dos dados...");

            // A ordem importa se houver dependências entre entidades
            inicializarSetores();
            inicializarUsuariosPadrao();

            logger.info("✅ Inicialização concluída com sucesso!");
        } catch (Exception e) {
            logger.error("❌ Erro durante a inicialização dos dados", e);
        }
    }

    private void inicializarSetores() {
        if (setorRepository.count() == 0) {
            logger.info("📝 Criando setores padrão...");



            logger.info("✅ {} setores criados!", Setores.values().length);
        } else {
            logger.info("ℹ️  Setores já existem ({} registros)", setorRepository.count());
        }
    }

    private void inicializarUsuariosPadrao() {
        if (funcionarioRepository.count() == 0) {
            logger.info("📝 Criando usuário administrador padrão...");


            logger.info("✅ Usuário administrador criado!");
        } else {
            logger.info("ℹ️  Usuários já existem ({} registros)", funcionarioRepository.count());
        }
    }
}