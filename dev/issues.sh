#!/bin/bash

curl -X GET --location "http://localhost:9191/api/issues"

# La requête suivante ne devrait pas être possible
curl -X POST 'localhost:9191/api/issues' -H 'Content-Type: application/json' --data-raw '{}'