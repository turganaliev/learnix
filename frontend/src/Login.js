import React, { useState } from 'react';
import { useNavigate, useLocation} from 'react-router-dom';
import api from './api';

function Login() {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const message = location.state?.message;

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/users/login', formData);
      localStorage.setItem('token', response.data.token);
      navigate('/chat');
    } catch (error) {
      console.error('Error:', error);
      setError('Invalid username or password');
    }
  };

    return (
      <div className="auth-page">
        <div className="auth-card">
          <h1>Welcome back</h1>
          <p className="auth-sub">Sign in to continue your conversations.</p>

          {message && <p className="notice notice-warn">{message}</p>}

          <form onSubmit={handleSubmit}>
            <input name="username" placeholder="Username" onChange={handleChange} />
            <input name="password" type="password" placeholder="Password" onChange={handleChange} />
            <button type="submit">Sign in</button>
          </form>

          {error && <p className="field-error">{error}</p>}
        </div>
      </div>
    );
}

export default Login;