#!/usr/bin/env bash

# Build project
cd .. && mvn clean install

# Run project
cd docker && docker-compose up
