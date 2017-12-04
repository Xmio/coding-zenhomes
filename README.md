## A simple service to collect and expose energy consumption from different villages

* I chose to use H2 as a embeded database.
* I used springboot framework.
* I also used lombok to make clean code.
* I also have included querydsl to retrive db infos with filters.

## Download dependencies
Just execute the command in the root.

    mvn clean install

## How to start
Just execute the command in the root. Use 8080 port to access.

    mvn spring-boot:run
    
## Some additional things
* I included a post to add counter to new villages
* Im not sure if is a necessity maintain the endpoints names. I have modified to preserve the restful convention. You can see below.

## End-point

### Electricity Counter

#### Creating an electricity counter

The example creates an electricity counter and returns the created counter id

    POST /counter
    {
        "villageName": "Villarriba"
    }

Response:

    {
        "id": 1
    }
    
#### Retrive an electricity counter

The example retrive an electricity counter by id

    GET /counter/{counterId}
    
Response:

    {
        "id": 1,
        "villageName": "Villarriba"
    }
    
#### Retrive a report of electricity counters

The example retrive a report of electricity counters with the consumption between the duration time in hours until now

    GET /consumption-report/{durationInHours}
    
Response:

    [
	    {
	        "villageName": "Villarriba",
	        "consumption": 200.5
	    },
	    {
	        "villageName": "Villaabajo",
	        "consumption": 200.5
	    }
    ]
    
### Electricity Counter Callback

#### Creating an electricity counter callback

The example creates an electricity counter callback and returns the created callback id

    POST /counter-callback
    {
        "counterId": 1,
        "amount": 200.5
    }

Response:

    {
        "id": 1
    }