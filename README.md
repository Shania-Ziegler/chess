# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5Tme1AgBXbADEACwAzAAcAExuIDAByPYAFmA6CL6GAEoo9kiqFnJIEGiYiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9L4GUAA6aADeAEQDlGjAALYo43XjMOMANCu46gDu0ByLy2srKLPASAj7KwC+mMK1MJWs7FyUDRNTUDPzF4fjm6o7UD2SxW63Gx1O52B42ubE43FgD1uogaUCyOTAlAAFJlsrlKJkAI5pXIAShuNVE9yqsnkShU6ga9hQYAAqoNMe9PigyTTFMo1KoqUYdHUAGJITgwNmUXkwHSWGCcuZiHSo4AAaylgxgWyQYASisGXJgwAQao4CpQAA90RpeXSBfdERSVA1pVBeeSRConVVbi8YAozShgBaOhr0ABRK0qbAEQpeu5lB4lcwNIJOEIjCbzdTAJmLFaRqA+eqG6bKk3B0MK+Tq9DQsycTD2-nqX3Vb0oBpoXwIBCJykPVv01R1EBqjHujmDXk87QO9sPYx1BQcDhamXaQc+4cLttjichjEKXz6zHAM8JOct-ejoUrtcb0-6z1I3cPWHPMs49H4tR9lgX7wh2-plm8RrKssDQHKCl76h0ED1mg0ErFciaUB2qYYA0EROE42aTJBXwwDBIIrPBCSIchqEHNc6AcKY3h+IE0DsEysSinAkbSHACgwAAMhA2RFNhzDOtQAYtO03R9AY6gFGg2ZKvM6x-ACHDXGBQrAQGCDCRKowqSgqzqbsMJPCBElUMiL4GqiRJqGARnEYYZmAtcO6GHutIHg0orMiACR2dOxmmfo-y7POvn3suIrVHgQUwAAZtA5aAtocoKsZmg6B2I4CnU7qegVS7Wcim4etuXlCqVY7wJOKB2TOFbzNFfKxVUK4wAAkmggzMJRRjbsY+V3oVJpXtR6C3jFjrJn6LrdpNCFITN77eZ+lkBkJImZKogEua1JkrVRa1oBZcKYQtna1K8RHHfs6yUdNKFLFpNTXSmyBpk0eFOD0EGPUscFTed71NoxTE+P4ATeCg6CxPESTw4ju1+FgYlCmBDSNNIkYCZGHSRj0vTyaoikjC950YQiVS6eBlGQMhl3ftjS0NPp9gYxeYPIWSG21eNDIwEyYDNdT-OzR181dfF4qSkN8owMzM2jTddUNHZJXC4KN0bVrV5vktQtzSLHAoNwJ5Xrzq1S3VD7xZkswQDQp0pT4szDfIegGLlY1m-VVAQEgjEO-rHMwMHoc1TdDMNNHjEM6Bn1lujZ6ogdCBgHUtNYT9OF-fhgP0c2zGw6iG4BNgEqagJ6IwAA4sqGhY9ZUmN0TpP2MqVN8+tqc6dtZYUJKjSd7JhLEkBw-s12nPos3ua22dUuCz5Msi2LEv92g7WLnrcv+RKG5K7WNPqxvB+G6+27h+VrqBkb27r9SuuMsyQYIEvaiYvvB6OwaJkCwqA3amgQKLFu-sNbv0EuiAAPD-XkFRpYHxTvPOBuREHKmQbHLaV006LxbvtQCmBk4R0kuBcYPdcyFgmDQlAPVpCLDKPcFYWwEh6hQO6LkqwUigHVDw5UqwGEADllSXA+pQ-OpQc5FwBjAUYDD6pKOVEwuorCyirA4VwoRqk5QIAEXok6YiJGQ2hixAIHAADsbgnAoCcLESMIQ4DcQAGwNWPIYH+MBigF3EotShuM2idG7r3GYu9symPmFIpM9Nh4wTIAkPAhgmbnXGCvVWaB1gMK5GSchgSbKP3GHARqDDMnnRycqPJ4xPCvxkLA8YyVPZN2ZKdLJtTw5H3gAkFAIB1RpOQn-VBAC4o32vHfXW6CinLTskwupJsr5+U8XIFAP9MQ-yYf-TqwoGh9QGpA+YMAY7GBgYHBomyw5TIoTMxkyoY7r3wd+BopSvGXLIcPeByYDYMJjuQ9uZZfmMW0jdMSuN-qAyBWwsopcobl0CJYS2+kthIyQIkMAiL+wQBRQAKRDmgJulYAj8P6X42Rc87pNGaCyWSvQGF9ztugbM2BDGIqgHACA+koBVPmEw2J114kENeJLdAalWWUA5VynljDpCsysoUiqAArfF6zBmipZcANlkroCrHdJs6QZJTD1M1qLT+Ns7KvW2bLXZKUT6UWgUs0c4zXojNig-WZz95ALK7KbTe9UxbrIYVs111ruoKw3AwrKvVpAOrfucwl8wdaB2mRVJB1VjWwP8FoDEgacHaCtUubpLJsDZsMJG3U+oYAgH8bGhp8aDoHhDWVBVj9ez9jwYK55MA20Dn+YUgMPa86gv8eC4uijPIMQsbDbwmrUXopnQqRAIZYDAGwCywg+RCi+Lbv2sseMCZExJn0YwQ7O3whggAWVdoYfdhNiY9ActPWpBTbrIhKdwPA3tgC1NjnGv1MELZWxQEdblDDVhqouk2w+NrxiAeZIYd42bv21pNVWj9ldhlGsWX+6+aHl0-1UMMuqXyyjdXGFet2j6nIpTSnBggDhamXxw8s6ty6UAcEwxm+NrG8DNQLdB7q0hLbwfdkhzwxHkwrh41AP+IwvIkc1tJ4ZDMSMGyUwLVOJH47qbJeYVTqc6iNAhTpydQA

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
