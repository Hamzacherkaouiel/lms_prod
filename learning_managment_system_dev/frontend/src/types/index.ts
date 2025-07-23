export interface User {
  id: number;
  firstname: string;
  lastname: string;
  mail: string;
  speciality:string
}

export interface Student extends User {
  enrolledCourses?: Course[];
}

export interface Teacher extends User {
  courses?: Course[];
}

export interface Admin extends User {
  // Admin specific properties
}

export interface Course {
  id: number;
  title: string;
  description: string;
  capacity: number;
  teacher: Teacher;
  modulesList: Module[];
  enrollementsCourses: Enrollment[];
  test?: Test;
}

export interface Module {
  id: number;
  title: string;
  description: string;
  course: Course;
  lessons: Lesson[];
}

export interface Lesson {
  id: number;
  description: string;
  contentType: string;
  s3Url: string;
  module: Module;
}
export interface SimpleLesson {
  id: number;
  description: string;
}

export interface Enrollment {
  id: number;
  student: Student;
  course: Course;
  enrollmentDate: Date;
}

export interface Test {
  id: number;
  title: string;
  questions: Question[];
}

export interface Question {
  id: number;
  description: string;
  scoreQuestion: number;
  options: Answer[];
}
export interface Answer {
  id: number;
  answer: string;
  isfalse: boolean
}
export interface TestAttempt {
  id: number;
  score: number;
  max_score: number;
  message: string
}

export interface UserCreation {
  firstname: string;
  lastname: string;
  mail: string;
  password: string;
  role: 'student' | 'teacher' | 'admin';
  operation:string;
  speciality:string
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export type UserRole = 'student' | 'teacher' | 'admin';

export interface AuthUser {
  role: string;
  token: string;
}