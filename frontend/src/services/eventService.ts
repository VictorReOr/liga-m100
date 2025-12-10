import api from '../lib/axios';
import { Event, Discipline } from '../types';

export interface CreateEventRequest {
    nombre: string;
    fecha: string;
    tipo: string;
    description?: string;
}

export interface CreateDisciplineRequest {
    nombre: string;
    horarioInicio: string;
    horarioFin: string;
    requiereJuez: boolean;
    tipoMarca: 'TIEMPO' | 'DISTANCIA' | 'PUNTUACION';
    reglasConfig?: Record<string, any>;
}

export const eventService = {
    getAll: async () => {
        const response = await api.get<Event[]>('/events');
        return response.data;
    },

    create: async (data: CreateEventRequest) => {
        const response = await api.post<Event>('/events', data);
        return response.data;
    },

    getDisciplines: async (eventId: number) => {
        const response = await api.get<Discipline[]>(`/events/${eventId}/disciplines`);
        return response.data;
    },

    createDiscipline: async (eventId: number, data: CreateDisciplineRequest) => {
        const response = await api.post<Discipline>(`/events/${eventId}/disciplines`, data);
        return response.data;
    },

    importSchedules: async (eventId: number, file: File) => {
        const formData = new FormData();
        formData.append('file', file);
        await api.post(`/events/${eventId}/import-schedules`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
    }
};
