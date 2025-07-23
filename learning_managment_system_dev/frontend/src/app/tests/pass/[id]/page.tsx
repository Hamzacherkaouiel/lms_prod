"use client";

import React, {useEffect, useRef, useState} from "react";
import { useParams, useRouter } from "next/navigation";
import Layout from "@/components/layout/Layout";
import {attemptService, testService} from "@/services/test-content";
import {Test, Question, Answer, TestAttempt} from "@/types";
import {authService} from "@/services/auth";

export default function AttemptTestPage() {
    const { id } = useParams();
    const router = useRouter();
    const testId = parseInt(id as string);
    const [test, setTest] = useState<Test | null>(null);
    const [selectedAnswers, setSelectedAnswers] = useState<Map<number, number>>(new Map());
    const [loading, setLoading] = useState(false);
    const user=authService.getUserMail()
    const role=authService.getUserRole()
    const hasFetchedRef = useRef(false);
    const[attemption,setAttemption]=useState<TestAttempt>(null)
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 3;

    useEffect(() => {
        if(role!='student'|| authService.isTokenExpired()) router.push("/login")
        if (hasFetchedRef.current) return;
        hasFetchedRef.current = true;
        async function fetchTest() {
            try {
                const data = await testService.startTest(testId);
                setTest(data);
                const response=await attemptService.createAttemption(testId,user)
                setAttemption(response)
            } catch (err) {
                console.error("Error :", err);
            }
        }
        fetchTest();
    }, [testId]);

    const handleOptionSelect = (questionId: number, answerId: number) => {
        setSelectedAnswers((prev) => new Map(prev.set(questionId, answerId)));
    };

    const  handleSubmit = async () => {
        const result: { [key: number]: number } = Object.fromEntries(selectedAnswers);
        await attemptService.submitTest(attemption.id,result)
        router.back()
    };

    if (!test) return <div>Chargement...</div>;

    const totalPages = Math.ceil(test.questions.length / pageSize);
    const startIndex = currentPage * pageSize;
    const currentQuestions = test.questions.slice(startIndex, startIndex + pageSize);

    return (
        <Layout>
            <div className="max-w-4xl mx-auto p-6 bg-white rounded shadow space-y-6">
                <h1 className="text-2xl font-bold text-black">{test.title}</h1>

                {currentQuestions.map((question) => (
                    <div key={question.id} className="border rounded p-4 space-y-2">
                        <p className="text-lg font-semibold text-black">{question.description}</p>

                        <ul className="space-y-1">
                            {question.options.map((option) => (
                                <li key={option.id} className="flex items-center space-x-2">
                                    <input
                                        type="radio"
                                        name={`question-${question.id}`}
                                        value={option.id}
                                        checked={selectedAnswers.get(question.id) === option.id}
                                        onChange={() => handleOptionSelect(question.id, option.id)}
                                    />
                                    <label className="text-black">{option.answer}</label>
                                </li>
                            ))}
                        </ul>
                    </div>
                ))}

                <div className="flex justify-between items-center pt-4">
                    <button
                        onClick={() => setCurrentPage((prev) => prev - 1)}
                        disabled={currentPage === 0}
                        className="px-4 py-2 bg-white border text-black rounded hover:bg-gray-100 disabled:opacity-50"
                    >
                        Preview
                    </button>

                    {currentPage < totalPages - 1 ? (
                        <button
                            onClick={() => setCurrentPage((prev) => prev + 1)}
                            className="px-4 py-2 bg-white border text-black rounded hover:bg-gray-100"
                        >
                            Next
                        </button>
                    ) : (
                        <button
                            onClick={handleSubmit}
                            disabled={loading}
                            className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
                        >
                            {loading ? "Soumission..." : "Submit test "}
                        </button>
                    )}
                </div>
            </div>
        </Layout>
    );
}
