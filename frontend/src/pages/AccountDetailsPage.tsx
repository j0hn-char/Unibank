import {useEffect, useState} from "react";
import type {AccountResponse, Page, TransactionResponse} from "@/types";
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
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";

export default function AccountDetailsPage() {
    const [ account, setAccount ] = useState<AccountResponse | null>(null)
    const [ loading, setLoading ] = useState(true)
    const [ submitting, setSubmitting ] = useState(false)
    const [ transactions, setTransactions ] = useState<TransactionResponse[]>([])
    const [ page, setPage ] = useState(0)
    const [ totalPages, setTotalPages ] = useState(0)
    const { id } = useParams()
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

    async function fetchTransactions() {
        try {
            const response = await api.get<Page<TransactionResponse>>(`/transactions/account/${id}?page=${page}&size=10`)
            setTransactions(response.data.content)
            setTotalPages(response.data.totalPages)
        } catch (err) {
            console.error(err)
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

    useEffect(() => {
        fetchTransactions()
    }, [id, page])

    return (
        <PageContainer>
            {loading ? (
                <p>Loading...</p>
            ) : !account ? (
                <p className="text-gray-500">Account not found.</p>
            ) : (
                <>
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
                    <div className="max-w-lg mt-6">
                        <h2 className="text-lg font-semibold mb-3">Transaction history</h2>
                        {transactions.length === 0 ? (
                            <p className="text-sm text-muted-foreground">No transactions yet.</p>
                        ) : (
                            <Table>
                                <TableHeader>
                                    <TableRow>
                                        <TableHead>Type</TableHead>
                                        <TableHead>Description</TableHead>
                                        <TableHead className="text-right">Amount</TableHead>
                                        <TableHead className="text-right">Balance</TableHead>
                                        <TableHead>Date</TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {transactions.map((tx) => (
                                        <TableRow key={tx.id}>
                                            <TableCell className="font-medium">{tx.type}</TableCell>
                                            <TableCell>{tx.description || '—'}</TableCell>
                                            <TableCell className="text-right">{tx.amount.toFixed(2)}€</TableCell>
                                            <TableCell className="text-right">{tx.balanceAfter.toFixed(2)}€</TableCell>
                                            <TableCell>{new Date(tx.createdAt).toLocaleDateString()}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        )}
                        {totalPages > 1 && (
                            <div className="flex items-center justify-between mt-4">
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => setPage(page - 1)}
                                    disabled={page === 0}
                                >
                                    Previous
                                </Button>
                                <span className="text-sm text-muted-foreground">
                                     Page {page + 1} of {totalPages}
                                </span>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => setPage(page + 1)}
                                    disabled={page >= totalPages - 1}
                                >
                                    Next
                                </Button>
                            </div>
                        )}
                    </div>
                </>
            )}
        </PageContainer>
    )
}