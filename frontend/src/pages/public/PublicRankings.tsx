import { useEffect, useState } from 'react';
import api from '../../lib/axios';
import { cn } from '../../lib/utils';

interface ProvinceRanking {
    provinceId: number;
    provinceName: string;
    totalPoints: number;
}

interface CpaRanking {
    cpaId: number;
    cpaName: string;
    provinceName: string;
    totalPoints: number;
}

export default function PublicRankings() {
    const [view, setView] = useState<'PROVINCE' | 'CPA'>('PROVINCE');
    const [provinceRanking, setProvinceRanking] = useState<ProvinceRanking[]>([]);
    const [cpaRanking, setCpaRanking] = useState<CpaRanking[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        Promise.all([
            api.get('/rankings/provinces'),
            api.get('/rankings/cpas')
        ])
            .then(([provRes, cpaRes]) => {
                setProvinceRanking(provRes.data);
                setCpaRanking(cpaRes.data);
            })
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <header className="bg-blue-900 text-white shadow-lg">
                <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
                    <div className="flex items-center space-x-4">
                        {/* Logo placeholder */}
                        <div className="w-10 h-10 bg-yellow-400 rounded-full flex items-center justify-center text-blue-900 font-bold">M100</div>
                        <h1 className="text-2xl font-bold tracking-tight">Liga M100</h1>
                    </div>
                    <a href="/login" className="px-4 py-2 bg-blue-800 rounded hover:bg-blue-700 text-sm font-medium transition-colors">
                        Acceso Privado
                    </a>
                </div>
            </header>

            <main className="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 lg:p-8">
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-extrabold text-gray-900">Resultados en Vivo</h2>
                    <p className="mt-2 text-lg text-gray-600">Clasiifcaciones actualizadas al instante</p>
                </div>

                <div className="bg-white rounded-xl shadow-xl overflow-hidden">
                    <div className="flex border-b border-gray-200">
                        <button
                            onClick={() => setView('PROVINCE')}
                            className={cn(
                                "flex-1 py-4 text-center font-medium text-sm sm:text-base hover:bg-gray-50 transition-colors",
                                view === 'PROVINCE' ? "border-b-4 border-blue-600 text-blue-600 bg-blue-50" : "text-gray-500"
                            )}
                        >
                            Ranking por Provincias
                        </button>
                        <button
                            onClick={() => setView('CPA')}
                            className={cn(
                                "flex-1 py-4 text-center font-medium text-sm sm:text-base hover:bg-gray-50 transition-colors",
                                view === 'CPA' ? "border-b-4 border-blue-600 text-blue-600 bg-blue-50" : "text-gray-500"
                            )}
                        >
                            Ranking por CPAs
                        </button>
                    </div>

                    <div className="p-0 sm:p-6">
                        {loading ? (
                            <div className="p-8 text-center text-gray-500">Cargando clasificaciones...</div>
                        ) : (
                            <div className="overflow-x-auto">
                                <table className="min-w-full divide-y divide-gray-200">
                                    <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-6 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider w-16">Pos</th>
                                            <th className="px-6 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">Nombre</th>
                                            {view === 'CPA' && <th className="px-6 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">Provincia</th>}
                                            <th className="px-6 py-3 text-right text-xs font-bold text-gray-500 uppercase tracking-wider">Puntos</th>
                                        </tr>
                                    </thead>
                                    <tbody className="bg-white divide-y divide-gray-200">
                                        {(view === 'PROVINCE' ? provinceRanking : cpaRanking).map((item: any, index) => (
                                            <tr key={index} className={index < 3 ? "bg-yellow-50" : ""}>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className={cn(
                                                        "w-8 h-8 rounded-full flex items-center justify-center font-bold text-sm",
                                                        index === 0 ? "bg-yellow-400 text-yellow-900" :
                                                            index === 1 ? "bg-gray-300 text-gray-800" :
                                                                index === 2 ? "bg-orange-300 text-orange-900" : "text-gray-500"
                                                    )}>
                                                        {index + 1}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap font-medium text-gray-900">
                                                    {view === 'PROVINCE' ? item.provinceName : item.cpaName}
                                                </td>
                                                {view === 'CPA' && (
                                                    <td className="px-6 py-4 whitespace-nowrap text-gray-500">{item.provinceName}</td>
                                                )}
                                                <td className="px-6 py-4 whitespace-nowrap text-right font-bold text-blue-600 text-lg">
                                                    {item.totalPoints}
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
}
