import api from '@/lib/api';
import {Course, Module, Lesson, Student} from '@/types';
import axios from "axios";

export const courseService = {
  // Get all courses
  getAllCourses: async (): Promise<Course[]> => {
    const response = await api.get('/courses/');
    return response.data;
  },

  // Get single course
  getCourse: async (id: number): Promise<Course> => {
    const response = await api.get(`/courses/${id}`);
    return response.data;
  },

  // Get courses by teacher ID
  getCoursesByTeacherId: async (teacherId: number): Promise<Course[]> => {
    const response = await api.get(`/courses/teacher/${teacherId}`);
    return response.data;
  },

  // Get courses by teacher email
  getCoursesByTeacherEmail: async (email: string | null): Promise<Course[]> => {
    const response = await fetch(`http://localhost:8082/courses/teacher/mail/${email}`,
        {
          headers:{
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
        )
    return response.json();
  },

  // Create new course
  createCourse: async (courseData: Partial<Course>, teacherId: number): Promise<Course> => {
    const response = await api.post(`/courses/teacher/${teacherId}`, courseData);
    return response.data;
  },

  // Create course by teacher email
  createCourseByEmail: async (courseData: Partial<Course>, email: string | null): Promise<Course> => {
    const response = await fetch(`http://localhost:8082/courses/teacher/mail/${email}`,
        {
            method: 'POST',
            headers:{
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(courseData)
        }
        );
    return response.json();
  },

  // Update course
  updateCourse: async (id: number, courseData: Partial<Course>): Promise<Course> => {
    const response = await fetch(`http://localhost:8082/courses/${id}`,
        {
          method: 'PUT',
          headers:{
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(courseData)
        }
        );
    return response.json();
  },

  // Delete course
  deleteCourse: async (id: number): Promise<void> => {
    await api.delete(`/courses/${id}`);
  },

  // Get enrolled courses by student ID
  getEnrolledCourses: async (studentId: number): Promise<Course[]> => {
    const response = await api.get(`/enrollemnt/${studentId}`);
    return response.data;
  },

  // Get enrolled courses by student email
  getEnrolledCoursesByEmail: async (email: string | null): Promise<Course[]> => {
    const response = await api.get(`/enrollemnt/mail/${email}`);
    return response.data;
  },
  getEnrolledStudentsByCourse: async (id: number): Promise<Student[]> => {
    const response = await fetch(`http://localhost:8082/Student/enrolled/${id}`,
        {
          headers:{
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
        );
    return response.json();
  },
  getNotEnrolledStudents: async (id:number):Promise<Student[]> =>{
    const response=await fetch(`http://localhost:8082/Student/not-enrolled/${id}`,
        {
          headers:{
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
        )
    return response.json()
  },

  // Create single enrollment
  createEnrollment: async (courseId: number, studentId: number): Promise<void> => {
    await api.post(`/enrollemnt/${courseId}/${studentId}/single`);
  },

  // Create multiple enrollments
  createMultipleEnrollments: async (courseId: number, studentIds: number[]): Promise<void> => {
    await fetch(`http://localhost:8082/enrollemnt/${courseId}/multiple`,
        {
            method: 'POST',
            headers:{
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(studentIds)
        }
        );
  },

  // Delete enrollment
  deleteEnrollment: async (enrollmentId: number): Promise<void> => {
    await api.delete(`/enrollemnt/${enrollmentId}`);
  },

  // Delete enrollment by student ID
  deleteEnrollmentByStudentId: async (studentId: number,courseId:number): Promise<void> => {
    await fetch(`http://localhost:8082/enrollemnt/student/${studentId}/course/${courseId}`,
        {
            method :'DELETE',
            headers :{
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
            }
        }
        );
  }
};