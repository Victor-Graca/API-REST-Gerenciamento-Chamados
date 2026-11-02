# API REST para gerenciamento de chamados de suporte técnico

# Análise técnica

## Como o Spring Boot recebe e processa requisições HTTP
Existem algumas etapas para o recebimento e processamento de requisições HTTP no Spring Boot: 

Primeiro, o Spring Boot inicia um servidor web, que por padrão é o Apache Tomcat na porta 8080. Esse servidor web faz um listen na porta, recebe a conexão TCP e lê a requisição HTTP enviada pelo cliente. Depois, essa requisição é passada para o núcleo do Spring MVC, que é o DispatcherServlet, que coordena a passagem de requisições para o código. Para saber qual método está associado ao endpoint da requisição, o DispatcherServlet consulta uma tabela interna de rotas. Com a rota correta definida, o Spring extrai os dados da requisição, executa o método no Controller e um valor é retornado. Esse valor retornado é convertido em JSON pelo Spring Boot, juntamente com um cabeçalho de informações sobre o HTTP e o servidor web manda o pacote de volta para o cliente.

---

## Qual é a função do Controller na aplicação
O Controller tem a função de ser uma porta de entrada para a comunicação entre a API e requisições externas que são enviadas por clientes. Os endpoints da aplicação são expostos, juntamente com os métodos HTTP que são aceitos por cada endpoint. Annotations como @RequestBody são utilizadas para converter os dados recebidos para o Java. Os códigos HTTP adequados que serão enviados de volta ao cliente também são responsabilidade do Controller.

---

## Como ocorre a conversão entre objetos Java e JSON
A conversão entre objetos Java e JSON no Spring Boot é gerenciada de forma automática pela biblioteca Jackson, com as etapas de Desserialização, que converte um JSON para um objeto Java e Serialização, que converte um objeto Java para JSON.

Na Desserialização, a conversão de JSON para objeto Java ocorre quando a API recebe uma requisição com Body, como POST, PUT ou PATCH por exemplo, recebendo o JSON enviado pelo cliente. O Jackson lê o texto do JSON, instancia a classe da API (Chamado no caso do trabalho) e utiliza os setters para preencher cada atributo com os valores do JSON.

Já na Serialização, a conversão de um objeto Java para JSON ocorre quando a API envia o resultado de uma requisição GET, quando o Controller retorna um objeto Java, (como a lista de chamados ou o ResumoDTO no endpoint /resumo). A annotation @RestController possui o @ResponseBody internamente, indicando que dados devem ser retornados. O Jackson lê o objeto Java a partir dos getters e monta o JSON que será enviado como resposta à requisição, juntamente com o cabeçalho.

---

## Por que os chamados não permanecem armazenados após reiniciar a aplicação
Os chamados não permanecem armazenados após reiniciar a aplicação porque sem o uso de um banco de dados, os chamados são armazenados apenas na memória RAM, pois a lista de chamados foi instanciada como um ArrayList e seus objetos são alocados na RAM pela JVM (Java Virtual Machine). Como a memória RAM é volátil, os dados são mantidos enquanto a aplicação está rodando e são liberados no momento em que a aplicação é parada. No caso do código do trabalho, os 10 chamados fictícios iniciais são instanciados novamente toda vez que a aplicação reinicia, mas alterações ou inclusões feitas por requisições HTTP são perdidas.

# Requisições HTTP da etapa de testes:

As requisições solicitadas para a etapa de testes e demonstração foram organizadas no arquivo requisicoes.http, localizado na raiz do projeto.

Para realizar os testes, foi utilizada a extensão REST Client feita por Huachao Mao no VS Code, permitindo executar as requisições diretamente pelo editor sem a necessidade de ferramentas externas como Postman ou Insomnia.


# Como executar a aplicação

Para rodar a aplicação localmente pelo terminal, execute o seguinte comando a partir da raiz do projeto:

```bash
./mvnw spring-boot:run
```

A API poderá ser acessada na porta 8080 : http://localhost:8080