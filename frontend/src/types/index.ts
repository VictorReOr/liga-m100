export interface Province {
    id: number;
    nombre: string;
}

export interface Cpa {
    id: number;
    nombre: string;
    provincia: Province;
}

export interface Athlete {
    id: number;
    nombre: string;
    dni: string;
    anioNacimiento: number;
    genero: string;
    cpa: Cpa;
}

export interface Discipline {
    id: number;
    nombre: string;
    horarioInicio: string;
    horarioFin: string;
    requiereJuez: boolean;
    tipoMarca: string;
}

export interface Event {
    id: number;
    nombre: string;
    fecha: string;
    tipo: string;
}

export interface Registration {
    id: number;
    deportista: Athlete;
    evento: Event;
    disciplina: Discipline;
}
