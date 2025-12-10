import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore, UserRole } from '../../store/authStore';

interface DashboardLayoutProps {
    role: UserRole;
}

export default function DashboardLayout({ role }: DashboardLayoutProps) {
    const { user, logout } = useAuthStore();

    if (!user) return <Navigate to="/login" replace />;
    if (user.role !== role) {
        // Redirect to correct dashboard based on role to avoid getting stuck
        if (user.role === 'SUPERADMIN') return <Navigate to="/admin" replace />;
        if (user.role === 'CPA_RESPONSIBLE') return <Navigate to="/cpa" replace />;
        if (user.role === 'JUDGE') return <Navigate to="/judge" replace />;
        return <Navigate to="/login" replace />;
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <nav className="bg-white shadow">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between h-16">
                        <div className="flex items-center">
                            <span className="text-xl font-bold text-blue-600">Liga M100 - {role}</span>
                        </div>
                        <div className="flex items-center">
                            <span className="mr-4 text-gray-700">Hola, {user.username}</span>
                            <button
                                onClick={logout}
                                className="px-3 py-1 text-sm text-red-600 border border-red-600 rounded hover:bg-red-50"
                            >
                                Salir
                            </button>
                        </div>
                    </div>
                </div>
            </nav>
            <main className="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 lg:p-8">
                <Outlet />
            </main>
        </div>
    );
}
