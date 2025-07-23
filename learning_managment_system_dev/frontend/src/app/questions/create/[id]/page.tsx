"use client";

import {useEffect, useState} from "react";
import { useRouter, useParams } from "next/navigation";
import { Button } from "@/components/ui/Button";
import {questionService} from "@/services/test-content";
import Layout from "@/components/layout/Layout";
import {authService} from "@/services/auth";
export default function CreateQuestionForm() {
    const router = useRouter();
    const { id } = useParams();
    const testId = parseInt(id as string);
    const [description, setDescription] = useState("");
    const [score, setScore] = useState<number>(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    useEffect(() => {
        if (authService.isTokenExpired()) router.push("/login")
    }, []);
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError("");

        try {
            let question={description:description,scoreQuestion:score}
            await questionService.createQuestion(testId,question);
            alert("Question créée !");
            router.back();
        } catch (err) {
            setError("Erreur lors de la création");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Layout>
        <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-md shadow-md">
            <h1 className="text-2xl font-semibold mb-6 text-gray-900">
                Ajouter une question
            </h1>

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-black mb-1">
                        Description de la question
                    </label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        className="w-full border border-gray-300 p-2 rounded text-black"
                        placeholder="Entrez la description"
                        required
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-black mb-1">
                        Score
                    </label>
                    <input
                        type="number"
                        value={score}
                        onChange={(e) => setScore(parseInt(e.target.value))}
                        className="w-full border border-gray-300 p-2 rounded text-black"
                        placeholder="0"
                        min={0}
                        required
                    />
                </div>

                {error && <p className="text-red-600">{error}</p>}

                <div className="flex justify-end space-x-2">
                    <Button
                        type="button"
                        variant="outline"
                        onClick={() => router.back()}
                        disabled={loading}
                    >
                        Annuler
                    </Button>
                    <Button type="submit" disabled={loading}>
                        {loading ? "Création..." : "Créer"}
                    </Button>
                </div>
            </form>
        </div>
        </Layout>
    );
}
