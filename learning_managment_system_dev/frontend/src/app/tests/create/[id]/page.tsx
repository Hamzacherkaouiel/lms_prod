"use client";

import React, {useEffect, useState} from "react";
import {useParams, useRouter} from "next/navigation";
import Layout from "@/components/layout/Layout";
import {testService} from "@/services/test-content";
import {authService} from "@/services/auth";

export default function CreateTestPage() {
    const params=useParams()
    const courseId = parseInt(params.id as string)
    const router = useRouter();
    const [title, setTitle] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    useEffect(() => {
        if (authService.isTokenExpired()) router.push("/login")
    }, []);

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        if (!title.trim()) {
            return;
        }

        setIsLoading(true);
        setError(null);

        try {
            let test={title:title}
            await testService.createTest(courseId,test);
            router.push("/dashboard");
        } catch (err) {
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <Layout>
            <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-md shadow-md">
                <h1 className="text-2xl font-semibold mb-6">Créer un nouveau test</h1>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="title" className="block mb-1 font-medium text-gray-700">
                            Titre du test
                        </label>
                        <input
                            id="title"
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            className="w-full border border-gray-300 rounded-md p-2 text-black focus:outline-none focus:ring-2 focus:ring-blue-600"
                            placeholder="Entrez le titre du test"
                            disabled={isLoading}
                        />
                    </div>

                    {error && <p className="text-red-600">{error}</p>}

                    <div className="flex justify-end space-x-2">
                        <button
                            type="button"
                            onClick={() => router.back()}
                            className="px-4 py-2 rounded-md border border-gray-300 text-black hover:bg-gray-100"
                            disabled={isLoading}
                        >
                            Annuler
                        </button>
                        <button
                            type="submit"
                            className="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50"
                            disabled={isLoading}
                        >
                            {isLoading ? "Création..." : "Créer"}
                        </button>
                    </div>
                </form>
            </div>
        </Layout>
    );
}
