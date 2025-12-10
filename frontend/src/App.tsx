import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import LoginPage from './pages/LoginPage'
import DashboardLayout from './components/layout/DashboardLayout'
import SuperAdminDashboard from './pages/admin/SuperAdminDashboard'
import CpaDashboard from './pages/cpa/CpaDashboard'
import JudgeDashboard from './pages/judge/JudgeDashboard'
import PublicRankings from './pages/public/PublicRankings'

function App() {
    return (
        <BrowserRouter>
            <Toaster position="top-right" />
            <Routes>
                <Route path="/login" element={<LoginPage />} />

                {/* Public Routes */}
                <Route path="/rankings" element={<PublicRankings />} />

                {/* Protected Routes (Placeholder structure) */}
                <Route path="/admin/*" element={<DashboardLayout role="SUPERADMIN" />}>
                    <Route index element={<SuperAdminDashboard />} />
                </Route>

                <Route path="/cpa/*" element={<DashboardLayout role="CPA_RESPONSIBLE" />}>
                    <Route index element={<CpaDashboard />} />
                </Route>

                <Route path="/judge/*" element={<DashboardLayout role="JUDGE" />}>
                    <Route index element={<JudgeDashboard />} />
                </Route>

                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </BrowserRouter>
    )
}

export default App
