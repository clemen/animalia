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
echo "deleting fact"
curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_SPECIES_FACT"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Bears are mammals"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
echo "deleting fact"
curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_FOOD_FACT"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Bears eat berries and salmon"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
echo "deleting fact"
curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
echo "TESTING ANIMAL_BODY_FACT"
id=$(curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Cats have tail"}'|grep "id"|cut -f2 -d":"|sed 's/\"//g'|sed 's/}//g')
echo "Getting fact"
curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id"
echo "deleting fact"
curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"

echo "setup db"
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "Cats have tail"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The bear eats berries and salmo"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The deer eats grass and branches"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The bear lives in the mountains and meadows"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The snake has scales"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The salmon has scales"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The bear has 4 legs"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The bear has fur"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The bear is a mammal"}'
curl -v -H "Content-Type: application/json" -X POST 'http://localhost:8080/animalia/animal/facts/' -d '{"fact": "The cat is a mammal"}'

echo "TESTING WHICH_ANIMAL_QUERY FOOD"
curl -v -H "Content-Type: application/json" -X GET 'http://localhost:8080/animalia/animal/?q=Which%20animals%20eat%20salmon%20and%20berries%3F'
echo "TESTING WHICH_ANIMAL_QUERY BODY_PART"
curl -v -H "Content-Type: application/json" -X GET 'http://locallia/animal/?q=Which%20animals%20have%20a%20tail%3F'
echo "TESTING WHICH_ANIMAL_QUERY PLACE"
curl -v -H "Content-Type: application/json" -X GET 'http://localhostlia/animal/?q=Which%20animals%20live%20in%20the%20mountains%3F'
echo "TESTING WHICH_ANIMAL_QUERY SPECIES"
curl -v -H "Content-Type: application/json" -X GET 'http://localhost:8080/animalia/animal/?q=Which%20animal%20is%20a%20mammal%3F'
echo "TESTING WHICH_ANIMAL_QUERY SCALES"
curl -v -H "Content-Type: application/json" -X GET 'http://localhost:8080/animalia/animal/?q=Which%20animals%20have%20scales%3F'
echo "TESTING WHICH_ANIMAL_QUERY FUR"
curl -v -H "Content-Type: application/json" -X GET 'http://localhost:8080/animalia/animal/?q=Which%20animals%20have%20fur%3F'
echo "TESTING HOW_MANY_ANIMAL_QUERT FOOD"
curl -v -H "Content-Type: application/json" -X GET 'http://localhost:8080/animalia/animal/?q=How%20many%20animals%20eat%20berries%3F'
#if [ curl -v -H "Content-Type: application/json" -X GET "http://localhost:8080/animalia/animal/facts/$id" | grep -q "Error" ] then
#	echo "Failed retrieving fact $id"
#else
#	curl -v -H "Content-Type: application/json" -X DELETE "http://localhost:8080/animalia/animal/facts/$id"
#fi;
