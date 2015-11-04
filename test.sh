#!/bin/bash
echo "adding fact The otter lives in rivers"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The otter lives in rivers"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
echo "deleting fact"
curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
#if [ curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id" | grep -q "Error" ] then
#	echo "Failed retrieving fact $id"
#else
#	curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
#fi;
