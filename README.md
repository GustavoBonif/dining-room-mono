Projeto Dining-Room | Arquitetura Monolítica

Este repositório contém a aplicação Dining-Room desenvolvido com arquitetura monolítica para o meu Trabalho de Conclusão de Curso (TCC) entitulado "ANÁLISE COMPARATIVA ENTRE ARQUITETURA DE SISTEMA MONOLÍTICO E MICROSSERVIÇO: DESEMPENHO E CONSUMO DE RECURSO".

A versão com arquitetura de microsserviços está distribuída nos seguintes repositórios:

- Eureka-Server: https://github.com/GustavoBonif/dining-room-eureka-server
- User-Registry: https://github.com/GustavoBonif/dining-room-user-registry
- Catalog: https://github.com/GustavoBonif/dining-room-catalog
- Warehouse: https://github.com/GustavoBonif/dining-room-warehouse
- Cart: https://github.com/GustavoBonif/dining-room-cart
- Gateway: https://github.com/GustavoBonif/dining-room-gateway 

O projeto é um _ecommerce_ simples, com apenas o _backend_, e tem como objetivo comparar o tempo do resposta e o consumo de CPU das instâncias EC2 na AWS de cada aplicação.

As tecnologias empregadas foram:
- Java 17;
- Spring 3.1.4;
- MySQL 8.0.29.

As ferramentas utilizadas: 
- _Postman_ para realização de consulta de API;
- _Apache JMeter_ para criação de plano de testes para _baseline test_ e _load test_

Além disso, foi utilizado o ambiente _Amazon Web Server_ (AWS) para hospedagem do sistema em plataforam nuvem.
