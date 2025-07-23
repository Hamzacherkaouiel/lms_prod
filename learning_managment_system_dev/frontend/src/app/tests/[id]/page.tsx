"use client";

import React, { useEffect, useState } from "react";
import Layout from "@/components/layout/Layout";
import { useParams, useRouter } from "next/navigation";
import { answerService, questionService, testService } from "@/services/test-content";
import { PencilIcon, TrashIcon, PlusIcon, CheckIcon } from "@heroicons/react/24/outline";
import { Answer, Question, Test } from "@/types";
import {authService} from "@/services/auth";

export default function TestDetails() {
    const { id } = useParams();
    const testId = parseInt(id as string);
    const router = useRouter();
    const [test, setTest] = useState<Test | null>(null);
    const [isEditingTitle, setIsEditingTitle] = useState(false);
    const [titleInput, setTitleInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [showQuestionForm, setShowQuestionForm] = useState(false);
    const [editingQuestion, setEditingQuestion] = useState<Question | null>(null);
    const [questionDescription, setQuestionDescription] = useState("");
    const [questionScore, setQuestionScore] = useState<number>(1);
    const [questions, setQuestions] = useState<Question[]>([]);
    const [showOptionForm, setShowOptionForm] = useState(false);
    const [editingOption, setEditingOption] = useState<Answer | null>(null);
    const [optionText, setOptionText] = useState("");
    const [optionIsCorrect, setOptionIsCorrect] = useState<boolean>(false);
    const [parentQuestionIdForOption, setParentQuestionIdForOption] = useState<number | null>(null);

    useEffect(() => {
        if (authService.isTokenExpired()) router.push("/login")
        async function loadTest() {
            try {
                const data = await testService.getTestById(testId);
                setTest(data);
                setTitleInput(data.title);
                const questionresponse = await questionService.getQuestionsByTestId(testId);
                setQuestions(questionresponse);
            } catch (error) {
                console.error("Failed to load test:", error);
            }
        }
        loadTest();
    }, [testId]);

    async function handleTitleSave() {
        if (!test) return;
        setLoading(true);
        try {
            const updatedTest={title:titleInput}
            await testService.updateTest(testId,updatedTest)
            const response=await testService.getTestById(testId)
            setTest(response)
            setIsEditingTitle(false);
        } catch (error) {
            console.error("Failed to update title", error);
        } finally {
            setLoading(false);
        }
    }

    function openEditQuestionForm(q: Question) {
        setEditingQuestion(q);
        setQuestionDescription(q.description);
        setQuestionScore(q.scoreQuestion);
        setShowQuestionForm(true);
    }

    async function saveQuestion() {
        if (!test) return;
        setLoading(true);
        try {
            if (editingQuestion) {
                const request = { description: questionDescription, scoreQuestion: questionScore };
                const updatedQuestion = await questionService.updateQuestion(editingQuestion.id, request);
                setQuestions(prev => prev.map(q => q.id === updatedQuestion.id ? updatedQuestion : q));
            }
            setShowQuestionForm(false);
        } catch (error) {
            console.error("Failed to save question", error);
        } finally {
            setLoading(false);
        }
    }

    async function deleteQuestion(questionId: number) {
        if (!test) return;
        setLoading(true);
        try {
            await questionService.deleteQuestion(questionId);
            setQuestions(prev => prev.filter(q => q.id !== questionId));
        } catch (error) {
            console.error("Failed to delete question", error);
        } finally {
            setLoading(false);
        }
    }

    function openNewOptionForm(questionId: number) {
        setEditingOption(null);
        setOptionText("");
        setOptionIsCorrect(false);
        setParentQuestionIdForOption(questionId);
        setShowOptionForm(true);
    }

    function openEditOptionForm(option: Answer, questionId: number) {
        setEditingOption(option);
        setOptionText(option.answer);
        setOptionIsCorrect(!option.isfalse);
        setParentQuestionIdForOption(questionId);
        setShowOptionForm(true);
    }

    async function saveOption() {
        if (parentQuestionIdForOption == null) return;
        setLoading(true);
        try {
            const option = {
                answer: optionText,
                isfalse: !optionIsCorrect,
            };

            if (editingOption) {
                await answerService.updateOption(editingOption.id, option);
                const updatedQuestions = await questionService.getQuestionsByTestId(testId);
                setQuestions(updatedQuestions);
            } else {
                await answerService.createOption(parentQuestionIdForOption, option);
                const updatedQuestions = await questionService.getQuestionsByTestId(testId);
                setQuestions(updatedQuestions);
            }

            setShowOptionForm(false);
            setOptionText("");
            setOptionIsCorrect(false);
            setEditingOption(null);
            setParentQuestionIdForOption(null);

        } catch (error) {
            console.error("Failed to save option", error);
        } finally {
            setLoading(false);
        }
    }



    async function deleteOption(optionId: number) {
        if (!test) return;
        setLoading(true);
        try {
            await answerService.deleteOption(optionId)
            const updatedQuestions = await questionService.getQuestionsByTestId(testId);
            setQuestions(updatedQuestions);
        } catch (error) {
            console.error("Failed to delete option", error);
        } finally {
            setLoading(false);
        }
    }

    if (!test) return <div>Loading...</div>;

    return (
        <Layout>
            <div className="max-w-5xl mx-auto p-6 bg-white rounded shadow space-y-6">
                <div className="flex items-center space-x-3">
                    {isEditingTitle ? (
                        <>
                            <input
                                type="text"
                                className="border p-2 rounded text-black flex-grow"
                                value={titleInput}
                                onChange={(e) => setTitleInput(e.target.value)}
                                disabled={loading}
                            />
                            <button
                                onClick={handleTitleSave}
                                className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
                                disabled={loading}
                            >
                                Save
                            </button>
                            <button
                                onClick={() => setIsEditingTitle(false)}
                                className="px-3 py-1 border rounded hover:bg-gray-100 text-black"
                                disabled={loading}
                            >
                                Cancel
                            </button>
                        </>
                    ) : (
                        <>
                            <h1 className="text-3xl font-bold text-black flex-grow">{test.title}</h1>
                            <button
                                onClick={() => setIsEditingTitle(true)}
                                className="p-2 rounded hover:bg-gray-100"
                                title="Edit Title"
                            >
                                <PencilIcon className="h-6 w-6 text-gray-600" />
                            </button>
                        </>
                    )}
                </div>

                <div>
                    <div className="flex justify-between items-center mb-4">
                        <h2 className="text-xl font-semibold text-black">Questions</h2>
                        <button
                            onClick={() => router.push(`/questions/create/${testId}`)}
                            className="flex items-center bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
                        >
                            <PlusIcon className="h-5 w-5 mr-1" /> Ajouter une question
                        </button>
                    </div>

                    {questions.length === 0 ? (
                        <p className="text-gray-500">Aucune question pour ce test.</p>
                    ) : (
                        <div className="space-y-4">
                            {questions.map((question) => (
                                <div key={question.id} className="border p-4 rounded shadow-sm">
                                    <div className="flex justify-between items-center">
                                        <div>
                                            <p className="text-lg font-semibold text-black">{question.description}</p>
                                            <p className="text-sm text-gray-500">Score : {question.scoreQuestion}</p>
                                        </div>
                                        <div className="flex space-x-2">
                                            <button
                                                onClick={() => openEditQuestionForm(question)}
                                                className="p-1 rounded hover:bg-gray-100 text-black"
                                                title="Edit Question"
                                            >
                                                <PencilIcon className="h-5 w-5 text-gray-600" />
                                            </button>
                                            <button
                                                onClick={() => deleteQuestion(question.id)}
                                                className="p-1 rounded hover:bg-red-100 text-black"
                                                title="Delete Question"
                                            >
                                                <TrashIcon className="h-5 w-5 text-red-600" />
                                            </button>
                                        </div>
                                    </div>

                                    <div className="mt-3 ml-4 space-y-2">
                                        <div className="flex justify-between items-center">
                                            <h3 className="font-semibold text-black">Options</h3>
                                            <button
                                                onClick={() => openNewOptionForm(question.id)}
                                                className="flex items-center bg-green-600 text-white px-2 py-0.5 rounded hover:bg-green-700 text-sm"
                                            >
                                                <PlusIcon className="h-4 w-4 mr-1" /> Ajouter une option
                                            </button>
                                        </div>
                                        {question.options.length === 0 ? (
                                            <p className="text-gray-500 text-sm">Aucune option pour cette question.</p>
                                        ) : (
                                            <ul className="list-disc list-inside text-black">
                                                {question.options.map((option) => (
                                                    <li key={option.id} className="flex justify-between items-center">
                        <span>
                          {option.answer} {!option.isfalse && <CheckIcon className="inline h-4 w-4 text-green-600" />}
                        </span>
                                                        <div className="flex space-x-2">
                                                            <button
                                                                onClick={() => {
                                                                    setEditingOption(option);
                                                                    setOptionText(option.answer);
                                                                    setOptionIsCorrect(!option.isfalse);
                                                                    setParentQuestionIdForOption(question.id); // important pour le ciblage
                                                                    setShowOptionForm(true);
                                                                }}
                                                                className="p-1 rounded hover:bg-gray-100 text-black"
                                                                title="Modifier l'option"
                                                            >
                                                                <PencilIcon className="h-4 w-4 text-gray-600" />
                                                            </button>
                                                            <button
                                                                onClick={() => deleteOption(option.id)}
                                                                className="p-1 rounded hover:bg-red-100 text-black"
                                                                title="Delete Option"
                                                            >
                                                                <TrashIcon className="h-4 w-4 text-red-600" />
                                                            </button>
                                                        </div>
                                                    </li>
                                                ))}
                                            </ul>
                                        )}

                                        {showOptionForm && parentQuestionIdForOption === question.id && (
                                            <div className="border mt-4 p-4 rounded bg-gray-50 space-y-4">
                                                <h3 className="text-sm font-semibold text-black">
                                                    {editingOption ? "Modifier l'option" : "Nouvelle option"}
                                                </h3>
                                                <input
                                                    type="text"
                                                    value={optionText}
                                                    onChange={(e) => setOptionText(e.target.value)}
                                                    className="w-full border border-gray-300 rounded p-2 text-black"
                                                    placeholder="Texte de l'option"
                                                />
                                                <label className="inline-flex items-center text-black">
                                                    <input
                                                        type="checkbox"
                                                        checked={optionIsCorrect}
                                                        onChange={() => setOptionIsCorrect(!optionIsCorrect)}
                                                        className="mr-2"
                                                    />
                                                    Option correcte
                                                </label>
                                                <div className="flex justify-end space-x-2">
                                                    <button
                                                        onClick={() => {
                                                            setShowOptionForm(false);
                                                            setEditingOption(null);
                                                            setParentQuestionIdForOption(null);
                                                        }}
                                                        className="px-4 py-1 border rounded hover:bg-gray-100 text-black"
                                                        disabled={loading}
                                                    >
                                                        Annuler
                                                    </button>
                                                    <button
                                                        onClick={saveOption}
                                                        className="px-4 py-1 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
                                                        disabled={loading}
                                                    >
                                                        {loading ? "Sauvegarde..." : "Sauvegarder"}
                                                    </button>
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {showQuestionForm && (
                    <div className="border p-4 rounded bg-gray-50 space-y-4">
                        <h3 className="text-lg font-semibold text-black">
                            {editingQuestion ? "Modifier la question" : "Nouvelle question"}
                        </h3>
                        <input
                            type="text"
                            value={questionDescription}
                            onChange={(e) => setQuestionDescription(e.target.value)}
                            className="w-full border border-gray-300 rounded p-2 text-black"
                            placeholder="Description de la question"
                        />
                        <input
                            type="number"
                            value={questionScore}
                            onChange={(e) => setQuestionScore(Number(e.target.value))}
                            className="w-full border border-gray-300 rounded p-2 text-black"
                            placeholder="Score de la question"
                        />
                        <div className="flex justify-end space-x-2">
                            <button
                                onClick={() => setShowQuestionForm(false)}
                                className="px-4 py-2 border rounded hover:bg-gray-100 text-black"
                                disabled={loading}
                            >
                                Annuler
                            </button>
                            <button
                                onClick={saveQuestion}
                                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
                                disabled={loading}
                            >
                                {loading ? "Sauvegarde..." : "Sauvegarder"}
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </Layout>)
}
