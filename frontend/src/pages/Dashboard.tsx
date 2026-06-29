import {useAuth} from "@/context/AuthContext.tsx";
import PageContainer from "@/components/PageContainer.tsx";

export default function Dashboard() {
    const { user } = useAuth()

    return (
        <PageContainer>
            <div className="flex justify-center items-center mb-8">
                <h1 className="text-2xl font-bold">Dashboard</h1>
            </div>
            <p>Welcome, {user?.email}</p>
            <p className="text-sm text-gray-500">Role: {user?.role}</p>
        </PageContainer>
    )
}