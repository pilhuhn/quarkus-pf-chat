# RAG chat bot backend

2 Endpoints
* /upload?url=<url> : this uses the embedding model to create embeddings and store them in Milvus
* /hello?q=<question> : this uses the data from Milvus and queries a plain granite model 


Using the embedding model fails with standard Quarkus.Langchain4j, as the two models sit on different
http endpoints. The granite model does not allow to produce embeddings.

