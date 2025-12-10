import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export type UserRole = 'SUPERADMIN' | 'CPA_RESPONSIBLE' | 'JUDGE' | 'PUBLIC';

interface User {
    id: number;
    username: string;
    role: UserRole;
    cpaId?: number;
}

interface AuthState {
    token: string | null;
    user: User | null;
    setAuth: (token: string, user: User) => void;
    logout: () => void;
}

export const useAuthStore = create<AuthState>()(
    persist(
        (set) => ({
            token: null,
            user: null,
            setAuth: (token, user) => {
                localStorage.setItem('token', token);
                set({ token, user });
            },
            logout: () => {
                localStorage.removeItem('token');
                set({ token: null, user: null });
            },
        }),
        {
            name: 'auth-storage',
        }
    )
);
