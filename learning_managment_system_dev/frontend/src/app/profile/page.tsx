'use client'

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card"
import { UserIcon, MailIcon } from "lucide-react"
import { useEffect, useState } from "react"
import Layout from "@/components/layout/Layout"
import { User } from "@/types"
import { authService } from "@/services/auth"
import { useRouter } from "next/navigation"
import {userService} from "@/services/user";

export default function UserProfilePage() {
    const [userProfile, setUserProfile] = useState<User | null>(null)
    const router = useRouter()
    const role=authService.getUserRole()
    useEffect(() => {
        if (authService.isTokenExpired()) {
            router.push("/login")
            return
        }

        async function fetchProfile() {
            let response;
            try {
                switch (role) {
                    case "teacher":
                        response = await userService.teacher.getProfile();
                        break;
                    case "admin":
                        response= await userService.admin.getProfile()
                        break
                    default:
                        response=await userService.student.getProfile()
                }
                setUserProfile(response);
            } catch (err) {
                console.error("Erreur lors du chargement du profil :", err)
            }
        }

        fetchProfile()
    }, [])

    if (!userProfile) {
        return (
            <Layout>
                <div className="max-w-2xl mx-auto mt-8 text-center text-gray-600">Chargement du profil...</div>
            </Layout>
        )
    }

    return (
        <Layout>
            <div className="max-w-2xl mx-auto mt-10 bg-white rounded-lg shadow-md p-8">
                {/* Avatar */}
                <div className="flex flex-col items-center mb-6">
                    <div className="w-24 h-24 rounded-full bg-gray-200 flex items-center justify-center text-gray-500 text-4xl">
                        <UserIcon className="h-12 w-12" />
                    </div>
                    {/* User Role */}
                    <p className="mt-3 text-center text-black font-semibold capitalize">
                        {role}
                    </p>
                </div>

                {/* User Info */}
                <div className="space-y-4 text-black">
                    <div>
                        <label className="block text-sm font-medium mb-1">First Name</label>
                        <input
                            type="text"
                            value={userProfile.firstname}
                            readOnly
                            className="w-full border border-gray-300 rounded p-2 bg-gray-100 text-black"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium mb-1">Last Name</label>
                        <input
                            type="text"
                            value={userProfile.lastname}
                            readOnly
                            className="w-full border border-gray-300 rounded p-2 bg-gray-100 text-black"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium mb-1">Email</label>
                        <input
                            type="email"
                            value={userProfile.mail}
                            readOnly
                            className="w-full border border-gray-300 rounded p-2 bg-gray-100 text-black"
                        />
                    </div>

                    {role === "teacher" && (
                        <div>
                            <label className="block text-sm font-medium mb-1">Speciality</label>
                            <input
                                type="text"
                                value={userProfile.speciality || ""}
                                readOnly
                                className="w-full border border-gray-300 rounded p-2 bg-gray-100 text-black"
                            />
                        </div>
                    )}
                </div>
            </div>
        </Layout>
    )

}
