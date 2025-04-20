-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS todo_app;
USE todo_app;

-- Configuração do charset
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Tabela auth_user (mantida pois é referenciada pelas outras tabelas)
CREATE TABLE `auth_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(128) NOT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `is_superuser` tinyint(1) NOT NULL,
  `username` varchar(150) NOT NULL,
  `first_name` varchar(150) NOT NULL,
  `last_name` varchar(150) NOT NULL,
  `email` varchar(254) NOT NULL,
  `is_staff` tinyint(1) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `date_joined` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela aulas
CREATE TABLE `aulas` (
  `id_aula` int(11) NOT NULL AUTO_INCREMENT,
  `nome_disciplina` varchar(100) NOT NULL,
  `professor` varchar(100) NOT NULL,
  `observacoes` longtext DEFAULT NULL,
  `usuario_id` int(11) NOT NULL,
  PRIMARY KEY (`id_aula`),
  KEY `aulas_usuario_id_c8fbede7_fk_auth_user_id` (`usuario_id`),
  CONSTRAINT `aulas_usuario_id_c8fbede7_fk_auth_user_id` FOREIGN KEY (`usuario_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela horarios
CREATE TABLE `horarios` (
  `id_horario` int(11) NOT NULL AUTO_INCREMENT,
  `dia_semana` varchar(3) NOT NULL,
  `hora_inicio` time(6) NOT NULL,
  `hora_fim` time(6) NOT NULL,
  `observacoes` longtext DEFAULT NULL,
  `id_aula_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  PRIMARY KEY (`id_horario`),
  KEY `horarios_id_aula_id_15e4b5e6_fk_aulas_id_aula` (`id_aula_id`),
  KEY `horarios_usuario_id_f684995b_fk_auth_user_id` (`usuario_id`),
  CONSTRAINT `horarios_id_aula_id_15e4b5e6_fk_aulas_id_aula` FOREIGN KEY (`id_aula_id`) REFERENCES `aulas` (`id_aula`),
  CONSTRAINT `horarios_usuario_id_f684995b_fk_auth_user_id` FOREIGN KEY (`usuario_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela categoriagastos
CREATE TABLE `categoriagastos` (
  `id_categoria` int(11) NOT NULL AUTO_INCREMENT,
  `nome_categoria` varchar(100) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  PRIMARY KEY (`id_categoria`),
  KEY `categoriagastos_usuario_id_cfa4f086_fk_auth_user_id` (`usuario_id`),
  CONSTRAINT `categoriagastos_usuario_id_cfa4f086_fk_auth_user_id` FOREIGN KEY (`usuario_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela gastos
CREATE TABLE `gastos` (
  `id_gasto` int(11) NOT NULL AUTO_INCREMENT,
  `data_gasto` date NOT NULL,
  `descricao` longtext NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `observacoes` longtext DEFAULT NULL,
  `id_categoria_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  PRIMARY KEY (`id_gasto`),
  KEY `gastos_id_categoria_id_afdae726_fk_categoriagastos_id_categoria` (`id_categoria_id`),
  KEY `gastos_usuario_id_b004774c_fk_auth_user_id` (`usuario_id`),
  CONSTRAINT `gastos_id_categoria_id_afdae726_fk_categoriagastos_id_categoria` FOREIGN KEY (`id_categoria_id`) REFERENCES `categoriagastos` (`id_categoria`),
  CONSTRAINT `gastos_usuario_id_b004774c_fk_auth_user_id` FOREIGN KEY (`usuario_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela gastosfuturos
CREATE TABLE `gastosfuturos` (
  `id_gasto_futuro` int(11) NOT NULL AUTO_INCREMENT,
  `data_prevista` date NOT NULL,
  `descricao` longtext NOT NULL,
  `valor_estimado` decimal(10,2) NOT NULL,
  `observacoes` longtext DEFAULT NULL,
  `id_categoria_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  PRIMARY KEY (`id_gasto_futuro`),
  KEY `gastosfuturos_id_categoria_id_36313470_fk_categoria` (`id_categoria_id`),
  KEY `gastosfuturos_usuario_id_9164f70a_fk_auth_user_id` (`usuario_id`),
  CONSTRAINT `gastosfuturos_id_categoria_id_36313470_fk_categoria` FOREIGN KEY (`id_categoria_id`) REFERENCES `categoriagastos` (`id_categoria`),
  CONSTRAINT `gastosfuturos_usuario_id_9164f70a_fk_auth_user_id` FOREIGN KEY (`usuario_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela contaspagar
CREATE TABLE `contaspagar` (
  `id_conta` int(11) NOT NULL AUTO_INCREMENT,
  `data_vencimento` date NOT NULL,
  `descricao` longtext NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `pago` tinyint(1) NOT NULL,
  `observacoes` longtext DEFAULT NULL,
  `id_categoria_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  PRIMARY KEY (`id_conta`),
  KEY `contaspagar_id_categoria_id_0238ec80_fk_categoria` (`id_categoria_id`),
  KEY `contaspagar_usuario_id_a00a7cec_fk_auth_user_id` (`usuario_id`),
  CONSTRAINT `contaspagar_id_categoria_id_0238ec80_fk_categoria` FOREIGN KEY (`id_categoria_id`) REFERENCES `categoriagastos` (`id_categoria`),
  CONSTRAINT `contaspagar_usuario_id_a00a7cec_fk_auth_user_id` FOREIGN KEY (`usuario_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela folha_pagamento
CREATE TABLE `folha_pagamento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mes_referencia` date NOT NULL,
  `salario_bruto` decimal(10,2) NOT NULL,
  `faltas` decimal(4,1) NOT NULL,
  `val` decimal(10,2) NOT NULL,
  `descontos` decimal(10,2) NOT NULL,
  `salario_liquido` decimal(10,2) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `folha_pagamento_usuario_id_0f02a8c5_fk_auth_user_id` (`usuario_id`),
  CONSTRAINT `folha_pagamento_usuario_id_0f02a8c5_fk_auth_user_id` FOREIGN KEY (`usuario_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1; 