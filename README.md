# AI Documentation Generator

Welcome to the AI Documentation Generator project! This application is designed to automatically generate documentation using AI.

## Project Structure

This project is organized as a full-stack application with separate backend and frontend modules:

- `backend/`: Contains the Spring Boot RESTful API application.
- `frontend/`: Contains the Angular single-page application (SPA).
- `.vscode/`: Shared VS Code workspace configurations.

## Technology Stack

### Backend
The backend is built using the Spring Boot framework to provide robust and scalable REST APIs.
- **Framework:** Spring Boot 3.2.5
- **Language:** Java 17
- **Build Tool:** Maven
- **Key Dependencies:**
  - `spring-boot-starter-web` for RESTful endpoints.
  - `lombok` for reducing boilerplate code.
  - `spring-boot-starter-test` for testing.

### Frontend
The frontend provides a modern user interface powered by Angular.
- **Framework:** Angular 21.0.0
- **Language:** TypeScript (~5.9.2)
- **Package Manager:** npm (10.9.4)
- **Testing:** Vitest and jsdom
- **Build Tools:** Angular CLI 21.0.5

## Execution & Running Locally

### Running the Frontend
The frontend can be easily started using the Angular CLI.
1. Navigate to the `frontend/` directory: `cd frontend`
2. Install dependencies (if not already installed): `npm install`
3. Start the development server: `npm start` (or `ng serve`)
4. Access the application at `http://localhost:4200/`

### Running the Backend
1. Navigate to the `backend/` directory: `cd backend`
2. Run the application using Maven: `./mvnw spring-boot:run` or `mvn spring-boot:run`
3. The API will typically be accessible on `http://localhost:8080` (unless configured otherwise).

## Current Project Status & Known Issues
- **Frontend:** Fully operational and runs successfully on port `4200`.
- **Backend:** Fully operational and runs successfully on port `8080`. The previous Maven build errors ("Network is unreachable") have been resolved, and the backend can now be built and run locally without network connectivity issues.
