# MineCrashService

MineCrashService is the backend for the MineCrash service. It is writen using Java and Springboot. This handles converting the solutions from [here](https://github.com/Eimer-Archive/MineCrashSolutions) into the database. It handles requests to check an input for any known solutions and if any are found it returns the solution. This is just a backend with endpoints to make requests to.

Currently this is basic and it just has basic `OR` match checking for solutions and arguments to extract relevant information, check [here](https://github.com/Eimer-Archive/MineCrashSolutions?tab=readme-ov-file#template) for more information. A more advanced system will most likely be worked on in the future but for now as a more proof of concept this works perfectly fine.

## Making your own "frontend"

This is just a backend, it is not how people are meant to use this. So it requires a frontend to handle the communication between the user and backend. Currently we have a [discord bot](https://github.com/Eimer-Archive/MineCrash) that interacts with the backend, but anything that can make GET and POST requests is possible to make into a frontend. 

Anyone is allowed to make their own frontend using either their own hosted version of this or the official backend. Currently we do not limit requests (On the TODO) but we ask you to please not spam any of these endpoints.

Documentation on the endpoints will be coming in the future, for now look at the classes inside the `controller` package for the endpoints that are accessable.

If you are making your own frontend please change the User Agent to something unique when making requests to our backend.

## FAQ

### Why Java/Springboot?

I picked Java because it is what I know the best and Springboot is a library that I have worked with before for backends. This is also a project to help me learn it from the ground up properly as I have not touched Springboot in a while but I want to use it for some larger projects in the future.

### What database does it use?

It uses MariaDB

## Contributing

All contributations are welcome. Contribution guidelines may be coming in the future. But for now just try to follow the existing code format.

### Setup

Clone with repository (or your fork) and open it up with your favourite IDE. In IntelliJ you can open up the project and it should start downloading any dependencies. Otherwise it is just a maven project so however you do that on your machine. Proper guide coming soon (maybe).
