'use client'

import React, { useState, useEffect } from 'react'
import Layout from '@/components/layout/Layout'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { authService } from '@/services/auth'
import { courseService } from '@/services/course'
import { Course } from '@/types'
import { 
  BookOpenIcon, 
  UsersIcon, 
  SearchIcon,
  PlusIcon,
  FilterIcon,
  UserIcon
} from 'lucide-react'
import Link from 'next/link'
import { debounce } from '@/lib/utils'
import {useRouter} from "next/navigation";
export default function CoursesPage() {
  const router=useRouter()
  const [courses, setCourses] = useState<Course[]>([])
  const [filteredCourses, setFilteredCourses] = useState<Course[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const [searchTerm, setSearchTerm] = useState('')
  const [filterType, setFilterType] = useState<'all' | 'enrolled' | 'created'>('all')
  const role = authService.getUserRole()
  const user=authService.getUserMail()
  const firstname=authService.getFirstName()
  const lastname=authService.getLastName()

  useEffect(() => {
    if (authService.isTokenExpired()) router.push("/login")
    const fetchCourses = async () => {
      try {
        let coursesData: Course[] = []
        
        if (role === 'admin') {
          // Admin can see all courses
          coursesData = await courseService.getAllCourses()
        } else if (role === 'teacher') {
          // Teacher can see all courses and their own courses
          coursesData = await courseService.getCoursesByTeacherEmail(user)
        } else {
          // Student can see all courses (for browsing/enrollment)
          coursesData = await courseService.getEnrolledCoursesByEmail(user)
        }
        
        setCourses(coursesData)
        setFilteredCourses(coursesData)
      } catch (error) {
        setError('Failed to load courses')
        console.error('Courses error:', error)
      } finally {
        setIsLoading(false)
      }
    }

    fetchCourses()
  }, [role])

  const debouncedSearch = debounce((term: string) => {
    const filtered = courses.filter(course =>
      course.title.toLowerCase().includes(term.toLowerCase()) ||
      course.description.toLowerCase().includes(term.toLowerCase())
    )
    setFilteredCourses(filtered)
  }, 300)

  useEffect(() => {
    debouncedSearch(searchTerm)
  }, [searchTerm, courses])

  const handleFilterChange = (type: 'all' | 'enrolled' | 'created') => {
    setFilterType(type)
    // In a real app, you'd filter based on enrollment status or teacher ownership
    // For now, we'll just show all courses
    setFilteredCourses(courses)
  }



  if (isLoading) {
    return (
      <Layout>
        <div className="flex items-center justify-center min-h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Loading courses...</p>
          </div>
        </div>
      </Layout>
    )
  }

  return (
    <Layout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Courses</h1>
          </div>
          {role === 'teacher' && (
            <Link href="/courses/create">
              <Button className="inline-flex items-center mt-4 sm:mt-0">
                <PlusIcon className="h-4 w-4 mr-2" />
                Create Course
              </Button>
            </Link>
          )}
        </div>

        {/* Search and Filter */}
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1 relative">
            <SearchIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            <Input
              placeholder="Search courses..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10"
            />
          </div>

        </div>

        {/* Error Message */}
        {error && (
          <div className="rounded-md bg-red-50 p-4">
            <div className="text-sm text-red-700">{error}</div>
          </div>
        )}

        {/* Courses Grid */}
        {filteredCourses.length === 0 ? (
          <div className="text-center py-12">
            <BookOpenIcon className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-500 text-lg">No courses found</p>
            <p className="text-gray-400 text-sm mt-2">
              {searchTerm ? 'Try adjusting your search terms' : 'Be the first to create a course!'}
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredCourses.map((course) => (
              <Card key={course.id} className="hover:shadow-lg transition-shadow">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <CardTitle className="text-lg">{course.title}</CardTitle>
                      <CardDescription className="mt-2 line-clamp-2">
                        {course.description}
                      </CardDescription>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">

                    
                    <div className="flex items-center justify-between text-sm">
                      <div className="flex items-center text-gray-500">
                        <UsersIcon className="h-4 w-4 mr-1" />
                        <span> students</span>
                      </div>
                      <div className="flex items-center text-gray-500">
                        <BookOpenIcon className="h-4 w-4 mr-1" />
                        <span> modules</span>
                      </div>
                    </div>

                    <div className="flex items-center justify-between">
                      <div className="text-sm">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          course.capacity > 0 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {course.capacity > 0 ? 'Available' : 'Full'}
                        </span>
                      </div>
                      <div className="text-sm text-gray-500">
                        {course.capacity} spots left
                      </div>
                    </div>

                    <div className="flex gap-2">
                      <Link href={`/courses/${course.id}`} className="flex-1">
                        <Button variant="outline" className="w-full">
                          View Details
                        </Button>
                      </Link>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </div>
    </Layout>
  )
}