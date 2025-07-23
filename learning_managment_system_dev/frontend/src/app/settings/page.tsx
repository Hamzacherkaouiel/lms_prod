'use client'

import { useEffect, useState } from "react"
import Layout from "@/components/layout/Layout"
import { authService } from "@/services/auth"
import { useRouter } from "next/navigation"
import { User } from "@/types"
import { Settings, Lock, User as UserIcon } from "lucide-react"
import UserInfoForm from "@/components/UserInfoForm"
import UpdatePasswordForm from "@/components/UpdatedPasswordForm"
import { userService } from "@/services/user"
import CreateUserForm from "@/components/CreateUserForm"

export default function SettingsPage() {
    const [activeTab, setActiveTab] = useState<"info" | "password" | "create">("info")
    const [user, setUser] = useState<User | null>(null)
    const router = useRouter()
    const role = authService.getUserRole()

    useEffect(() => {
        if (authService.isTokenExpired()) router.push("/login")
        async function fetchUser() {
            let response
            switch (role) {
                case "teacher":
                    response = await userService.teacher.getProfile()
                    break
                case "admin":
                    response = await userService.admin.getProfile()
                    break
                case "student":
                    response = await userService.student.getProfile()
                    break
            }
            setUser(response)
        }
        fetchUser()
    }, [])

    return (
        <Layout>
            <div className="max-w-5xl mx-auto mt-10 flex">
                {/* Sidebar */}
                <div className="w-1/4 pr-6 border-r">
                    <div className="space-y-2">
                        <button
                            className={`w-full flex items-center px-4 py-2 rounded ${
                                activeTab === "info"
                                    ? "bg-gray-200 text-black font-semibold"
                                    : "text-gray-600 hover:bg-gray-100"
                            }`}
                            onClick={() => setActiveTab("info")}
                        >
                            <UserIcon className="h-4 w-4 mr-2" />
                            User Information
                        </button>
                        <button
                            className={`w-full flex items-center px-4 py-2 rounded ${
                                activeTab === "password"
                                    ? "bg-gray-200 text-black font-semibold"
                                    : "text-gray-600 hover:bg-gray-100"
                            }`}
                            onClick={() => setActiveTab("password")}
                        >
                            <Lock className="h-4 w-4 mr-2" />
                            Update Password
                        </button>
                        {role === "admin" && (
                            <button
                                className={`w-full flex items-center px-4 py-2 rounded ${
                                    activeTab === "create"
                                        ? "bg-gray-200 text-black font-semibold"
                                        : "text-gray-600 hover:bg-gray-100"
                                }`}
                                onClick={() => setActiveTab("create")}
                            >
                                <Settings className="h-4 w-4 mr-2" />
                                Create User
                            </button>
                        )}
                    </div>
                </div>

                {/* Main Content */}
                <div className="w-3/4 pl-6">
                    {activeTab === "info" && user && <UserInfoForm user={user} />}
                    {activeTab === "password" && <UpdatePasswordForm />}
                    {activeTab === "create" && role === "admin" && <CreateUserForm />}
                </div>
            </div>
        </Layout>
    )
}
