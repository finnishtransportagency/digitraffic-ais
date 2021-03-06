# Digitraffic marine database


## ;TL;TR

````bash
docker-compose rm db && docker-compose build && docker-compose up
````

## Running db instance

````bash
docker-compose build && docker-compose up
````

PostgreSql db is running at localhost:54321 and adminer at localhost:8081

List containers
``````bash
docker-compose ps
``````

Removing containers
``````bash
docker-compose rm db
``````

In case building PostGIS fails because of unknown version:
1. git clone https://github.com/appropriate/docker-postgis.git
2. ./update.sh
3. Grab the next version from the script output, example: `s/%%POSTGIS_VERSION%%/2.5.4+dfsg-1.pgdg90+1/g;` The bit you need is *2.5.4+dfsg-1.pgdg90+1*
4. Update the variable *POSTGIS_VERSION* in *postgis/Dockerfile_postgis*

If postgis extension is not initialized run following commands manually:
``````bash
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;
CREATE EXTENSION IF NOT EXISTS postgis_tiger_geocoder;
``````