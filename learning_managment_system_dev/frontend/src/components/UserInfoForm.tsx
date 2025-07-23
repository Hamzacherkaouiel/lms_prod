'use client'

import { useState } from "react"
import { User } from "@/types"
import {userService} from "@/services/user";
import {authService} from "@/services/auth";
import {router} from "next/client";

export default function UserInfoForm({ user }: { user: User }) {
    const [form, setForm] = useState<User>({ ...user })
    const [success, setSuccess] = useState(false)
    const role=authService.getUserRole()

    const handleChange = (e: any) => {
        setForm({ ...form, [e.target.name]: e.target.value })
    }

    const handleSubmit = async () => {
        switch (role){
            case "teacher":
                await userService.teacher.updateProfile(form.id,form)
                break;
            case "admin":
                await userService.admin.updateProfile(form.id,form)
                break;
            default:
                await userService.student.updateProfile(form.id,form)
                break
        }
        setSuccess(true)
    }

    return (
        <div className="space-y-4">
            <h2 className="text-xl font-semibold text-black">Edit Profile</h2>
            <input name="firstname" value={form.firstname} onChange={handleChange} className="w-full border rounded p-2 text-black" placeholder="First Name" />
            <input name="lastname" value={form.lastname} onChange={handleChange} className="w-full border rounded p-2 text-black" placeholder="Last Name" />
            <input name="email" value={form.mail} readOnly className="w-full border rounded p-2 text-gray-500 bg-gray-100" />

            {role === "teacher" && (
                <input name="speciality" value={form.speciality || ""} onChange={handleChange} className="w-full border rounded p-2 text-black" placeholder="Speciality" />
            )}

            <button onClick={handleSubmit} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">Save Changes</button>

            {success && <p className="text-green-600">Profile updated successfully!</p>}
        </div>
    )
}
