# Nomad Navigator - Travel Management Application

Nomad Navigator is a Java application developed to facilitate the management and organization of trips, whether for work or leisure purposes. It allows users to create trips, invite participants, organize activities, and save important links related to the trip.
Main Features

1. Register Next Trip

- Allows users to create a new trip by specifying the destination, start and end dates, and inviting participants.

2. Invite Participants

- After registering the trip, the invited participants receive an invitation email. By clicking the confirmation link, they can confirm their participation in the trip.

3. Organize Activities

- The trip creator can add planned activities for each day of the trip, including the title, date, and time of the activities.

4. Save Important Links

- Participants can add useful links related to the trip, such as accommodation reservations, places of interest, etc.

## Requirements
### Functional Requirements

1. The user registers a trip by providing the destination, start date, end date, guests' email addresses, as well as their full name and email address.
2. The trip creator receives an email to confirm the new trip via a link. By clicking the link, the trip is confirmed, the guests receive a confirmation email, and the creator is redirected to the trip page.
3. Guests, by clicking the confirmation link, are redirected to the application where they must enter their name (the email is already filled in) and will then be confirmed on the trip.
4. On the trip page, participants can add important trip links, such as Airbnb reservations, places to visit, etc.
5. The trip creator can add activities that will occur during the trip, specifying the title, date, and time.

## Other Requirements

- Customized exception handling for better error identification.
- Endpoints documentation with Springdoc OpenAPI.

## Endpoints Documentation

After starting the project, the endpoints documentation can be accessed through the link: http://localhost:8080/swagger-ui/index.html

##Technologies Used

- Java
- Spring Boot
- Springdoc OpenAPI
- H2 In-Memory Database
- Customized Exception Handling

## Running the Project

1. Clone the Repository

```bash
git clone <repository-url>
cd nomad-navigator
```

2. Set Up the Development Environment

Ensure you have the following installed on your system:

- Java JDK 11+
- Maven

3. Configure the Database

The application is configured to use the H2 in-memory database. Ensure the application.properties file in the src/main/resources directory has the following configuration:

```bash
spring.datasource.url=jdbc:h2:mem:nomad_navigator
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.datasource.username=sa
spring.datasource.password=
server.error.include-stacktrace=never
```

4. Install Dependencies

Navigate to the project directory and run:

```bash
mvn clean install
```

5. Run the Application

Start the application by running:

```bash
mvn spring-boot:run
```

6. Access the Application

The application should now be running on http://localhost:8080. You can access the endpoints documentation at http://localhost:8080/swagger-ui/index.html.
