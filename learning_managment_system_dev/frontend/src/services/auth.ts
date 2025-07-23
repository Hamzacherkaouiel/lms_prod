import axios from "axios";

const isClient = typeof window !== 'undefined';

import api from '@/lib/api';
import { UserCreation, AuthUser } from '@/types';

export const authService = {
  // Register new user
  register: async (userData: UserCreation): Promise<AuthUser> => {
    const response = await api.post('/sign/', userData);
    return response.data;
  },

  // Create user (admin only)
  createUser: async (userData: UserCreation): Promise<AuthUser> => {
    const response = await api.post('/sign/create-user', userData);
    return response.data;
  },

  // Login with Keycloak (handled by NextAuth)
  login: async (credentials: { username: string; password: string }) => {
    const body = new URLSearchParams();
    body.append('client_id', 'lms-auth');
    body.append('username', credentials.username);
    body.append('password', credentials.password);
    body.append('grant_type', 'password');
    const  response = await  fetch('http://localhost:8081/realms/hamza/protocol/openid-connect/token',
        {
          method: 'POST',
          headers:{
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: body
        }
        )
    return response.json();
  },
 /* profile: async (email:string,role:string)=>{

  }*/

  // Logout
  logout:  () => {
    if (!isClient) return;
    localStorage.removeItem('token');
  },

  // Get current user profile
  getCurrentUser: (): string | null => {
    if (typeof window === 'undefined') return null; // <-- protection SSR
    const token = localStorage.getItem('token');
    return token ? token : null;
  },

  // Save user to localStorage
  saveUser: (token: String) => {
    localStorage.setItem('token', token);
  },

  // Check if user is authenticated
  isAuthenticated: (): boolean => {
    if (typeof window === 'undefined') return false; // <-- protection SSR
    return !!localStorage.getItem('token');
  },

  getUserMail: ():string | null =>{
    if (typeof window === 'undefined') return null;

    const token = authService.getCurrentUser();
    if (!token) return null;

    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) return null;
    const decodedPayload = JSON.parse(atob(payloadBase64));
    return decodedPayload?.preferred_username || null;
  },
  getFirstName: ():string | null =>{
    if (typeof window === 'undefined') return null; // <-- protection SSR

    const token = authService.getCurrentUser();
    if (!token) return null;

    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) return null;
    const decodedPayload = JSON.parse(atob(payloadBase64));
    return decodedPayload?.given_name || null;
  },
  getLastName: ():string | null =>{
    if (typeof window === 'undefined') return null; // <-- protection SSR

    const token = authService.getCurrentUser();
    if (!token) return null;

    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) return null;
    const decodedPayload = JSON.parse(atob(payloadBase64));
    return decodedPayload?.family_name || null;
  },


  // Get user role
  getUserRole: (): string | null => {
    if (typeof window === 'undefined') return null; // <-- protection SSR

    const token = authService.getCurrentUser();
    if (!token) return null;

    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) return null;


      const decodedPayload = JSON.parse(atob(payloadBase64));
      const roles = decodedPayload?.resource_access?.['lms-auth']?.roles || [];

      if (roles.includes('student')) return 'student';
      if (roles.includes('admin')) return 'admin';
      if (roles.includes('teacher')) return 'teacher';

    return null;
  },
  isTokenExpired(): boolean {
  const token = localStorage.getItem('token');
  if (!token) return true;

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp;
    const now = Math.floor(Date.now() / 1000);

    return exp < now;
  } catch (e) {
    return true;
  }
}

};
