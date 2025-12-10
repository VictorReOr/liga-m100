import api from '../lib/axios';
import { Athlete } from '../types';

export interface CreateAthleteRequest {
    nombre: string;
    dni: string;
    anioNacimiento: number;
    genero: string;
    cpaId: number;
}

export const athleteService = {
    getAll: async () => {
        const response = await api.get<Athlete[]>('/athletes');
        return response.data;
    },

    getByCpa: async (cpaId: number) => {
        const response = await api.get<Athlete[]>('/athletes', {
            params: { cpaId }
        });
        return response.data;
    },

    create: async (data: CreateAthleteRequest) => {
        const response = await api.post<Athlete>('/athletes', data);
        return response.data;
    }
};
