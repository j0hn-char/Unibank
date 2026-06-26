import { Routes, Route, Navigate } from 'react-router-dom'
import AuthPage from '@/pages/AuthPage'
import ProtectedRoute from "@/components/ProtectedRoute.tsx";
import Dashboard from "@/pages/Dashboard.tsx";

function App() {
  return (
      <Routes>
          <Route path="/login" element={<AuthPage />} />
          <Route
              path="/dashboard"
              element={
              <ProtectedRoute>
                  <Dashboard />
              </ProtectedRoute>
              }
          />
          <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
  )
}

export default App