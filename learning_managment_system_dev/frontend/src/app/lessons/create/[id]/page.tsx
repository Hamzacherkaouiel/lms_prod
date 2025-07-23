'use client'

import {useEffect, useState} from 'react'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/Card'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Textarea } from '@/components/ui/Textarea'
import { UploadIcon } from 'lucide-react'
import {useParams} from "next/navigation";
import {lessonService} from "@/services/lesson";
import Layout from "@/components/layout/Layout";
import {authService} from "@/services/auth";
import {router} from "next/client";
export default function AddLessonForm() {
    const params = useParams();
    const moduleId = params.id as string;
    console.log("Module ID:", moduleId);
    const [description, setDescription] = useState('')
    const [file, setFile] = useState<File | null>(null)
    const [loading, setLoading] = useState(false)
    useEffect(() => {
        if (authService.isTokenExpired()) router.push("/login")
    }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if (!file || !description) {
            alert('All fields are required')
            return
        }

        setLoading(true)
        try {
            console.log(moduleId,file.name)
            const lesson={description:description,contentType:file.type}
            const res = await lessonService.createLesson(lesson,moduleId,file.name)

            if (!res.ok) throw new Error('Lesson creation failed')

            const { uploadUrl } = await res.json()
            const upload = await lessonService.uploadLessonVideo(uploadUrl,file)

            if (!upload.ok) throw new Error('File upload failed')

            alert('Lesson created and file uploaded successfully!')
            setDescription('')
            setFile(null)

        } catch (err) {
            console.error(err)
            alert('Error occurred while uploading lesson.')
        } finally {
            setLoading(false)
        }
    }
    return (
        <Layout>
        <div className="max-w-2xl mx-auto mt-10">
            <Card>
                <CardHeader>
                    <CardTitle>Create a New Lesson</CardTitle>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Description</label>
                            <Textarea
                                placeholder="Lesson description..."
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                className="mt-1 text-black"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Upload File</label>
                            <Input
                                type="file"
                                accept=".mp4,.pdf,.png,.jpg,.jpeg"
                                onChange={(e) => setFile(e.target.files?.[0] || null)}
                            />
                            {file && <p className="text-sm text-gray-600 mt-1">Selected: {file.name}</p>}
                        </div>

                        <div className="flex justify-end">
                            <Button type="submit" disabled={loading}>
                                <UploadIcon className="w-4 h-4 mr-2" />
                                {loading ? 'Uploading...' : 'Create Lesson'}
                            </Button>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
        </Layout>
    )
}
