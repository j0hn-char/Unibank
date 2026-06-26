import {useAuth} from "@/context/AuthContext.tsx";
import {Button} from "@/components/ui/button.tsx";

export default function Dashboard() {
    const { user, logout } = useAuth()

    return (
        <div className="min-h-screen p-8">
            <div className="flex justify-center items-center mb-8">
                <h1 className="text-2xl font-bold">Dashboard</h1>
                <Button variant="outline" onClick={logout}>Log out</Button>
            </div>
            <p>Welcome, {user?.email}</p>
            <p className="text-sm text-gray-500">Role: {user?.role}</p>
        </div>
    )
}