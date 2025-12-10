import { useEffect, useState } from 'react';
import { useAuthStore } from '../../store/authStore';
import { Athlete, Event, Discipline } from '../../types';
import toast from 'react-hot-toast';
import { athleteService } from '../../services/athleteService';
import { eventService } from '../../services/eventService';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import api from '../../lib/axios';

export default function CpaDashboard() {
    const { user } = useAuthStore();
    const [athletes, setAthletes] = useState<Athlete[]>([]);
    const [events, setEvents] = useState<Event[]>([]);
    const [selectedEvent, setSelectedEvent] = useState<number | null>(null);
    const [disciplines, setDisciplines] = useState<Discipline[]>([]);
    const [loading, setLoading] = useState(false);

    // New Athlete Form
    const [newAthlete, setNewAthlete] = useState({
        nombre: '',
        dni: '',
        anioNacimiento: '',
        genero: 'MASCULINO'
    });

    useEffect(() => {
        if (user?.cpaId) {
            loadAthletes();
        }
        loadEvents();
    }, [user]);

    useEffect(() => {
        if (selectedEvent) {
            loadDisciplines(selectedEvent);
        } else {
            setDisciplines([]);
        }
    }, [selectedEvent]);

    const loadAthletes = async () => {
        if (!user?.cpaId) return;
        setLoading(true);
        try {
            const data = await athleteService.getByCpa(user.cpaId);
            setAthletes(data);
        } catch (error) {
            console.error(error);
            toast.error('Error cargando deportistas');
        } finally {
            setLoading(false);
        }
    };

    const loadEvents = async () => {
        try {
            const data = await eventService.getAll();
            setEvents(data);
        } catch (error) {
            console.error(error);
            toast.error('Error cargando eventos');
        }
    };

    const loadDisciplines = async (eventId: number) => {
        try {
            const data = await eventService.getDisciplines(eventId);
            setDisciplines(data);
        } catch (error) {
            console.error(error);
            toast.error('Error cargando disciplinas');
        }
    };

    const handleCreateAthlete = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!user?.cpaId) return;

        try {
            await athleteService.create({
                ...newAthlete,
                anioNacimiento: parseInt(newAthlete.anioNacimiento),
                cpaId: user.cpaId
            });
            toast.success('Deportista creado exitosamente');
            setNewAthlete({ nombre: '', dni: '', anioNacimiento: '', genero: 'MASCULINO' });
            loadAthletes();
        } catch (error) {
            console.error(error);
            toast.error('Error al crear deportista. Verifique los datos.');
        }
    };

    const handleRegister = async (athleteId: number, disciplineId: number) => {
        if (!selectedEvent) return;
        try {
            await api.post('/registrations', {
                eventId: selectedEvent,
                athleteId,
                disciplineId
            });
            toast.success('Inscripción realizada');
        } catch (error: any) {
            toast.error(error.response?.data?.message || 'Error al inscribir (posible solapamiento o ya inscrito)');
        }
    };

    return (
        <div className="space-y-8">
            <header>
                <h1 className="text-3xl font-bold text-gray-900">Gestión de CPA</h1>
                <p className="text-gray-600">Administre sus deportistas e inscripciones.</p>
            </header>

            {/* Sección Alta Deportistas */}
            <section className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
                <h2 className="text-xl font-semibold mb-6 flex items-center gap-2">
                    <span className="bg-blue-100 text-blue-600 p-1 rounded">👤</span> Registrar Nuevo Deportista
                </h2>
                <form onSubmit={handleCreateAthlete} className="grid grid-cols-1 md:grid-cols-12 gap-4 items-end">
                    <div className="md:col-span-4">
                        <Input
                            label="Nombre Completo"
                            placeholder="Ej: Juan Pérez"
                            value={newAthlete.nombre}
                            onChange={e => setNewAthlete({ ...newAthlete, nombre: e.target.value })}
                            required
                        />
                    </div>
                    <div className="md:col-span-3">
                        <Input
                            label="DNI"
                            placeholder="12345678A"
                            value={newAthlete.dni}
                            onChange={e => setNewAthlete({ ...newAthlete, dni: e.target.value })}
                            required
                        />
                    </div>
                    <div className="md:col-span-2">
                        <Input
                            type="number"
                            label="Año Nac."
                            placeholder="2010"
                            value={newAthlete.anioNacimiento}
                            onChange={e => setNewAthlete({ ...newAthlete, anioNacimiento: e.target.value })}
                            required
                        />
                    </div>
                    <div className="md:col-span-2">
                        <label className="block text-sm font-medium text-gray-700 mb-1">Género</label>
                        <select
                            className="w-full px-3 py-2 border rounded-md focus:ring-blue-500 focus:border-blue-500"
                            value={newAthlete.genero}
                            onChange={e => setNewAthlete({ ...newAthlete, genero: e.target.value })}
                        >
                            <option value="MASCULINO">Masculino</option>
                            <option value="FEMENINO">Femenino</option>
                        </select>
                    </div>
                    <div className="md:col-span-1">
                        <Button type="submit" className="w-full">
                            +
                        </Button>
                    </div>
                </form>
            </section>

            {/* Sección Inscripciones */}
            <section className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
                <h2 className="text-xl font-semibold mb-6 flex items-center gap-2">
                    <span className="bg-green-100 text-green-600 p-1 rounded">📝</span> Inscripción a Eventos
                </h2>

                <div className="mb-6 max-w-md">
                    <label className="block text-sm font-medium text-gray-700 mb-1">Seleccionar Evento para Inscribir</label>
                    <div className="flex gap-2">
                        <select
                            className="w-full border p-2 rounded-md"
                            onChange={e => setSelectedEvent(Number(e.target.value))}
                            value={selectedEvent || ''}
                        >
                            <option value="">-- Seleccione un evento activo --</option>
                            {events.map(ev => (
                                <option key={ev.id} value={ev.id}>{ev.nombre} ({ev.fecha})</option>
                            ))}
                        </select>
                    </div>
                </div>

                {selectedEvent && (
                    <div className="overflow-hidden rounded-lg border border-gray-200">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Deportista</th>
                                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Detalles</th>
                                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Inscribir en Disciplinas Disponibles</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {loading && (
                                    <tr>
                                        <td colSpan={3} className="px-4 py-4 text-center text-gray-500">Cargando deportistas...</td>
                                    </tr>
                                )}
                                {!loading && athletes.length === 0 ? (
                                    <tr>
                                        <td colSpan={3} className="px-4 py-4 text-center text-gray-500">
                                            No hay deportistas registrados en este CPA.
                                        </td>
                                    </tr>
                                ) : (
                                    !loading && athletes.map(athlete => (
                                        <tr key={athlete.id} className="hover:bg-gray-50 transition-colors">
                                            <td className="px-4 py-3 font-medium text-gray-900">{athlete.nombre}</td>
                                            <td className="px-4 py-3 text-sm text-gray-500">
                                                {athlete.dni} | {athlete.anioNacimiento} | {athlete.genero.charAt(0)}
                                            </td>
                                            <td className="px-4 py-3">
                                                <div className="flex flex-wrap gap-2">
                                                    {disciplines.length > 0 ? disciplines.map(disc => (
                                                        <button
                                                            key={disc.id}
                                                            onClick={() => handleRegister(athlete.id, disc.id)}
                                                            className="inline-flex items-center px-2.5 py-1.5 border border-transparent text-xs font-medium rounded text-blue-700 bg-blue-100 hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                                                            title={`Horario: ${disc.horarioInicio}`}
                                                        >
                                                            {disc.nombre}
                                                        </button>
                                                    )) : (
                                                        <span className="text-gray-400 text-xs italic">No hay disciplinas configuradas</span>
                                                    )}
                                                </div>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                )}
            </section>
        </div>
    );
}
