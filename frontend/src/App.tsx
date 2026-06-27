import { Routes, Route, Navigate } from 'react-router-dom'
import AuthPage from '@/pages/AuthPage'
import ProtectedRoute from "@/components/ProtectedRoute.tsx";
import Dashboard from "@/pages/Dashboard.tsx";
import AccountsPage from "@/pages/AccountsPage.tsx";
import AccountDetailsPage from "@/pages/AccountDetailsPage.tsx";
import Layout from "@/components/Layout.tsx";

function App() {
  return (
      <Routes>
          <Route path="/login" element={<AuthPage />} />
          <Route element={<Layout />}>
              <Route
                  path="/dashboard"
                  element={
                      <ProtectedRoute>
                          <Dashboard />
                      </ProtectedRoute>
                  }
              />
              <Route
                  path="/accounts"
                  element={
                      <ProtectedRoute>
                          <AccountsPage />
                      </ProtectedRoute>
                  }
              />
              <Route
                  path="/accounts/:id"
                  element={
                      <ProtectedRoute>
                          <AccountDetailsPage />
                      </ProtectedRoute>
                  }
              />
          </Route>
          <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
  )
}

export default App