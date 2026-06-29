import {useEffect, useState} from "react";
import type {AccountResponse} from "@/types";
import api from "@/api/axios.ts";
import {useNavigate, useParams} from "react-router-dom";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent, AlertDialogDescription, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger
} from "@/components/ui/alert-dialog.tsx";
import {Button} from "@/components/ui/button.tsx";
import PageContainer from "@/components/PageContainer.tsx";

export default function AccountDetailsPage() {
    const [ account, setAccount ] = useState<AccountResponse | null>(null)
    const [ loading, setLoading ] = useState(true)
    const [ submitting, setSubmitting ] = useState(false)
    const { id } = useParams();
    const navigate = useNavigate()

    async function fetchAccount() {
        try {
            const response = await api.get<AccountResponse | null>(`/accounts/${id}`)
            setAccount(response.data)
        } catch (err) {
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    async function handleCloseAccount() {
        setSubmitting(true)
        try {
            await api.delete(`/accounts/${id}`)
            navigate('/accounts')
        } catch (err) {
            console.error(err)
        } finally {
            setSubmitting(false)
        }
    }

    useEffect(() => {
        fetchAccount()
    }, [id])

    return (
        <PageContainer>
            {loading ? (
                <p>Loading...</p>
            ) : !account ? (
                <p className="text-gray-500">Account not found.</p>
            ) : (
                <Card className="max-w-lg">
                    <CardHeader>
                        <CardTitle className="text-2xl">{account.nickname}</CardTitle>
                        <p className="text-sm text-gray-500">{account.type}</p>
                    </CardHeader>
                    <CardContent>
                        <p className="text-sm text-gray-500">{account.accountNumber}</p>
                        <p className="text-3xl font-bold mt-2">{account.balance.toFixed(2)}€</p>
                        <p className="text-sm mt-1">{account.status}</p>
                        <div className="mt-4">
                            <AlertDialog>
                                <AlertDialogTrigger asChild>
                                    <Button variant="destructive">Close account</Button>
                                </AlertDialogTrigger>
                                <AlertDialogContent>
                                    <AlertDialogHeader>
                                        <AlertDialogTitle>Close this account?</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            This will permanently close "{account.nickname}". This action cannot be undone.
                                        </AlertDialogDescription>
                                    </AlertDialogHeader>

                                    <AlertDialogFooter>
                                        <AlertDialogCancel>Cancel</AlertDialogCancel>
                                        <AlertDialogAction onClick={handleCloseAccount} disabled={submitting}>
                                            {submitting ? 'Closing...' : 'Close account'}
                                        </AlertDialogAction>
                                    </AlertDialogFooter>
                                </AlertDialogContent>
                            </AlertDialog>
                        </div>
                    </CardContent>
                </Card>
            )}
        </PageContainer>
    )
}