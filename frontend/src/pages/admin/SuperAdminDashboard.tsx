import { useEffect, useState } from 'react';
import { Event, Discipline } from '../../types';
import { eventService } from '../../services/eventService';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import toast from 'react-hot-toast';

export default function SuperAdminDashboard() {
    const [events, setEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState(false);

    // Modal/Form states
    const [showCreateEvent, setShowCreateEvent] = useState(false);
    const [selectedEventId, setSelectedEventId] = useState<number | null>(null); // For listing/adding disciplines
    const [disciplines, setDisciplines] = useState<Discipline[]>([]);

    // New Event State
    const [newEvent, setNewEvent] = useState({
        nombre: '',
        fecha: '',
        tipo: 'LOCAL',
        description: ''
    });

    // New Discipline State
    const [newDiscipline, setNewDiscipline] = useState({
        nombre: '',
        horarioInicio: '',
        horarioFin: '',
        requiereJuez: true,
        tipoMarca: 'PUNTUACION',
        // reglasConfig is handled separately via jsonRules
    });
    const [jsonRules, setJsonRules] = useState<string>('{\n  "type": "POSITION",\n  "points": [10, 8, 6, 5, 4, 3, 2, 1]\n}');

    useEffect(() => {
        loadEvents();
    }, []);

    useEffect(() => {
        if (selectedEventId) {
            loadDisciplines(selectedEventId);
        } else {
            setDisciplines([]);
        }
    }, [selectedEventId]);

    const loadEvents = async () => {
        setLoading(true);
        try {
            const data = await eventService.getAll();
            setEvents(data);
        } catch (error) {
            console.error(error);
            toast.error('Error cargando eventos');
        } finally {
            setLoading(false);
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

    const handleCreateEvent = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await eventService.create(newEvent);
            toast.success('Evento creado exitosamente');
            setNewEvent({ nombre: '', fecha: '', tipo: 'LOCAL', description: '' });
            setShowCreateEvent(false);
            loadEvents();
        } catch (error) {
            toast.error('Error creando evento');
        }
    };

    const handleCreateDiscipline = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedEventId) return;

        let parsedRules = {};
        try {
            parsedRules = JSON.parse(jsonRules);
        } catch (e) {
            toast.error('JSON de reglas inválido. Verifique la sintaxis.');
            return;
        }

        try {
            // Asegurar formato fecha ISO si es necesario, o concatenar fecha evento
            // Por simplicidad, asumimos que el input trae fecha y hora o el backend lo maneja.
            // Para HTML datetime-local: YYYY-MM-DDTHH:mm
            await eventService.createDiscipline(selectedEventId, {
                ...newDiscipline,
                // Si tipoMarca no coincide con enum backend, ajustar. 
                // Backend espera: TIEMPO, DISTANCIA, PUNTUACION
                tipoMarca: newDiscipline.tipoMarca as any,
                reglasConfig: parsedRules
            });
            toast.success('Disciplina añadida reglas configuradas');
            setNewDiscipline({ nombre: '', horarioInicio: '', horarioFin: '', requiereJuez: true, tipoMarca: 'PUNTUACION' });
            loadDisciplines(selectedEventId);
        } catch (error) {
            console.error(error);
            toast.error('Error creando disciplina');
        }
    };

    return (
        <div className="space-y-8">
            <header className="flex justify-between items-center">
                <div>
                    <h1 className="text-2xl font-bold text-gray-900">Panel de Administración</h1>
                    <p className="text-gray-600">Configuración completa de eventos y reglas.</p>
                </div>
                <Button onClick={() => setShowCreateEvent(!showCreateEvent)}>
                    {showCreateEvent ? 'Cancelar' : '+ Nuevo Evento'}
                </Button>
            </header>

            {/* Create Event Form */}
            {showCreateEvent && (
                <div className="bg-white p-6 rounded-lg shadow border-l-4 border-blue-500 animate-fade-in">
                    <h2 className="text-lg font-bold mb-4">Crear Nuevo Evento</h2>
                    <form onSubmit={handleCreateEvent} className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <Input
                            label="Nombre del Evento"
                            value={newEvent.nombre}
                            onChange={e => setNewEvent({ ...newEvent, nombre: e.target.value })}
                            required
                        />
                        <Input
                            type="date"
                            label="Fecha"
                            value={newEvent.fecha}
                            onChange={e => setNewEvent({ ...newEvent, fecha: e.target.value })}
                            required
                        />
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Tipo</label>
                            <select
                                className="w-full border p-2 rounded"
                                value={newEvent.tipo}
                                onChange={e => setNewEvent({ ...newEvent, tipo: e.target.value })}
                            >
                                <option value="LOCAL">Local</option>
                                <option value="PROVINCIAL">Provincial</option>
                                <option value="REGIONAL">Regional</option>
                            </select>
                        </div>
                        <div className="md:col-span-2 flex justify-end mt-4">
                            <Button type="submit">Guardar Evento</Button>
                        </div>
                    </form>
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Events List */}
                <div className="bg-white rounded-lg shadow overflow-hidden lg:col-span-1 border border-gray-200">
                    <div className="px-4 py-3 bg-gray-50 border-b font-medium text-gray-700">
                        Eventos Disponibles
                    </div>
                    <ul className="divide-y divide-gray-200 max-h-[600px] overflow-y-auto">
                        {loading && <li className="p-4 text-center">Cargando...</li>}
                        {events.map(event => (
                            <li
                                key={event.id}
                                className={`cursor-pointer hover:bg-blue-50 transition-colors ${selectedEventId === event.id ? 'bg-blue-50 border-l-4 border-blue-500' : ''}`}
                                onClick={() => setSelectedEventId(event.id)}
                            >
                                <div className="px-4 py-4">
                                    <div className="font-medium text-gray-900">{event.nombre}</div>
                                    <div className="text-sm text-gray-500">{event.fecha} • {event.tipo}</div>
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>

                {/* Disciplines & Rules Editor */}
                <div className="bg-white rounded-lg shadow lg:col-span-2 border border-gray-200 flex flex-col">
                    {!selectedEventId ? (
                        <div className="flex-1 flex items-center justify-center p-12 text-gray-400">
                            Selecciona un evento para gestionar sus disciplinas
                        </div>
                    ) : (
                        <div className="p-6">
                            <h2 className="text-xl font-bold mb-6 pb-2 border-b">
                                Disciplinas del Evento: {events.find(e => e.id === selectedEventId)?.nombre}
                            </h2>

                            {/* Discipline List */}
                            <div className="mb-8">
                                <h3 className="font-medium text-gray-700 mb-3">Disciplinas Existentes</h3>
                                <div className="flex flex-wrap gap-2">
                                    {disciplines.length === 0 && <span className="text-sm text-gray-400">Sin disciplinas</span>}
                                    {disciplines.map(d => (
                                        <span key={d.id} className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-gray-100 text-gray-800">
                                            {d.nombre} ({d.tipoMarca})
                                        </span>
                                    ))}
                                </div>
                            </div>

                            {/* Add Discipline Form */}
                            <div className="bg-gray-50 p-6 rounded-lg border border-gray-200">
                                <h3 className="font-bold text-gray-800 mb-4 flex items-center gap-2">
                                    <span className="bg-purple-100 text-purple-600 p-1 rounded text-xs">JSON</span>
                                    Añadir Nueva Disciplina
                                </h3>
                                <form onSubmit={handleCreateDiscipline} className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <Input
                                        label="Nombre Disciplina"
                                        value={newDiscipline.nombre}
                                        onChange={e => setNewDiscipline({ ...newDiscipline, nombre: e.target.value })}
                                        required
                                    />
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Tipo de Marca</label>
                                        <select
                                            className="w-full border p-2 rounded-md"
                                            value={newDiscipline.tipoMarca}
                                            onChange={e => setNewDiscipline({ ...newDiscipline, tipoMarca: e.target.value })}
                                        >
                                            <option value="PUNTUACION">Puntuación (Jueces)</option>
                                            <option value="TIEMPO">Tiempo (Carreras)</option>
                                            <option value="DISTANCIA">Distancia (Lanzamientos)</option>
                                        </select>
                                    </div>
                                    <div className="grid grid-cols-2 gap-2 md:col-span-2">
                                        <Input
                                            type="datetime-local"
                                            label="Inicio"
                                            value={newDiscipline.horarioInicio}
                                            onChange={e => setNewDiscipline({ ...newDiscipline, horarioInicio: e.target.value })}
                                            required
                                        />
                                        <Input
                                            type="datetime-local"
                                            label="Fin"
                                            value={newDiscipline.horarioFin}
                                            onChange={e => setNewDiscipline({ ...newDiscipline, horarioFin: e.target.value })}
                                            required
                                        />
                                    </div>

                                    {/* JSON Editor */}
                                    <div className="md:col-span-2 mt-2">
                                        <label className="block text-sm font-medium text-gray-700 mb-1">
                                            Configuración de Reglas (JSON)
                                        </label>
                                        <p className="text-xs text-gray-500 mb-2">
                                            Define cómo se calculan los puntos. Ejemplo: "type": "POSITION" para dar puntos fijos por posición.
                                        </p>
                                        <textarea
                                            className="w-full h-32 p-3 font-mono text-xs bg-slate-900 text-green-400 rounded-md border focus:ring-2 focus:ring-purple-500"
                                            value={jsonRules}
                                            onChange={e => setJsonRules(e.target.value)}
                                            spellCheck={false}
                                        />
                                    </div>

                                    <div className="md:col-span-2 flex justify-end mt-2">
                                        <Button type="submit" className="bg-purple-600 hover:bg-purple-700">
                                            Guardar Disciplina
                                        </Button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
