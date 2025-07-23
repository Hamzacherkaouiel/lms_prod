'use client'

import { useState } from "react"
import {userService} from "@/services/user";
import {authService} from "@/services/auth";
import {useRouter} from "next/navigation";
export default function UpdatePasswordForm() {
    const [oldPassword, setOldPassword] = useState("")
    const [newPassword, setNewPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [error, setError] = useState("")
    const [success, setSuccess] = useState(false)
    const role=authService.getUserRole();
    const mail=authService.getUserMail()
    const router=useRouter()
    const handleSubmit = async () => {
        if (newPassword !== confirmPassword) {
            setError("Passwords do not match")
            return
        }

        try {
            let userCreation= {mail:mail,password:newPassword,operation:"UPDATE_PASSWORD"};
            switch (role){
                case "teacher":
                    await userService.teacher.updatePassword(userCreation);
                    break
                case "admin":
                    await userService.admin.updatePassword(userCreation)
                    break
                default:
                    break
            }
            setSuccess(true)
            authService.logout()
            router.push("/login")
            setError("")
        } catch (err) {
            setError("Failed to update password")
        }
    }

    return (
        <div className="space-y-4">
            <h2 className="text-xl font-semibold text-black">Update Password</h2>

            <input type="password" value={oldPassword} onChange={(e) => setOldPassword(e.target.value)} className="w-full border rounded p-2 text-black" placeholder="Old Password" />
            <input type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} className="w-full border rounded p-2 text-black" placeholder="New Password" />
            <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} className="w-full border rounded p-2 text-black" placeholder="Confirm New Password" />

            <button onClick={handleSubmit} className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">Update</button>

            {error && <p className="text-red-600">{error}</p>}
            {success && <p className="text-green-600">Password updated!</p>}
        </div>
    )
}
