**Splitty** is a *lightweight* and *user-friendly* application to facilitate tracking of expenses within a group of people during events or parties. 

---
## Installation

>##### Ensure prerequisites:
>- Java version 21+
>```shell
>java --version
>```
>- JavaFX SDK 21

- Navigate to the directory you would like to install the product to
- Clone the repository
```shell
git clone git@gitlab.ewi.tudelft.nl:cse1105/2023-2024/teams/oopp-team-75.git
```
- Build the project
```shell
./gradlew build
```
- Run the server
```
./gradlew bootRun
```
- Run the client (from a separate terminal)
```
./gradlew run -PskipProcessResources=true
```

## Configuration
Most configuration options are available in the config file located at `client/src/resources/config/config.properties`.

To set up e-mail functionality, you must first have a Gmail account with [app passwords enabled](https://support.google.com/mail/answer/185833?hl=en).

Keyboard shortcuts are available at the shortcuts option in the menu.
