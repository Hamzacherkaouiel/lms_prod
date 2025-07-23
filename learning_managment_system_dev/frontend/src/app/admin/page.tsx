"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { authService } from "@/services/auth";
import { userService } from "@/services/user";
import { TrashIcon, UserCircleIcon } from "@heroicons/react/24/solid";
import Layout from "@/components/layout/Layout";

export default function Teachers() {
    const router = useRouter();
    const role = authService.getUserRole();

    const isAdmin = role === "admin";
    const [admins, setAdmins] = useState([]);

    async function getAdmins() {
        const response = await userService.admin.getAdmins();
        setAdmins(response);
    }

    async function handleDelete(adminId: number) {
        if (confirm("Are you sure about that ?")) {
            await userService.admin.deleteUser(adminId);
            setAdmins((prev) => prev.filter((s: any) => s.id !== adminId));
        }
    }

    useEffect(() => {
        if (authService.isTokenExpired()) router.push("/login");
        getAdmins();
    }, []);

    return (
        <Layout>
            <div className="max-w-5xl mx-auto p-6">
                <h1 className="text-3xl font-bold mb-6 text-black">Liste des Admins</h1>

                <div className="grid gap-4">
                    {admins.map((admin: any) => (
                        <div
                            key={admin.id}
                            className="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition"
                        >
                            <div className="flex items-center space-x-4">
                                <UserCircleIcon className="h-10 w-10 text-gray-400" />

                                <div>
                                    <p className="text-lg font-semibold text-black">{admin.firstname}</p>
                                    <p className="text-sm text-gray-700">{admin.mail}</p>
                                </div>
                            </div>

                            {isAdmin && (
                                <button
                                    onClick={() => handleDelete(admin.id)}
                                    className="text-red-600 hover:text-red-800 transition"
                                    title="Supprimer"
                                >
                                    <TrashIcon className="h-5 w-5" />
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </Layout>
    );
}
