# Learning Management System - Frontend

A modern, responsive frontend for the Learning Management System built with Next.js 14, TypeScript, and Tailwind CSS.

## Features

### 🎯 Role-based Access Control
- **Students**: Browse and enroll in courses, view course materials, track progress
- **Teachers**: Create and manage courses, add modules and lessons, manage enrollments
- **Admins**: Full system access, user management, course oversight

### 📱 Modern UI/UX
- Responsive design that works on all devices
- Clean, intuitive interface with modern components
- Dark mode support (coming soon)
- Accessible design following WCAG guidelines

### 🔐 Authentication & Security
- Integration with Keycloak for secure authentication
- JWT token-based authentication
- Protected routes and components
- Role-based component rendering

### 📚 Course Management
- Browse all available courses
- Advanced search and filtering
- Course enrollment system
- Module and lesson organization
- Progress tracking

### 🎓 Learning Experience
- Structured course content with modules and lessons
- Video content support
- Interactive lessons
- Progress tracking and completion status

## Tech Stack

- **Frontend Framework**: Next.js 14 with App Router
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **UI Components**: Custom component library with Radix UI primitives
- **State Management**: React hooks and context
- **HTTP Client**: Axios
- **Authentication**: NextAuth.js with Keycloak integration
- **Icons**: Lucide React
- **Form Handling**: React Hook Form with Yup validation

## Getting Started

### Prerequisites

- Node.js 18+ and npm
- Running backend server (Spring Boot)
- Keycloak server for authentication

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Environment Setup**
   
   Create a `.env.local` file in the root directory:
   ```env
   NEXT_PUBLIC_API_URL=http://localhost:8082
   NEXT_PUBLIC_KEYCLOAK_URL=http://localhost:8081
   NEXT_PUBLIC_KEYCLOAK_REALM=hamza
   NEXT_PUBLIC_KEYCLOAK_CLIENT_ID=lms-auth
   NEXTAUTH_URL=http://localhost:3000
   NEXTAUTH_SECRET=your-secret-key-here
   ```

4. **Start the development server**
   ```bash
   npm run dev
   ```

5. **Open your browser**
   
   Navigate to [http://localhost:3000](http://localhost:3000)

### Backend Integration

Make sure your backend is running on `http://localhost:8082` with the following endpoints available:

- **Authentication**: `/sign/`, `/sign/create-user`
- **Courses**: `/courses/`, `/courses/{id}`, `/courses/teacher/{id}`
- **Enrollment**: `/enrollemnt/`, `/enrollemnt/{id}`
- **Students**: `/Student/`, `/Student/profile`
- **Teachers**: `/Teacher/`, `/Teacher/profile`
- **Lessons**: `/lessons/`, `/lessons/{id}`
- **Modules**: `/modules/`, `/modules/{id}`

## Project Structure

```
frontend/
├── src/
│   ├── app/                    # Next.js App Router pages
│   │   ├── login/              # Login page
│   │   ├── register/           # Registration page
│   │   ├── dashboard/          # Dashboard page
│   │   └── courses/            # Course-related pages
│   ├── components/             # Reusable components
│   │   ├── ui/                 # Base UI components
│   │   └── layout/             # Layout components
│   ├── services/               # API service functions
│   ├── types/                  # TypeScript type definitions
│   ├── lib/                    # Utility functions
│   └── styles/                 # Global styles
├── public/                     # Static assets
└── README.md
```

## Key Components

### Layout Components
- **Navigation**: Responsive navigation with role-based menu items
- **Layout**: Main layout wrapper with authentication checks
- **Protected Routes**: Higher-order component for route protection

### UI Components
- **Button**: Versatile button component with variants
- **Card**: Content container with header, content, and footer
- **Input**: Form input with validation support
- **Form Components**: Integrated form handling with validation

### Page Components
- **Dashboard**: Role-specific dashboard with stats and quick actions
- **Courses**: Course listing with search and filtering
- **Course Detail**: Individual course view with enrollment functionality
- **Authentication**: Login and registration forms

## API Integration

### Service Layer
The application uses a service layer pattern for API integration:

- **authService**: Authentication and user management
- **courseService**: Course operations and enrollment
- **userService**: User profile and management
- **lessonService**: Lesson and module operations

### Error Handling
- Global error interceptors for API responses
- User-friendly error messages
- Automatic token refresh and redirect on authentication errors

## Development

### Available Scripts

```bash
# Development server
npm run dev

# Production build
npm run build

# Start production server
npm start

# Type checking
npm run type-check

# Linting
npm run lint
```

### Code Style

- TypeScript with strict mode enabled
- ESLint for code quality
- Prettier for code formatting
- Tailwind CSS for styling

## Authentication Flow

1. User visits the application
2. If not authenticated, redirected to login page
3. Login integrates with Keycloak for authentication
4. JWT token stored in localStorage
5. Protected routes check authentication status
6. Role-based access control applied throughout the app

## Deployment

### Production Build

```bash
npm run build
npm start
```

### Environment Variables

Make sure to set the following environment variables for production:

- `NEXT_PUBLIC_API_URL`: Backend API URL
- `NEXT_PUBLIC_KEYCLOAK_URL`: Keycloak server URL
- `NEXTAUTH_URL`: Frontend URL
- `NEXTAUTH_SECRET`: Secret key for session encryption

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For questions and support, please contact the development team or create an issue in the repository.
