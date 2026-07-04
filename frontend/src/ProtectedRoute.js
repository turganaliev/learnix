import React from 'react';
import { Navigate } from 'react-router-dom';

function ProtectedRoute({ children }) {
  const token = localStorage.getItem('token');

  if (!token) {
    return <Navigate to="/login" replace state={{ message: 'You must be logged in to access the chat.' }} />;
  }

  return children;
}

export default ProtectedRoute;