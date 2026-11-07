# HRMS Frontend (React + Vite)

This is a frontend-only project prepared against the controllers and DTOs found in the supplied HRMS demo ZIP. It does not include or modify any Spring Boot backend project.

## Requirements

- Node.js 20.19+ or 22.12+
- npm
- Your HRMS backend services running locally

## Run in STS workspace / HRMS root

1. Extract the `hrms-frontend` folder into the existing `HRMS_DEMO` root, beside the four backend projects.
2. Open a terminal inside `HRMS_DEMO/hrms-frontend`.
3. Install dependencies:

   ```bash
   npm install
   ```

4. Start the React development server:

   ```bash
   npm run dev
   ```

5. Open http://localhost:5173.

The `.env` file points to `http://localhost:8080` (your API Gateway). Change `VITE_API_BASE_URL` only when your Gateway runs on a different host/port. Restart Vite after changing `.env`.

## Frontend features

- Login and registration using `POST /api/auth/login` and `POST /api/auth/register`.
- Bearer token attachment using an Axios request interceptor.
- Protected React routes.
- Dashboard overview using employee, department and designation APIs.
- Employee list with pagination, search, status filtering, create, update and delete.
- Department CRUD.
- Designation CRUD.
- Onboarding task list by employee, task creation and status updates.

## API contracts used from the supplied code

| Feature | Method | Path |
|---|---|---|
| Register | POST | `/api/auth/register` |
| Login | POST | `/api/auth/login` |
| Employees | GET | `/api/employees?page=0&size=10` |
| Employee by ID | GET | `/api/employees/{id}` |
| Create employee | POST | `/api/employees` |
| Update employee | PUT | `/api/employees/{id}` |
| Delete employee | DELETE | `/api/employees/{id}` |
| Departments | GET/POST | `/api/departments` |
| Department | PUT/DELETE | `/api/departments/{id}` |
| Designations | GET/POST | `/api/designations` |
| Designation | PUT/DELETE | `/api/designations/{id}` |
| Employee onboarding tasks | GET/POST | `/api/onboarding/employees/{employeeId}/tasks` |
| Task status | PATCH | `/api/onboarding/tasks/{taskId}/status` |

Employee creation sends nested references matching the current `Employee` entity:

```json
{
  "firstName": "Asha",
  "lastName": "Rao",
  "email": "asha@example.com",
  "phone": "9876543210",
  "dateOfJoining": "2026-11-06",
  "salary": 45000,
  "status": "ACTIVE",
  "department": { "id": 1 },
  "designation": { "id": 1 }
}
```

The Auth Service login response in the supplied code is expected to contain `accessToken`, `tokenType`, and `expiresIn`. The frontend stores `accessToken` in `sessionStorage` and sends it as `Authorization: Bearer <token>` on API calls.

## Backend integration items noticed in the supplied ZIP

These are observations only. This frontend package does not edit backend files.

1. The Employee Service `application.yml` in the ZIP registers the service as `employee-service`, while the API Gateway route uses `lb://hrms-employee-service`. Those service IDs must be made consistent in the backend for service discovery to route employee traffic.
2. The Gateway configuration in the ZIP contains routes for `/api/auth/**` and `/api/employees/**` only. Department (`/api/departments/**`), designation (`/api/designations/**`) and onboarding (`/api/onboarding/**`) calls also need Gateway routes to the Employee Service before those screens can use the Gateway.
3. The Gateway route's load-balanced service ID must match the final Employee Service application name.
4. The supplied Gateway configuration should allow CORS requests from `http://localhost:5173` (including `OPTIONS` preflight requests). This frontend does not change Gateway CORS settings.
5. The supplied employee controller returns `Employee` entities and expects nested `department: { id }` and `designation: { id }` references for create/update. The UI sends that payload shape. The uploaded `EmployeeRequest`/`EmployeeResponse` DTOs are not currently used by those controller methods.
6. The uploaded Employee Service source does not show a JWT validation filter/security configuration. The frontend attaches the JWT, but authorization must be enforced by the backend before protected data is considered secure.

## Security notes

- Do not add DB passwords or JWT signing secrets to this frontend. Vite variables prefixed with `VITE_` are public in the browser bundle.
- The supplied ZIP contained backend configuration values that look like credentials/secrets. Rotate them and remove committed secrets from source control history as appropriate.
- This demo stores the access token in `sessionStorage` for a simple tab-scoped session. For production, prefer an appropriately configured `HttpOnly`, `Secure`, `SameSite` cookie-based flow and stronger XSS protections.

## Build

```bash
npm run build
```

The production build is written to `dist/`.
