import type {ReactNode} from "react";
import {useAuth} from "@/context/AuthContext.tsx";
import {Navigate} from "react-router-dom";

export default function ProtectedRoute({ children }: { children: ReactNode }) {
    const { user, isLoading } = useAuth()

    if(isLoading) {
        return (
            <div>Loading...</div>
        )
    }

    if(user === null) {
        return <Navigate to={"/login"} replace/>
    }

    return children
}