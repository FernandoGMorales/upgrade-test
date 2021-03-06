# upgrade-test
* The app is a Spring boot app
* The DB is H2 (memory DB), I know that DB is mainly for testing purposes, but I've foused on requeriments and constraints.
* In order to provide scalability, the app has been built as Docker image and has been configured to run as a service in a cluster, and scale up to 5 replicas. For true scalability the image should be deployed as a service in a cluster like Google cloud, Docker swarm, Amazon AWS, etc.
* I've decided to add integration tests, altough not required, but it's hard to provide quality without them.

## BUILD, GENERATE ARTIFACTS AND RUN TESTS

1. _mvn clean install_

2. _mvn tests_

3. _mvn spring-boot:run_

* by default tomcat is at http://localhost:8080




## APP ENDPOINTS (I recommend POSTMAN for DELETE and POST requests)

### USER

* POST /users/save
  
	Example: http://localhost:8080/users/save
	```javascript
			{
			  "email": "alexander.magnus@gmail.com",
			  "firstName": "Alexander",
			  "lastName": "The Great",
			  "reservation": {
			    "startDate": "2019-02-24",
			    "endDate": "2019-02-25"
			  }
			}
	
	Response: 
	
			{
			    "email": "alexander.magnus@gmail.com",
			    "firstName": "Alexander",
			    "lastName": "The Great",
			    "reservation": {
				"id": 2,
				"startDate": "2019-02-24T00:00:00.000+0000",
				"endDate": "2019-02-25T00:00:00.000+0000"
			    }
			}

	```

* GET /users/all


### RESERVATION

* DELETE /reservations/cancel/{id}    

* POST /reservations/modify
		
 	Example: http://localhost:8080/reservations/modify
	```javascript
			 {"id": 2,
			    "startDate": "2019-02-25",
			    "endDate": "2019-02-26"
			  }
	
	Response: 
	
			{
			    "id": 2,
			    "startDate": "2019-02-25T00:00:00.000+0000",
			    "endDate": "2019-02-26T00:00:00.000+0000"
			}
	```

### CAMPSITE
	
* GET /campsite/availability/start/{startRange}/end/{endRange}

	Example: http://localhost:8080/campsite/availability/start/2019-02-25/end/2019-02-26



# DOCKER
### To run the from a docker image (I asume you have docker-engine and docker-compose):

_docker run -p 8080:8080 csravgvstvs/upgrade-test:beta1.1_

__That is my private DockerHub repository, here you have the credentials: user: csravgvstvs, pass: 321DockerhuB__

### Go to: http://localhost:8080/

### At this point the app is running locally as a single container.


## In order to scale the app, run in a local cluster:
### to run the commands below you will ned a file named docker-compose.yml which is located at: reservation/

_docker swarm init_

_docker stack deploy -c docker-compose.yml upgrade-test-1_

### Stop the service

_docker stack rm upgrade-test-1_

### Stop swarm

_docker swarm leave --force_
