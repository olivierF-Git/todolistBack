# Lister les taches

````bash
curl -X GET --location "http://localhost:9191/api/taches" | jq
````

# Devrait générer un message d'erreur indiquant qu'on ne peut pas convertir une String en UUID
````bash
curl -X GET --location "http://localhost:9191/api/taches/1" | jq
````

# Devrait générer un message d'erreur
curl -X GET "http://localhost:9191/api/taches/840b987a-3ab7-4a9a-a95f-7509aa9fea3e" | jq

````json
{
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Issue with id 840b987a-3ab7-4a9a-a95f-7509aa9fea3e not found",
  "timeStamp": 1639911160305,
  "developerMessage": "org.example.issuetracker.configuration.exception.ResourceNotFoundException",
  "errors": null
}
````


# La requête suivante ne devrait pas créer de nouvelle tache
curl -X POST 'localhost:9191/api/taches' -H 'Content-Type: application/json' --data-raw '{}' | jq