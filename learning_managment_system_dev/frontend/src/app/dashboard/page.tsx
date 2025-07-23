'use client'
import { LogOut } from 'lucide-react';
import React, { useState, useEffect } from 'react'
import Layout from '@/components/layout/Layout'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card'
import { Button } from '@/components/ui/Button'
import { authService } from '@/services/auth'
import { courseService } from '@/services/course'
import { Course } from '@/types'
import { 
  BookOpenIcon, 
  UsersIcon, 
  TrendingUpIcon, 
  ClockIcon,
  PlusIcon,
  ArrowRightIcon
} from 'lucide-react'
import Link from 'next/link'
import {useRouter} from "next/navigation";

export default function DashboardPage() {
  const [courses, setCourses] = useState<Course[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const role = authService.getUserRole()
  const user = authService.getUserMail();
  const router = useRouter()

  useEffect(() => {
    const fetchDashboardData = async () => {
      if (authService.isTokenExpired()) router.push("/login")
      try {
        if (role === 'student') {
          // Fetch enrolled courses for students
          const enrolledCourses = await courseService.getEnrolledCoursesByEmail(user)
          setCourses(enrolledCourses)
        } else if (role === 'teacher') {
          // Fetch courses created by teacher
          const teacherCourses = await courseService.getCoursesByTeacherEmail(user)
          setCourses(teacherCourses)
          console.log(teacherCourses)
          console.log(courses)
        } else {
          // For admin, fetch all courses
          const allCourses = await courseService.getAllCourses()
          setCourses(allCourses)
        }
      } catch (error) {
        setError('Failed to load dashboard data')
        console.error('Dashboard error:', error)
      } finally {
        setIsLoading(false)
      }
    }

    fetchDashboardData()
  }, [user])
  useEffect(() => {
    if (courses.length > 0) {
      console.log("✅ State 'courses' updated:", courses);
    }
  }, [courses]);


  const getWelcomeMessage = () => {
    switch (role) {
      case 'admin':
        return 'Welcome to the Admin Dashboard'
      case 'teacher':
        return 'Welcome to your Teaching Dashboard'
      case 'student':
        return 'Welcome to your Learning Dashboard'
      default:
        return 'Welcome to LMS'
    }
  }

  const getStatsCards = () => {
    const baseStats = [
      {
        title: 'Total Courses',
        value: courses.length,
        icon: BookOpenIcon,
        color: 'bg-blue-500'
      }
    ]

    if (user === 'teacher') {

      const base = [...baseStats]

      /*if (hasEnrollmentData) {
        const totalStudents = courses.reduce(
            (acc, course) => acc + (course.enrollementsCourses?.length || 0),
            0
        )
        base.push({
          title: 'Total Students',
          value: totalStudents,
          icon: UsersIcon,
          color: 'bg-green-500'
        })
      }*/

      base.push({
        title: 'Active Courses',
        value: courses.filter(c => c.capacity > 0).length,
        icon: TrendingUpIcon,
        color: 'bg-purple-500'
      })

      return base
    }


    if (role === 'student') {
      return [
        ...baseStats,
        {
          title: 'Enrolled Courses',
          value: courses.length,
          icon: BookOpenIcon,
          color: 'bg-green-500'
        },
        {
          title: 'In Progress',
          value: courses.length,
          icon: ClockIcon,
          color: 'bg-yellow-500'
        }
      ]
    }

    return baseStats
  }

  if (isLoading) {
    return (
      <Layout>
        <div className="flex items-center justify-center min-h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Loading dashboard...</p>
          </div>
        </div>
      </Layout>
    )
  }

  return (
    <Layout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              {getWelcomeMessage()}
            </h1>
            <p className="text-gray-600 mt-1">
              Hello,
            </p>
          </div>
          {user === 'teacher' && (
            <Link href="/courses/create">
              <Button className="inline-flex items-center">
                <PlusIcon className="h-4 w-4 mr-2" />
                Create Course
              </Button>
            </Link>
          )}
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {getStatsCards().map((stat, index) => {
            const Icon = stat.icon
            return (
              <Card key={index}>
                <CardContent className="p-6">
                  <div className="flex items-center">
                    <div className={`p-3 rounded-lg ${stat.color}`}>
                      <Icon className="h-6 w-6 text-white" />
                    </div>
                    <div className="ml-4">
                      <p className="text-sm font-medium text-gray-500">{stat.title}</p>
                      <p className="text-2xl font-bold text-gray-900">{stat.value}</p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            )
          })}
        </div>

        {/* Recent Courses */}
        <Card>
          <CardHeader>
            <div className="flex justify-between items-center">
              <CardTitle>
                {user === 'student' ? 'My Courses' : 'Recent Courses'}
              </CardTitle>
              <Link href="/courses">
                <Button variant="outline" size="sm">
                  View All
                  <ArrowRightIcon className="h-4 w-4 ml-2" />
                </Button>
              </Link>
            </div>
          </CardHeader>
          <CardContent>
            {error && (
              <div className="rounded-md bg-red-50 p-4 mb-4">
                <div className="text-sm text-red-700">{error}</div>
              </div>
            )}
            
            {courses.length === 0 ? (
              <div className="text-center py-8">
                <BookOpenIcon className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <p className="text-gray-500">
                  {user === 'student' ? 'No courses enrolled yet' : 'No courses created yet'}
                </p>
                {user === 'teacher' && (
                  <Link href="/courses/create">
                    <Button className="mt-4">
                      Create Your First Course
                    </Button>
                  </Link>
                )}
              </div>
            ) : (
              <div className="space-y-4">
                {courses.slice(0, 5).map((course) => (
                  <div key={course.id} className="flex items-center justify-between p-4 border rounded-lg hover:bg-gray-50">
                    <div className="flex items-center space-x-4">
                      <div className="p-2 bg-blue-100 rounded-lg">
                        <BookOpenIcon className="h-5 w-5 text-blue-600" />
                      </div>
                      <div>
                        <h3 className="font-semibold text-gray-900">{course.title}</h3>
                        <p className="text-sm text-gray-500">{course.description}</p>
                      </div>
                    </div>
                    <Link href={`/courses/${course.id}`}>
                      <Button variant="outline" size="sm">
                        View
                      </Button>
                    </Link>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </Layout>
  )
}