#!/bin/bash
echo "TESTING ANIMAL_PLACE_FACT"
echo "adding fact The otter lives in rivers"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The otter lives in rivers"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
echo "deleting fact"
curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_FUR_FACT"
echo "adding fact Bears have fur"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Bears have fur"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
# echo "deleting fact"
# curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_SCALES_FACT"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Salmons have scales"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
# echo "deleting fact"
# curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_LEG_FACT"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Bears have 4 legs"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
# echo "deleting fact"
# curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_SPECIES_FACT"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Bears are mammals"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
# echo "deleting fact"
# curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_FOOD_FACT"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Bears eat berries and salmon"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
# echo "deleting fact"
# curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_BODY_FACT"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Cats have tail"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
# echo "deleting fact"
# curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"

#if [ curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id" | grep -q "Error" ] then
#	echo "Failed retrieving fact $id"
#else
#	curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
#fi;