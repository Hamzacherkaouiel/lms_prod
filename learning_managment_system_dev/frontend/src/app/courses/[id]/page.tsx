'use client'

import React, { useState, useEffect } from 'react'
import { useParams, useRouter } from 'next/navigation'
import Layout from '@/components/layout/Layout'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/Card'
import { Button } from '@/components/ui/Button'
import { authService } from '@/services/auth'
import { courseService } from '@/services/course'
import {lessonService, moduleService} from '@/services/lesson'
import {Course, Module, SimpleLesson, Student, Test} from '@/types'
import {
  BookOpenIcon,
  UsersIcon,
  UserIcon,
  PlayIcon,
  CheckCircleIcon,
  PlusIcon,
  EditIcon,
  TrashIcon,
  ChevronDownIcon,
  ChevronRightIcon, EyeIcon
} from 'lucide-react'
import Link from 'next/link'
import {PencilIcon} from "@heroicons/react/24/outline";
import {attemptService, testService} from "@/services/test-content";

export default function CourseDetailPage() {
  const params = useParams()
  const router = useRouter()
  const courseId = parseInt(params.id as string)
  const [showStudentPicker, setShowStudentPicker] = useState(false);
  const [nonEnrolledStudents, setNonEnrolledStudents] = useState<Student[]>([]);
  const [selectedStudents, setSelectedStudents] = useState<number[]>([]);
  const [course, setCourse] = useState<Course | null>(null)
  const [modules, setModules] = useState<Module[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [isEnrolled, setIsEnrolled] = useState(false)
  const [error, setError] = useState('')
  const [expandedModules, setExpandedModules] = useState<number[]>([])
  const [enroll,setenroll] =useState([])
  const user = authService.getUserMail()
  const role =authService.getUserRole();
  const firstName=authService.getFirstName();
  const Lastname=authService.getLastName();
  const [isEditing, setIsEditing] = useState(false);
  const [newModuleData,setNewModuleData]=useState({title:''})
  const [test, setTest] = useState<Test>();
  const [showAddModuleForm, setShowAddModuleForm] = useState(false)
  const [editingModuleId, setEditingModuleId] = useState<number | null>(null);
  const [editedModuleTitle, setEditedModuleTitle] = useState<string>('');
  const [lessonsByModuleId, setLessonsByModuleId] = useState<Record<number, any[]>>({})
  const [attemptions,setAttemptions]=useState([])
  const [editData, setEditData] = useState({
    title: '',
    description: '',
    capacity: 0
  });


  useEffect(() => {
    if (authService.isTokenExpired()) router.push("/login")
    const fetchCourseDetail = async () => {
      try {
        const courseData = await courseService.getCourse(courseId)
        setCourse(courseData)
        setEditData({title: courseData.title,description: courseData.description,capacity: courseData.capacity})
        const enrolledData=await courseService.getEnrolledStudentsByCourse(courseId)
        setenroll(enrolledData)
        const modulesData = await moduleService.getModulesByCourse(courseId)
        console.log(modulesData)
        setModules(modulesData)
        const responseTest=await testService.getTestByCourse(courseId)
        setTest(responseTest)
        if (role === 'student') {
          const enrolledCourses = await courseService.getEnrolledCoursesByEmail(user)
          setIsEnrolled(enrolledCourses.some(c => c.id === courseId))
          if(responseTest.id){
            const response=await attemptService.getAttemptionofTest(user,responseTest.id)
            setAttemptions(response)
          }

        }
      } catch (error) {
        setError('Failed to load course details')
        console.error('Course detail error:', error)
      } finally {
        setIsLoading(false)
      }
    }

    fetchCourseDetail()
  }, [courseId, user])


  async function deleteCourse(){
    await courseService.deleteCourse(courseId)
    router.push("/courses")
  }
  const handleAddModule = async () => {
    try {
      const newModule = await moduleService.createModule(newModuleData, courseId)
      setModules((prev) => [...prev, newModule])
      setNewModuleData({ title: '' })
    } catch (error) {
      console.error('Failed to add module:', error)
    }
  }
  async function getNonEnrolledStudent(){
    courseService.getNotEnrolledStudents(courseId).then((response)=>{

      setNonEnrolledStudents(response)
    })
  }

  const toggleModule = async (moduleId: number) => {
    if (expandedModules.includes(moduleId)) {
      setExpandedModules(expandedModules.filter((id) => id !== moduleId))
    } else {
      if (!lessonsByModuleId[moduleId]) {
        try {
          console.log(moduleId)
          const res = await lessonService.getLessonsByModule(moduleId)
          setLessonsByModuleId((prev) => ({ ...prev, [moduleId]: res }))
        } catch (error) {
          console.error('Failed to fetch lessons:', error)
          setLessonsByModuleId((prev) => ({ ...prev, [moduleId]: [] }))
        }
      }

      setExpandedModules([...expandedModules, moduleId])
    }
  }
  function handleAddLesson(id : number){
      router.push(`/lessons/create/${id}`)
  }
  function inspectLesson(id : number){
    router.push(`/lessons/${id}`)
  }
   async function updateCourse(){
      courseService.updateCourse(courseId,editData).then((response)=>setCourse(response))
   }

  const canEditCourse = role === 'teacher'

  if (isLoading) {
    return (
      <Layout>
        <div className="flex items-center justify-center min-h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Loading course details...</p>
          </div>
        </div>
      </Layout>
    )
  }

  if (error || !course) {
    return (
      <Layout>
        <div className="text-center py-12">
          <div className="rounded-md bg-red-50 p-4 max-w-md mx-auto">
            <div className="text-sm text-red-700">{error || 'Course not found'}</div>
          </div>
          <Button 
            onClick={() => router.push('/courses')}
            className="mt-4"
          >
            Back to Courses
          </Button>
        </div>
      </Layout>
    )
  }


  return (
    <Layout>
      <div className="space-y-6">
        {/* Course Header */}
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between">
            <div className="flex-1">
              <h1 className="text-3xl font-bold text-gray-900 mb-2">
                {course.title}
              </h1>
              <p className="text-gray-600 mb-4">
                {course.description}
              </p>
              {canEditCourse && (
                  <Button
                      onClick={() => {
                        setShowStudentPicker(true);
                        getNonEnrolledStudent();
                      }}
                      size="sm"
                      disabled={course.capacity <= 0}
                  >
                    <PlusIcon className="h-2 w-2 mr-2" />
                    Add Student
                  </Button>
              )}
              {showStudentPicker  &&(
                  <Card className="mt-4">
                    <CardHeader>
                      <CardTitle className="text-gray-900">Select Students to Enroll</CardTitle>
                    </CardHeader>
                    <CardContent className="overflow-x-auto">
                      <table className="min-w-full text-sm text-gray-900">
                        <thead>
                        <tr className="border-b">
                          <th className="text-left p-2">Select</th>
                          <th className="text-left p-2">First Name</th>
                          <th className="text-left p-2">Last Name</th>
                          <th className="text-left p-2">Email</th>
                        </tr>
                        </thead>
                        <tbody>
                        {nonEnrolledStudents.map((student) => (
                            <tr key={student.id} className="border-b hover:bg-gray-50">
                              <td className="p-2">
                                <input
                                    type="checkbox"
                                    checked={selectedStudents.includes(student.id)}
                                    onChange={(e) => {
                                      const updated = e.target.checked
                                          ? [...selectedStudents, student.id]
                                          : selectedStudents.filter((id) => id !== student.id);
                                      setSelectedStudents(updated);
                                    }}
                                />
                              </td>
                              <td className="p-2 text-gray-900">{student.firstname}</td>
                              <td className="p-2 text-gray-900">{student.lastname}</td>
                              <td className="p-2 text-gray-900">{student.mail}</td>
                            </tr>
                        ))}
                        </tbody>
                      </table>

                      <div className="flex justify-end mt-4 space-x-2">
                        <Button
                            onClick={async () => {
                              try {
                                await courseService.createMultipleEnrollments(courseId, selectedStudents);
                                alert("Students enrolled successfully!");
                                setShowStudentPicker(false);
                                setSelectedStudents([]);
                                // refresh enrollment list maybe
                              } catch (err) {
                                console.error("Enrollment failed", err);
                              }
                            }}
                            disabled={selectedStudents.length === 0}
                        >
                          Enroll Selected Students
                        </Button>
                        <Button variant="outline" onClick={() => setShowStudentPicker(false)}>
                          Cancel
                        </Button>
                      </div>
                    </CardContent>
                  </Card>
              )}


              {isEditing && (
                  <form
                      onSubmit={async (e) => {
                        e.preventDefault();
                        try {
                          await updateCourse();
                          alert("Course updated!");
                          setIsEditing(false);
                          // Tu peux faire un refresh des infos ici
                        } catch (err) {
                          console.error("Update failed", err);
                        }
                      }}
                      className="bg-gray-50 border p-4 rounded-lg space-y-4 mb-4"
                  >
                    <div>
                      <label className="block text-sm font-medium text-black">Title</label>
                      <input
                          type="text"
                          value={editData.title}
                          onChange={(e) => setEditData({ ...editData, title: e.target.value })}
                          className="mt-1 block w-full border px-3 py-2 rounded text-black"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-black">Description</label>
                      <textarea
                          value={editData.description}
                          onChange={(e) => setEditData({ ...editData, description: e.target.value })}
                          className="mt-1 block w-full border px-3 py-2 rounded text-black"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-black">Capacity</label>
                      <input
                          type="number"
                          value={editData.capacity}
                          onChange={(e) => setEditData({ ...editData, capacity: parseInt(e.target.value) })}
                          className="mt-1 block w-full border px-3 py-2 rounded text-black"
                      />
                    </div>

                    <div className="flex justify-end space-x-2">
                      <Button type="submit">Save</Button>
                      <Button variant="outline" type="button" onClick={() => setIsEditing(false)}>
                        Cancel
                      </Button>
                    </div>
                  </form>
              )}
              <div className="flex items-center space-x-6 text-sm text-gray-500">
                <div className="flex items-center">
                  <UserIcon className="h-4 w-4 mr-2" />
                </div>
                <div className="flex items-center">
                  <UsersIcon className="h-4 w-4 mr-2" />
                </div>
                <div className="flex items-center">
                  <BookOpenIcon className="h-4 w-4 mr-2" />
                </div>
              </div>
            </div>

            <div className="mt-6 lg:mt-0 lg:ml-6 flex flex-col space-y-2">
              {role === 'student' && (
                <>
                    <div className="flex items-center text-green-600">
                      <CheckCircleIcon className="h-5 w-5 mr-2" />
                      <span>Enrolled</span>
                    </div>

                </>
              )}
              {canEditCourse && (
                <div className="flex space-x-2">
                  <Button variant="outline" size="sm" onClick={() => setIsEditing(true)}>
                    <EditIcon className="h-4 w-4 mr-2" />
                    Edit Course
                  </Button>
                  <Button variant="destructive" size="sm" onClick={deleteCourse}>
                    <TrashIcon className="h-4 w-4 mr-2" />
                    Delete
                  </Button>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Course Content */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Main Content */}
          <div className="lg:col-span-2">
            <Card>
              <CardHeader>
                <div className="flex justify-between items-center">
                  <CardTitle>Course Content</CardTitle>
                  {canEditCourse && (
                      <Button size="sm" variant="outline" onClick={() => setShowAddModuleForm(prev => !prev)}>
                        <PlusIcon className="h-4 w-4 mr-2" />
                        {showAddModuleForm ? 'Cancel' : 'Add Module'}
                      </Button>
                  )}
                </div>
              </CardHeader>
              <CardContent>
                {showAddModuleForm && (
                    <form onSubmit={(e) => { e.preventDefault(); handleAddModule(); }} className="space-y-2 mb-4">
                      <input
                          type="text"
                          placeholder="Module title"
                          onChange={(e) => setNewModuleData({ ...newModuleData, title: e.target.value })}
                          className="w-full border px-3 py-2 rounded text-black"
                      />
                      <Button size="sm" type="submit" >
                        <PlusIcon className="h-4 w-4 mr-2" /> Create Module
                      </Button>
                    </form>
                )}
              </CardContent>
              <CardContent>
                {modules.length === 0 ? (
                  <div className="text-center py-8">
                    <BookOpenIcon className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                    <p className="text-gray-500">No modules available yet</p>
                  </div>
                ) : (
                  <div className="space-y-3">
                    {modules.map((module, index) => (
                      <div key={module.id} className="border rounded-lg">
                        <div
                            className="p-4 cursor-pointer hover:bg-gray-50 flex items-center justify-between"

                            onClick={() => {
                              toggleModule(module.id)
                              console.log(module.id)
                            }}
                        >
                          <div className="p-4 hover:bg-gray-50 flex items-center justify-between">
                            <div onClick={() => toggleModule(module.id)}
                                 className="cursor-pointer flex items-center flex-1">
                              <div
                                  className="flex-shrink-0 w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center mr-3">
                                <span className="text-blue-600 font-semibold text-sm">{index + 1}</span>
                              </div>
                              <div>
                                <h3 className="font-medium text-gray-900">{module.title}</h3>
                                <p className="text-sm text-gray-500">{module.description}</p>
                              </div>
                            </div>

                            {canEditCourse && (
                                <>
                                  <Button
                                      variant="ghost"
                                      size="icon"
                                      onClick={(e) => {
                                        e.stopPropagation(); // éviter d'expand le module
                                        setEditingModuleId(module.id);
                                        setEditedModuleTitle(module.title);
                                      }}
                                  >
                                    <EditIcon className="h-5 w-5 text-blue-600" />
                                  </Button>
                                  <Button
                                      variant="ghost"
                                      size="icon"
                                      onClick={async (e) => {
                                        e.stopPropagation();
                                        try {
                                          await moduleService.deleteModule(module.id);
                                          setModules(modules.filter(m => m.id !== module.id));
                                        } catch (err) {
                                          console.error('Failed to delete module:', err);
                                        }
                                      }}
                                  >
                                    <TrashIcon className="h-5 w-5 text-red-600" />
                                  </Button>
                                </>
                            )}
                          </div>

                          {expandedModules.includes(module.id) ? (
                              <ChevronDownIcon className="h-5 w-5 text-gray-400"/>
                          ) : (
                              <ChevronRightIcon className="h-5 w-5 text-gray-400"/>
                          )}
                        </div>
                        {editingModuleId === module.id && (
                            <div className="bg-white p-4 border-t">
                              <form
                                  onSubmit={async (e) => {
                                    e.preventDefault();
                                    try {
                                      const updated = await moduleService.updateModule(module.id, {title:editedModuleTitle});
                                      setModules(modules.map((m) =>
                                          m.id === module.id ? { ...m, title: updated.title } : m
                                      ));
                                      setEditingModuleId(null);
                                    } catch (err) {
                                      console.error("Failed to update module title:", err);
                                    }
                                  }}
                                  className="space-y-2"
                              >
                                <input
                                    type="text"
                                    value={editedModuleTitle}
                                    onChange={(e) => setEditedModuleTitle(e.target.value)}
                                    className="w-full border px-3 py-2 rounded text-black"
                                    placeholder="New module title"
                                />
                                <div className="flex justify-end space-x-2">
                                  <Button size="sm" type="submit">Save</Button>
                                  <Button
                                      size="sm"
                                      variant="outline"
                                      type="button"
                                      onClick={() => setEditingModuleId(null)}
                                  >
                                    Cancel
                                  </Button>
                                </div>
                              </form>
                            </div>
                        )}

                        {expandedModules.includes(module.id) && (

                            <div className="border-t bg-gray-50 p-4">
                              {canEditCourse && (
                                  <div className="flex justify-end mb-3">
                                    <Button
                                        size="sm"
                                        variant="outline"
                                        onClick={() => handleAddLesson(module.id)}
                                    >
                                      <PlusIcon className="h-4 w-4 mr-2" />
                                      Add Lesson
                                    </Button>
                                  </div>
                              )}

                              {(lessonsByModuleId[module.id]?.length ?? 0) === 0 ? (
                                  <p className="text-gray-500 text-sm">No lessons available</p>
                              ) : (
                                  <div className="space-y-2">
                                    {lessonsByModuleId[module.id].map((lesson, lessonIndex) => (
                                        <div key={lesson.id} className="flex items-center justify-between py-2">
                                          <div className="flex items-center">
                                            <PlayIcon className="h-4 w-4 text-blue-600 mr-3"/>
                                            <span className="text-sm font-medium text-gray-900">
                {lesson.description}
              </span>
                                          </div>
                                          {(isEnrolled || canEditCourse || role==='admin') && (
                                              <Button variant="solid" size="sm"
                                                     onClick={()=>inspectLesson(lesson.id)} className="bg-purple-600 hover:bg-purple-900 text-white font-semibold shadow-lg transition-colors duration-300"
                                              >
                                                View
                                              </Button>

                                          )}
                                        </div>
                                    ))}
                                  </div>
                              )}
                            </div>
                        )}

                      </div>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* Course Info */}
            <Card>
              <CardHeader>
                <CardTitle>Course Information</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div>
                  <h4 className="font-medium text-gray-900">User</h4>

                  <p className="text-sm text-gray-600">
                    {firstName} {Lastname}
                  </p>
                </div>

                <div>
                  <h4 className="font-medium text-gray-900">Enrollment</h4>
                  <p className="text-sm text-gray-600">
                    {enroll.length} students enrolled
                  </p>
                </div>

                <div>
                  <h4 className="font-medium text-gray-900">Capacity</h4>
                  <p className="text-sm text-gray-600">
                    {course.capacity} spots remaining
                  </p>
                </div>

                <div>
                  <h4 className="font-medium text-gray-900">Status</h4>
                  <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      course.capacity > 0 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  }`}>
                    {course.capacity > 0 ? 'Open for enrollment' : 'Enrollment closed'}
                  </span>
                </div>
              </CardContent>
            </Card>

            {/* Enrolled Students (for teachers) */}
            {canEditCourse && (
                <Card>
                  <CardHeader>
                    <CardTitle>Enrolled Students</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-2">
                      {enroll.map((enrollment) => (
                          <div key={enrollment.id} className="flex items-center justify-between">
                            <div className="flex items-center">
                              <div className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center mr-3">
                                <UserIcon className="h-4 w-4 text-gray-600"/>
                              </div>
                              <span className="text-sm font-medium text-black">
                {enrollment.firstname} {enrollment.lastname}
              </span>
                            </div>
                            <Button
                                variant="ghost"
                                size="icon"
                                onClick={async () => {
                                  try {
                                    await courseService.deleteEnrollmentByStudentId(enrollment.id, courseId);
                                    router.refresh();
                                  } catch (err) {
                                    console.error("Failed to unenroll student:", err);
                                  }
                                }}
                            >
                              <TrashIcon className="h-4 w-4 text-red-600"/>
                            </Button>
                          </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
            )}
            <div className="flex space-x-4 mt-6">
              {canEditCourse && (
                  <>
                    {!test && (
                        <button
                            onClick={() => router.push(`/tests/create/${courseId}`)}
                            className="flex items-center px-4 py-2 bg-white text-black hover:bg-gray-100 border border-gray-300 rounded-md transition-colors duration-300"
                        >
                          <PencilIcon className="h-5 w-5 mr-2"/>
                          Créer un test
                        </button>
                    )}

                    {test && (
                        <>
                          <button
                              onClick={() => router.push(`/tests/${test?.id}`)}
                              className="flex items-center px-4 py-2 bg-white text-black hover:bg-gray-100 border border-gray-300 rounded-md transition-colors duration-300"
                          >
                            <EyeIcon className="h-5 w-5 mr-2"/>
                            Voir le test
                          </button>

                          <button
                              onClick={async () => {
                                if (confirm("Voulez-vous vraiment supprimer ce test ?")) {
                                  try {
                                    await testService.deleteTest(test?.id);
                                    setTest(null);
                                    alert("Test supprimé");
                                  } catch (err) {
                                    console.error("Erreur suppression test", err);
                                  }
                                }
                              }}
                              className="flex items-center px-4 py-2 bg-red-600 hover:bg-red-800 text-white rounded-md transition-colors duration-300"
                          >
                            <TrashIcon className="h-5 w-5 mr-2"/>
                            Supprimer le test
                          </button>
                        </>
                    )}
                  </>
              )}

              {role === 'student' && test && (
                  <div className="flex space-x-4">
                    <button
                        onClick={() => router.push(`/tests/pass/${test?.id}`)}
                        className="flex items-center px-4 py-2 bg-green-600 hover:bg-green-900 text-white rounded-md transition-colors duration-300"
                    >
                      <CheckCircleIcon className="h-5 w-5 mr-2" />
                      Passer un test
                    </button>

                    {attemptions.length > 0 && (
                        <button onClick={()=>{router.push(`/attemptions/${test?.id}`)}}
                            className="flex items-center px-4 py-2 bg-white text-black border rounded-md hover:bg-gray-100 transition-colors duration-300"
                        >
                          <CheckCircleIcon className="h-5 w-5 mr-2" />
                          View Attemption
                        </button>
                    )}
                  </div>
              )}

            </div>
          </div>
        </div>
      </div>
    </Layout>
  )
}