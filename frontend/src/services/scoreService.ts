import api from '../lib/axios';

export interface Score {
    id: number;
    registrationId: number;
    judgeId: number;
    marca: number;
    marcaTexto: string;
    notas: string;
    timestamp: string;
}

export interface RecordScoreRequest {
    registrationId: number;
    judgeId: number;
    marca: number | null;
    marcaTexto: string | null;
    notas: string | null;
}

export const scoreService = {
    recordScore: async (data: RecordScoreRequest) => {
        const response = await api.post<Score>('/scores/record', data);
        return response.data;
    },

    getByDiscipline: async (disciplineId: number) => {
        const response = await api.get<Score[]>(`/scores/discipline/${disciplineId}`);
        return response.data;
    }
};
