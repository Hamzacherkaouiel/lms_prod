'use client'

import React, { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card'
import { authService } from '@/services/auth'
import { EyeIcon, EyeOffIcon, LockIcon, MailIcon, UserIcon } from 'lucide-react'
import { UserCreation } from '@/types'

export default function CreateUserForm() {
    const [formData, setFormData] = useState<UserCreation>({
        operation: 'CREATE_USER',
        firstname: '',
        lastname: '',
        mail: '',
        password: '',
        role: 'student',
        speciality: ""
    })

    const [confirmPassword, setConfirmPassword] = useState('')
    const [showPassword, setShowPassword] = useState(false)
    const [showConfirmPassword, setShowConfirmPassword] = useState(false)
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')
    const router = useRouter()

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target
        setFormData(prev => ({ ...prev, [name]: value }))
    }

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsLoading(true)
        setError('')

        if (formData.password !== confirmPassword) {
            setError('Passwords do not match')
            setIsLoading(false)
            return
        }

        if (formData.password.length < 6) {
            setError('Password must be at least 6 characters long')
            setIsLoading(false)
            return
        }

        try {
            await authService.createUser(formData)
            router.push('/settings?created=true')
        } catch (err) {
            setError('Failed to create user. Please try again.')
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <div className="w-full">
            <Card>
                <CardHeader>
                    <CardTitle className="text-center">Create New User</CardTitle>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleRegister} className="space-y-6">
                        {error && (
                            <div className="rounded-md bg-red-50 p-4">
                                <div className="text-sm text-red-700">{error}</div>
                            </div>
                        )}

                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label htmlFor="firstname" className="block text-sm font-medium text-gray-700">
                                    First Name
                                </label>
                                <Input
                                    id="firstname"
                                    name="firstname"
                                    required
                                    value={formData.firstname}
                                    className="text-black"
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div>
                                <label htmlFor="lastname" className="block text-sm font-medium text-gray-700">
                                    Last Name
                                </label>
                                <Input
                                    id="lastname"
                                    name="lastname"
                                    required
                                    value={formData.lastname}
                                    className="text-black"
                                    onChange={handleInputChange}
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="mail" className="block text-sm font-medium text-gray-700">
                                Email address
                            </label>
                            <div className="relative mt-1">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <MailIcon className="h-5 w-5 text-gray-400" />
                                </div>
                                <Input
                                    id="mail"
                                    name="mail"
                                    type="email"
                                    required
                                    className="pl-10 text-black"
                                    value={formData.mail}
                                    onChange={handleInputChange}
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="role" className="block text-sm font-medium text-gray-700">
                                Role
                            </label>
                            <select
                                id="role"
                                name="role"
                                value={formData.role}
                                onChange={handleInputChange}

                                className="text-black block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm"
                            >
                                <option value="student">Student</option>
                                <option value="teacher">Teacher</option>
                                <option value="admin">Admin</option>
                            </select>
                        </div>

                        {formData.role === 'teacher' && (
                            <div>
                                <label htmlFor="specialty" className="block text-sm font-medium text-gray-700">
                                    Specialty
                                </label>
                                <Input
                                    id="specialty"
                                    name="speciality"
                                    required
                                    className="text-black"
                                    value={formData.speciality}
                                    onChange={handleInputChange}
                                />
                            </div>
                        )}

                        <div>
                            <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                                Password
                            </label>
                            <div className="relative mt-1">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <LockIcon className="h-5 w-5 text-gray-400" />
                                </div>
                                <Input
                                    id="password"
                                    name="password"
                                    type={showPassword ? 'text' : 'password'}
                                    required
                                    className="pl-10 pr-10 text-black"
                                    value={formData.password}
                                    onChange={handleInputChange}
                                />
                                <button
                                    type="button"
                                    className="absolute inset-y-0 right-0 pr-3 flex items-center"
                                    onClick={() => setShowPassword(!showPassword)}
                                >
                                    {showPassword ? <EyeOffIcon className="h-5 w-5" /> : <EyeIcon className="h-5 w-5" />}
                                </button>
                            </div>
                        </div>

                        <div>
                            <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700">
                                Confirm Password
                            </label>
                            <div className="relative mt-1">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <LockIcon className="h-5 w-5 text-gray-400" />
                                </div>
                                <Input
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    type={showConfirmPassword ? 'text' : 'password'}
                                    required
                                    className="pl-10 pr-10 text-black"

                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                />
                                <button
                                    type="button"
                                    className="absolute inset-y-0 right-0 pr-3 flex items-center"
                                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                >
                                    {showConfirmPassword ? <EyeOffIcon className="h-5 w-5" /> : <EyeIcon className="h-5 w-5" />}
                                </button>
                            </div>
                        </div>

                        <Button type="submit" className="w-full" disabled={isLoading}>
                            {isLoading ? 'Creating user...' : 'Create User'}
                        </Button>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}
