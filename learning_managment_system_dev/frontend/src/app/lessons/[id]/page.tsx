"use client";

import React, { useEffect, useState } from "react";
import { PencilIcon, TrashIcon } from "@heroicons/react/24/outline";
import { authService } from "@/services/auth";
import { Lesson } from "@/types";
import { useParams, useRouter } from "next/navigation";
import { lessonService } from "@/services/lesson";
import Layout from "@/components/layout/Layout";

export default function LessonDetail() {
    const role = authService.getUserRole();
    const [lesson, setLesson] = useState<Lesson>();
    const isTeacher = role === "teacher";
    const params = useParams();
    const router = useRouter();
    const lessonId = params.id as string;
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
    const [showForm, setShowForm] = useState(false);
    const [newDesc, setNewDesc] = useState("");

    async function getLesson() {
        const response = await lessonService.getFullLesson(lessonId);
        setLesson(response);
        setNewDesc(response.description);
    }

    useEffect(() => {
        if (authService.isTokenExpired()) router.push("/login")
        getLesson();
    }, [lessonId]);

    async function onEdit() {
        if (!newDesc.trim()) return;
        const updatedLesson={description:newDesc}
        await lessonService.updateLesson(lessonId, updatedLesson);
        await getLesson();
    }

    async function onDelete() {
        await lessonService.deleteLesson(lessonId);
        router.push("/dashboard");
    }

    return (
        <Layout>
            <div className="max-w-4xl mx-auto p-6 bg-white rounded-md shadow-md">
                <h1 className="text-2xl font-semibold mb-4">Détail de la leçon</h1>
                <p className="mb-6 text-gray-700">{lesson?.description}</p>

                <div className="mb-6">
                    {lesson?.contentType?.startsWith("video/") && (
                        <video controls className="w-full rounded-md shadow">
                            <source src={lesson?.s3Url} type={lesson?.contentType} />
                            Your browser does not support the video tag.
                        </video>
                    )}

                    {lesson?.contentType?.startsWith("image/") && (
                        <img src={lesson?.s3Url} alt="Lesson content" className="w-full rounded-md shadow" />
                    )}

                    {lesson?.contentType === "application/pdf" && (
                        <iframe
                            src={lesson?.s3Url}
                            title="Lesson PDF"
                            className="w-full h-[600px] rounded-md shadow"
                        />
                    )}
                </div>

                {isTeacher && (
                    <div className="flex space-x-4">
                        <button
                            onClick={() => setShowForm(true)}
                            className="flex items-center px-4 py-2 bg-purple-600 hover:bg-purple-900 text-white rounded-md transition-colors duration-300"
                        >
                            <PencilIcon className="h-5 w-5 mr-2"/>
                            Modifier
                        </button>

                        <button
                            onClick={() => setShowDeleteConfirm(true)}
                            className="flex items-center px-4 py-2 bg-red-600 hover:bg-red-800 text-white rounded-md transition-colors duration-300"
                        >
                            <TrashIcon className="h-5 w-5 mr-2"/>
                            Supprimer
                        </button>

                    </div>
                )}

                {showForm && (
                    <div className="mt-6 p-4 border border-gray-300 rounded-lg shadow-sm bg-white">
                        <h2 className="text-lg font-semibold text-black mb-4">
                        Modifier la description
                        </h2>

                        <textarea
                            value={newDesc ?? ""}
                            onChange={(e) => setNewDesc(e.target.value)}
                            className="w-full border border-gray-400 rounded-md p-2 mb-4 text-black focus:outline-none focus:ring-1 focus:ring-purple-500"
                            rows={4}
                            placeholder="Nouvelle description..."
                        />

                        <div className="flex justify-end space-x-2">
                            <button
                                onClick={() => setShowForm(false)}
                                className="px-4 py-2 text-sm bg-gray-700 hover:bg-gray-800 text-white rounded-md"
                            >
                                Annuler
                            </button>

                            <button
                                onClick={async () => {
                                    await onEdit(); // tu dois y inclure l'appel update
                                    setShowForm(false);
                                }}
                                className="px-4 py-2 text-sm bg-purple-600 hover:bg-purple-800 text-white rounded-md"
                            >
                                Sauvegarder
                            </button>
                        </div>
                    </div>
                )}
                {showDeleteConfirm && (
                    <div className="mt-4 p-4 border border-red-400 rounded-lg bg-red-50">
                        <p className="text-sm text-red-700 mb-4">
                            are you sure?
                        </p>

                        <div className="flex justify-end space-x-2">
                            <button
                                onClick={() => setShowDeleteConfirm(false)}
                                className="px-4 py-2 text-sm bg-gray-700 hover:bg-gray-800 text-white rounded-md"
                            >
                                Annuler
                            </button>

                            <button
                                onClick={async () => {
                                    await onDelete();
                                    setShowDeleteConfirm(false);
                                }}
                                className="px-4 py-2 text-sm bg-red-600 hover:bg-red-800 text-white rounded-md"
                            >
                                Confirmer
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </Layout>
    );
}
