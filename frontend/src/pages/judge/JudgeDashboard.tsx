import { useEffect, useState } from 'react';
import { useAuthStore } from '../../store/authStore';
import { Discipline, Event } from '../../types';
import toast from 'react-hot-toast';
import { scoreService } from '../../services/scoreService';
import { eventService } from '../../services/eventService';
import api from '../../lib/axios'; // For registrations fetching
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';

interface RegistrationItem {
    id: number;
    deportista: {
        id: number;
        nombre: string;
        cpa: { nombre: string; }
    };
    disciplina: {
        id: number;
    }
}

export default function JudgeDashboard() {
    const { user } = useAuthStore();
    const [events, setEvents] = useState<Event[]>([]);
    const [selectedEventId, setSelectedEventId] = useState<number | null>(null);
    const [disciplines, setDisciplines] = useState<Discipline[]>([]);
    const [selectedDisciplineId, setSelectedDisciplineId] = useState<number | null>(null);
    const [registrations, setRegistrations] = useState<RegistrationItem[]>([]);

    // Scoring state
    const [scores, setScores] = useState<Record<number, string>>({}); // registrationId -> marca value
    const [notes, setNotes] = useState<Record<number, string>>({});   // registrationId -> notes (optional)

    useEffect(() => {
        loadEvents();
    }, []);

    useEffect(() => {
        if (selectedEventId) {
            loadDisciplines(selectedEventId);
            setSelectedDisciplineId(null);
            setRegistrations([]);
        }
    }, [selectedEventId]);

    useEffect(() => {
        if (selectedDisciplineId) {
            loadRegistrations(selectedDisciplineId);
        }
    }, [selectedDisciplineId]);

    const loadEvents = async () => {
        try {
            const data = await eventService.getAll();
            setEvents(data);
        } catch (error) {
            toast.error('Error cargando eventos');
        }
    };

    const loadDisciplines = async (eventId: number) => {
        try {
            const data = await eventService.getDisciplines(eventId);
            setDisciplines(data);
        } catch (error) {
            toast.error('Error cargando disciplinas');
        }
    };

    const loadRegistrations = async (discId: number) => {
        if (!selectedEventId) return;
        try {
            // Actualmente obtenemos todas las del evento y filtramos.
            // TODO: Optimizar con endpoint backend filtrado por disciplina si crece el volumen
            const res = await api.get(`/registrations?eventId=${selectedEventId}`);
            const filtered = res.data.filter((r: any) => r.disciplina.id === discId);
            setRegistrations(filtered);
        } catch (error) {
            console.error(error);
            toast.error('Error cargando inscripciones');
        }
    };

    const handleScoreSubmit = async (registrationId: number) => {
        const marcaVal = scores[registrationId];
        if (!marcaVal) {
            toast.error('Debe ingresar una marca');
            return;
        }

        try {
            await scoreService.recordScore({
                registrationId,
                judgeId: user?.id!,
                marca: parseFloat(marcaVal),
                marcaTexto: marcaVal, // En el futuro esto podría ser diferente para tiempos/distancia
                notas: notes[registrationId] || ''
            });
            toast.success('Puntuación guardada correctamente');
        } catch (error) {
            console.error(error);
            toast.error('Error al guardar puntuación');
        }
    };

    return (
        <div className="space-y-6">
            <header>
                <h1 className="text-2xl font-bold text-gray-900">Panel de Juez</h1>
                <p className="text-gray-600">Registro de puntuaciones y marcas.</p>
            </header>

            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Evento</label>
                    <select
                        className="w-full border p-2 rounded-md"
                        onChange={e => setSelectedEventId(Number(e.target.value))}
                        value={selectedEventId || ''}
                    >
                        <option value="">-- Seleccionar Evento --</option>
                        {events.map(e => <option key={e.id} value={e.id}>{e.nombre}</option>)}
                    </select>
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Disciplina</label>
                    <select
                        className="w-full border p-2 rounded-md"
                        onChange={e => setSelectedDisciplineId(Number(e.target.value))}
                        value={selectedDisciplineId || ''}
                        disabled={!selectedEventId}
                    >
                        <option value="">-- Seleccionar Disciplina --</option>
                        {disciplines.map(d => <option key={d.id} value={d.id}>{d.nombre}</option>)}
                    </select>
                </div>
            </div>

            {selectedDisciplineId && (
                <div className="bg-white shadow-sm rounded-lg overflow-hidden border border-gray-200">
                    <div className="px-6 py-4 border-b border-gray-200 bg-gray-50 flex justify-between items-center">
                        <h2 className="text-lg font-semibold text-gray-800">
                            Evaluación - {disciplines.find(d => d.id === selectedDisciplineId)?.nombre}
                        </h2>
                        <span className="text-sm text-gray-500">{registrations.length} Participantes</span>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Deportista</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">CPA</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Marca/Puntuación</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Notas</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Acción</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {registrations.map(reg => (
                                    <tr key={reg.id} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="text-sm font-medium text-gray-900">{reg.deportista.nombre}</div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="text-sm text-gray-500">{reg.deportista.cpa.nombre}</div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <Input
                                                type="number"
                                                step="0.001"
                                                className="w-32"
                                                placeholder="0.000"
                                                value={scores[reg.id] || ''}
                                                onChange={e => setScores({ ...scores, [reg.id]: e.target.value })}
                                            />
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <Input
                                                type="text"
                                                className="w-40"
                                                placeholder="Opcional"
                                                value={notes[reg.id] || ''}
                                                onChange={e => setNotes({ ...notes, [reg.id]: e.target.value })}
                                            />
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <Button
                                                onClick={() => handleScoreSubmit(reg.id)}
                                                className="text-xs"
                                            >
                                                Guardar
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        {registrations.length === 0 && (
                            <div className="p-8 text-center text-gray-500">
                                <p>No hay inscripciones para esta disciplina.</p>
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}
