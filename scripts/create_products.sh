#!/bin/bash

for i in {1..100}; do
	curl -X POST http://localhost:8080/api/product -H "Content-Type: application/json"  -d "{\"name\":\"Name $i\", \"description\":\"Description $i\", \"price\":\"$i\", \"sku\":\"SKU_$i\"}";
done