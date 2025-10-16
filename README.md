# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]
: https://tinyurl.com/ycxm6nz3
## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Server Architecture Map
[![Server Architecture Map](Server_architecture)](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5xDAaVnylajVN7KAgAV2wAYgAWAGYADgAmNxAYCOR7AAswHQRQwwAlFHskVQs5JAg0TERUUgBaAD5yShooAC4YAG0ABQB5MgAVAF0YAHpQgygAHTQAbwAiCco0YABbFHm2+Zh5gBod3HUAd2gOTe29nZRV4CQEc52AX0xhVphG1nYuSg6FpagVusHpd5odVCcoGctjt9vNrrd7tD5s82JxuLAPq9RB0oEUSmBKAAKQrFUqUQoARzypQAlC8WqJ3k1fIplAEOvYUGAAKqTQn-QEoOkspQqdRMow6NoAMSQnBgvMoLJgOksMAFazEOlxwAA1grJjAjkgwBl1ZNBTBgAgdRw1SgAB74jQitniuqYhkqDqKqAs+kiFTvT3UH4wBQ2lDAO0DPXoACiDpU2AIlQDbw9TRq5g6UScMRmC3W6mAnM2O3jUBC7XNy01Vsj0bV8l16GRZk4mFdYtUwaaWO9MDQoQQCHTjI+3fZIB1BN9-MmLOF2lFAQlxjaCg4HANSu046Dk5XbtUbRnUYJClCpsJwGvGSXXePPfXUq3O6vpv9A8Mmc+aLDEl8XJNQRywVFvgxENWl+RYLU1bYOguWE71NAYIFbNBEJ2J500oPt4GQHMYASJwnELOC6yBGAkJhHZUIydDMOwi5nnQDgghCcIImgdhOVSaU4HjaQ4AUGAABkIGKKps3qfsWjDHp+mGMYDHUCo0ELDV1n2MEIQ4Z5Xnwj4IPRX5tJQXY9NOFEvnRCUfzaWZJOkmBcSpNQwEMr1f2ZZ92RgWZfRgAAzLkQDNCznind0mg3UK5R3f4tHkFU1Qsg9fOaQMUA6Fy5RgSYqEZVoQsIcpKhgCAQpgazIUwbUo11WZi1UUsNkratNjqnc5RAaBcXAdtjAImLTxgQowFCAE5UcX1vx8iUxo6T8zUgTDMoI0yw3ytBcVUMDMG2qD5NDGtZgsmAAGorXvdb0G8s6CNksBzPg9Zrtu017rQNjO0wYIwkiYIUHQVJ0iyEGwZcsIsBehyFJrTppHjcT4wGeMRlGNTVA0mYGKY9A8IxJpjt+BifseMZRgyOzDAQKS3JQDzSiOumSey4rB0JBn7FhnmpNh9zqTAGl5mOmBuDAXsisZXU0AgI4YEgSXGdhmAQCIjB5k2o8-BPDoOBQKXDFWmBCbQGAn31l8Pni+ZClWCAaC+s0Ut0fQNg7DiAa4yJcR3CJsDlfVxPxGAAHFNQ0eHoMUiP0ax+xNXx+8LeJiUyeaeVOgTlTKRFtmAI5xydm23tOWYZBSiqmrk4+kLoHDe9fR1n8lv89QkIpjCwbAUYQp1sbXy2SuYHr+n8R1ka-2WnZY1bCv62rsAh873s-1L+YyFp3FJfDwkJ5gJAODF3WTPZpCI65XsV9r8f6xCkJVlqqAKlgWWVDbxHnq116dkmtNS2R874q01rUVe3tOJAwiBwAA7G4JwKAnCpHjDEOAQkABs8BZyGCjh9aof8EZnQ6EpQYScU4rDTr3TScwJ4ADlNSPQzKTS+Owd54EMD3ZiRdILEK5rlHYcBcEPw+sLTy38codxtgFeYT8IAv2vswbhbZrasltnFKUOwIooBAPqFRltHTh3PHIPBCE1GrlipKJCaAUBK3wYYE+490CUFMWcK2Y0AA8HoNzzFxFNAEoiUAAElpCSIEd4uoy15jFBdkfUJOtjqRMcvMBxJ9wmUEiWTVJSA4manSYQiByTEZbGEReQwE8dbsWgdxSwxsGZKzSEgTIYA6mjkVjAAAUhAAqDjUg5FALqQp5h+GKW6NyFSowJ6pzQjQws2AEDADqVAOAEAGZQH2BPUJzDjKsOLuTahmFdKLOWas9ZmzNTbN4fZaC2IYAACseloAcbrPyMiu4wCoNaE+pjVoWJPCPIRGRdH6lWhbGARjSiqGnjoUa68kLBOYBwCAahCoQEKuxFUgyglGGABAcJE43nqNkWPSp-yNHWJ2LKeUR9VQwASZoWFs94WR01P6YedstHcmwClCp9ZjSmg1n-c+ezIIdG6XKBxhQDoIHAuzAiRkazb0xf1NAjcoA3FTFhYZGBikkPFsAF24CRmPIKkfYqwAcUMuqb7GBwQlng2aZDKADrEBRlgMAbACzypaqtrHU6MEugozRhjLGxgM4X32TsAAss7QwyNUbo0xkzFmkCJa3O9PMN1eBcXyAJYeIlljxrzCNibWsGyJ67AMeStcnKkKlq5IYd2a93mqEifbWNLtxE13VUYY2XJZr5pQJE6JAAhN+DgU0i2Vuiz+Xskkei3k7GgvY5SNwbJUewGSoBZMvvMaNcZCrTSbdoY+vYG0EjODW9Q7apSa3dYSGkMwdVgD1TBPxXIgEwFau1XNHsDBCvdSgK9P4R3wvvXgR91zMmLp8mebgkG6SKt3fsiDUAoMvTfT8NDUHqlAA)

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
