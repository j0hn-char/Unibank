import type {DecodedToken, Role} from "@/types";
import {createContext, type ReactNode, useContext, useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";

interface AuthContextType {
    user: AuthUser | null
    login: (token: string) => void
    logout: () => void
    isLoading: boolean
}

interface AuthUser {
    email: string
    role: Role
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode}) {
    const [user, setUser] = useState<AuthUser | null>(null)
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        const token = localStorage.getItem('token')
        if(token) {
            try {
                const decoded = jwtDecode<DecodedToken>(token)
                //expiration check
                if (decoded.exp * 1000 > Date.now()) {
                    setUser({ email: decoded.sub, role: decoded.role })
                } else {
                    localStorage.removeItem('token')
                }
            } catch {
                localStorage.removeItem('token')
            }
        }
        setIsLoading(false)
    }, [])

    function login(token: string) {
        localStorage.setItem('token', token)
        const decoded = jwtDecode<DecodedToken>(token)
        setUser({ email: decoded.sub, role: decoded.role })
    }

    function logout() {
        localStorage.removeItem('token')
        setUser(null)
    }

    return (
        <AuthContext.Provider value={{ user, login, logout, isLoading }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    const context = useContext(AuthContext)
    if(context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider')
    }
    return context
}
