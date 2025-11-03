---
title: "Spring AI"
description: "Integrando Inteligencia Artificial en aplicaciones Spring."
author: "Alberto Basalo"
url: "3-spring-ai.md"
marp: true
theme: ab
---

# Spring AI
Un curso de [Academia](https://aicode.academy) para Empresa.
Por [Alberto Basalo](https://albertobasalo.dev)
> Noviembre 2025
---

## CONEXIÓN

### ¿Limitaciones?

- Memoria, sesgos y contenido obsoleto.

---

## CONCEPTOS

### Configuración básica
`spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-5-nano`

```java
private final ChatClient chatClient;
public AstroController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
}
``` 

### Hola Mundo

```java
chatClient = builder.build()
chatClient.prompt().user(message).call().content();
```
### Instrucciones del sistema

```java
var instructions = "You are an expert in astronomy. Tell I do not know about a topic if you are not sure.";
chatClient.prompt().system(instructions).user(message).call().content();
```

### Plantillas

```java
chatClient.prompt().system(instructions).user(u->{
    u.text("Explain me {topic} in a simple way.");
    u.param("topic", "Mars");
}).call().content();
```

### Estructura de datos

```java
record Satellite(String name, double radius, double mass) {}
record Satellites(List<Satellite> satellites) {}
```;
chatClient.prompt().system(instructions).user(u->{
    u.text("List the satellites of {planet} as JSON array of Satellite objects.");
    u.param("planet", "Mars");
}).call().entity(Satellites.class);
```


### Streaming y control de la respuesta

```java
chatClient = builder.build();
chatClient.prompt().user(message).stream().content();
chatClient.prompt().user(message).call().chatResponse();
```

### Memoria y contexto

```java
public AstroController(ChatClient.Builder builder, ChatMemory chatMemory) {
    var memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
    this.chatClient = builder.defaultAdvisors(memoryAdvisor).build();
}
```

### Guarda raíles

- Moderación previa (desinfectar prompts maliciosos)
- Fact-checking (bring your own data -> RAG)

---

## CONCRETANDO

---

## CONCLUSIONES

 ### Próxima lección: 
 **RAG con Spring AI.**

> _No es magia, es tecnología._ 
> **Alberto Basalo**
