'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import Layout from '@/components/layout/Layout'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Textarea } from '@/components/ui/Textarea'
import {courseService} from "@/services/course";
import {authService} from "@/services/auth";

export default function CreateCoursePage() {
    const user=authService.getUserMail()
    const router = useRouter()
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        capacity: 0,
    })

    const [isSubmitting, setIsSubmitting] = useState(false)

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target
        setFormData(prev => ({
            ...prev,
            [name]: name === 'capacity' ? parseInt(value) : value,
        }))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsSubmitting(true)

        try {
            await courseService.createCourseByEmail(formData,user)
            alert('Course created!')

            router.push('/courses')
        } catch (error) {
            alert('Failed to create course')
            console.error(error)
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <Layout>
            <div className="max-w-3xl mx-auto p-6">
                <h1 className="text-2xl font-bold mb-6 text-black">Create a New Course</h1>

                <form onSubmit={handleSubmit} className="space-y-6 bg-white p-6 rounded-lg shadow">
                    <div>
                        <label htmlFor="title" className="block text-black font-medium mb-1">
                            Title
                        </label>
                        <Input
                            id="title"
                            name="title"
                            type="text"
                            value={formData.title}
                            onChange={handleChange}
                            required
                            className="text-black"
                        />
                    </div>

                    <div>
                        <label htmlFor="description" className="block text-black font-medium mb-1">
                            Description
                        </label>
                        <Textarea
                            id="description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            required
                            rows={4}
                        />
                    </div>

                    <div>
                        <label htmlFor="capacity" className="block text-black font-medium mb-1">
                            Capacity
                        </label>
                        <Input
                            id="capacity"
                            name="capacity"
                            type="number"
                            min={0}
                            value={formData.capacity}
                            onChange={handleChange}
                            required
                            className="text-black"
                        />
                    </div>

                    <div className="flex justify-end">
                        <Button type="submit" disabled={isSubmitting}>
                            {isSubmitting ? 'Creating...' : 'Create Course'}
                        </Button>
                    </div>
                </form>
            </div>
        </Layout>
    )
}
