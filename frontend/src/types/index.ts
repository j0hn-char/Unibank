export type Role = "CUSTOMER" | "ADMIN"
export type AccountType = "CHECKING" | "SAVINGS"
export type AccountStatus = "ACTIVE" | "CLOSED" | "FROZEN"
export type TransType =
    | "DEPOSIT"
    | "WITHDRAWAL"
    | "TRANSFER_IN"
    | "TRANSFER_OUT"
    | "LOAN_DISBURSEMENT"
    | "LOAN_REPAYMENT"
export type LoanStatus = "PENDING" | "APPROVED" | "REJECTED" | "ACTIVE" | "PAID_OFF"

export interface AuthResponse {
    token: string
}

export interface DecodedToken {
    sub: string
    role: Role
    iat: number
    exp: number
}

export interface AccountResponse {
    id: number
    accountNumber: string
    type: AccountType
    balance: number
    status: AccountStatus
}

export interface TransactionResponse {
    id: number
    type: TransType
    amount: number
    balanceAfter: number
    description: string
    createdAt: string
}

export interface LoanResponse {
    id: number
    principalAmount: number
    remainingBalance: number
    interestRate: number
    termMonths: number
    monthlyPayment: number
    status: LoanStatus
    purpose: string
    appliedAt: string
}

export interface ErrorResponse {
    timestamp: string
    status: number
    message: string
    path: string
}

export interface Page<T> {
    content: T[]
    totalPages: number
    totalElements: number
    number: number
    size: number
    first: boolean
    last: boolean
}