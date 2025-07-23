import api from '@/lib/api';
import {Answer, Question, Test, TestAttempt} from "@/types";
export const testService={
    getTestByCourse:async (courseId:number):Promise<Test> =>{
        const response = await api.get(`http://localhost:8082/Test/${courseId}/course`);
        return response.data;
    },
    getTestById:async (testId:number):Promise<Test> =>{
        const response = await api.get(`http://localhost:8082/Test/${testId}`);
        return response.data;
    },
    startTest:async (testId:number):Promise<Test> =>{
        const response = await fetch(`http://localhost:8082/Test/${testId}/student`,
            {
                method: 'POST',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            }
            )
        return response.json();
    },
    createTest:async (courseId:number,testData:Partial<Test> ):Promise<Test> =>{
        const response = await fetch(`http://localhost:8082/Test/${courseId}`,
            {
                method: 'POST',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(testData)
            }
        )
        return response.json();
    },
    updateTest:async (testId:number,testData:Partial<Test> ):Promise<Test> =>{
        const response = await fetch(`http://localhost:8082/Test/${testId}`,
            {
                method: 'PUT',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(testData)
            }
        )
        return response.json();
    },
    deleteTest:async (testId: number | undefined):Promise<void> =>{
        const response = await fetch(`http://localhost:8082/Test/${testId}`,
            {
                method: 'DELETE',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            }
        )
    }
}
export const questionService={
   getQuestionsByTestId:async (testId:number):Promise<Question[]> =>{
       const response = await api.get(`http://localhost:8082/Questions/${testId}/test`);
       return response.data;
   },
    getQuestionById:async (questionId:number):Promise<Question> =>{
        const response = await api.get(`http://localhost:8082/Questions/${questionId}`);
        return response.data;
    },
    createQuestion: async (testId:number,questionData:Partial<Question> ):Promise<Question> =>{
        const response = await fetch(`http://localhost:8082/Questions/${testId}`,
            {
                method: 'POST',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(questionData)
            }
        )
        return response.json();
    },
    updateQuestion: async (questionId:number,questionData:Partial<Question> ):Promise<Question> =>{
        const response = await fetch(`http://localhost:8082/Questions/${questionId}`,
            {
                method: 'PUT',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(questionData)
            }
        )
        return response.json();
    },
    deleteQuestion: async (id:number ):Promise<void> =>{
        const response = await fetch(`http://localhost:8082/Questions/${id}`,
            {
                method: 'DELETE',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            }
        )
    }
}
export const answerService={
    getOptionsByQuestionId:async (questionId:number):Promise<Answer[]> =>{
        const response = await api.get(`http://localhost:8082/Options/${questionId}/questions`);
        return response.data;
    },
    getOptionById: async (id:number):Promise<Answer> =>{
        const response = await api.get(`http://localhost:8082/Options/${id}`);
        return response.data;
    },
    createOption: async (questionId:number,answerData:Partial<Answer> ):Promise<Answer> =>{
        const response = await fetch(`http://localhost:8082/Options/${questionId}`,
            {
                method: 'POST',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(answerData)
            }
        )
        return response.json();
    },
    updateOption: async (id:number,answerData:Partial<Answer> ):Promise<Answer> =>{
        const response = await fetch(`http://localhost:8082/Options/${id}`,
            {
                method: 'PUT',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(answerData)
            }
        )
        return response.json();
    },
    deleteOption: async (id:number ):Promise<void> =>{
        const response = await fetch(`http://localhost:8082/Options/${id}`,
            {
                method: 'DELETE',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            }
        )
    }
}
export const attemptService={
    getAttemptionsByStudentMail: async (email: string | null):Promise<TestAttempt[]> =>{
        const response = await api.get(`http://localhost:8082/Attemption/${email}/student`);
        return response.data;
    },
    getAttemptionofTest: async (email: string | null, testId: number):Promise<TestAttempt[]> =>{
        const response = await api.get(`http://localhost:8082/Attemption/${email}/student/${testId}/test`);
        return response.data;
    },
    getAttemptionById: async (id:number):Promise<TestAttempt[]> =>{
        const response = await api.get(`http://localhost:8082/Attemption/${id}`);
        return response.data;
    },
    createAttemption: async (testId: number, email: string | null):Promise<TestAttempt> =>{
        const response = await fetch(`http://localhost:8082/Attemption/${testId}/student/${email}`,
            {
                method: 'POST',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
            }
        )
        return response.json();
    },
    submitTest: async (id:number,choice:any ):Promise<TestAttempt> =>{
        const response = await fetch(`http://localhost:8082/Attemption/${id}`,
            {
                method: 'POST',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(choice)
            }
        )
        return response.json();
    },
    deleteAttemption: async (id:number ):Promise<void> =>{
        const response = await fetch(`http://localhost:8082/Attemption/${id}`,
            {
                method: 'DELETE',
                headers:{
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                }
            }
        )
    }
}