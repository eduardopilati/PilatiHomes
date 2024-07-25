# PilatiHomes

Um simples plugin de homes com apenas 4 comandos:

| comando | descrição |
|---|---|
| home [name] | Teleporta o jogador para uma home |
| homes | Lista todas as homes |
| sethome [name] | Define uma home com um nome personalizado |
| delhome [name] | Remove uma home |

# Objetivo

Esse plugin tem por objetivo cumprir uma função simples usando dois tipos de Banco de Dados: H2 e MySql, dois bancos de dados extremamente populares na comunidade de plugins.

Por ser simples e objetivo esse plugin não tem requisitos ideias para um plugin real como:

- Possibilidade de customizar e traduzir mensagens
- Comandos administrativos
- Funções mais elaboradas de gestão de homes em menu de inventário e possibilidade de escolha de itens visuais

## Como Instalar:

Para instalar deve-se colocar o plugin compilado ou baixado de [Releases](https://github.com/eduardopilati/EnhancedWindCharge/releases) na pasta `plugins` do servidor

### Compilando

Para compilar o código fonte deve-se executar o passo `install` do maven usando Java 21 e maven 3.9.4 ou superior