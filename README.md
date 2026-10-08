# Sistema de Login Seguro

Projeto feito para a disciplina, com o objetivo de montar um sistema de login com cadastro, controle de acesso por perfil e armazenamento no MongoDB Atlas. A ideia é que ele sirva de base para outros projetos, então tentei deixar a lógica separada da parte visual.

Tecnologias: Java 21, Spring Boot 4, Spring Security, Thymeleaf e MongoDB Atlas.

## O que o sistema faz

- Cadastro, login e logout
- Senha salva com hash BCrypt
- Validação dos dados do cadastro (nome, e-mail, senha com letras e números)
- Três perfis de usuário: ADMIN, GESTOR e USUARIO
- Área de administração onde o ADMIN muda o perfil dos outros usuários
- Usuários e sessões salvos no MongoDB Atlas
- Visual baseado em temas (dá pra trocar o tema mudando uma linha de configuração)

Quem se cadastra entra como USUARIO. O GESTOR acessa a área de gestão, e o ADMIN acessa tudo, incluindo a lista de usuários.

## Configurando o MongoDB Atlas

1. Crie um cluster gratuito no [MongoDB Atlas](https://cloud.mongodb.com).
2. Crie um usuário de banco em Database Access.
3. Libere seu IP em Network Access (para testes, pode usar `0.0.0.0/0`).
4. Em Connect > Drivers > Java, copie a connection string.
5. Troque o usuário e a senha na string e coloque `/loginseguro` antes do `?`. Fica assim:

```
mongodb+srv://USUARIO:SENHA@cluster0.xxxxx.mongodb.net/loginseguro?retryWrites=true&w=majority
```

As coleções `users` e `sessions` são criadas sozinhas quando o sistema roda pela primeira vez.

## Variáveis de ambiente

A senha do banco não fica no código. O projeto lê as configurações de variáveis de ambiente:

- `MONGODB_URI`: a connection string do Atlas (obrigatória)
- `ADMIN_EMAIL`: e-mail que vai virar administrador ao iniciar o sistema (opcional)
- `ADMIN_SENHA`: senha do administrador, usada só se esse e-mail ainda não tiver conta (opcional)

## Como rodar

No IntelliJ:

1. Abra a pasta do projeto e espere o Maven baixar as dependências.
2. Vá em Run > Edit Configurations, selecione `LoginSeguroApplication` e coloque as variáveis em Environment variables.
3. Rode e acesse http://localhost:8080

Pelo terminal (Windows):

```powershell
$env:MONGODB_URI="sua_connection_string"
$env:ADMIN_EMAIL="seu@email.com"
$env:ADMIN_SENHA="Senha12345"
.\mvnw.cmd spring-boot:run
```

Depois de mudar o perfil de alguém, a pessoa precisa sair e entrar de novo para a mudança valer.

Se der erro de timeout ao conectar no banco, confira se o IP está liberado no Atlas e se não tem VPN ligada.

## Documentação

A explicação da estrutura do projeto e das decisões de design está em [docs/ARQUITETURA.md](docs/ARQUITETURA.md).