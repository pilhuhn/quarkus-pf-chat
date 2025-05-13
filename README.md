# RAG chat bot backend

This needs a Milvus instance running on localhost:19530 -- see [`application.properties`](src/main/resources/application.properties) on how to change this.
Application properties is also the file to set the model to use

```properties
quarkus.langchain4j.ollama.model1.base-url=http://localhost:11434
quarkus.langchain4j.ollama.model1.chat-model.model-name=granite3.3:8b
```

Here we use Granite 3.3 served by a local Ollama instance. 
If you want to use e.g. ChatGPT, comment out the `quarkus.langchain4j.ollama` entries
and use the respective `quarkus.langchain4j.openai` entries.

There are 2 http Endpoints
* /upload?url=<url> : this uses the embedding model to create embeddings and store them in Milvus
* /hello?q=<question> : this uses the data from Milvus and queries a plain granite model 

You can also use a WebSocket, which also enables streaming of the response:

```bash
$ websocat ws://localhost:8080/websocket
How can I create a primary button?
Pattern
Fly
 is
 a
 popular
 open
-
source
 framework
[...] 
```


