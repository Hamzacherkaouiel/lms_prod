"use client";

import React, { useEffect, useState } from "react";
import Layout from "@/components/layout/Layout";
import { attemptService, testService } from "@/services/test-content";
import { TestAttempt, Test } from "@/types";
import {useParams, useRouter} from "next/navigation";
import { EyeIcon } from "@heroicons/react/24/outline";
import {authService} from "@/services/auth";
import {ArrowLeftIcon} from "lucide-react";

export default function AttemptionsPage() {
    const params = useParams()
    const testId = parseInt(params.id as string)
    const router = useRouter();
    const role = authService.getUserRole();
    const userEmail = authService.getUserMail();
    const [attemptions, setAttemptions] = useState<TestAttempt[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (role !== "student" || authService.isTokenExpired()) {
            router.push("/login");
            return;
        }

        async function fetchAttemptions() {
            try {
                const data = await attemptService.getAttemptionofTest(userEmail,testId);
                setAttemptions(data);
            } catch (error) {
                console.error("Erreur lors du chargement des tentatives :", error);
            } finally {
                setLoading(false);
            }
        }

        fetchAttemptions();
    }, []);

    return (
        <Layout>
            <div className="max-w-4xl mx-auto p-6 bg-white rounded shadow space-y-6">
                <h1 className="text-2xl font-bold text-black">My Attemptions</h1>

                {loading ? (
                    <p className="text-gray-500">Chargement...</p>
                ) : attemptions.length === 0 ? (
                    <p className="text-gray-500">Aucune tentative trouvée.</p>
                ) : (
                    <ul className="space-y-4">
                        {attemptions.map((attemption) => (
                            <li
                                key={attemption.id}
                                className="border rounded p-4 shadow hover:shadow-md transition"
                            >
                                <div className="flex justify-between items-center">
                                    <div>

                                        <p className="text-gray-700">Message : {attemption.message}</p>
                                        <p className="text-gray-700">
                                            Score :{" "}
                                            <span className="font-semibold">
                        {attemption.score}/{attemption.max_score}
                      </span>
                                        </p>
                                    </div>
                                    <button
                                        onClick={() => router.back()}
                                        className="flex items-center px-4 py-2 border border-gray-300 rounded-md text-black hover:bg-gray-100 transition-colors duration-300"
                                    >
                                        <ArrowLeftIcon className="h-5 w-5 mr-2"/>
                                        Back to the main page
                                    </button>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </Layout>
    );
}
