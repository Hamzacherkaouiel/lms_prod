import api from '@/lib/api';
import {Lesson, Module, SimpleLesson} from '@/types';

interface SimpleLessonDto {
  id: number;
  title: string;
  content: string;
  moduleId: number;
}

interface FullLessonDto extends SimpleLessonDto {
  videoUrl?: string;
  videoContent?: string;
}

export const lessonService = {
  // Get all lessons
  getAllLessons: async (): Promise<SimpleLessonDto[]> => {
    const response = await api.get('/lessons/');
    return response.data;
  },

  // Get simple lesson
  getSimpleLesson: async (id: number): Promise<SimpleLessonDto> => {
    const response = await api.get(`/lessons/${id}/simple`);
    return response.data;
  },

  // Get full lesson with video
  getFullLesson: async (id: string): Promise<Lesson> => {
    const response = await api.get(`/lessons/${id}/full`);
    return response.data;
  },

  // Get lessons by module ID
  getLessonsByModule: async (moduleId: number): Promise<SimpleLesson[]> => {
    console.log(moduleId)
    const response = await fetch(`http://localhost:8082/lessons/module/${moduleId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    return response.json();
  },

  // Create new lesson
  createLesson: async (lessonData: Partial<Lesson>, moduleId: string, filename: string)=> {
    const response = await fetch(`http://localhost:8082/lessons/module/${moduleId}/full?filename=${encodeURIComponent(filename)}`,
        {
          method: 'POST',
          headers:{
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(lessonData)
        }
        )
    return response;
  },

  // Update lesson
  updateLesson: async (id: string, lessonData: Partial<Lesson>): Promise<SimpleLessonDto> => {
    const response = await api.put(`/lessons/${id}`, lessonData);
    return response.data;
  },

  // Delete lesson
  deleteLesson: async (id: string): Promise<void> => {
    await api.delete(`/lessons/${id}`);
  },

  // Upload video for lesson
  uploadLessonVideo: async (url: string, videoFile: File) => {
    const response = await fetch(url,
        {
          method: 'PUT',
          body: videoFile
        }
        )
    return response;
  }
};

export const moduleService = {
  // Get all modules
  getAllModules: async (): Promise<Module[]> => {
    const response = await api.get('/modules/');
    return response.data;
  },

  // Get single module
  getModule: async (id: string | Array<string> | undefined): Promise<Module> => {
    const response = await fetch(`http://localhost:8082/modules/${id}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    return response.json();
  },

  // Get modules by course ID
  getModulesByCourse: async (courseId: number): Promise<Module[]> => {
    const response = await api.get(`/modules/course/${courseId}`);
    return response.data;
  },

  // Create new module
  createModule: async (moduleData: Partial<Module>, courseId: number): Promise<Module> => {
    const response = await api.post(`/modules/course/${courseId}`, moduleData);
    return response.data;
  },

  // Update module
  updateModule: async (id: number, moduleData: Partial<Module>): Promise<Module> => {
    const response = await api.put(`/modules/${id}`, moduleData);
    return response.data;
  },

  // Delete module
  deleteModule: async (id: number): Promise<void> => {
    await fetch(`http://localhost:8082/modules/${id}`,
        {
          method: 'DELETE',
          headers:{
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          }
        }
        );
  },

  // Get module with lessons
  getModuleWithLessons: async (id: number): Promise<Module> => {
    const response = await api.get(`/modules/${id}/lessons`);
    return response.data;
  }
};